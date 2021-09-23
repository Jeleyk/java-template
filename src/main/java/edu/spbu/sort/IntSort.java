package edu.spbu.sort;

import java.util.List;

/**
 * Created by artemaliev on 07/09/15.
 */
public class IntSort {
    public static void sort(int[] array) {
        mergeSort(array);
    }

    private static void mergeSort(int[] array) {
        mergeSort(array, 0, array.length);
    }

    private static void mergeSort(int[] array, int startIndex, int endIndex) {

        int length = endIndex - startIndex;

        if (endIndex - startIndex <= 1) {
            return;
        }

        int centerIndex = (startIndex + endIndex) / 2;
        mergeSort(array, startIndex, centerIndex);
        mergeSort(array, centerIndex, endIndex);


        int[] arrayCopy = new int[length];
        System.arraycopy(array, startIndex, arrayCopy, 0, length);

        int m1 = length / 2;
        int i = 0, j = m1;
        while (i < m1 || j < length) {
            int index = startIndex + i + j - m1;
            if (i >= m1) {
                array[index] = arrayCopy[j++];
                continue;
            }
            if (j >= endIndex - startIndex) {
                array[index] = arrayCopy[i++];
                continue;
            }
            if (arrayCopy[i] < arrayCopy[j]) {
                array[index] = arrayCopy[i++];
            } else {
                array[index] = arrayCopy[j++];
            }
        }

    }

    public static void sort(List<Integer> list) {
        mergeSort(list);
    }

    private static void mergeSort(List<Integer> list) {
        mergeSort(list, 0, list.size());
    }

    private static void mergeSort(List<Integer> list, int startIndex, int endIndex) {

        int length = endIndex - startIndex;

        if (endIndex - startIndex <= 1) {
            return;
        }

        int centerIndex = (startIndex + endIndex) / 2;
        mergeSort(list, startIndex, centerIndex);
        mergeSort(list, centerIndex, endIndex);


        int[] arrayCopy = new int[length];
        for (int i = 0; i < length; i++) {
            arrayCopy[i] = list.get(startIndex + i);
        }

        int m1 = length / 2;
        int i = 0, j = m1;

        while (i < m1 || j < length) {
            int index = startIndex + i + j - m1;
            if (i >= m1) {
                list.set(index, arrayCopy[j++]);
                continue;
            }
            if (j >= endIndex - startIndex) {
                list.set(index, arrayCopy[i++]);
                continue;
            }
            if (arrayCopy[i] < arrayCopy[j]) {
                list.set(index, arrayCopy[i++]);
            } else {
                list.set(index, arrayCopy[j++]);
            }
        }

    }
}
