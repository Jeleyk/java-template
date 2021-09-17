package edu.spbu.matrix;

/**
 *
 */
public interface Matrix {
    /**
     * однопоточное умнджение матриц
     * должно поддерживаться для всех 4-х вариантов
     *
     * @param o
     * @return
     */
    Matrix mul(Matrix o);

    /**
     * многопоточное умножение матриц
     *
     * @param o
     * @return
     */
    Matrix dmul(Matrix o);

    int getElement(int i, int j);

    int getColumnsCount();

    int getLinesCount();



}
