package example.json;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonExample {
	public static void main(String[] args) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

			// Create a Person object
			Person person = new Person();
			person.setName("John Doe");
			person.setAge(null); // This will be serialized as null
			person.setBirthDate(new Date());
			person.setAddress(null); // This will be serialized as null

			// Serialize
			String jsonString = mapper.writeValueAsString(person);
			System.out.println("Serialized JSON: " + jsonString);

			// Deserialize
			Person deserializedPerson = mapper.readValue(jsonString, Person.class);
			System.out.println("Deserialized Person: " + deserializedPerson.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}