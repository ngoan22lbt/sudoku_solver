public class test {
    // Common base class for nodes in the Dancing Links structure
    static abstract class DLBaseNode {
        DLBaseNode up, down, left, right;

        DLBaseNode() {
            up = down = left = right = this;
        }
    }

    // Node for column headers in the Dancing Links matrix
    static class DLHeaderNode extends DLBaseNode {
        int size; // Number of 1s in the column

        DLHeaderNode() {
            super();
            size = 0;
        }
    }

    // Node for 1s in the Dancing Links matrix
    static class DLNode extends DLBaseNode {
        DLHeaderNode header; // Column header this node belongs to
        int rowNumber; // Row index in the matrix (0 to 728)

        DLNode(DLHeaderNode header, int rowNumber) {
            super();
            this.header = header;
            this.rowNumber = rowNumber;
        }
    }

    private boolean solutionFound; // Flag to stop search once solution is found
    private int[][] result; // Store the solved puzzle

    public int[][] solve(int[][] puzzle) {
        // Initialize result matrix by copying the input puzzle
        result = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                result[i][j] = puzzle[i][j];
            }
        }

        // Initialize Dancing Links structures
        solutionFound = false;
        dancingLinks(result);

        // Return the solved puzzle
        return result;
    }

    private void dancingLinks(int[][] matrix) {
        // Initialize the Dancing Links matrix
        int numRows = 729; // 9 rows * 9 cols * 9 numbers
        int numCols = 324; // 81 cells + 81 rows + 81 cols + 81 boxes
        DLHeaderNode masterNode = new DLHeaderNode();
        DLHeaderNode[] topColumnNodeList = new DLHeaderNode[numCols];
        DLNode[] topRowNodeList = new DLNode[numRows];

        // Set up column headers
        DLHeaderNode prevCol = masterNode;
        for (int c = 0; c < numCols; c++) {
            DLHeaderNode currentCol = new DLHeaderNode();
            topColumnNodeList[c] = currentCol;
            currentCol.left = prevCol;
            prevCol.right = currentCol;
            prevCol = currentCol;
        }
        topColumnNodeList[numCols - 1].right = masterNode;
        masterNode.left = topColumnNodeList[numCols - 1];

        // Set up rows (each representing a possible number placement)
        for (int i = 0; i < numRows; i++) {
            int r = i / 81; // Row of the Sudoku grid
            int c = (i % 81) / 9; // Column of the Sudoku grid
            int num = (i % 9) + 1; // Number (1 to 9)

            // Calculate column indices for the four constraints
            int node1Index = r * 9 + c; // Cell constraint
            int node2Index = 81 + r * 9 + (num - 1); // Row-number constraint
            int node3Index = 162 + c * 9 + (num - 1); // Column-number constraint
            int b = (r / 3) * 3 + (c / 3); // Box index
            int node4Index = 243 + b * 9 + (num - 1); // Box-number constraint

            // Create four nodes for this row
            DLNode node1 = new DLNode(topColumnNodeList[node1Index], i);
            DLNode node2 = new DLNode(topColumnNodeList[node2Index], i);
            DLNode node3 = new DLNode(topColumnNodeList[node3Index], i);
            DLNode node4 = new DLNode(topColumnNodeList[node4Index], i);

            // Connect nodes horizontally
            node1.right = node2;
            node2.left = node1;
            node2.right = node3;
            node3.left = node2;
            node3.right = node4;
            node4.left = node3;
            node4.right = node1;
            node1.left = node4;

            topRowNodeList[i] = node1;

            // Add nodes to their respective columns
            addNodeToBottomOfAColumn(node1, topColumnNodeList[node1Index]);
            addNodeToBottomOfAColumn(node2, topColumnNodeList[node2Index]);
            addNodeToBottomOfAColumn(node3, topColumnNodeList[node3Index]);
            addNodeToBottomOfAColumn(node4, topColumnNodeList[node4Index]);

            // Update column sizes
            topColumnNodeList[node1Index].size++;
            topColumnNodeList[node2Index].size++;
            topColumnNodeList[node3Index].size++;
            topColumnNodeList[node4Index].size++;
        }

        // Handle pre-filled Sudoku entries
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (matrix[i][j] != 0) {
                    int num = matrix[i][j];
                    int rowNumber = (num - 1) + i * 81 + j * 9; // Matrix row index
                    DLNode headNodeFromARow = topRowNodeList[rowNumber];
                    coverColumn(headNodeFromARow.header);
                    DLNode temp = (DLNode) headNodeFromARow.right;
                    while (temp != headNodeFromARow) {
                        coverColumn(temp.header);
                        temp = (DLNode) temp.right;
                    }
                }
            }
        }

        // Solve the puzzle
        dlSearch(0, masterNode, matrix);
    }

    private void dlSearch(int k, DLHeaderNode masterNode, int[][] matrix) {
        if (!solutionFound && masterNode.right == masterNode) {
            solutionFound = true; // Solution found
            return;
        }

        // Choose the column with the smallest size
        DLHeaderNode c = (DLHeaderNode) masterNode.right;
        DLHeaderNode tempC = (DLHeaderNode) masterNode.right;
        while (tempC != masterNode) {
            if (tempC.size < c.size) {
                c = tempC;
            }
            tempC = (DLHeaderNode) tempC.right;
        }

        coverColumn(c);

        DLBaseNode r = c.down; // Use DLBaseNode to allow comparison with c
        while (r != c && !solutionFound) {
            // Cast to DLNode for row-specific fields
            DLNode rowNode = (DLNode) r;
            // Update the matrix with the chosen row
            int row = rowNode.rowNumber / 81;
            int col = (rowNode.rowNumber % 81) / 9;
            int num = (rowNode.rowNumber % 9) + 1;
            matrix[row][col] = num;

            // Cover columns for this row
            DLBaseNode j = r.right;
            while (j != r) {
                coverColumn(((DLNode) j).header);
                j = j.right;
            }

            // Recurse
            dlSearch(k + 1, masterNode, matrix);

            if (solutionFound) return;

            // Backtrack: undo the matrix update
            matrix[row][col] = 0;

            // Uncover columns in reverse order
            j = r.left;
            while (j != r) {
                uncoverColumn(((DLNode) j).header);
                j = j.left;
            }

            r = r.down;
        }

        uncoverColumn(c);
    }

    private void addNodeToBottomOfAColumn(DLNode nodeToAdd, DLHeaderNode columnTop) {
        DLBaseNode temp = columnTop;
        while (temp.down != columnTop && temp.down != null) {
            temp = temp.down;
        }
        temp.down = nodeToAdd;
        nodeToAdd.up = temp;
        nodeToAdd.down = columnTop;
        columnTop.up = nodeToAdd;
    }

    private void coverColumn(DLHeaderNode topColumnNode) {
        // Remove the column from the header list
        topColumnNode.right.left = topColumnNode.left;
        topColumnNode.left.right = topColumnNode.right;

        // Remove all rows that have a 1 in this column
        DLBaseNode i = topColumnNode.down;
        while (i != topColumnNode) {
            DLBaseNode j = i.right;
            while (j != i) {
                j.down.up = j.up;
                j.up.down = j.down;
                ((DLNode) j).header.size--;
                j = j.right;
            }
            i = i.down;
        }
    }

    private void uncoverColumn(DLHeaderNode topColumnNode) {
        // Restore rows in reverse order
        DLBaseNode i = topColumnNode.up;
        while (i != topColumnNode) {
            DLBaseNode j = i.left;
            while (j != i) {
                ((DLNode) j).header.size++;
                j.down.up = j;
                j.up.down = j;
                j = j.left;
            }
            i = i.up;
        }

        // Restore the column to the header list
        topColumnNode.right.left = topColumnNode;
        topColumnNode.left.right = topColumnNode;
    }



}

