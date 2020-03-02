import java.util.Vector;

public class Algorithms {

	public static int alphaBetaPruning(GameState gameState, int depth, int alpha, int beta, int player) {
		
		int v = 0;
		if(gameState.isEOG() || depth == 0)  {
			v = gamma(gameState);
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

	public static int gamma(GameState gameState) {
		int score = 0;
		if(gameState.isOWin()) {
			score = Integer.MIN_VALUE;
		} else if(gameState.isXWin()) {
			score =  Integer.MAX_VALUE;
		} else if(gameState.isEOG()) {
			return 0;
		}
		else {
			// 10 Point for piece in the center
			/*int xxCnt = 0;
			int ooCnt = 0;

			for(int i = 1; i < 3; i++) {
				for(int j = 1; j < 3; j++) {
					for(int k = 1; k < 3; k++) {

						if(gameState.at(i, j, k) == Constants.CELL_X) { xxCnt++; }
						if(gameState.at(i, j, k) == Constants.CELL_O) { ooCnt++; }

					}
				}
			}
			
			if(gameState.at(0, 0, 0) == Constants.CELL_X) { xxCnt++; }
			if(gameState.at(0, 0, 0) == Constants.CELL_O) { ooCnt++; }
			if(gameState.at(3, 0, 0) == Constants.CELL_X) { xxCnt++; }
			if(gameState.at(3, 0, 0) == Constants.CELL_O) { ooCnt++; }
			if(gameState.at(0, 3, 0) == Constants.CELL_X) { xxCnt++; }
			if(gameState.at(0, 3, 0) == Constants.CELL_O) { ooCnt++; }
			if(gameState.at(3, 3, 0) == Constants.CELL_X) { xxCnt++; }
			if(gameState.at(3, 3, 0) == Constants.CELL_O) { ooCnt++; }
			if(gameState.at(0, 0, 3) == Constants.CELL_X) { xxCnt++; }
			if(gameState.at(0, 0, 3) == Constants.CELL_O) { ooCnt++; }
			if(gameState.at(3, 0, 3) == Constants.CELL_X) { xxCnt++; }
			if(gameState.at(3, 0, 3) == Constants.CELL_O) { ooCnt++; }
			if(gameState.at(0, 3, 3) == Constants.CELL_X) { xxCnt++; }
			if(gameState.at(0, 3, 3) == Constants.CELL_O) { ooCnt++; }
			if(gameState.at(3, 3, 3) == Constants.CELL_X) { xxCnt++; }
			if(gameState.at(3, 3, 3) == Constants.CELL_O) { ooCnt++; }

			score += xxCnt * 10;
			score -= ooCnt * 10;
			*/
			for(int lay = 0; lay < 4; lay++) {

				// Point for rows 16
				for(int col = 0; col < 4; col++) {
					int xCnt = 0;
					int oCnt = 0;
					for(int row = 0; row < 4; row++) {
						if(gameState.at(row, col, lay) == Constants.CELL_X) { xCnt++; }
						if(gameState.at(row, col, lay) == Constants.CELL_O) { oCnt++; }
					}
					if(oCnt == 0 && xCnt > 0) {
						score += Math.pow(10, xCnt);
					} else if(xCnt == 0 && oCnt > 0) {
						score -= Math.pow(10, oCnt);
					}
				}
				
				// Point for cols 16
				for(int row = 0; row < 4; row++) {
					int xCnt = 0;
					int oCnt = 0;
					for(int col = 0; col < 4; col++) {
						if(gameState.at(row, col, lay) == Constants.CELL_X) { xCnt++; }
						if(gameState.at(row, col, lay) == Constants.CELL_O) { oCnt++; }
					}
					if(oCnt == 0 && xCnt > 0) {
						score += Math.pow(10, xCnt);
					} else if(xCnt == 0 && oCnt > 0) {
						score -= Math.pow(10, oCnt);
					}
				}
				
				// Points for diagonal 4
				int xCnt = 0;
				int oCnt = 0;
				for(int diag = 0; diag < 4; diag++) {
					if(gameState.at(diag, diag, lay) == Constants.CELL_X) { xCnt++; }
					if(gameState.at(diag, diag, lay) == Constants.CELL_O) { oCnt++; }
				}
				if(oCnt == 0 && xCnt > 0) {
					score += Math.pow(10, xCnt);
				} else if(xCnt == 0 && oCnt > 0) {
					score -= Math.pow(10, oCnt);
				}

				// Points for reverse diagonal 4
				xCnt = 0;
				oCnt = 0;
				for(int diag = 0; diag < 4; diag++) {
					if(gameState.at(diag, 3-diag, lay) == Constants.CELL_X) { xCnt++; }
					if(gameState.at(diag, 3-diag, lay) == Constants.CELL_O) { oCnt++; }
				}
				if(oCnt == 0 && xCnt > 0) {
					score += Math.pow(10, xCnt);
				} else if(xCnt == 0 && oCnt > 0) {
					score -= Math.pow(10, oCnt);
				}
			}
			// Points for layers 16
			for(int row = 0; row < 4; row++) {
				for(int col = 0; col < 4; col++) {
					int xCnt = 0;
					int oCnt = 0;
					for(int lay = 0; lay < 4; lay++) {
						if(gameState.at(row, col, lay) == Constants.CELL_X) { xCnt++; }
						if(gameState.at(row, col, lay) == Constants.CELL_O) { oCnt++; }
					}
					if(oCnt == 0 && xCnt > 0) {
						score += Math.pow(10, xCnt);
					} else if(xCnt == 0 && oCnt > 0) {
						score -= Math.pow(10, oCnt);
					}
				}
			}
			
			//  8
			for(int row = 0; row < 4; row++) {
				int xCnt = 0;
				int oCnt = 0;
				for(int diag = 0; diag < 4; diag++) {
					if(gameState.at(row, diag, diag) == Constants.CELL_X) { xCnt++; }
					if(gameState.at(row, diag, diag) == Constants.CELL_O) { oCnt++; }
				}
				if(oCnt == 0 && xCnt > 0) {
					score += Math.pow(10, xCnt);
				} else if(xCnt == 0 && oCnt > 0) {
					score -= Math.pow(10, oCnt);
				}
				
				xCnt = 0;
				oCnt = 0;
				
				for(int diag = 0; diag < 4; diag++) {
					if(gameState.at(row, diag, 3-diag) == Constants.CELL_X) { xCnt++; }
					if(gameState.at(row, diag, 3-diag) == Constants.CELL_O) { oCnt++; }
				}
				if(oCnt == 0 && xCnt > 0) {
					score += Math.pow(10, xCnt);
				} else if(xCnt == 0 && oCnt > 0) {
					score -= Math.pow(10, oCnt);
				}
			}
			
			// 8
			for(int col = 0; col < 4; col++) {
				int xCnt = 0;
				int oCnt = 0;
				for(int diag = 0; diag < 4; diag++) {
					if(gameState.at(diag, col, diag) == Constants.CELL_X) { xCnt++; }
					if(gameState.at(diag, col, diag) == Constants.CELL_O) { oCnt++; }
				}
				if(oCnt == 0 && xCnt > 0) {
					score += Math.pow(10, xCnt);
				} else if(xCnt == 0 && oCnt > 0) {
					score -= Math.pow(10, oCnt);
				}
				
				xCnt = 0;
				oCnt = 0;
				
				for(int diag = 0; diag < 4; diag++) {
					if(gameState.at(diag, col, 3-diag) == Constants.CELL_X) { xCnt++; }
					if(gameState.at(diag, col, 3-diag) == Constants.CELL_O) { oCnt++; }
				}
				if(oCnt == 0 && xCnt > 0) {
					score += Math.pow(10, xCnt);
				} else if(xCnt == 0 && oCnt > 0) {
					score -= Math.pow(10, oCnt);
				}
			}
			
			// 1
			int xCnt = 0;
			int oCnt = 0;
			for(int diag = 0; diag < 4; diag++) {
				if(gameState.at(diag, diag, diag) == Constants.CELL_X) { xCnt++; }
				if(gameState.at(diag, diag, diag) == Constants.CELL_O) { oCnt++; }
			}
			if(oCnt == 0 && xCnt > 0) {
				score += Math.pow(10, xCnt);
			} else if(xCnt == 0 && oCnt > 0) {
				score -= Math.pow(10, oCnt);
			}
			
			// 1
			xCnt = 0;
			oCnt = 0;
			for(int diag = 0; diag < 4; diag++) {
				if(gameState.at(3-diag, diag, diag) == Constants.CELL_X) { xCnt++; }
				if(gameState.at(3-diag, diag, diag) == Constants.CELL_O) { oCnt++; }
			}
			if(oCnt == 0 && xCnt > 0) {
				score += Math.pow(10, xCnt);
			} else if(xCnt == 0 && oCnt > 0) {
				score -= Math.pow(10, oCnt);
			}
			
			// 1
			xCnt = 0;
			oCnt = 0;
			for(int diag = 0; diag < 4; diag++) {
				if(gameState.at(diag, 3-diag, diag) == Constants.CELL_X) { xCnt++; }
				if(gameState.at(diag, 3-diag, diag) == Constants.CELL_O) { oCnt++; }
			}
			if(oCnt == 0 && xCnt > 0) {
				score += Math.pow(10, xCnt);
			} else if(xCnt == 0 && oCnt > 0) {
				score -= Math.pow(10, oCnt);
			}
			
			// 1
			xCnt = 0;
			oCnt = 0;
			for(int diag = 0; diag < 4; diag++) {
				if(gameState.at(diag, diag, 3-diag) == Constants.CELL_X) { xCnt++; }
				if(gameState.at(diag, diag, 3-diag) == Constants.CELL_O) { oCnt++; }
			}
			if(oCnt == 0 && xCnt > 0) {
				score += Math.pow(10, xCnt);
			} else if(xCnt == 0 && oCnt > 0) {
				score -= Math.pow(10, oCnt);
			}
		}
		return score;
	}
}