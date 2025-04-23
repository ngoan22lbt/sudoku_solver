// Base node class for Dancing Links structure
abstract class DLBaseNode {
    DLBaseNode up, down, left, right;

    DLBaseNode() {
        up = down = left = right = this;
    }
}

// Column header node for Dancing Links matrix
class DLHeaderNode extends DLBaseNode {
    int size; // Number of 1s in the column

    DLHeaderNode() {
        super();
        size = 0;
    }
}

// Node for 1s in the Dancing Links matrix
class DLNode extends DLBaseNode {
    DLHeaderNode header; // Column header this node belongs to
    int rowNumber; // Row index in the matrix (0 to 728)

    DLNode(DLHeaderNode header, int rowNumber) {
        super();
        this.header = header;
        this.rowNumber = rowNumber;
    }
}