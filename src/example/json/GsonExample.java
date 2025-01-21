package example.json;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonExample {
	public static void main(String[] args) {
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd")
				.serializeNulls()
				.create();

		// Create a Person object
		Person person = new Person();
		person.setName("John Doe");
		person.setAge(null); // This will be serialized as null
		person.setBirthDate(new Date());
		person.setAddress(null); // This will be serialized as null

		// Serialize
		String jsonString = gson.toJson(person);
		System.out.println("Serialized JSON: " + jsonString);

		// Deserialize
		Person deserializedPerson = gson.fromJson(jsonString, Person.class);
		System.out.println("Deserialized Person: " + deserializedPerson.getName());
	}
}
