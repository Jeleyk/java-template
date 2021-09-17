package edu.spbu.matrix;

import java.util.Arrays;

public class MatrixUtil {

    private static final double SPARSE_ZEROS_COEFFICIENT = 0.90D;

    public static Matrix generateMatrix(int[][] array) {
        int zeros = 0;
        for (int[] ints : array) {
            zeros += Arrays.stream(ints)
                    .filter(i -> i == 0)
                    .count();
        }
        int arraySize = array.length * array[0].length;
        if (zeros >= arraySize * SPARSE_ZEROS_COEFFICIENT) {
            return new SparseMatrix(array);
        } else {
            return new DenseMatrix(array);
        }
    }

}
