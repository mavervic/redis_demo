package example.jedis.types.json;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import example.json.MockDataUtils;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path2;
import utils.RedisUtils;

public class BasicUpdateExample {
	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();
		Gson gson = new Gson();

		// ["10","20","30"] => ["10","20","30","40","50","60"]
		List<String> array = MockDataUtils.getArray();
		jedis.jsonSet("json:array:normal.array", gson.toJson(array));
		jedis.jsonArrAppend("json:array:normal.array", Path2.ROOT_PATH, "\"40\"", "\"50\"", "\"60\"");

		// [{"name":"vic"},{"name":"david"},{"name":"peter"}] =>
		// [{"name":"vic"},{"name":"david"},{"name":"peter"},{"name":"Ben"},{"name":"Ann"}]
		// [{"name":"vic","hello":"world"},{"name":"david","hello":"world"},{"name":"peter","hello":"world"},{"name":"Ben","hello":"world"},{"name":"Ann","hello":"world"}]
		List<Object> objArray = MockDataUtils.getObjArray();
		jedis.jsonSet("json:array:wrap.obj", gson.toJson(objArray));
		jedis.jsonArrAppend("json:array:wrap.obj", Path2.ROOT_PATH, "{\"name\":\"Ben\"}", "{\"name\":\"Ann\"}");
		jedis.jsonMerge("json:array:wrap.obj", Path2.of(".[*]"), "{\"hello\":\"world\"}");
		
		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123} =>
		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123, "hello": "world"}
		Map<String, Object> obj = MockDataUtils.getObj();
		jedis.jsonSet("json:obj:normal.obj", gson.toJson(obj));
		jedis.jsonMerge("json:obj:normal.obj", Path2.ROOT_PATH, "{\"hello\":\"world\"}");

		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123,"array":["10","20","30"]} =>
		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123,"array":["10","20","30","5","10","15"],"array2":["100","200","300"]}
		Map<String, Object> objContentArray = MockDataUtils.getObjContentArray();
		jedis.jsonSet("json:obj:field.has.array", gson.toJson(objContentArray));
		jedis.jsonMerge("json:obj:field.has.array", Path2.ROOT_PATH, "{\"array2\":[\"100\",\"200\",\"300\"]}");
		jedis.jsonArrAppend("json:obj:field.has.array", Path2.of(".array"), "\"5\"", "\"10\"", "\"15\"");

		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123,
		// "array":["10","20","30"],
		// "objArray":[{"name":"vic"},{"name":"david"},{"name":"peter"}]} =>
		// "objArray":[{"name":"vic","hello":"world"},{"name":"david","hello":"world"},{"name":"peter","hello":"world"}]} =>
		Map<String, Object> objContentArrayAndObjArray = MockDataUtils.getObjContentArrayAndObjArray();
		jedis.jsonSet("json:obj:complex.array", gson.toJson(objContentArrayAndObjArray));
		jedis.jsonMerge("json:obj:complex.array", Path2.of(".objArray[*]"), "{\"hello\":\"world\"}");

	}
}
