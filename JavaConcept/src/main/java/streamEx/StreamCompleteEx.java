package java8.streamEx;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamCompleteEx {

	public static void main(String[] args) {

		
		/*
		 * The anatomy of a stream is defined as:
		 * 
		 * 1. Obtain the source (source)
		 *		a. Stream.of(val1, val2,….)
		 *		b. List.stream()
		 *		c. Stream.of(arrayOfElements)
		 *		d. String chars or String tokens
		 *
		 * 2. Process (intermediate operations)
		 *		a. Stream.filter()
		 *		b. Stream.map()
		 *		c. Stream.sorted()
		 *
		 * 3. Get the result (terminal operation)
		 * 		a. Stream.forEach()
		 * 		b. Stream.collect()
		 * 		c. Stream.match()
		 * 
		 */
		
		/*
		 * *******************************************S
		 * 1. How to obtain a Stream?
		 * ********************************************
		 */
		
		// 1. using Stream.of(val1, val2,….)
		Stream<String> stream1 = Stream.of("Iron Man", "Rajinikanth", "Katara");
		stream1.forEach(item -> System.out.println(item));

		// 2. using List.stream()
		List<String> list = Arrays.asList("Iron Man", "Rajinikanth", "Katara");
		Stream<String> stream2 = list.stream();
		stream2.forEach(item -> System.out.println(item));

		// 3. using Stream.of(arrayOfElements)
		Stream<String> stream3 = Stream.of( new String[]{"Iron Man", "Rajinikanth", "Katara"} );
		stream3.forEach(item -> System.out.println(item));

		// 4. using String chars or String tokens
		IntStream stream4 = "1_k".chars();
		stream4.forEach(item -> System.out.println(item));

		Stream<String> stream5 = Stream.of("A,B,C".split(","));
		stream5.forEach(item -> System.out.println(item));
		
		/*
		 * 2. Process (intermediate operations)
		 */
		
		List<String> numbers = Arrays.asList(new String[]{"one","two","three","four","five"});
		
		//.filter() is provided with the condition to filter out all strings with length more than 3.
		//filter takes Predicates as the argument
		
		//.map() converts each element into another object using the given function. 
		//In this example, it converts each string into its uppercase string.
		//map takes function as argument
		
		//.sorted() creates a sorted stream. But, it doesn’t sort the numbers list.
		numbers.stream()
		        .sorted()
		        .filter(s -> s.length() > 3)
		        .map(String::toUpperCase)
		        .forEach(System.out::println);
		
		//Output:
		//FIVE
		//FOUR
		//THREE
		
		
		/*
		 *  3. Get the result (terminal operation)
		 */
		
		// Stream.collect()
		List<String> stringsAsUppercaseList = numbers.stream()
		        .map(value -> value.toUpperCase())
		        .collect(Collectors.toList());

		System.out.println(stringsAsUppercaseList);
		
//		Output:
//			[ONE, TWO, THREE, FOUR, FIVE]
	

		//Stream.match()
		boolean result = numbers.stream()
		        .anyMatch((s) -> s.length() > 3);
	
		System.out.println(result);
	
		result = numbers.stream()
		        .allMatch((s) -> s.length() > 3);
	
		System.out.println(result);
	
		result = numbers.stream()
		        .noneMatch((s) -> s.length() > 3);
	
		System.out.println(result);
		
//		Output:
//			true
//			false
//			false

	}
}
