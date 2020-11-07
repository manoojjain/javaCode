package java8.streamEx;

import java.util.stream.IntStream;

public class IntStreamEx {

	public static void main(String[] args) {
		int[] numbers = {4,1,13,19,90,5};
		
		//To get the min number 
		int minNum = IntStream.of(numbers).min().getAsInt();
		System.out.println("Min number is: "+minNum);	
		//Output: Min number is: 1
		
		//To get max number 
		int maxNum = IntStream.of(numbers).max().getAsInt();
		System.out.println("Max number is: "+maxNum);
		//Output: Max number is: 90
		
		//To get count 
		int count = (int) IntStream.of(numbers).count();
		System.out.println("Count of numbers: "+count);
		//Output: Count of numbers: 6
		
		//To get sum 
		int sum = (int) IntStream.of(numbers).sum();
		System.out.println("Sum of numbers: "+sum);
		//Output: Sum of numbers: 132
		
		// Print the min number with the consumer (ifPresent will take the consumer)
		IntStream.of(numbers).min().ifPresent(min -> System.out.println("Available min number: "+min));
		//Output: Available min number: 1

		// Filter will take the predicates and foreach will take consumer
		IntStream.of(numbers).filter(i -> (i%2==0)).forEach(i -> System.out.println("Even numbers: "+i));
		//Output: Even numbers: 4
		//Even numbers: 90
		
		//Sort the numbers and get the shortest 3 distinct numbers 
		IntStream.of(numbers).sorted().distinct().limit(3).forEach(System.out::println);
		//Output: 1 4 5
	

	}

}
