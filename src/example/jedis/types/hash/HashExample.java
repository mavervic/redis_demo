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

		/**
		 * 這邊是一個 hincrBy 的範例，用來記錄腳踏車的騎乘次數、車禍次數、車主更換次數
		 */
		Long res8 = jedis.hincrBy("bike:1:stats", "rides", 1);
		System.out.println(res8); // 1
		Long res9 = jedis.hincrBy("bike:1:stats", "rides", 1);
		System.out.println(res9); // 2
		Long res10 = jedis.hincrBy("bike:1:stats", "rides", 1);
		System.out.println(res10); // 3
		Long res11 = jedis.hincrBy("bike:1:stats", "crashes", 1);
		System.out.println(res11); // 1
		Long res12 = jedis.hincrBy("bike:1:stats", "owners", 1);
		System.out.println(res12); // 1
		String res13 = jedis.hget("bike:1:stats", "rides");
		System.out.println(res13); // 3
		// 一次 GET 多個 filed 的範例
		List<String> res14 = jedis.hmget("bike:1:stats", "crashes", "owners");
		System.out.println(res14); // [1, 1]

	}

}
