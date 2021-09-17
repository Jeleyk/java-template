package edu.spbu;

import edu.spbu.matrix.*;

public class MatrixPerfTest {
    public static final String MATRIX1_NAME = "m1.txt";
    public static final String MATRIX2_NAME = "m2.txt";

    public static void main(String s[]) {

        System.out.println("Starting loading dense matrices");
        Matrix m1 = new DenseMatrix(MATRIX1_NAME);
        System.out.println("1 loaded");
        Matrix m2 = new DenseMatrix(MATRIX2_NAME);
        System.out.println("2 loaded");
        long start = System.currentTimeMillis();
        Matrix r1 = m1.mul(m2);
        System.out.println("Dense Matrix time: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        Matrix r2 = m1.dmul(m2);
        System.out.println("Dense Matrix multithreading time: " + (System.currentTimeMillis() - start));

        System.out.println("Starting loading sparse matrices");
        m1 = new SparseMatrix(MATRIX1_NAME);
        System.out.println("1 loaded");
        m2 = new SparseMatrix(MATRIX2_NAME);
        System.out.println("2 loaded");
        start = System.currentTimeMillis();
        Matrix r3 = m1.mul(m2);
        System.out.println("Sparse Matrix time: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        Matrix r4 = m1.dmul(m2);
        System.out.println("Sparse Matrix multithreading time: " + (System.currentTimeMillis() - start));

        System.out.println("equals: " + r1.equals(r2));
        System.out.println("equals: " + r2.equals(r3));
        System.out.println("equals: " + r3.equals(r4));
    }
}
