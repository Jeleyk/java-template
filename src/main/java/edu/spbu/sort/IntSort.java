package edu.spbu.sort;

import java.util.Arrays;
import java.util.Collections;
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

        int m = (startIndex + endIndex) / 2;
        mergeSort(array, startIndex, m);
        mergeSort(array, m, endIndex);


        int i = startIndex, j = m;
        while (i < m || j < endIndex) {
            int index = i + j - m;
            if (i >= m) {
                array[index] = array[j++];
                continue;
            }
            if (j >= endIndex) {
                array[index] = array[i++];
                continue;
            }
            if (array[i] < array[j]) {
                array[index] = array[i++];
            } else {
                array[index] = array[j++];
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

        int m = (startIndex + endIndex) / 2;
        mergeSort(list, startIndex, m);
        mergeSort(list, m, endIndex);


        int i = startIndex, j = m;
        while (i < m || j < endIndex) {
            int index = i + j - m;
            if (i >= m) {
                list.set(index, list.get(j++));
                continue;
            }
            if (j >= endIndex) {
                list.set(index, list.get(i++));
                continue;
            }
            if (list.get(i) < list.get(j)) {
                list.set(index, list.get(i++));
            } else {
                list.set(index, list.get(j++));
            }
        }

    }
}
