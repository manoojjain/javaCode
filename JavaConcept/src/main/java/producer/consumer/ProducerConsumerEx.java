package producer.consumer;

public class ProducerConsumerEx {

	public static void main(String[] args) throws InterruptedException {

		Buffer buffer = new Buffer(2);

		Thread producerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					buffer.produce();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		Thread consumerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					buffer.consume();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		producerThread.start();
		consumerThread.start();
		producerThread.join();
		consumerThread.join();
	}
}
