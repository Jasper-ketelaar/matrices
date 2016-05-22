package nl.jasper.matrices.square.homogeneous;

import nl.jasper.matrices.square.SquareMatrix;

/**
 * Created by jasperketelaar on 5/22/16.
 */
public abstract class HomogeneousMatrix extends SquareMatrix {

    public HomogeneousMatrix(int dim) {
        super(dim + 1);
    }
}
