import java.awt.Point;
import java.util.ArrayList;

/**
 * Brute force algorithm for solving the circuit trace. It starts at the starting point,
 * trying every open adjacent cell and recursively repeating down all paths until they are
 * either longer the the shortest found path, lead to a dead-end, or are a solution.
 * 
 * This is the default for the final.
 * 
 * @author Hunter Barclay
 */
public class BruteForceSolver implements CircuitSolver {
    @Override
    public ArrayList<CircuitBoard> Solve(Storage<TraceState> storage, CircuitBoard board) {
        ArrayList<TraceState> foundSolutions = new ArrayList<TraceState>();
		ArrayList<CircuitBoard> solvedBoards = new ArrayList<CircuitBoard>();

		// Start it off with the adjacent cells
		if (board.isOpen(board.getStartingPoint().y, board.getStartingPoint().x + 1)) {
			storage.store(new TraceState(board, board.getStartingPoint().y, board.getStartingPoint().x + 1));
		}
		if (board.isOpen(board.getStartingPoint().y, board.getStartingPoint().x - 1)) {
			storage.store(new TraceState(board, board.getStartingPoint().y, board.getStartingPoint().x - 1));
		}
		if (board.isOpen(board.getStartingPoint().y + 1, board.getStartingPoint().x)) {
			storage.store(new TraceState(board, board.getStartingPoint().y + 1, board.getStartingPoint().x));
		}
		if (board.isOpen(board.getStartingPoint().y - 1, board.getStartingPoint().x)) {
			storage.store(new TraceState(board, board.getStartingPoint().y - 1, board.getStartingPoint().x));
		}

		while (storage.size() > 0) {
			TraceState state = storage.retrieve();
			if (state.isComplete()) {

				if (foundSolutions.isEmpty() || foundSolutions.get(0).pathLength() == state.pathLength()) {
					foundSolutions.add(state);
				} else if (state.pathLength() < foundSolutions.get(0).pathLength()) {
					foundSolutions.clear();
					foundSolutions.add(state);
				}
			} else if (foundSolutions.isEmpty() || foundSolutions.get(0).pathLength() > state.pathLength()) {

				Point lastPoint = state.getPath().get(state.pathLength() - 1);

				if (state.isOpen(lastPoint.y, lastPoint.x + 1)) {
					storage.store(new TraceState(state, lastPoint.y, lastPoint.x + 1));
				}
				if (state.isOpen(lastPoint.y, lastPoint.x - 1)) {
					storage.store(new TraceState(state, lastPoint.y, lastPoint.x - 1));
				}
				if (state.isOpen(lastPoint.y + 1, lastPoint.x)) {
					storage.store(new TraceState(state, lastPoint.y + 1, lastPoint.x));
				}
				if (state.isOpen(lastPoint.y - 1, lastPoint.x)) {
					storage.store(new TraceState(state, lastPoint.y - 1, lastPoint.x));
				}
			}
		}

		foundSolutions.forEach(x -> solvedBoards.add(x.getBoard()));
		return solvedBoards;
    }
    
}
