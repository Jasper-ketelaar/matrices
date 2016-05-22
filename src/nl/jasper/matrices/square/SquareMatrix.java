package nl.jasper.matrices.square;

import nl.jasper.matrices.Matrix;

/**
 * Created by jasperketelaar on 5/22/16.
 */
public class SquareMatrix extends Matrix {

    protected SquareMatrix(int dim) {
        super(dim, dim);
    }

    public static SquareMatrix fromMatrix(Matrix matrix) {
        if (matrix.getRowCount() != matrix.getColumnCount()) {
            throw new IllegalArgumentException("The matrix has to be squared");
        }
        SquareMatrix sqMatrix = new SquareMatrix(matrix.getRowCount());
        sqMatrix.setValues(matrix.getValues());
        return sqMatrix;
    }

    public float getDeterminant() {
        switch (getColumnCount()) {
            case 1:
                return getValueAt(0, 0);

            case 2:
                return getValueAt(0, 0) * getValueAt(1, 1) - getValueAt(1, 0) * getValueAt(0, 1);

            default:
                return 0;
        }
    }
}
