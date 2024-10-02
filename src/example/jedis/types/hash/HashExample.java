package example.jedis.types.hash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import redis.clients.jedis.JedisPooled;
import utils.RedisUtils;

/**
 * https://redis.io/docs/latest/develop/data-types/hashes/
 */
public class HashExample {

	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();

		Map<String, String> bike1 = new HashMap<>();
		bike1.put("model", "Deimos");
		bike1.put("brand", "Ergonom");
		bike1.put("type", "Enduro bikes");
		bike1.put("price", "4972");

		// SET 跟 GET 的範例
		Long res1 = jedis.hset("bike:1", bike1);
		System.out.println(res1); // 4
		String res2 = jedis.hget("bike:1", "model");
		System.out.println(res2); // Deimos
		String res3 = jedis.hget("bike:1", "price");
		System.out.println(res3); // 4972

		// 一次 GET 大量數據的範例
		Map<String, String> res4 = jedis.hgetAll("bike:1");
		System.out.println(res4); // {type=Enduro bikes, brand=Ergonom, price=4972, model=Deimos}
		List<String> res5 = jedis.hmget("bike:1", "model", "price");
		System.out.println(res5); // [Deimos, 4972]

		Long res6 = jedis.hincrBy("bike:1", "price", 100);
		System.out.println(res6); // 5072
		Long res7 = jedis.hincrBy("bike:1", "price", -100);
		System.out.println(res7); // 4972

	}

}
