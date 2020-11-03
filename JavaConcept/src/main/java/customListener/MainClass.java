package customListener;


/**
 * This is main class where we have created object of EventInitiator and Listeners 
 * and registered listener with the event initiator then we are triggering event 
 *
 * Output: 
 * Hello!!
 * customListener.EventResponser1 Response Hello
 * customListener.EventResponser2 Response Hello
 * End of program
 *
 */
public class MainClass {

	public static void main(String[] args) {
		EventInitiator e1 = new EventInitiator();
		
		HelloListener listener1 = new EventResponser1();
		HelloListener listener2 = new EventResponser2();
		
		e1.addListener(listener1);
		e1.addListener(listener2);
		
		//Event is created by calling below functions
		e1.sayHello();
		
		System.out.println("End of program");

	}

}
