package example.jedis.types.json;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import example.json.MockDataUtils;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path2;
import utils.RedisUtils;

/**
 * @see https://github.com/redis/jedis/blob/master/src/test/java/redis/clients/jedis/modules/json/RedisJsonV2Test.java
 */
public class BasicUpdateExample {
	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();
		Gson gson = new Gson();

		pushArrayExample(jedis, gson);
		pushArrayExample2(jedis, gson);

		addNewField(jedis, gson);
		editArrayField(jedis, gson);
		addComplexOjbField(jedis, gson);
	}

	private static void addComplexOjbField(JedisPooled jedis, Gson gson) {
		// {"number":123,"objArray":[{"name":"vic"},{"name":"david"},{"name":"peter"}],"string":"hello
		// world","array":["10","20","30"],"boolean_false":false,"boolean_true":true}
		Map<String, Object> objContentArrayAndObjArray = MockDataUtils.getObjContentArrayAndObjArray();
		String originJson = gson.toJson(objContentArrayAndObjArray);
		System.out.println("origin json: " + originJson);
		jedis.jsonSet("json:obj:complex.array", originJson);

		// "objArray":[{"name":"vic","hello":"world"},{"name":"david","hello":"world"},{"name":"peter","hello":"world"}]}
		jedis.jsonMerge("json:obj:complex.array", Path2.of(".objArray[*]"), "{\"hello\":\"world\"}");
		
		Object jsonGet = jedis.jsonGet("json:obj:complex.array");
		System.out.println("jsonGet" + jsonGet);
	}

	private static void editArrayField(JedisPooled jedis, Gson gson) {
		// {"boolean_true":true,"boolean_false":false,"string":"hello
		// world","number":123,"array":["10","20","30"]} =>
		Map<String, Object> objContentArray = MockDataUtils.getObjContentArray();
		String originJson = gson.toJson(objContentArray);
		System.out.println("origin json: " + originJson);
		jedis.jsonSet("json:obj:field.has.array", originJson);

		// {"boolean_true":true,"boolean_false":false,"string":"hello
		// world","number":123,"array":["10","20","30","5","10","15"],"array2":["100","200","300"]}
		jedis.jsonMerge("json:obj:field.has.array", Path2.ROOT_PATH, "{\"array2\":[\"100\",\"200\",\"300\"]}");
		jedis.jsonArrAppend("json:obj:field.has.array", Path2.of(".array"), "\"5\"", "\"10\"", "\"15\"");
		
		Object jsonGet = jedis.jsonGet("json:obj:field.has.array");
		System.out.println("jsonGet" + jsonGet);
	}

	private static void addNewField(JedisPooled jedis, Gson gson) {
		// {"boolean_true":true,"boolean_false":false,"string":"hello
		// world","number":123} =>
		Map<String, Object> obj = MockDataUtils.getObj();
		String originJson = gson.toJson(obj);
		System.out.println("origin json: " + originJson);
		jedis.jsonSet("json:obj:normal.obj", originJson);

		// {"boolean_true":true,"boolean_false":false,"string":"hello
		// world","number":123, "hello": "world"}
		jedis.jsonMerge("json:obj:normal.obj", Path2.ROOT_PATH, "{\"hello\":\"world\"}");
		
		Object jsonGet = jedis.jsonGet("json:obj:normal.obj");
		System.out.println("jsonGet" + jsonGet);
	}

	private static void pushArrayExample2(JedisPooled jedis, Gson gson) {
		// [{"name":"vic"},{"name":"david"},{"name":"peter"}] =>
		List<Object> objArray = MockDataUtils.getObjArray();
		String originJson = gson.toJson(objArray);
		System.out.println("origin json: " + originJson);
		jedis.jsonSet("json:array:wrap.obj", originJson);

		// [{"name":"vic"},{"name":"david"},{"name":"peter"},{"name":"Ben"},{"name":"Ann"}]
		jedis.jsonArrAppend("json:array:wrap.obj", Path2.ROOT_PATH, "{\"name\":\"Ben\"}", "{\"name\":\"Ann\"}");

		// [{"name":"vic","hello":"world"},{"name":"david","hello":"world"},{"name":"peter","hello":"world"},{"name":"Ben","hello":"world"},{"name":"Ann","hello":"world"}]
		jedis.jsonMerge("json:array:wrap.obj", Path2.of(".[*]"), "{\"hello\":\"world\"}");
		
		Object jsonGet = jedis.jsonGet("json:array:wrap.obj");
		System.out.println("jsonGet" + jsonGet);
	}

	private static void pushArrayExample(JedisPooled jedis, Gson gson) {
		// ["10","20","30"]
		List<String> array = MockDataUtils.getArray();
		String originJson = gson.toJson(array);
		System.out.println("origin json: " + originJson);
		jedis.jsonSet("json:array:normal.array", originJson);

		// ["10","20","30","40","50","60"]
		jedis.jsonArrAppend("json:array:normal.array", Path2.ROOT_PATH, "\"40\"", "\"50\"", "\"60\"");
		
		Object jsonGet = jedis.jsonGet("json:array:normal.array");
		System.out.println("jsonGet" + jsonGet);
	}
}
