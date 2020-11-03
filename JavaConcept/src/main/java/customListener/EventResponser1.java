package customListener;


//Someone interested in "Hello" events
public class EventResponser1 implements HelloListener {

	@Override
	public void someSayHelloResponse() {
		System.out.println(this.getClass().getName()+ " Response Hello");
	}

}
