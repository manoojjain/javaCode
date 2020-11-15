package jsonEx;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;


/**
 * Sometimes you might want to read a JSON string into a Java object in a way
 * that is different from how the Jackson ObjectMapper does this by default. You
 * can add a custom deserializer to the ObjectMapper which can perform the
 * deserialization as you want it done.
 * 
 * Here is how you register and use a custom deserializer with the Jackson
 * ObjectMapper:
 * 
 *
 */
class CarDeserializer extends StdDeserializer<Car> {

	private static final long serialVersionUID = 8251988188870822736L;

	public CarDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Car deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
        Car car = new Car();
        while(!parser.isClosed()){
            JsonToken jsonToken = parser.nextToken();

            if(JsonToken.FIELD_NAME.equals(jsonToken)){
                String fieldName = parser.getCurrentName();
                System.out.println(fieldName);

                jsonToken = parser.nextToken();

                if("brand".equals(fieldName)){
                    car.setBrand(parser.getValueAsString());
                } else if ("doors".equals(fieldName)){
                    car.setDoors(parser.getValueAsInt());
                }
            }
        }
        return car;
    }
}

public class CustomDeserializer {

	public static void main(String[] args) {

		String json = "{ \"brand\" : \"Ford\", \"doors\" : 6 }";

		SimpleModule module =
		        new SimpleModule("CarDeserializer", new Version(3, 1, 8, null, null, null));
		module.addDeserializer(Car.class, new CarDeserializer(Car.class));

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);

		try {
			Car car = mapper.readValue(json, Car.class);
			System.out.println(car);
			/*
			 * Output:
			 * Car [brand=Ford, doors=6]
			 * 
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
