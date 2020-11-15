package jsonEx;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Sometimes you want to serialize a Java object to JSON differently than what
 * Jackson does by default. For instance, you might want to use different field
 * names in the JSON than in the Java object, or you might want to leave out
 * certain fields altogether.
 *
 */
class CarSerializer extends StdSerializer<Car> {

    protected CarSerializer(Class<Car> t) {
        super(t);
    }

    public void serialize(Car car, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("producer", car.getBrand());
        jsonGenerator.writeNumberField("doorCount", car.getDoors());
        jsonGenerator.writeEndObject();
    }
}

public class CustomSerializer {

	public static void main(String[] args) {
		CarSerializer carSerializer = new CarSerializer(Car.class);
		ObjectMapper objectMapper = new ObjectMapper();

		SimpleModule module =
		        new SimpleModule("CarSerializer", new Version(2, 1, 3, null, null, null));
		module.addSerializer(Car.class, carSerializer);

		objectMapper.registerModule(module);

		Car car = new Car();
		car.setBrand("Mercedes");
		car.setDoors(5);

		try {
			String carJson = objectMapper.writeValueAsString(car);
			System.out.println(carJson);
			
			/*
			 * Output:
			 * {"producer":"Mercedes","doorCount":5}
			 */
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

}
