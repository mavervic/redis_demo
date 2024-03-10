package utils;

import redis.clients.jedis.JedisPooled;

public class RedisUtils {

	private RedisUtils() {
	}

	public static JedisPooled getJedisPooled() {
		return new JedisPooled("localhost", 6379, null, "mypassword");
	}

	public static void delAll() {
		JedisPooled jedis = getJedisPooled();
		var keysSet = jedis.keys("*");
		if(keysSet.size() == 0) {
			System.out.println("目前Redis為空，沒有需要清理");
			return;
		}
		var keysStr = keysSet.toArray(String[]::new);
		long deled = jedis.del(keysStr);
		System.out.println("一共清理了" + deled + "個keys");
	}

	public static void main(String[] args) {
		delAll();
	}

}
