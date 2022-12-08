import java.util.ArrayList;

/**
 * CircuitSolver interface for creating solvers for the circuit tracer.
 */
public interface CircuitSolver {
    /** Solves the Circuit
     * @param storage Storage used to store next tracestates to test while solving the circuit.
     * @param board Unsolved circuit board.
     * @return A list of all solutions has specificed by the solver.
     */
    ArrayList<CircuitBoard> Solve(Storage<TraceState> storage, CircuitBoard board);
}
