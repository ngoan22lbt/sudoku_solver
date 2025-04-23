// Manages the Dancing Links matrix construction and operations
class DancingLinks {
    private static final int NUM_ROWS = 729; // 9 rows * 9 cols * 9 numbers
    private static final int NUM_COLS = 324; // 81 cells + 81 rows + 81 cols + 81 boxes
    private DLHeaderNode masterNode;
    private DLHeaderNode[] topColumnNodeList;
    private DLNode[] topRowNodeList;

    public DancingLinks(int[][] matrix) {
        initializeMatrix();
        setupRows();
        handlePreFilledEntries(matrix);
    }

    private void initializeMatrix() {
        masterNode = new DLHeaderNode();
        topColumnNodeList = new DLHeaderNode[NUM_COLS];
        topRowNodeList = new DLNode[NUM_ROWS];

        // Set up column headers
        DLHeaderNode prevCol = masterNode;
        for (int c = 0; c < NUM_COLS; c++) {
            DLHeaderNode currentCol = new DLHeaderNode();
            topColumnNodeList[c] = currentCol;
            currentCol.left = prevCol;
            prevCol.right = currentCol;
            prevCol = currentCol;
        }
        topColumnNodeList[NUM_COLS - 1].right = masterNode;
        masterNode.left = topColumnNodeList[NUM_COLS - 1];
    }

    private void setupRows() {
        for (int i = 0; i < NUM_ROWS; i++) {
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
            addNodeToColumn(node1, topColumnNodeList[node1Index]);
            addNodeToColumn(node2, topColumnNodeList[node2Index]);
            addNodeToColumn(node3, topColumnNodeList[node3Index]);
            addNodeToColumn(node4, topColumnNodeList[node4Index]);

            // Update column sizes
            topColumnNodeList[node1Index].size++;
            topColumnNodeList[node2Index].size++;
            topColumnNodeList[node3Index].size++;
            topColumnNodeList[node4Index].size++;
        }
    }

    private void handlePreFilledEntries(int[][] matrix) {
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
    }

    private void addNodeToColumn(DLNode nodeToAdd, DLHeaderNode columnTop) {
        DLBaseNode temp = columnTop;
        while (temp.down != columnTop && temp.down != null) {
            temp = temp.down;
        }
        temp.down = nodeToAdd;
        nodeToAdd.up = temp;
        nodeToAdd.down = columnTop;
        columnTop.up = nodeToAdd;
    }

    public void coverColumn(DLHeaderNode topColumnNode) {
        topColumnNode.right.left = topColumnNode.left;
        topColumnNode.left.right = topColumnNode.right;

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

    public void uncoverColumn(DLHeaderNode topColumnNode) {
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

        topColumnNode.right.left = topColumnNode;
        topColumnNode.left.right = topColumnNode;
    }

    public DLHeaderNode getMasterNode() {
        return masterNode;
    }

    public DLHeaderNode getSmallestColumn() {
        DLHeaderNode c = (DLHeaderNode) masterNode.right;
        DLHeaderNode tempC = (DLHeaderNode) masterNode.right;
        while (tempC != masterNode) {
            if (tempC.size < c.size) {
                c = tempC;
            }
            tempC = (DLHeaderNode) tempC.right;
        }
        return c;
    }
}