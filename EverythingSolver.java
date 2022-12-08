import java.awt.Point;
import java.util.ArrayList;

/**
 * Everything solver recursively checks all adjacent nodes from the starting position
 * until it finds either a dead-end or a solution. This does the same thing as the brute
 * force solver. However, it saves all solutions regardless of the path length.
 * 
 * This solver is inherently slower than the BruteForceSolver, but I think its kinda cool
 * to see.
 * 
 * @author Hunter Barclay
 */
public class EverythingSolver implements CircuitSolver {

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
                foundSolutions.add(state);
			} else {

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
