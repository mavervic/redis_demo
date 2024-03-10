package example.json;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class JsonParse {
	public static void main(String[] args) {
		Gson gson = new Gson();

		// ["10","20","30"]
		System.out.println("字串轉回List: " + gson.fromJson("[\"10\",\"20\",\"30\"]", List.class));

		// [{"name":"vic"},{"name":"david"},{"name":"peter"}]
		System.out.println("字串轉回List: " + gson.fromJson("[{\"name\":\"vic\"},{\"name\":\"david\"},{\"name\":\"peter\"}]", List.class));

		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123}
		System.out.println("字串轉回Map: " + gson.fromJson("{\"boolean_true\":true,\"boolean_false\":false,\"string\":\"hello world\",\"number\":123}", Map.class));

		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123,"array":["10","20","30"]}
		System.out.println("字串轉回Map: " + gson.fromJson("{\"boolean_true\":true,\"boolean_false\":false,\"string\":\"hello world\",\"number\":123,\"array\":[\"10\",\"20\",\"30\"]}", Map.class));

		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123,"array":["10","20","30"],"objArray":[{"name":"vic"},{"name":"david"},{"name":"peter"}]}
		System.out.println("字串轉回Map: " + gson.fromJson("{\"boolean_true\":true,\"boolean_false\":false,\"string\":\"hello world\",\"number\":123,\"array\":[\"10\",\"20\",\"30\"],\"objArray\":[{\"name\":\"vic\"},{\"name\":\"david\"},{\"name\":\"peter\"}]}", Map.class));
	}
}
