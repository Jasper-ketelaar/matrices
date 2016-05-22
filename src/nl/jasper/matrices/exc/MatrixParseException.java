package nl.jasper.matrices.exc;

/**
 * Created by jasperketelaar on 5/22/16.
 */
public class MatrixParseException extends IllegalArgumentException {

    public MatrixParseException() {
        super("Invalid arguments given for parsing a matrix.");
    }
}
