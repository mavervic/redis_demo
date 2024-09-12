package utils;

import redis.clients.jedis.JedisPooled;

public class RedisUtils {

	private RedisUtils() {
	}

	// 一個內部類別，保證JedisPooled的單例，並具備懶加載的特性
	private static class JedisPooledHolder {
		private static String url = "localhost";
		private static int port = 6379;
		private static String user = null;
		private static String password = "mypassword";
		private static final JedisPooled jedisPooled = new JedisPooled(url, port, user, password);
	}

	/**
	 * 獲取JedisPooled的單例
	 */
	public static JedisPooled getJedisPooled() {
		return JedisPooledHolder.jedisPooled;
	}

	/**
	 * 獲取新的自增鍵
	 */
	public static String getAutoIncreamentKey(JedisPooled jedisPooled, String key) {
		String newKey = String.valueOf(jedisPooled.incr(key));
		return newKey;
	}

	public static void main(String[] args) {
		JedisPooled jedisPooled = RedisUtils.getJedisPooled();
		String key = RedisUtils.getAutoIncreamentKey(jedisPooled, "seq");
		System.out.println(key);
	}

}
