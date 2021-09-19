package edu.spbu.sort;

import java.util.ArrayList;
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


        int[] a = new int[length];
        System.arraycopy(array, startIndex, a, 0, length);

        int m1 = length / 2;
        int i = 0, j = m1;

        while (i < m1 || j < length) {
            int index = startIndex + i + j - m1;
            if (i >= m1) {
                array[index] = a[j++];
                continue;
            }
            if (j >= endIndex - startIndex) {
                array[index] = a[i++];
                continue;
            }
            if (a[i] < a[j]) {
                array[index] = a[i++];
            } else {
                array[index] = a[j++];
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


        int[] a = new int[length];
        for (int i = 0; i < length; i++) {
            a[i] = list.get(startIndex + i);
        }

        int m1 = length / 2;
        int i = 0, j = m1;

        while (i < m1 || j < length) {
            int index = startIndex + i + j - m1;
            if (i >= m1) {
                list.set(index, a[j++]);
                continue;
            }
            if (j >= endIndex - startIndex) {
                list.set(index, a[i++]);
                continue;
            }
            if (a[i] < a[j]) {
                list.set(index, a[i++]);
            } else {
                list.set(index, a[j++]);
            }
        }

    }
}
