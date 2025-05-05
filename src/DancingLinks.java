// Manages the Dancing Links matrix construction and operations for solving Sudoku using the Exact Cover problem
class DancingLinks {
    // Constants defining the matrix dimensions
    // 9 rows * 9 cols * 9 numbers = 729 rows, representing every possible move (each cell can have values 1-9)
    private static final int NUM_ROWS = 729;
    // 81 cells + 81 row-number constraints + 81 column-number constraints + 81 box-number constraints = 324 columns
    // Each column represents a constraint that must be satisfied in the Sudoku grid
    private static final int NUM_COLS = 324;

    // The root node of the doubly-linked matrix, serving as the entry point to the column headers
    private DLHeaderNode masterNode;
    // Array of column header nodes, one for each of the 324 constraints
    private DLHeaderNode[] topColumnNodeList;
    // Array of the first node in each row, used to track the 729 rows
    private DLNode[] topRowNodeList;

    // Constructor: Initializes the Dancing Links matrix based on a given 9x9 Sudoku grid
    // @param matrix The initial Sudoku grid, where 0 represents empty cells
    public DancingLinks(int[][] matrix) {
        initializeMatrix();           // Set up the column headers
        setupRows();                 // Create the rows for all possible moves
        handlePreFilledEntries(matrix); // Process pre-filled cells in the Sudoku grid
    }

    // Initializes the circular doubly-linked list of column headers
    private void initializeMatrix() {
        // Create the master node (root of the matrix)
        masterNode = new DLHeaderNode();
        // Initialize the array to store column headers
        topColumnNodeList = new DLHeaderNode[NUM_COLS];
        // Initialize the array to store the first node of each row
        topRowNodeList = new DLNode[NUM_ROWS];

        // Set up column headers in a circular doubly-linked list
        DLHeaderNode prevCol = masterNode;
        for (int c = 0; c < NUM_COLS; c++) {
            // Create a new column header node
            DLHeaderNode currentCol = new DLHeaderNode();
            topColumnNodeList[c] = currentCol;
            // Link the current column header to the previous one (left)
            currentCol.left = prevCol;
            // Link the previous column header to the current one (right)
            prevCol.right = currentCol;
            prevCol = currentCol;
        }
        // Complete the circular list by linking the last column to the master node
        topColumnNodeList[NUM_COLS - 1].right = masterNode;
        masterNode.left = topColumnNodeList[NUM_COLS - 1];
    }

    // Sets up the rows of the matrix, representing all possible moves (729 rows)
    // Each row corresponds to placing a number (1-9) in a specific cell (row, col)
    private void setupRows() {
        for (int i = 0; i < NUM_ROWS; i++) {
            // Decode the row index into Sudoku grid coordinates and number
            int r = i / 81;           // Row of the Sudoku grid (0-8)
            int c = (i % 81) / 9;     // Column of the Sudoku grid (0-8)
            int num = (i % 9) + 1;    // Number to place (1-9)

            // Calculate column indices for the four constraints for this move
            int node1Index = r * 9 + c;                // Cell constraint: Each cell has exactly one number
            int node2Index = 81 + r * 9 + (num - 1);   // Row-number constraint: Each row has each number once
            int node3Index = 162 + c * 9 + (num - 1);  // Column-number constraint: Each column has each number once
            int b = (r / 3) * 3 + (c / 3);            // Box index (0-8 for 3x3 subgrids)
            int node4Index = 243 + b * 9 + (num - 1);  // Box-number constraint: Each box has each number once

            // Create four nodes for this row, one for each constraint
            DLNode node1 = new DLNode(topColumnNodeList[node1Index], i);
            DLNode node2 = new DLNode(topColumnNodeList[node2Index], i);
            DLNode node3 = new DLNode(topColumnNodeList[node3Index], i);
            DLNode node4 = new DLNode(topColumnNodeList[node4Index], i);

            // Connect the nodes horizontally to form a circular row
            node1.right = node2;
            node2.left = node1;
            node2.right = node3;
            node3.left = node2;
            node3.right = node4;
            node4.left = node3;
            node4.right = node1;
            node1.left = node4;

            // Store the first node of the row for easy access
            topRowNodeList[i] = node1;

            // Add each node to its respective column's vertical linked list
            addNodeToColumn(node1, topColumnNodeList[node1Index]);
            addNodeToColumn(node2, topColumnNodeList[node2Index]);
            addNodeToColumn(node3, topColumnNodeList[node3Index]);
            addNodeToColumn(node4, topColumnNodeList[node4Index]);

            // Increment the size of each column to track the number of 1s
            topColumnNodeList[node1Index].size++;
            topColumnNodeList[node2Index].size++;
            topColumnNodeList[node3Index].size++;
            topColumnNodeList[node4Index].size++;
        }
    }

