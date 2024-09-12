package example.jedis.basic;

import redis.clients.jedis.JedisPooled;

public class HelloWorld {
	public static void main(String[] args) {
		// 建立連線
		JedisPooled jedis = new JedisPooled("localhost", 6379, null, "mypassword");
		// 寫入資料
		jedis.set("hello", "world");
	}
}