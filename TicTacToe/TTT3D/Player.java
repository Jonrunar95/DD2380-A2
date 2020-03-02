import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
		}
        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
		int max = Integer.MIN_VALUE;
		int maxMove = 0;
		int min = Integer.MAX_VALUE;
		int minMove = 0;
		int depth = 0;

		for(int i = 0; i < nextStates.size(); i++) {
			int move = Algorithms.alphaBetaPruning(nextStates.elementAt(i), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, nextStates.elementAt(i).getNextPlayer());
			if(gameState.getNextPlayer() == 1) {
				if(move >= max) {
					max = move;
					maxMove = i;
				}
			} else {
				if(move <= min) {
					min = move;
					minMove = i;
				}
			}
		}
		if(gameState.getNextPlayer() == 1) {
			return nextStates.elementAt(maxMove);
		} else {
			return nextStates.elementAt(minMove);
		}
    }    
}