package jsonEx;

import java.io.FileOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonWriter {

	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();

		Car car = new Car();
		car.setBrand("BMW");
		car.setDoors(4);

		try {
			objectMapper.writeValue(
			    new FileOutputStream("src/main/resources/write_car.json"), car);
			
			/*
			 * Below content will be written into write_car.json
			 * {"brand":"BMW","doors":4}
			 */
			
			String json = objectMapper.writeValueAsString(car);
			System.out.println(json);
			
			/*
			 * Output:
			 * {"brand":"BMW","doors":4}
			 */
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
