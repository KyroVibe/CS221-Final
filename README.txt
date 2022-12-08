****************
* Circuit Tracer (Final Project)
* CS 221-3
* Dec. 8th, 2022
* Hunter Barclay
**************** 

OVERVIEW:

 CircuitTracer takes in a data file representing a start and end point on
 a circuit board and traces all possible paths between them to provide you
 with the shortest possible paths to take in order to connect the two points.


INCLUDED FILES:
 
 CircuitTracer.java - source file
 CircuitBoard.java - source file
 CircuitSolver.java - source file
 Storage.java - source file
 TraceState.java - source file
 OccupiedPositionException.java - source file
 InvalidFileFormatException.java - source file
 BruteForceSolver.java - source file
 EverythingSolver.java - source file
 GUI.java - source file
 README.txt - this file


COMPILING AND RUNNING:

 From the directory containing all source files, compile
 CircuitTracer with the command:
 $ javac CircuitTracer.java

 Run CircuitTracer with the command:
 $ java CircuitTracer [-s | -q] [-c | -g] FILE

 -s tells the solver to use a stack for storage while finding solutions.
 Alternatively -q uses a queue.

 -c tells CircuitTracer to output its findings to the console,
 whereas -g displays a function GUI to display the results.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:
 
 At the core of the program we have the CircuitTracer class. This class handles
 the entire runtime of the application, as well as the solving of the circuit.
 It handles the argument compliance and pieces the enitre program together.
 The extent of this class doesn't really leave the constructor.

 As the CircuitTracer takes in the input arguments, the CircuitBoard class parses
 the provided file into an array of characters, as well as checking for compliance
 to the required format. The CircuitBoard class will then indicate to the CircuitTracer
 class if it meets compliance or not. If so, it will store the necessary board make up
 for the solver to use when solving.

 The CircuitSolver interface is an addition I added to abstract out the solving method.
 Just me being extra. It provides an interface for the CircuitTracer to use to request
 an implemented solver to, ya know, solve the the board. With this submission, I'm providing
 two implementations of CircuitSolver: BruteForceSolver and EverythingSolver. The
 BruteForceSolver starts with all adjacent cells to the starting cell, then recursively
 looks through all possible paths until it either leads to a solution, dead-end, or a path
 that is longer than the current shortest solution. The EverythingSolver I added for fun.
 It uses the same algorithm as the BruteForceSolver. However, it doesn't cull paths that are
 longer than the shortest solution, so it shows you all possible paths between the start
 and end points.

 While solving, the solvers use two classes to help organize. It uses the TraceState class
 to store a current path in on the board. This class holds a list of the cells it followed,
 as well as a copy of the original board with that trace written into its cells. The Storage
 class then stores these TraceStates as the solving makes its way through the board. This Storage
 class is used to abstract between a stack or a queue, depending on the user's specifications in
 the arguments.

 Finally, after the program has solved for the shortest paths between the start and end points of
 a CircuitBoard, it then displays the results to the user. Depending on the arguments, it can either
 quickly display the solved boards to the console, or use the GUI class to construct a GUI
 for the user. The GUI class takes in the solved boards, constructs the GUI, then links a list of
 options to idividual solutions, than will then store the requested solution in the displayed board.

 The InvalidFileFormatException and OccupiedPositionException classes are very basic extensions
 of the RuntimeException class, just named differently.

 I would've like to add an optional command-line arguement to specify which solver to use. However
 multiple solvers weren't something that was expected for this project. I'm not the biggest fan of
 having the TraceState do a deep copy of the CircuitBoard, just because there are a lot of TraceStates
 so that takes up a ton of memory that could be avoided. That being said, I made a version of TraceState
 that uses a single-linked list to store the path and doesn't have a modified copy of the original
 CircuitBoard, and though it does save a lot of memory, it makes it so checking if the next cell to
 check is already occupied by a trace is much slower instead of it being constant.


TESTING:

 A lot of the confirmation that the program works correctly was done using
 the provided CircuitTracerTester class for the project. However, this class
 was not to be included in the submission. The testing strategy included
 first trying a number of files with valid and invalid formats with properly
 specified arguments. Then after verifying those tests, we then repeated the
 same, but with incorrect arguments. This pretty much verifies that it can handle
 bad data. Verifying that the solver actually provides the desired results is
 a little more tricky. I can verify by hand for smaller cases that it does in
 fact provide accurate results. That being said, I'm not brain damaged enough
 to test this for anything bigger than a 3x3 grid. There aren't any known
 bugs in my program. That being said, I'm not ignorant enough to declare my
 program bug-free.


ANALYSIS:

 1) When using a stack, that last added TraceState is going to be address next.
    So it will essentially navigate down a long path and then, once it reaches
    an end, will back up and try all the paths from the previous, and so on.
    With the queue, it radiates out like a wave from the starting point.

 2) If you prioritze movement in rows and you are using a stack, an ending point
    can potentially be found much faster if it is in the same column as the starting
    point, verse if it waited for the queue to radiate out to it. That being said,
    if the point were in the same row and a different column, the stack would take
    much longer to find it, because it would go through the row first, then column,
    whereas the queue spreads out equally in all directions.

 3) Because it is a brute force algorithm, all possibilities will be addresses, regardless
    of storage type. Therefore they will settle on the same solutions.

 4) I think the queue has better odds in finding the shortest path first, because
    the leading edge of its wave will be there the faster, whereas a stack will
    rely on the composure of the data.

 5) I think considering they will have similar averages and worst cases. That being said,
    I think queues have a wider range of situations that result in something closer to the
    average, whereas the stack will likely result in more extras towards the best/worst cases.

 6) The brute force solver has to run through all TraceStates that are stored, so the runtime
    is defintely proportional to the memory usage (or vice-versa). The source of the
    time it is going to take is the main while loop of the algorithm which doesn't stop until
    all TraceStates have been checked. Assuming the worst case, each iteration can add a maximum
    of 3 new traces to be checked, so the worst time would be O(3^n); n being the number of
    cells on the circuit board. For each cell there are 3 ways to go, then you do that for each
    cell you pick so 3 * 3 * 3 * 3... which is 3^n.


DISCUSSION:

 I might've written all the provided class files by hand originally
 before bothering to read through the instructions fully and see that
 classes like Storage and TraceState were completely provided to me.

 Other than that goof, the GUI gave me some grief at first because the size
 of the frame was the entire window, including the border, title, and
 close/minimize buttons, so specifying a size took some testing but I figured
 it out.
 
 
EXTRA CREDIT:

 I created a GUI for the CircuitTracer results, following closely to the
 provided example images for the GUI when it comes to styling and layout.
