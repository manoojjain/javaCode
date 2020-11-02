package producer.consumer;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
	private Queue<Integer> list;
	private int size;

	public Buffer(int size) {
		this.list = new LinkedList<>();
		this.size = size;
	}

	public void produce() throws InterruptedException {
		int value = 0;
		while (true) {
			synchronized (this) {
				while (list.size() >= size) {
					// wait for the consumer
					wait();
				}

				list.add(value);
				System.out.println("Produced " + value);
				value++;

				// notify the consumer
				notify();
				Thread.sleep(1000);
			}
		}
	}

	public void consume() throws InterruptedException {
		while (true) {
			synchronized (this) {
				while (list.size() == 0) {
					// wait for the producer
					wait();
				}
				int value = list.poll();
				System.out.println("Consume " + value);

				// notify the producer
				notify();

				Thread.sleep(1000);
			}
		}
	}
}
