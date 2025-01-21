package example.deprecated;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import utils.MockDataUtils;

public class GsonStringify {
	

	public static void main(String[] args) {
		Gson gson = new Gson();

		// ["10","20","30"]
		List<String> array = MockDataUtils.getArray();
		System.out.println("陣列的JSON Stringify: " + gson.toJson(array));

		// [{"name":"vic"},{"name":"david"},{"name":"peter"}]
		List<Object> objArray = MockDataUtils.getObjArray();
		System.out.println("物件的陣列的JSON Stringify: " + gson.toJson(objArray));

		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123}
		Map<String, Object> obj = MockDataUtils.getObj();
		System.out.println("物件的JSON Stringify: " + gson.toJson(obj));

		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123,"array":["10","20","30"]}
		Map<String, Object> objContentArray = MockDataUtils.getObjContentArray();
		System.out.println("物件含陣列的JSON Stringify: " + gson.toJson(objContentArray));

		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123,
		// "array":["10","20","30"],
		// "objArray":[{"name":"vic"},{"name":"david"},{"name":"peter"}]}
		Map<String, Object> objContentArrayAndObjArray = MockDataUtils.getObjContentArrayAndObjArray();
		System.out.println("物件含不同類型的陣列的JSON Stringify: " + gson.toJson(objContentArrayAndObjArray));
	}

}
