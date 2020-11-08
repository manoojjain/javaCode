package java8.streamEx;
import java.util.ArrayList;
import java.util.List;

class Emp{
	String id;
	String name;
	String comp;
	
	
	public Emp(String id, String name, String comp) {
		super();
		this.id = id;
		this.name = name;
		this.comp = comp;
	}
	@Override
	public String toString() {
		return "Emp [id=" + id + ", name=" + name + ", comp=" + comp + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComp() {
		return comp;
	}
	public void setComp(String comp) {
		this.comp = comp;
	}
}

public class StreamCustomObj {

	public static void main(String[] args) {
		
		Emp e1 = new Emp("E1", "Ename1", "TIAA");
		Emp e2 = new Emp("E2", "Ename2", "TIAA");
		Emp e3 = new Emp("E3", "Ename3", "TIAA");
		Emp e4 = new Emp("E4", "Ename4", "TIAA1");
		
		List<Emp> empList = new ArrayList<Emp>();
		empList.add(e1);
		empList.add(e2);
		empList.add(e3);
		empList.add(e4);
		
		empList.stream().filter(emp -> emp.getComp().equalsIgnoreCase("TIAA"))
		.map(emp -> emp.getName()).forEach(System.out::println);
		
//		Output:
//		Ename1
//		Ename2
//		Ename3
		
	}
}
