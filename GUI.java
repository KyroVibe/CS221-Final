import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;

/**
 * GUI class for displaying the results of the CircuitTracer search.
 * Uses Java swing graphics library.
 * @author Hunter Barclay
 */
public class GUI extends JFrame {

    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 800;
    private static final int SOLUTIONS_WIDTH = 250;
    private static final int SOLUTION_CELL_HEIGHT = 25;

    private static final Color START_COLOR = new Color(0x57bcf2);
    private static final Color END_COLOR = new Color(0xe03a3a);
    private static final Color TRACE_COLOR = new Color(0xe45eeb);

    private static final Font BOARD_FONT = new Font("Arial", Font.BOLD, 50);

    private ArrayList<CircuitBoard> mSolvedBoards;
    private JTextField[][] mBoardSpots;

    private int mViewingSolution;

    /** Creates a new GUI window to display search results from CircuitTracer.
     * @param unsolvedBoard Default, unsolved board as read from the provided file.
     * @param solvedBoards List of solved boards to display to the user.
     */
    public GUI(CircuitBoard unsolvedBoard, ArrayList<CircuitBoard> solvedBoards) {

        super("--={ Circuit Tracer }=--");

        mSolvedBoards = solvedBoards;

        setResizable(false);

        // Create MenuBar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem fileExitItem = new JMenuItem("Exit");
        fileExitItem.addActionListener((e) -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        fileMenu.add(fileExitItem);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        JMenuItem helpAboutItem = new JMenuItem("About...");

        helpAboutItem.addActionListener(
            (e) -> JOptionPane.showMessageDialog(
                this, 
                "Circuit Tracer\nBy Hunter \"The Great\" Barclay", 
                "--={ About }=--", 
                JOptionPane.INFORMATION_MESSAGE
            )
        );
        helpMenu.add(helpAboutItem);

        setJMenuBar(menuBar);

        // Setup frame and actually make it the right size. So annoying
        getContentPane().setPreferredSize(new Dimension(BOARD_WIDTH + SOLUTIONS_WIDTH, BOARD_HEIGHT));
        pack();
        CenterFrame(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Create board components
        mBoardSpots = new JTextField[unsolvedBoard.numRows()][unsolvedBoard.numCols()];
        int width = BOARD_WIDTH / unsolvedBoard.numCols();
        int height = BOARD_HEIGHT / unsolvedBoard.numRows();
        for (int x = 0; x < unsolvedBoard.numCols(); x++) {
            for (int y = 0; y < unsolvedBoard.numRows(); y++) {
                JTextField spot = new JTextField(String.format("%c", unsolvedBoard.charAt(y, x)));
                spot.setHorizontalAlignment(JTextField.CENTER);
                spot.setBounds(x * width, y * height, width, height);
                spot.setEditable(false);
                spot.setDisabledTextColor(Color.BLACK);
                spot.setEnabled(false);
                spot.setFont(BOARD_FONT);

                switch (unsolvedBoard.charAt(y, x)) {
                    case CircuitBoard.START:
                        spot.setBackground(START_COLOR);
                        break;
                    case CircuitBoard.END:
                        spot.setBackground(END_COLOR);
                        break;
                    case CircuitBoard.TRACE:
                        spot.setBackground(TRACE_COLOR);
                        break;
                    default:
                        spot.setBackground(Color.WHITE);
                        break;
                }

                mBoardSpots[y][x] = spot;

                getContentPane().add(spot);
            }
        }

        JLabel numSolutionsLabel = new JLabel(String.format("%d Solution%s", solvedBoards.size(), solvedBoards.size() != 1 ? "s" : ""));
        getContentPane().add(numSolutionsLabel);
        numSolutionsLabel.setBounds(BOARD_WIDTH, 0, SOLUTIONS_WIDTH, 30);

        mViewingSolution = -1;
        // Not saving this object because it isn't modified after the constructor
        JList<String> mSolutionsList = new JList<String>(new SolutionsListModel(solvedBoards.size()));
        mSolutionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mSolutionsList.setLayoutOrientation(JList.VERTICAL);
        mSolutionsList.setFixedCellHeight(SOLUTION_CELL_HEIGHT);
        mSolutionsList.setBounds(BOARD_WIDTH, 30, SOLUTIONS_WIDTH, BOARD_HEIGHT);
        mSolutionsList.addListSelectionListener((e) -> {
            if (mViewingSolution != mSolutionsList.getSelectedIndex()) {
                ViewSolution(mSolutionsList.getSelectedIndex());
            }
        });
        getContentPane().add(mSolutionsList);

        setVisible(true);
    }

    /** Loads a provided solution into the GUI for the user to view.
     * @param solutionIndex Index of the solution to display.
     */
    private void ViewSolution(int solutionIndex) {
        mViewingSolution = solutionIndex;

        SwingUtilities.invokeLater(() -> {
            CircuitBoard board = mSolvedBoards.get(mViewingSolution);
            for (int x = 0; x < board.numCols(); x++) {
                for (int y = 0; y < board.numRows(); y++) {
                    mBoardSpots[y][x].setText(String.format("%c", board.charAt(y, x)));

                    switch (board.charAt(y, x)) {
                        case CircuitBoard.START:
                            mBoardSpots[y][x].setBackground(START_COLOR);
                            break;
                        case CircuitBoard.END:
                            mBoardSpots[y][x].setBackground(END_COLOR);
                            break;
                        case CircuitBoard.TRACE:
                            mBoardSpots[y][x].setBackground(TRACE_COLOR);
                            break;
                        default:
                            mBoardSpots[y][x].setBackground(Color.WHITE);
                            break;
                    }
                }
            }
        });
    }

    /** Places the frame in the center of the window
     * @param frame The JFrame to center
     */
    private void CenterFrame(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(
            (screenSize.width - frame.getSize().width) / 2,
            (screenSize.height - frame.getSize().height) / 2,
            frame.getSize().width,
            frame.getSize().height
        );
    }

    /**
     * ListModel for generating the indices in the solutions list for the GUI.
     * 
     * @author Hunter Barclay
     */
    private class SolutionsListModel extends AbstractListModel<String> {

        private int mNumSolutions;

        /** Constructs the ListModel for the solutions list.
         * @param numSolutions Number of solutions to pick from.
         */
        public SolutionsListModel(int numSolutions) {
            mNumSolutions = numSolutions;
        }

        @Override
        public String getElementAt(int index) {
            return String.format("Solution %d", index + 1);
        }

        @Override
        public int getSize() {
            return mNumSolutions;
        }

    }
}
