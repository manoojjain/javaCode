package javaconcept;


class Parent{
	private void hello() {
		System.out.println("This is parent class method");
	}
	
	public void display() {
		Parent p = new Parent();
		p.hello();	//Works fine. Parent class hello method will be called.
	}
}

class Child extends Parent{
	private void hello() {
		System.out.println("This is child class method");
	}
	
	public void display2() {
		Parent p1 = new Child();
//		p1.hello();		// Compile time error -> The method hello() from the type Parent is not visible
		
		Child c1 = new Child();
		c1.hello();     //Works fine. Child class hello method will be called.
		
	}
} 
public class PrivateMethodOverride {

	public static void main(String[] args) {
		
		Parent p = new Parent();
//		p.hello();	//private method hello is not visible here -> Compile time error 
		p.display();	//output: This is parent class method
		
		Parent p1 = new Child();
//		p1.hello();	//private method hello is not visible here  -> Compile time error
		p1.display();		//output: This is parent class method
		
		Child c1 = new Child();
		c1.display2();		//output: This is child class method
	}
}
