package example.jedis.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import redis.clients.jedis.JedisPooled;
import utils.RedisUtils;

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

		cartExample(jedis);

	}

	/**
	 * 簡易購物車的範例
	 * 
	 * @usageNotes 會員編號目前為寫死
	 * @usageNotes HashMap 的 value 可以存 JSON 格式的字串，以儲存更多的資訊
	 * @usageNotes 有可能要紀錄加入商品的時間，這樣網頁顯示時會依照加入時間排序顯示
	 */
	public static void cartExample(JedisPooled jedis) {
		Map<String, String> stock = new HashMap<>();
		stock.put("iphone", "10");
		stock.put("mac", "1");
		stock.put("watch", "2");

		// 存進redis
		long hset = jedis.hset("stock:member:01", stock);
		System.out.println(hset); // 3

		// 從redis取出來
		Map<String, String> hgetAll = jedis.hgetAll("stock:member:01");
		System.out.println(hgetAll); // {watch=2, iphone=10, mac=1}

		// 序列化成JSON格式字串
		Gson gson = new Gson();
		String json = gson.toJson(hgetAll);
		System.out.println(json); // {"watch":"2","iphone":"10","mac":"1"}
	}
}
