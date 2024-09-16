package example.jedis.types.json;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import example.json.MockDataUtils;
import redis.clients.jedis.JedisPooled;
import utils.RedisUtils;

public class BasicInsertExample {
	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();
		Gson gson = new Gson();

		// ["10","20","30"]
		List<String> array = MockDataUtils.getArray();
		jedis.jsonSet("array", gson.toJson(array));

		// [{"name":"vic"},{"name":"david"},{"name":"peter"}]
		List<Object> objArray = MockDataUtils.getObjArray();
		jedis.jsonSet("objArray", gson.toJson(objArray));

		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123}
		Map<String, Object> obj = MockDataUtils.getObj();
		jedis.jsonSet("obj", gson.toJson(obj));

		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123,"array":["10","20","30"]}
		Map<String, Object> objContentArray = MockDataUtils.getObjContentArray();
		jedis.jsonSet("objContentArray", gson.toJson(objContentArray));

		// {"boolean_true":true,"boolean_false":false,"string":"hello world","number":123,
		// "array":["10","20","30"],
		// "objArray":[{"name":"vic"},{"name":"david"},{"name":"peter"}]}
		Map<String, Object> objContentArrayAndObjArray = MockDataUtils.getObjContentArrayAndObjArray();
		jedis.jsonSet("objContentArrayAndObjArray", gson.toJson(objContentArrayAndObjArray));

	}
}
