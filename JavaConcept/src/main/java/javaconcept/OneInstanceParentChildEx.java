package javaconcept;


/**
 * problem: 
 * Write the logic to create only one instance of class A and class B.
 * class B is sub class where class A is parent class.
 * Exception will be thrown if we create more than one instance.
 * 
 * No code modification is allowed in class B.
 *
 */

class A{
	static int aCount = 0;
	static int bCount = 0;

	A() {
		if (this instanceof B) {
			System.out.println("B instance");
			bCount++;
			if (bCount > 1) {
				throw new RuntimeException();
			}
		} else if (this instanceof A) {
			System.out.println("A instance");
			aCount++;
			if (aCount > 1) {
				throw new RuntimeException();
			}
		}
		
	}
	
}

class B extends A{
	
	
}

public class OneInstanceParentChildEx {

	public static void main(String[] args) {
		A a = new A();	//No Exception as A's 1st instance  
		B b = new B();	//No exception as B's 1st instance 
		
		A a1 = new A();	//Runtime Exception 
		A a2 = new B();	//Runtime exception 
	}

}
