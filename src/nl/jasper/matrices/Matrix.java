package nl.jasper.matrices;

import nl.jasper.matrices.exc.MatrixParseException;
import nl.jasper.matrices.exc.math.InvalidMatrixOperationException;
import nl.jasper.matrices.square.SquareMatrix;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by jasperketelaar on 5/22/16.
 */
public class Matrix {

    private final int rows;
    private final int columns;
    private final float[][] values;

    protected Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.values = new float[rows][columns];
    }

    protected Matrix(float[][] values) {
        this.rows = values.length;
        this.columns = values[0].length;
        this.values = values;
    }

    public static Matrix parse(String string) {
        int rowsCount = StringUtils.countMatches(string, "\\") + 1;
        int valuesCount = StringUtils.countMatches(string, "&") + rowsCount;

        String trimmed = StringUtils.replaceChars(string, ' ', (char) 0).replaceAll("\n", "");
        if (rowsCount == 1) {
            float[][] values = {parseRow(trimmed)};
            return new Matrix(values);
        }

        if (valuesCount % rowsCount != 0) {
            throw new MatrixParseException();
        }

        int columnCount = valuesCount / rowsCount;

        float[][] values = new float[rowsCount][columnCount];
        String[] rowsString = string.split("\\\\");

        for (int i = 0; i < rowsCount; i++) {
            String row = rowsString[i];
            float[] rowValues = parseRow(row);
            values[i] = rowValues;
        }
        return new Matrix(values);
    }

    protected static float[] parseRow(String row) {
        String[] valuesString = row.split("&");
        float[] values = new float[valuesString.length];
        for (int i = 0; i < valuesString.length; i++) {
            values[i] = Float.parseFloat(valuesString[i]);
        }
        return values;
    }

    public static SquareMatrix create(int dim, int... values) {
        return SquareMatrix.fromMatrix(create(dim, dim, values));
    }

    public static Matrix create(int rows, int columns, int... values) {
        if (values.length != rows * columns) {
            throw new IllegalArgumentException("The supplied amount of values have to be exactly " +
                    "equal to the amount of values the matrix can hold");
        }
        Matrix matrix = new Matrix(rows, columns);
        matrix.fillWith(values);
        return matrix;
    }

    public static Matrix create(int rows, int columns, double... values) {
        if (values.length != rows * columns) {
            throw new IllegalArgumentException("The supplied amount of values have to be exactly " +
                    "equal to the amount of values the matrix can hold");
        }
        Matrix matrix = new Matrix(rows, columns);
        matrix.fillWith(values);
        return matrix;
    }

    public static Matrix create(int rows, int columns, float... values) {
        if (values.length != rows * columns) {
            throw new IllegalArgumentException("The supplied amount of values have to be exactly " +
                    "equal to the amount of values the matrix can hold");
        }
        Matrix matrix = new Matrix(rows, columns);
        matrix.fillWith(values);
        return matrix;
    }

    public static Matrix multiply(Matrix left, Matrix right) {
        if (left.getColumnCount() != right.getRowCount()) {
            throw new InvalidMatrixOperationException("Multiplication",
                    "The left matrix's column count must be equal to the right matrix's row count");
        }
        float[][] result = new float[left.rows][right.columns];
        for (int i = 0; i < left.rows; i++) {
            for (int i2 = 0; i2 < right.columns; i2++) {
                result[i][i2] = groupMultiply(left.getNthRow(i), right.getNthColumn(i2));
            }
        }
        return new Matrix(result);
    }

    protected static float groupMultiply(float[] groupA, float[] groupB) {
        assert groupA.length == groupB.length;
        float sum = 0;
        for (int i = 0; i < groupA.length; i++) {
            sum += (groupA[i] * groupB[i]);
        }
        return sum;
    }

    protected void fillWith(float... values) {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < rows; y++) {
                int index = x * columns + y;
                this.values[x][y] = values[index];
            }
        }
    }

    protected void fillWith(int... values) {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < rows; y++) {
                int index = x * columns + y;
                this.values[x][y] = (float) values[index];
            }
        }
    }

    protected void fillWith(double... values) {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < rows; y++) {
                int index = x * columns + y;
                this.values[x][y] = (float) values[index];
            }
        }
    }

    /**
     * 1 2 3
     * 4 5 6
     * 7 8 9
     * <p>
     * newRow = 2
     *
     * @param beginRow
     * @param beginCol
     * @param endRow
     * @param endCol
     * @return
     */
    public Matrix subMatrix(int beginRow, int beginCol, int endRow, int endCol) {
        if (beginRow < 0 || beginCol < 0 || beginRow >= getRowCount() || beginCol >= getColumnCount()
                || endRow < 0 || endCol < 0 || endRow >= getRowCount() || endCol >= getColumnCount()) {
            throw new MatrixParseException();
        }
        int newRowCount = endRow - beginRow + 1;
        float[][] subRows = new float[newRowCount][];
        for (int i = beginRow; i <= endRow; i++) {
            subRows[i - beginRow] = ArrayUtils.subarray(values[i], beginCol, endCol + 1);
        }
        return new Matrix(subRows);

    }

    public Matrix subtract(Matrix other) {
        if (other.getColumnCount() != this.getColumnCount() && other.getRowCount() != this.getRowCount())
            throw new InvalidMatrixOperationException("Subtraction",
                    "The row count/column count of both matrices must be equal");
        Matrix matrix = new Matrix(getRowCount(), getColumnCount());
        for (int i = 0; i < getRowCount(); i++) {
            for (int i2 = 0; i2 < getColumnCount(); i2++) {
                matrix.setValueAt(i, i2, this.getValueAt(i, i2) - other.getValueAt(i, i2));
            }
        }
        return matrix;
    }

    public Matrix add(Matrix other) {
        if (other.getColumnCount() != this.getColumnCount() && other.getRowCount() != this.getRowCount())
            throw new InvalidMatrixOperationException("Subtraction",
                    "The row count/column count of both matrices must be equal");
        Matrix matrix = new Matrix(getRowCount(), getColumnCount());
        for (int i = 0; i < getRowCount(); i++) {
            for (int i2 = 0; i2 < getColumnCount(); i2++) {
                matrix.setValueAt(i, i2, this.getValueAt(i, i2) + other.getValueAt(i, i2));
            }
        }
        return matrix;
    }

    public Matrix scale(float scalar) {
        Matrix matrix = new Matrix(getRowCount(), getColumnCount());
        for (int i = 0; i < getRowCount(); i++) {
            for (int i2 = 0; i2 < getColumnCount(); i2++) {
                matrix.setValueAt(i, i2, this.getValueAt(i, i2) * scalar);
            }
        }
        return matrix;
    }

    public float[] getNthRow(int n) {
        return values[n];
    }

    public float[] getNthColumn(int n) {
        float[] result = new float[this.getRowCount()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values[i][n];
        }
        return result;
    }

    public int getColumnCount() {
        return columns;
    }

    public int getRowCount() {
        return rows;
    }

    public Matrix leftMultiplyWith(Matrix otherMatrix) {
        return Matrix.multiply(this, otherMatrix);
    }

    public Matrix rightMultiplyWith(Matrix otherMatrix) {
        return Matrix.multiply(otherMatrix, this);
    }

    public float[][] getValues() {
        return values;
    }

    protected void setValues(float[][] values) {
        for (int i = 0; i < values.length; i++) {
            for (int i2 = 0; i2 < values[i].length; i2++) {
                this.values[i][i2] = values[i][i2];
            }
        }
    }

    protected void setValueAt(int row, int col, float val) {
        values[row][col] = val;
    }

    public void print() {
        System.out.println(toString());
    }

    public float getValueAt(int row, int column) {
        return values[row][column];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            for (int i2 = 0; i2 < values[0].length; i2++) {
                builder.append(values[i][i2]);
                if (i2 + 1 < values[0].length) {
                    builder.append(" | ");
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public String toParseableMatrixString() {
        return StringUtils.substringBeforeLast(StringUtils.replaceChars(toString(), '|', '&').replaceAll("\n", "\\\\\n"), "\\\n");
    }
}
