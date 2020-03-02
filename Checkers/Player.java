import java.util.*;
/* todo:
	Ef move minnkar í líkum í næstu ítrun, setja næst besta move ef það er betra
*/
public class Player {
    /**
     * Performs a move
     *
     * @param pState
     *            the current state of the board
     * @param pDue
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState pState, final Deadline pDue) {
        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);

        if (lNextStates.size() == 0) {
            return new GameState(pState, new Move());
        }

		int[] values = new int[lNextStates.size()];

		int depth = 1;
		long timeUntil = 1000000;
		boolean b = true;

		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		int move = 0;

		while(b && depth < 100) {

			for(int i = 0; i < lNextStates.size(); i++) {

				if(pDue.timeUntil() < timeUntil) {
					b = false;
					break;
				}
				values[i] = Algor.alphaBetaPruning(lNextStates.elementAt(i), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, lNextStates.elementAt(i).getNextPlayer(), pDue, timeUntil);
			}
			// Find the max after each iteration.
			if(pState.getNextPlayer() == 1) {
				for(int i = 0; i < values.length; i++) {
					if(values[i] >= max) {
						max = values[i];
						move = i;
						if(values[i] == Integer.MAX_VALUE) {
							b = false;
							break;
						}
					} else if(move == i && values[i] < max) {
						max = values[i];
						i = 0;
					}
				}
			} 
			// Find the min after each iteration
			else {
				for(int i = 0; i < values.length; i++) {
					if(values[i] < min) {
						min = values[i];
						move = i;
						if(values[i] == Integer.MIN_VALUE) {
							b = false;
							break;
						}
					} else if(move == i && values[i] > min) {
						min = values[i];
						i = 0;
					}
				}
			}
			// State reordering
			if(pState.getNextPlayer() == 1) {
				for(int i = 0; i < values.length-1; i++) {
					if(values[i+1] > values[i]) {
						int temp = values[i+1];
						values[i+1] = values[i];
						values[i] = temp;
						GameState gameState = lNextStates.remove(i+1);
						lNextStates.add(i, gameState);
						if(move == i+1) {
							move = i;
						}
					}
				}
			} else {
				for(int i = 0; i < values.length-1; i++) {
					if(values[i+1] < values[i]) {
						int temp = values[i+1];
						values[i+1] = values[i];
						values[i] = temp;
						GameState gameState = lNextStates.remove(i+1);
						lNextStates.add(i, gameState);
						if(move == i+1) {
							move = i;
						}
					}
				}
			}
			depth+=2;
		}
		return lNextStates.elementAt(move);
    }
}
