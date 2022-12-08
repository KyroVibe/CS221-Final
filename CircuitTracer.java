import java.util.ArrayList;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail
 */
public class CircuitTracer {

	// You can swap solving algorithms. I wanted to add another but I got lazy
	private static final CircuitSolver ACTIVE_SOLVER = new BruteForceSolver();
	// private static final CircuitSolver ACTIVE_SOLVER = new EverythingSolver();

	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		new CircuitTracer(args); //create this with args
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private void printUsage() {
		System.out.println("Usage: java CircuitTracer [-s | -q] [-c | -g] FILE");
		System.out.println("  -s\tUse a stack for storage of cells while solving.");
		System.out.println("  -q\tUse a queue for storage of cells while solving.");
		System.out.println("  -c\tOutput results to the console.");
		System.out.println("  -g\tOutput results to a GUI.");
	}
	
	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {
		if (args.length != 3) {
			printUsage();
			return; //exit the constructor immediately
		}
		
		Storage<TraceState> storage = null;
		if (args[0].equals("-s")) {
			storage = new Storage<TraceState>(Storage.DataStructure.stack);
		} else if (args[0].equals("-q")) {
			storage = new Storage<TraceState>(Storage.DataStructure.queue);
		} else {
			printUsage();
			return;
		}

		boolean useConsole;
		if (args[1].equals("-c")) {
			useConsole = true;
		} else if (args[1].equals("-g")) {
			useConsole = false;
		} else {
			printUsage();
			return;
		}

		CircuitBoard board;

		try {
			board = new CircuitBoard(args[2]);
		} catch (Exception e) {
			System.out.println(e.toString());
			return;
		}

		ArrayList<CircuitBoard> solvedBoards;

		solvedBoards = ACTIVE_SOLVER.Solve(storage, board);

		if (useConsole) {
			solvedBoards.forEach(x -> System.out.println(x));
		} else {
			try {
				new GUI(board, solvedBoards);
			} catch (java.awt.HeadlessException e) {
				System.out.println("Failed to open GUI: No display");
			}
		}
	}
	
}
