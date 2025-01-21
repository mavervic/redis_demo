package example.deprecated;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class GsonParse {
	public static void main(String[] args) {
		Gson gson = new Gson();

		String example1 = """
				["10", "20", "30"]
				""";
		System.out.println("字串轉回List: " + gson.fromJson(example1, List.class));

		
		String example2 = """
				[{ "name": "vic" }, { "name": "david" }, { "name": "peter" }]
				""";
		System.out.println("字串轉回List: " + gson.fromJson(example2, List.class));

		
		String example3 = """
				{
				  "boolean_true": true,
				  "boolean_false": false,
				  "string": "hello world",
				  "number": 123
				}
				""";
		System.out.println("字串轉回Map: " + gson.fromJson(example3 ,Map.class));

		
		String example4 = """
				{
				  "boolean_true": true,
				  "boolean_false": false,
				  "string": "hello world",
				  "number": 123,
				  "array": ["10", "20", "30"]
				}
				""";
		System.out.println("字串轉回Map: " + gson.fromJson(example4, Map.class));

		
		String example5 = """
				{
				  "boolean_true": true,
				  "boolean_false": false,
				  "string": "hello world",
				  "number": 123,
				  "array": ["10", "20", "30"],
				  "objArray": [{ "name": "vic" }, { "name": "david" }, { "name": "peter" }]
				}
				""";
		System.out.println("字串轉回Map: " + gson.fromJson(example5, Map.class));
	}
}
