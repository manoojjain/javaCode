package jsonEx;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson JSON Tree Model Jackson has a built-in tree model which can be used
 * to represent a JSON object. Jackson's tree model is useful if you don't know
 * how the JSON you will receive looks, or if you for some reason cannot (or
 * just don't want to) create a class to represent it. The Jackson Tree Model is
 * also useful if you need to manipulate the JSON before using or forwarding it.
 * All of these situations can easily occur in a Data Streaming scenario.
 * 
 *
 */
public class JsonNodeEx {

	public static void main(String[] args) {
		String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			/*
			 * the JSON string is parsed into a JsonNode object instead of a Car object,
			 * simply by passing the JsonNode.class as second parameter to the readValue()
			 * method instead of the Car.class
			 */
			JsonNode jsonNode2 = objectMapper.readValue(carJson, JsonNode.class);
			System.out.println(jsonNode2);

			/*
			 * Output: {"brand":"Mercedes","doors":5}
			 */

			// The ObjectMapper class also has a special readTree() method which always
			// returns a JsonNode.
			JsonNode jsonNode1 = objectMapper.readTree(carJson);
			System.out.println(jsonNode1);

			/*
			 * Output: {"brand":"Mercedes","doors":5}
			 */

			String carJson1 =
			        "{ \"brand\" : \"Mercedes\", \"doors\" : 5," +
			        "  \"owners\" : [\"John\", \"Jack\", \"Jill\"]," +
			        "  \"nestedObject\" : { \"field\" : \"value\" } }";


			try {

			    JsonNode jsonNode = objectMapper.readValue(carJson1, JsonNode.class);

			    JsonNode brandNode = jsonNode.get("brand");
			    String brand = brandNode.asText();
			    System.out.println("brand = " + brand);

			    JsonNode doorsNode = jsonNode.get("doors");
			    int doors = doorsNode.asInt();
			    System.out.println("doors = " + doors);

			    JsonNode array = jsonNode.get("owners");
			    JsonNode jsonNode11 = array.get(0);
			    String john = jsonNode11.asText();
			    System.out.println("john  = " + john);

			    JsonNode child = jsonNode.get("nestedObject");
			    JsonNode childField = child.get("field");
			    String field = childField.asText();
			    System.out.println("field = " + field);

			} catch (IOException e) {
			    e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