    // Processes pre-filled cells in the Sudoku grid by covering the corresponding columns
    // This ensures that the constraints for pre-filled cells are satisfied
    // @param matrix The initial 9x9 Sudoku grid
    // Processes pre-filled cells in the Sudoku grid by covering the corresponding columns
// This ensures that the constraints for pre-filled cells are satisfied before the search begins
// @param matrix The initial 9x9 Sudoku grid, where 0 represents empty cells
    private void handlePreFilledEntries(int[][] matrix) {
        // Iterate through each cell in the 9x9 Sudoku grid
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // Check if the cell is pre-filled (contains a number 1-9, not 0)
                if (matrix[i][j] != 0) {
                    // Get the number in the pre-filled cell
                    int num = matrix[i][j];
                    // Calculate the row index in the DLX matrix for this move
                    // Formula: rowNumber = (num - 1) + i * 81 + j * 9
                    // - num - 1: Adjusts number (1-9) to 0-based index (0-8)
                    // - i * 81: Accounts for the row in the grid (81 possible moves per grid row)
                    // - j * 9: Accounts for the column in the grid (9 possible numbers per cell)
                    int rowNumber = (num - 1) + i * 81 + j * 9; // Row index for (i, j, num)
                    // Get the first node in the corresponding row of the DLX matrix
                    DLNode headNodeFromARow = topRowNodeList[rowNumber];
                    // Cover the column for the cell constraint (ensures this cell is filled)
                    coverColumn(headNodeFromARow.header);
                    // Cover the columns for the other constraints (row-number, column-number, box-number)
                    // Iterate through the other nodes in this row (representing the other constraints)
                    DLNode temp = (DLNode) headNodeFromARow.right;
                    while (temp != headNodeFromARow) {
                        coverColumn(temp.header);
                        temp = (DLNode) temp.right;
                    }
                }
            }
        }
    }

    // Adds a node to the vertical linked list of a column
    // @param nodeToAdd The node to add to the column
    // @param columnTop The header node of the column
    private void addNodeToColumn(DLNode nodeToAdd, DLHeaderNode columnTop) {
        // Start at the column header
        DLBaseNode temp = columnTop;
        // Traverse to the last node in the column
        while (temp.down != columnTop && temp.down != null) {
            temp = temp.down;
        }
        // Link the new node vertically
        temp.down = nodeToAdd;
        nodeToAdd.up = temp;
        nodeToAdd.down = columnTop;
        columnTop.up = nodeToAdd;
    }

    // Covers a column by removing it and its associated rows from the matrix
    // This is part of the DLX algorithm to temporarily exclude a column during the search
    // @param topColumnNode The header node of the column to cover
    public void coverColumn(DLHeaderNode topColumnNode) {
        // Remove the column from the horizontal linked list
        topColumnNode.right.left = topColumnNode.left;
        topColumnNode.left.right = topColumnNode.right;

        // Traverse each row in the column
        DLBaseNode i = topColumnNode.down;
        while (i != topColumnNode) {
            // For each node in the row, remove the row from its column
            DLBaseNode j = i.right;
            while (j != i) {
                // Unlink the node vertically
                j.down.up = j.up;
                j.up.down = j.down;
                // Decrement the size of the column
                ((DLNode) j).header.size--;
                j = j.right;
            }
            i = i.down;
        }
    }

    // Uncovers a previously covered column, restoring it and its associated rows
    // This reverses the coverColumn operation during backtracking
    // @param topColumnNode The header node of the column to uncover
    public void uncoverColumn(DLHeaderNode topColumnNode) {
        // Traverse the rows in reverse order (bottom to top)
        DLBaseNode i = topColumnNode.up;
        while (i != topColumnNode) {
            // For each node in the row, restore the row in its column
            DLBaseNode j = i.left;
            while (j != i) {
                // Restore the vertical links
                ((DLNode) j).header.size++;
                j.down.up = j;
                j.up.down = j;
                j = j.left;
            }
            i = i.up;
        }

        // Restore the column in the horizontal linked list
        topColumnNode.right.left = topColumnNode;
        topColumnNode.left.right = topColumnNode;
    }

    // Returns the master node (root) of the matrix
    // @return The master node
    public DLHeaderNode getMasterNode() {
        return masterNode;
    }
    // Finds the column with the smallest number of 1s (fewest remaining rows)
    // This heuristic improves the efficiency of the DLX algorithm
    // @return The column header with the smallest size
    public DLHeaderNode getSmallestColumn() {
        DLHeaderNode c = (DLHeaderNode) masterNode.right; // Start with the first column
        DLHeaderNode tempC = (DLHeaderNode) masterNode.right;
        // Iterate through all columns to find the one with the smallest size
        while (tempC != masterNode) {
            if (tempC.size < c.size) {
                c = tempC;
            }
            tempC = (DLHeaderNode) tempC.right;
        }
        return c;
    }
}