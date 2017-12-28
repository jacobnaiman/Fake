package edu.yu.cs.dataStructures.fall2016.SimpleSQLParser;

	/**
	 * Example 116 - A Generic Quicksort Method for Comparable Values
	 */
	public class GenericQuicksort {

	    public static <T extends Comparable<T>> void qsort(T[] arr, int a, int b) {
	        if (a < b) {
	            int i = a, j = b;
	            T x = arr[(i + j) / 2];

	            do {
	                while (arr[i].compareTo(x) < 0) i++;
	                while (x.compareTo(arr[j]) < 0) j--;

	                if ( i <= j) {
	                    T tmp = arr[i];
	                    arr[i] = arr[j];
	                    arr[j] = tmp;
	                    i++;
	                    j--;
	                }

	            } while (i <= j);

	            qsort(arr, a, j);
	            qsort(arr, i, b);
	        }
	    }

	    public static void main(String[] args) {
	    	GenericQuicksort test = new GenericQuicksort();
	        Integer[] integers = {30, 20, 10, 5, 6, 99};
	        String[] strings = {"hi", "hey", "z", "Ho", "yo", "hu", "how", "you", "justin", "just", "Just"};
	        Double[] doubles = {2.3, 5.4, 6.701, 6.7001, 5.4, 5.41};
	        test.<Integer>qsort(integers, 0, integers.length-1);
	        GenericQuicksort.<String>qsort(strings, 0, strings.length-1);
	        GenericQuicksort.<Double>qsort(doubles, 0, doubles.length-1);
	        for(Integer i: integers) {
	            System.out.println(i);
	        }
	        for(String i: strings) {
	            System.out.println(i);
	        }
	        for(Double i: doubles) {
	            System.out.println(i);
	        }
	        System.out.println(strings[0]);

	    }
	}
