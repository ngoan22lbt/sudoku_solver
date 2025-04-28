package HUY;

// Base node class for Dancing Links structure
abstract class DLBaseNode {
    DLBaseNode up, down, left, right;

    DLBaseNode() {
        up = down = left = right = this;
    }
}

// Column header node for Dancing Links matrix
class DLHeaderNode extends DLBaseNode {
    // Number of 1s in the column, with this we can figure out which column has fewest 1s to choose this for efficient (heuristic to minimize branching)
    int size;

    DLHeaderNode() {
        super();
        size = 0;
    }
}

// Node for 1s in the Dancing Links matrix
class DLNode extends DLBaseNode {
    // Column header this node belongs to
    DLHeaderNode header;
    // Row index in the matrix (0 to 728)
    int rowNumber;

    DLNode(DLHeaderNode header, int rowNumber) {
        super();
        this.header = header;
        this.rowNumber = rowNumber;
    }
}