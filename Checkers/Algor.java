import java.util.Hashtable;
import java.util.Vector;

public class Algor {

	public static Hashtable<String, Integer> htable = new Hashtable<>();

	public static int alphaBetaPruning(GameState gameState, int depth, int alpha, int beta, int player, Deadline deadline, long timeUntil) {
		
		if(deadline.timeUntil() < timeUntil) {
			if(player == 1) {
				return Integer.MAX_VALUE;
			}
			else {
				return Integer.MIN_VALUE;
			}
		}

		int v = 0;

		if(gameState.isEOG() || depth == 0)  {

			if(gameState.isDraw()) { return 0; }
			else if(gameState.isRedWin()) { return Integer.MAX_VALUE; }
			else if(gameState.isWhiteWin()) { return Integer.MIN_VALUE; }
			
			v = gamma(gameState, player);
		}
		
		else if(player == 1) { // If player = Red
			v = Integer.MIN_VALUE;
			Vector<GameState> nextStates = new Vector<GameState>();
			gameState.findPossibleMoves(nextStates);
			for(int i = 0; i < nextStates.size(); i++) {
				if(deadline.timeUntil() < timeUntil) { break; }
				v = Math.max(v, alphaBetaPruning(nextStates.elementAt(i), depth-1, alpha, beta, 2, deadline, timeUntil));
				alpha = Math.max(v, alpha);
				if(beta <= alpha) {
					break;
				}
			}
		} else { // If player = White
			v = Integer.MAX_VALUE;
			Vector<GameState> nextStates = new Vector<GameState>();
			gameState.findPossibleMoves(nextStates);
			for(int i = 0; i < nextStates.size(); i++) {
				if(deadline.timeUntil() < timeUntil) { break; }
				v = Math.min(v, alphaBetaPruning(nextStates.elementAt(i), depth-1, alpha, beta, 1, deadline, timeUntil));
				beta = Math.min(v, beta);
				if(beta <= alpha) {
					break;
				}
			}
		}
		return v;
	}

	public static int gamma(GameState gameState, int player) {
		int score = 0;
		
		int rCnt = 0;
		int wCnt = 0;

		String[] state = gameState.toMessage().split(" ");
		if(htable.containsKey(state[0]+state[2])) {
			return htable.get(state[0]+state[2]);
		}
		// 10000 points for every piece, 20000 points for a king
		for(int i = 0; i < 32; i++) {
			if(gameState.get(i) == Constants.CELL_RED) {
				rCnt++; 
				if(gameState.get(i) == Constants.CELL_KING) {
					rCnt++;
				}
			} else if(gameState.get(i) == Constants.CELL_WHITE) {
				wCnt++; 
				if(gameState.get(i) == Constants.CELL_KING) {
					wCnt++;
				}
			}
		}

		score += 10000*rCnt;
		score -= 10000*wCnt;

		rCnt = 0;
		wCnt = 0;
		
		for(int i = 4; i < 28; i++) {
			// Red at 4, 12, 20 and behind him at 0, 8, 16
			if((i+4) % 8 == 0) {
				if(gameState.get(i) == Constants.CELL_RED && gameState.get(i-4) == Constants.CELL_RED) {
					rCnt+= i*(i/4);
				}
			}
			// Red at 8, 16, 24 and behind him at 4,5 12,13 and 20,21
			else if(i % 8 == 0) {
				if(gameState.get(i) == Constants.CELL_RED && gameState.get(i-4) == Constants.CELL_RED && gameState.get(i-3) == Constants.CELL_RED) {
					rCnt+= i*(i/4);
				}
			}
			// Red at 11, 19, 27 and behind him at 7, 15, 23
			else if((i+5) % 8 == 0){
				if(gameState.get(i) == Constants.CELL_RED && gameState.get(i-4) == Constants.CELL_RED) {
					rCnt+= i*(i/4);
				}
			}
			
			// Red at 7, 15, 23 and behind him at 2,3 10,11 and 18,19
			else if((i+1) % 8 == 0){
				if(gameState.get(i) == Constants.CELL_RED && gameState.get(i-4) == Constants.CELL_RED && gameState.get(i-5) == Constants.CELL_RED) {
					rCnt+= i*(i/4);
				}
			}
			// Red at even rows and behind him
			else if(GameState.cellToRow(i) % 2 == 0){
				if(gameState.get(i) == Constants.CELL_RED && gameState.get(i-4) == Constants.CELL_RED && gameState.get(i-3) == Constants.CELL_RED) {
					rCnt+= i*(i/4);
				}
			}
			// Red at odd rows and behind him
			else if(GameState.cellToRow(i) % 2 != 0){
				if(gameState.get(i) == Constants.CELL_RED && gameState.get(i-5) == Constants.CELL_RED && gameState.get(i-4) == Constants.CELL_RED) {
					rCnt+= i*(i/4);
				}
			}
		}
		
		for(int i = 27; i > 3; i--) {
			// White at 20, 12, 4 and behind him at 24, 16, 8
			if((i+4) % 8 == 0){
				if(gameState.get(i) == Constants.CELL_WHITE && gameState.get(i+4) == Constants.CELL_WHITE) {
					wCnt+= i*((31-i)%4);
				}
			} 
			// White at 24, 16, 8 and behind him at 28,29 20,21 and 12,13
			else if(i % 8 == 0){
				if(gameState.get(i) == Constants.CELL_WHITE && gameState.get(i+4) == Constants.CELL_WHITE && gameState.get(i+5) == Constants.CELL_WHITE) {
					wCnt+= i*((31-i)%4);
				}
			}
			// White at 11, 19, 27 and behind him at 15, 23, 31
			else if((i+5) % 8 == 0){
				if(gameState.get(i) == Constants.CELL_WHITE && gameState.get(i+4) == Constants.CELL_WHITE) {
					wCnt+= i*((31-i)%4);
				}
			}
			
			// White at 7, 15, 23 and behind him at 26,27 18,19 and 10,11
			else if((i+1) % 8 == 0){
				if(gameState.get(i) == Constants.CELL_WHITE && gameState.get(i+3) == Constants.CELL_WHITE && gameState.get(i+4) == Constants.CELL_WHITE) {
					wCnt+= i*((31-i)%4);
				}
			}
			// White at even rows and behind him
			else if(GameState.cellToRow(i) % 2 == 0){
				if(gameState.get(i) == Constants.CELL_WHITE && gameState.get(i+4) == Constants.CELL_WHITE && gameState.get(i+5) == Constants.CELL_WHITE) {
					wCnt+= i*((31-i)%4);
				}
			}
			// White at odd rows and behind him
			else if(GameState.cellToRow(i) % 2 != 0) {
				if(gameState.get(i) == Constants.CELL_WHITE && gameState.get(i+3) == Constants.CELL_WHITE && gameState.get(i+4) == Constants.CELL_WHITE) {
					wCnt+= i*((31-i)%4);
				}
			}
		}
		
		score += rCnt*1000;
		score -= wCnt*1000;
		
		// Put gameState into hashmap
		String[] pstate = gameState.toMessage().split(" ");
		htable.put(pstate[0]+pstate[2], score);
		
		// Put reverse gameState into hashmap
		GameState reverse = gameState.reversed();
		String[] revState = reverse.toMessage().split(" ");
		htable.put(revState[0]+revState[2], (-score));
		
		return score;
	}
}