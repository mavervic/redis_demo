package example.jedis.types.string;

import java.util.List;
import java.util.UUID;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.params.SetParams;
import utils.RedisUtils;

/**
 * https://redis.io/docs/latest/develop/data-types/strings/
 */
public class StringExample {
	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();

		// SET 跟 GET 的範例
		String res1 = jedis.set("bike:1", "Deimos");
		System.out.println(res1); // OK
		String res2 = jedis.get("bike:1");
		System.out.println(res2); // Deimos

		// 如果 Key 存在則"不"執行 SET 的範例
		Long res3 = jedis.setnx("bike:1", "bike");
		System.out.println(res3); // 0 (because key already exists)
		System.out.println(jedis.get("bike:1")); // Deimos (value is unchanged)

		// 如果 Key 存在"才"執行 SET 的範例
		// set the value to "bike" if it already exists
		String res4 = jedis.set("bike:1", "bike", SetParams.setParams().xx());
		System.out.println(res4); // OK

		// SET 跟 GET 多個 KEY 跟 VALUE
		String res5 = jedis.mset("bike:1", "Deimos", "bike:2", "Ares", "bike:3", "Vanth");
		System.out.println(res5); // OK
		List<String> res6 = jedis.mget("bike:1", "bike:2", "bike:3");
		System.out.println(res6); // [Deimos, Ares, Vanth]

		// 執行計算的範例，類似於 count++
		jedis.set("total_crashes", "0");
		Long res7 = jedis.incr("total_crashes");
		System.out.println(res7); // 1
		Long res8 = jedis.incrBy("total_crashes", 10);
		System.out.println(res8); // 11

	}

}
