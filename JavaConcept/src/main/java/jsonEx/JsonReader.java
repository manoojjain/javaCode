package jsonEx;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


/*
 * Reference: http://tutorials.jenkov.com/java-json/jackson-objectmapper.html
 */

class Car {
	private String brand;
	private int doors;

	public String getBrand() {
		return brand;
	}

	@Override
	public String toString() {
		return "Car [brand=" + brand + ", doors=" + doors + "]";
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getDoors() {
		return doors;
	}

	public void setDoors(int doors) {
		this.doors = doors;
	}
}

public class JsonReader {

	public static void main(String[] args) {

		/*
		 * By default Jackson maps the fields of a JSON object to fields in a Java
		 * object by matching the names of the JSON field to the getter and setter
		 * methods in the Java object. Jackson removes the "get" and "set" part of the
		 * names of the getter and setter methods, and converts the first character of
		 * the remaining name to lowercase.
		 */
		ObjectMapper objectMapper = new ObjectMapper();

		String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

		try {
			//Read Object From JSON String
			Car car = objectMapper.readValue(carJson, Car.class);

			System.out.println("car brand = " + car.getBrand());
			System.out.println("car doors = " + car.getDoors());

			/*
			 * Output:
			 * car brand = Mercedes 
			 * car doors = 5
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Read Object From JSON Reader
		String carJsonReader =
		        "{ \"brand\" : \"Mercedes\", \"doors\" : 4 }";
		Reader reader = new StringReader(carJsonReader);

		try {
			Car car = objectMapper.readValue(reader, Car.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Read Object From JSON InputStream
		try {
			InputStream input = new FileInputStream("src/main/resources/car.json");
			Car car = objectMapper.readValue(input, Car.class);
			
			System.out.println("Car from JSON file: "+car);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Read Object Array From JSON Array String
		String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";

		try {
			/*
			 * Car array class is passed as the second parameter to the readValue() method
			 * to tell the ObjectMapper that you want to read an array of Car instances.
			 */
			Car[] cars2 = objectMapper.readValue(jsonArray, Car[].class);
			for (Car car : cars2) {
				System.out.println(car);
			}

			/*
			 * Output:
			 * Car [brand=ford, doors=0] 
			 * Car [brand=Fiat, doors=0]
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Read Object List From JSON Array String
		try {
			/*
			 * TypeReference parameter passed to readValue(). 
			 * This parameter tells Jackson to read a List of Car objects.
			 */
			List<Car> cars1 = objectMapper.readValue(jsonArray, new TypeReference<List<Car>>(){});
			System.out.println(cars1);
			/*
			 * Output:
			 * [Car [brand=ford, doors=0], Car [brand=Fiat, doors=0]]
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			// Read Map from JSON String
			/*
			 * The Jackson ObjectMapper can also read a Java Map from a JSON string. This
			 * can be useful if you do not know ahead of time the exact JSON structure that
			 * you will be parsing. Usually you will be reading a JSON object into a Java
			 * Map. Each field in the JSON object will become a key, value pair in the Java
			 * Map.
			 */
			Map<String, Object> jsonMap = objectMapper.readValue(carJson, new TypeReference<Map<String, Object>>() {
			});
			System.out.println(jsonMap);

			/*
			 * Output: {brand=Mercedes, doors=5}
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
