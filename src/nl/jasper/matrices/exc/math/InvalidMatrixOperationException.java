package nl.jasper.matrices.exc.math;

/**
 * Created by jasperketelaar on 5/22/16.
 */
public class InvalidMatrixOperationException extends ArithmeticException {

    public InvalidMatrixOperationException(String type, String message) {
        super("Invalid matrix operation of type: " + type + (message.length() > 0 ? "\n" + message : ""));
    }

    public InvalidMatrixOperationException(String type) {
        this(type, "");
    }
}
