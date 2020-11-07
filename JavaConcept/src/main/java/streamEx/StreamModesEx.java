package java8.streamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StreamModesEx {

	public static void main(String[] args) {

		/*
		 * Modes of Streams:
		 * 		1. Collection.stream() creates a sequential stream
		 * 		2. Collection.parallelStream() creates a parallel stream.
		 */
		
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
		    list.add(i);
		}
		System.out.println("Input List = " + list);
		
//		Output:
//			Input List = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
		
		
		//Sequential Stream
		List serialList = new ArrayList();
		Stream<Integer> serialStream = list.stream();
		serialStream.filter(item -> item %2==0).forEach(item -> {
		    System.out.println("Thread = " + Thread.currentThread().getName());
		    serialList.add(item);
		});

		System.out.println("Serial Stream = " + serialList);
//		Output:
//			Thread = main
//			Thread = main
//			Thread = main
//			Thread = main
//			Thread = main
//			Serial Stream = [0, 2, 4, 6, 8]
	
		// Parallel Stream
		List parallelList = new ArrayList();
		Stream<Integer> parallelStream = list.parallelStream();
		parallelStream.filter(item -> item %2==0).forEach(item -> {
		    System.out.println("Thread = " + Thread.currentThread().getName());
		    parallelList.add(item);
		});

		System.out.println("Parallel Stream = " + parallelList);
		
//		Output:
//			Thread = main
//			Thread = ForkJoinPool.commonPool-worker-19
//			Thread = ForkJoinPool.commonPool-worker-27
//			Thread = ForkJoinPool.commonPool-worker-5
//			Thread = ForkJoinPool.commonPool-worker-9
//			Parallel Stream = [6, 2, 0, 8, 4]
		
		System.out.println("is serialStream Parallel: " + serialStream.isParallel());
		System.out.println("is parallelStream Parallel: " + parallelStream.isParallel());
		
//		Output:
//			is serialStream Parallel: false
//			is parallelStream Parallel: true
	}

}
