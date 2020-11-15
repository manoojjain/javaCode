package jsonEx;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Working Example:
 * Add below dependency:      
		<dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-yaml</artifactId>
            <version>2.11.3</version>
        </dependency>
 *
 */

/**
 * Reading and Writing YAML With the Jackson ObjectMapper YAML is a textual data
 * format which is similar to JSON but uses a different syntax. The Jackson
 * ObjectMapper can read and write YAML in the same way you read and write JSON.
 * In order to read and write YAML with Jackson, you need to add an extra Maven
 * dependency to your project.
 *
 */
class Employee{

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	String name;
	String email;
	
	@Override
	public String toString() {
		return "Employee [name=" + name + ", email=" + email + "]";
	}

	public Employee(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}
	
	//Empty Constructor is required while deserialize the Employee object
	//Also, setters are required to set value for object after deserialize
	//While serializing getters are needed
	public Employee() {
		super();
	}
	
}

public class ReadWriteYML {

	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

		Employee employee = new Employee("John Doe", "john@doe.com");

		String yamlString = null;
		try {
			yamlString = objectMapper.writeValueAsString(employee);
			System.out.println("yamlString: " + yamlString);

			/*
			 * Output: yamlString: 
			 * --- 
			 * name: "John Doe" 
			 * email: "john@doe.com"
			 * 
			 */

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		try {
			Employee employee2 = objectMapper.readValue(yamlString, Employee.class);
			System.out.println("After Deserialize: " + employee2);
			
			/*
			 * Output:
			 * After Deserialize: Employee [name=John Doe, email=john@doe.com]
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
