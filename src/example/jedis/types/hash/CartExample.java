package example.jedis.types.hash;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import redis.clients.jedis.JedisPooled;
import utils.RedisUtils;

public class CartExample {

	public static void main(String[] args) {
		String memberId = "stock:member:01";

		Map<String, String> stock = new HashMap<>();
		stock.put("iphone", "10");
		stock.put("mac", "1");
		stock.put("watch", "2");

		// 將購物車存入 redis
		add2Cart(memberId, stock);

		// 取得購物車中的內容
		Map<String, String> cart = getCart(memberId);

		// 序列化成 JSON 格式字串，可以用來回傳給前端 (非必要)
		Gson gson = new Gson();
		String json = gson.toJson(cart);
		System.out.println(json); // {"watch":"2","iphone":"10","mac":"1"}
	}

	public static void add2Cart(String memberId, Map<String, String> stockMap) {
		JedisPooled jedis = RedisUtils.getJedisPooled();
		long hset = jedis.hset(memberId, stockMap);
		System.out.println(memberId + "加入了" + hset + "樣商品"); // 3
	}

	public static Map<String, String> getCart(String memberId) {
		JedisPooled jedis = RedisUtils.getJedisPooled();
		Map<String, String> hgetAll = jedis.hgetAll(memberId);
		System.out.println("取得" + memberId + "購物車: " + hgetAll); // {watch=2, iphone=10, mac=1}
		return hgetAll;
	}
}
