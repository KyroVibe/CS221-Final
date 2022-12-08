import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail
 */
public class CircuitBoard {
	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;

	//constants you may find useful
	private final int ROWS; //initialized in constructor
	private final int COLS; //initialized in constructor
	public static final char OPEN = 'O'; //capital 'o'
	public static final char CLOSED = 'X';
	public static final char TRACE = 'T';
	public static final char START = '1';
	public static final char END = '2';
	private final String ALLOWED_CHARS = "OXT12";

	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot read the file
	 * @throws InvalidFileFormatException for any other format or content issue that prevents reading a valid input file
	 */
	public CircuitBoard(String filename) throws FileNotFoundException {
		Scanner fileScan = new Scanner(new File(filename));

        String[] headerElements = fileScan.nextLine().split(" ");
        if (headerElements.length != 2) {
			fileScan.close();
            throw new InvalidFileFormatException("Wrong number of elements in the header");
        }

		try {
        	ROWS = Integer.parseInt(headerElements[0].trim());
        	COLS = Integer.parseInt(headerElements[1].trim());
		} catch (NumberFormatException e) {
			fileScan.close();
			throw new InvalidFileFormatException("Failed to parse header");
		}

        board = new char[ROWS][COLS];
		int row = 0;
        while (fileScan.hasNextLine()) {

			if (row >= ROWS) {
				fileScan.close();
				throw new InvalidFileFormatException(String.format("Inconsistent row size"));
			}

			String rowString;

			try {
            	rowString = fileScan.nextLine();
			} catch (NoSuchElementException e) {
				fileScan.close();
				throw new InvalidFileFormatException(String.format("Failed to read row %d", row + 1));
			}

			char tokenIndex = 0;
			int col = 0;
			while (tokenIndex < rowString.length()) {
				char token = rowString.charAt(tokenIndex);
				if (ALLOWED_CHARS.indexOf(token) > -1) {

					if (col >= COLS) {
						fileScan.close();
						throw new InvalidFileFormatException("Inconsistent column size");
					}

					if (token == START) {
						if (startingPoint != null) {
							fileScan.close();
							throw new InvalidFileFormatException("More than 1 starting point found");
						}
						startingPoint = new Point(col, row);
					} else if (token == END) {
						if (endingPoint != null) {
							fileScan.close();
							throw new InvalidFileFormatException("More than 1 ending point found");
						}
						endingPoint = new Point(col, row);
					}

					board[row][col] = token;
					col++;
				}
				tokenIndex++;
			}

			if (col != COLS) {
				fileScan.close();
				throw new InvalidFileFormatException("Inconsistent column size");
			}

			row++;
        }

		if (row < ROWS) {
			fileScan.close();
			throw new InvalidFileFormatException("Inconsistent row size");
		}

		fileScan.close();

        if (startingPoint == null)
            throw new InvalidFileFormatException("No start cell found");
        if (endingPoint == null)
            throw new InvalidFileFormatException("No end cell found");
		
		fileScan.close();
	}
	
	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/** utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}
	
	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}
	
	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}
	
	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}
	
	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}
	
	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}
	
	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}
	
	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}
	
}// class CircuitBoard
