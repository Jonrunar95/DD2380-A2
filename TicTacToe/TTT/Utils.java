import java.util.Vector;

public class Utils {
	public static int alphaBetaPruning(GameState gameState, int depth, int alpha, int beta, int player) {
		int v = 0;
		if(gameState.isEOG() || depth == 0) {
			v = gamma(gameState, player);
		}
		else if(player == 1) { // If player = X
			v = -Integer.MIN_VALUE;
			Vector<GameState> nextStates = new Vector<GameState>();
			gameState.findPossibleMoves(nextStates);
			for(int i = 0; i < nextStates.size(); i++) {
				v = Math.max(v, alphaBetaPruning(nextStates.elementAt(i), depth-1, alpha, beta, 2));
				alpha = Math.max(v, alpha);
				if(beta <= alpha) {
					break;
				}
			}
		} else { // If player = O
			v = Integer.MAX_VALUE;
			Vector<GameState> nextStates = new Vector<GameState>();
			gameState.findPossibleMoves(nextStates);
			for(int i = 0; i < nextStates.size(); i++) {
				v = Math.min(v, alphaBetaPruning(nextStates.elementAt(i), depth-1, alpha, beta, 1));
				beta = Math.min(v, beta);
				if(beta <= alpha) {
					break;
				}
			}
		}
		return v;
	}

	public static int gamma(GameState gameState, int player) {
		if(gameState.isOWin()) {
			return -50;
		} else if(gameState.isXWin()) {
			return 50;
		} else {
			int score = 0;
			// If you have one in the center you get one point
			for(int i = 1; i < 3; i++) {
				for(int j = 1; j < 3; j++) {
					if(gameState.at(i, j) == Constants.CELL_X) {
						score += 1;
					}
					if(gameState.at(i, j) == Constants.CELL_O) {
						score -= 1;
					} 
				}
			}
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
					// If you have 2 in a row you get 1 point. If your opponent has 2 in a row you get -1 point
					// rows
					if(j<3 && gameState.at(i, j) == Constants.CELL_X && gameState.at(i, j+1) == Constants.CELL_X) {
						score += 1;
					}
					if(i<3 &&gameState.at(i, j) == Constants.CELL_X && gameState.at(i+1, j) == Constants.CELL_X) {
						score += 1;
					}
					// columns
					if(j<3 && gameState.at(i, j) == Constants.CELL_O && gameState.at(i, j+1) == Constants.CELL_O) {
						score -= 1;
					}
					if(i<3 && gameState.at(i, j) == Constants.CELL_O && gameState.at(i+1, j) == Constants.CELL_O) {
						score -= 1;
					}
					// diagonal
					if(j<3 && i<3 && gameState.at(i, j) == Constants.CELL_X && gameState.at(i+1, j+1) == Constants.CELL_X) {
						score += 1;
					}
					if(j<3 && i<3 && gameState.at(i, j) == Constants.CELL_O && gameState.at(i+1, j+1) == Constants.CELL_O) {
						score -= 1;
					}
					// If you have 3 in a row you get 2 point. If your opponent has 3 in a row you get -2 points
					// row
					if(j<2 &&gameState.at(i, j) == Constants.CELL_X && gameState.at(i, j+1) == Constants.CELL_X && gameState.at(i, j+2) == Constants.CELL_X) {
						score += 2;
					}
					if(i<2 &&gameState.at(i, j) == Constants.CELL_X && gameState.at(i+1, j) == Constants.CELL_X && gameState.at(i+2, j) == Constants.CELL_X) {
						score += 2;
					}
					// column
					if(j<2 && gameState.at(i, j) == Constants.CELL_O && gameState.at(i, j+1) == Constants.CELL_O && gameState.at(i, j+2) == Constants.CELL_O) {
						score -= 2;
					}
					if(i<2 && gameState.at(i, j) == Constants.CELL_O && gameState.at(i+1, j) == Constants.CELL_O  && gameState.at(i+2, j) == Constants.CELL_O) {
						score -= 2;
					}
					// diagonal
					if(i<2 && j<2 && gameState.at(i, j) == Constants.CELL_X && gameState.at(i+1, j+1) == Constants.CELL_X && gameState.at(i+2, j+2) == Constants.CELL_X) {
						score += 2;
					}
					if(j<2 && i<2 && gameState.at(i, j) == Constants.CELL_O && gameState.at(i+1, j+1) == Constants.CELL_O && gameState.at(i+2, j+2) == Constants.CELL_O) {
						score -= 2;
					}
				}
			}
			return score;
		}
	}
}