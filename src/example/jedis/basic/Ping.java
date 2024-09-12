package example.jedis.basic;

import redis.clients.jedis.JedisPooled;

public class Ping {
	public static void main(String[] args) {
		// 建立連線
		JedisPooled jedis = new JedisPooled("localhost", 6379, null, "mypassword");
		// 測試連線
		String result = jedis.ping();
		System.out.println(result);
	}
}