/**
 * 
 */
package javaconcept;

/**
 * Problem: 
 * We want to print numbers from 1 to 100 from Thread1 and Thread2 
 * One constraint -> Thread 2 should not print number greater then Thread 1
 * 
 * Output: 
 * 
Thread-0@@@0
Thread-0@@@1
Thread-0@@@2
Thread-0@@@3
Thread-0@@@4
Thread-1###0
Thread-1###1
Thread-1###2
Thread-1###3
Thread-1###4
Thread-0@@@5
Thread-1###5
Thread-0@@@6
Thread-0@@@7
Thread-0@@@8
Thread-0@@@9
Thread-0@@@10
Thread-0@@@11
Thread-1###6
Thread-1###7
Thread-1###8
Thread-1###9
Thread-0@@@12
Thread-0@@@13
Thread-0@@@14
Thread-0@@@15
Thread-0@@@16
Thread-0@@@17
Thread-0@@@18
Thread-0@@@19
Thread-0@@@20
Thread-1###10
Thread-1###11
Thread-1###12
Thread-1###13
Thread-1###14
Thread-1###15
Thread-0@@@21
 * 
 *
 */
public class TwoThreadVolatileEx {

	// Here, we use volatile keyword because only one thread want to update the
	// variable other just read.
	volatile static int thread1Count = 0;

	public static void main(String[] args) {

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < 100; i++) {

					System.out.println(Thread.currentThread().getName() + "@@@" + i);
					thread1Count++;
				}

			}
		});

		Thread t2 = new Thread(new Runnable() {

			boolean isThread2finish = false;
			int threadLastPrint = 0;

			@Override
			public void run() {
				while (!isThread2finish) {
					for (int i = threadLastPrint; i < 100; i++) {

						if (i < thread1Count) {
							System.out.println(Thread.currentThread().getName() + "###" + i);
							threadLastPrint++;
						} else {
							break;
						}
					}
					if (threadLastPrint == 100) {
						isThread2finish = true;
					}
				}

			}
		});

		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
