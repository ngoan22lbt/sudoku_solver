package HUY;

public class test {

    public static int[][] initializeExactCoverMatrix() {
        int rows = 9, cols = 9, digits = 9;
        int totalRows = rows * cols * digits; // 729
        int totalCols = 4 * rows * cols;      // 324

        int[][] matrix = new int[totalRows][totalCols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                for (int d = 0; d < digits; d++) {
                    int matrixRow = r * 81 + c * 9 + d;

                    // 1. Cell constraint: (r, c) must have a digit
                    int cellConstraint = r * 9 + c;

                    // 2. Row constraint: row r must have digit d
                    int rowConstraint = 81 + r * 9 + d;

                    // 3. Column constraint: column c must have digit d
                    int colConstraint = 162 + c * 9 + d;

                    // 4. Block constraint: block must have digit d
                    int block = (r / 3) * 3 + (c / 3);
                    int blockConstraint = 243 + block * 9 + d;

                    matrix[matrixRow][cellConstraint] = 1;
                    matrix[matrixRow][rowConstraint] = 1;
                    matrix[matrixRow][colConstraint] = 1;
                    matrix[matrixRow][blockConstraint] = 1;
                }
            }
        }

        return matrix;
    }

    public static void main(String[] args) {
        int[][] matrix = initializeExactCoverMatrix();
        System.out.println("Matrix initialized with dimensions: " +
                matrix.length + " x " + matrix[0].length);
    }
}
