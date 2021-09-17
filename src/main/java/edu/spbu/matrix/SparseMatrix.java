package edu.spbu.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Разряженная матрица
 */
public class SparseMatrix implements Matrix {

    private int columns;
    private int lines;
    private final HashMap<Cell, Integer> source;

    /**
     * загружает матрицу из файла
     *
     * @param fileName
     */
    public SparseMatrix(String fileName) {
        source = new HashMap<>();
        lines = 0;
        try (FileReader fileReader = new FileReader(fileName);
             final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while (bufferedReader.ready()) {
                final String[] split = bufferedReader.readLine().split(" ");
                columns = split.length;

                for (int i = 0; i < split.length; i++) {
                    int value = Integer.parseInt(split[i]);
                    if (value != 0)
                        source.put(new Cell(lines, i), value);
                }

                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SparseMatrix(int lines, int columns) {
        source = new HashMap<>();
        this.columns = columns;
        this.lines = lines;
    }

    public SparseMatrix(int[][] array) {
        source = new HashMap<>();
        this.columns = array[0].length;
        this.lines = array.length;

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (array[i][j] != 0) {
                    source.put(new Cell(i, j), array[i][j]);
                }
            }
        }
    }

    /**
     * однопоточное умнджение матриц
     * должно поддерживаться для всех 4-х вариантов
     *
     * @param o
     * @return
     */
    @Override
    public Matrix mul(Matrix o) {
        if (o == null) return null;
        if (getColumnsCount() != o.getLinesCount()) return null;
        int[][] array = new int[getLinesCount()][o.getColumnsCount()];
        if (o instanceof DenseMatrix) {
            source.forEach((key, value) -> {
                for (int k = 0; k < o.getColumnsCount(); k++) {
                    array[key.line][k] += o.getElement(key.column, k) * value;
                }
            });
        } else if (o instanceof SparseMatrix) {
            SparseMatrix sparseMatrix = (SparseMatrix) o;
            Set<Map.Entry<Cell, Integer>> entrySet1 = getElements();
            Set<Map.Entry<Cell, Integer>> entrySet2 = sparseMatrix.getElements();
            entrySet1.forEach(entry1 ->
                    entrySet2.forEach(entry2 -> {
                                if (entry1.getKey().column == entry2.getKey().line) {
                                    array[entry1.getKey().line][entry2.getKey().column] +=
                                            entry1.getValue() * entry2.getValue();
                                }
                            }
                    ));
        }
        return MatrixUtil.generateMatrix(array);
    }

    /**
     * многопоточное умножение матриц
     *
     * @param o
     * @return
     */
    @Override
    public Matrix dmul(Matrix o) {
        if (o == null) return null;
        if (getColumnsCount() != o.getLinesCount()) return null;
        int[][] array = new int[getLinesCount()][o.getColumnsCount()];
        if (o instanceof DenseMatrix) {
            getElements().parallelStream().forEach(entry -> {
                for (int k = 0; k < o.getColumnsCount(); k++) {
                    array[entry.getKey().line][k] += o.getElement(entry.getKey().column, k) * entry.getValue();
                }
            });
        } else if (o instanceof SparseMatrix) {
            SparseMatrix sparseMatrix = (SparseMatrix) o;
            Set<Map.Entry<Cell, Integer>> entrySet1 = getElements();
            Set<Map.Entry<Cell, Integer>> entrySet2 = sparseMatrix.getElements();
            entrySet1.forEach(entry1 ->
                    entrySet2.parallelStream().forEach(entry2 -> {
                                if (entry1.getKey().column == entry2.getKey().line) {
                                    array[entry1.getKey().line][entry2.getKey().column] +=
                                            entry1.getValue() * entry2.getValue();
                                }
                            }
                    ));
        }
        return MatrixUtil.generateMatrix(array);
    }

    @Override
    public int getElement(int i, int j) {
        return getElement(new Cell(i, j));
    }

    public int getElement(Cell cell) {
        return source.getOrDefault(cell, 0);
    }

    public Set<Map.Entry<Cell, Integer>> getElements() {
        return source.entrySet();
    }

    @Override
    public int getColumnsCount() {
        return columns;
    }

    @Override
    public int getLinesCount() {
        return lines;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Matrix)) return false;
        Matrix matrix = ((Matrix) o);
        if (getColumnsCount() != matrix.getColumnsCount()) return false;
        if (getLinesCount() != matrix.getLinesCount()) return false;

        for (int i = 0; i < getLinesCount(); i++) {
            for (int j = 0; j < getColumnsCount(); j++) {
                if (getElement(i, j) != matrix.getElement(i, j)) return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getLinesCount(); i++) {
            for (int j = 0; j < getColumnsCount(); j++) {
                builder.append(getElement(i, j)).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public static class Cell {
        int line;
        int column;

        public Cell(int line, int column) {
            this.line = line;
            this.column = column;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return line == cell.line &&
                    column == cell.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(line, column);
        }
    }

}
