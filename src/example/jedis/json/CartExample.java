package example.jedis.json;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path2;
import utils.RedisUtils;

public class CartExample {
	public static void main(String[] args) {
		simpleCart();
		advanceCart();
	}

	private static void simpleCart() {
		JedisPooled jedis = RedisUtils.getJedisPooled();
		Gson gson = new Gson();

		// member01的購物車
		Map<String, Integer> cart = new HashMap<>();
		cart.put("product01", 1);
		cart.put("product02", 1);
		// insert
		jedis.jsonSet("member:01:simple.cart", gson.toJson(cart));

		// getAll 未指定泛型
		Map<String, Integer> cart2 = gson.fromJson(jedis.jsonGet("member:01:simple.cart").toString(), Map.class);
		System.out.println(cart2);

		// getAll 指定泛型 type
		Map<String, Integer> cart3 = gson.fromJson(jedis.jsonGet("member:01:simple.cart").toString(),
				new TypeToken<Map<String, Integer>>() {
				}.getType());
		System.out.println(cart3);

		// 拿出指定路徑的value
		System.out.println(jedis.jsonGet("member:01:simple.cart", Path2.of(".product02")));

		// update
		System.out.println(jedis.jsonSet("member:01:simple.cart", Path2.of("product01"), 2));

		// delete
		System.out.println(jedis.jsonDel("member:01:simple.cart", Path2.of("product01")));
	}

	private static void advanceCart() {
		JedisPooled jedis = RedisUtils.getJedisPooled();
		Gson gson = new Gson();

		// Create an advanced cart with Cart objects
		Map<String, Cart> cart = new HashMap<>();
		cart.put("product01", new Cart("product01", 1, Instant.now().getEpochSecond()));
		cart.put("product02", new Cart("product02", 1, Instant.now().getEpochSecond()));

		// Convert the cart to JSON
		String jsonCart = gson.toJson(cart);

		// Store the cart in Redis
		jedis.jsonSet("member:01:advanced.cart", jsonCart);

		// Retrieve the cart from Redis
		String res = jedis.jsonGet("member:01:advanced.cart").toString();
		System.out.println(res);

		// Convert the JSON cart back to a Map of Cart objects
		Type type = new TypeToken<Map<String, Cart>>() {
		}.getType();
		Map<String, Cart> retrievedCart = gson.fromJson(res, type);

		System.out.println(retrievedCart); // Prints the cart as a Map of Cart objects
	}

	static class Cart {
		private String productId;
		private Integer quantity;
		private Long addedTimestamp;

		public Cart(String productId, Integer quantity, Long addedTimestamp) {
			super();
			this.productId = productId;
			this.quantity = quantity;
			this.addedTimestamp = addedTimestamp;
		}

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}

		public Long getAddedTimestamp() {
			return addedTimestamp;
		}

		public void setAddedTimestamp(Long addedTimestamp) {
			this.addedTimestamp = addedTimestamp;
		}

		@Override
		public String toString() {
			return "Cart [productId=" + productId + ", quantity=" + quantity + ", addedTimestamp=" + addedTimestamp
					+ "]";
		}

	}
}
