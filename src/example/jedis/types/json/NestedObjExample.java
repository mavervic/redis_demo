package example.jedis.types.json;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path2;
import utils.RedisUtils;

public class NestedObjExample {

	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();

		// 巢狀結構
		String nestedObj = """
				{
				    "a": {
				        "number": 1,
				        "b": {
				            "number": 1,
				            "c": {
				                "number": 1
				            }
				        }
				    }
				}
				""";
		jedis.jsonSet("nestedObj", nestedObj);

		// nestedObj.a.b.c.number += 1
		jedis.jsonNumIncrBy("nestedObj", Path2.of(".a.b.c.number"), 1d);

		// 全部的 number += 1
		jedis.jsonNumIncrBy("nestedObj", Path2.of("..number"), 1d);
	}
}
