package edu.spbu.matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix {

    private List<List<Integer>> source;

    /**
     * загружает матрицу из файла
     *
     * @param fileName
     */
    public DenseMatrix(String fileName) {
        source = new ArrayList<>();
        try (FileReader fileReader = new FileReader(fileName);
             final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while (bufferedReader.ready()) {
                List<Integer> list = new ArrayList<>();

                final String[] split = bufferedReader.readLine().split(" ");
                for (String s : split) {
                    list.add(Integer.parseInt(s));
                }

                source.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DenseMatrix(int lines, int columns) {
        this(new int[lines][columns]);
    }

    public DenseMatrix(int[][] array) {
        source = new ArrayList<>();

        for (int[] ints : array) {
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < array[0].length; j++) {
                list.add(ints[j]);
            }
            source.add(list);
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
            for (int i = 0; i < getLinesCount(); i++) {
                for (int j = 0; j < getColumnsCount(); j++) {
                    if (getElement(i, j) == 0) continue;
                    for (int k = 0; k < o.getColumnsCount(); k++) {
                        array[i][k] += getElement(i, j) * o.getElement(j, k);
                    }
                }
            }
        } else if (o instanceof SparseMatrix) {
            SparseMatrix sparseMatrix = (SparseMatrix) o;
            sparseMatrix.getElements().forEach(entry -> {
                for (int k = 0; k < getLinesCount(); k++) {
                    array[k][entry.getKey().column] += getElement(k, entry.getKey().line) * entry.getValue();
                }
            });
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
            AtomicReferenceArray<AtomicIntegerArray> array1 = new AtomicReferenceArray<>(getLinesCount());
            for (int i = 0; i < o.getColumnsCount(); i++) {
                array1.set(i, new AtomicIntegerArray(o.getColumnsCount()));
            }
            fillArray(array1, o, 8);
            for (int i = 0; i < getLinesCount(); i++) {
                for (int j = 0; j < o.getColumnsCount(); j++) {
                    array[i][j] = array1.get(i).get(j);
                }
            }
        } else if (o instanceof SparseMatrix) {
            SparseMatrix sparseMatrix = (SparseMatrix) o;
            sparseMatrix.getElements().parallelStream().forEach(entry -> {
                for (int k = 0; k < getLinesCount(); k++) {
                    array[k][entry.getKey().column] += getElement(k, entry.getKey().line) * entry.getValue();
                }
            });
        }
        return MatrixUtil.generateMatrix(array);
    }

    private void fillArray(AtomicReferenceArray<AtomicIntegerArray> array, Matrix o, int parts) {
        ExecutorService service = Executors.newFixedThreadPool(parts);
        int linesCount = getLinesCount();
        List<Future<?>> list = new ArrayList<>();
        for (int i = 0; i < parts; i++) {
            int i1 = i;
            list.add(service.submit(new Thread(() ->
                    fillArray(array, linesCount / parts * i1, linesCount / parts * (i1 + 1), o))));
        }

        try {
            for (Future<?> future : list) {
                future.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        service.shutdown();
    }

    private void fillArray(AtomicReferenceArray<AtomicIntegerArray> array, int startLine, int endLine, Matrix o) {
        for (int i = startLine; i < endLine; i++) {
            for (int j = 0; j < getColumnsCount(); j++) {
                if (getElement(i, j) == 0) continue;
                for (int k = 0; k < o.getColumnsCount(); k++) {
                    array.get(i).addAndGet(k, getElement(i, j) * o.getElement(j, k));
                }
            }
        }
    }

    @Override
    public int getElement(int i, int j) {
        return source.get(i).get(j);
    }

    @Override
    public int getColumnsCount() {
        return source.get(0).size();
    }

    @Override
    public int getLinesCount() {
        return source.size();
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
}
