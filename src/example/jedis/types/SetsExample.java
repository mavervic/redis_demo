package example.jedis.types;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.JedisPooled;
import utils.RedisUtils;

public class SetsExample {
	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();

		/*
		 * 存放法國和美國比賽的自行車組。如果新增已存在的成員將被忽略。
		 */
		long res1 = jedis.sadd("bikes:racing:france", "bike:1");
		System.out.println(res1); // >>> 1

		long res2 = jedis.sadd("bikes:racing:france", "bike:1");
		System.out.println(res2); // >>> 0

		long res3 = jedis.sadd("bikes:racing:france", "bike:2", "bike:3");
		System.out.println(res3); // >>> 2

		long res4 = jedis.sadd("bikes:racing:usa", "bike:1", "bike:4");
		System.out.println(res4); // >>> 2

		
		
		/*
		 * 檢查bike:1 或bike:2 是否在美國比賽。
		 */
		jedis.sadd("bikes:racing:france", "bike:1", "bike:2", "bike:3");
		jedis.sadd("bikes:racing:usa", "bike:1", "bike:4");

		boolean res5 = jedis.sismember("bikes:racing:usa", "bike:1");
		System.out.println(res5); // >>> true

		boolean res6 = jedis.sismember("bikes:racing:usa", "bike:2");
		System.out.println(res6); // >>> false

		
		
		/*
		 * 有哪些自行車都參加了這 France 跟 USA 兩場比賽？
		 */
		jedis.sadd("bikes:racing:france", "bike:1", "bike:2", "bike:3");
		jedis.sadd("bikes:racing:usa", "bike:1", "bike:4");

		Set<String> res7 = jedis.sinter("bikes:racing:france", "bikes:racing:usa");
		System.out.println(res7); // >>> [bike:1]

		
		
		/*
		 * Sets的長度，有多少輛自行​​車參加法國的比賽？
		 */
		jedis.sadd("bikes:racing:france", "bike:1", "bike:2", "bike:3");

		long res8 = jedis.scard("bikes:racing:france"); // return the set cardinality (number of elements) 
		System.out.println(res8); // >>> 3

		
		/*
		 * 取得Sets所有成員
		 */
		long res9 = jedis.sadd("bikes:racing:france", "bike:1", "bike:2", "bike:3");
		System.out.println(res9); // >>> 3
		
		Set<String> res10 = jedis.smembers("bikes:racing:france");
		System.out.println(res10); // >>> [bike:1, bike:2, bike:3]

		
		
		/*
		 * 測試特定字串是否已經存在於Sets中
		 */
		boolean res11 = jedis.sismember("bikes:racing:france", "bike:1");
		System.out.println(res11); // >>> true

		List<Boolean> res12 = jedis.smismember("bikes:racing:france", "bike:2", "bike:3", "bike:4");
		System.out.println(res12); // >>> [true,true,false]

		
		
		/*
		 * 差集: 哪些自行車在法國比賽，但不在美國比賽
		 */
		jedis.sadd("bikes:racing:france", "bike:1", "bike:2", "bike:3");
		jedis.sadd("bikes:racing:usa", "bike:1", "bike:4");

		Set<String> res13 = jedis.sdiff("bikes:racing:france", "bikes:racing:usa");
		System.out.println(res13); // >>> [bike:2, bike:3]

		
		/*
		 * 交集: 取得哪些自行車同時有在法國、美國、義大利比賽
		 */
		jedis.sadd("bikes:racing:france", "bike:1", "bike:2", "bike:3");
		jedis.sadd("bikes:racing:usa", "bike:1", "bike:4");
		jedis.sadd("bikes:racing:italy", "bike:1", "bike:2", "bike:3", "bike:4");

		Set<String> res14 = jedis.sinter("bikes:racing:france", "bikes:racing:usa", "bikes:racing:italy");
		System.out.println(res14); // >>> [bike:1]

		
		/*
		 * 並集: 取得法國、美國、義大利比賽中出現過哪些自行車
		 */
		Set<String> res15 = jedis.sunion("bikes:racing:france", "bikes:racing:usa", "bikes:racing:italy");
		System.out.println(res15); // >>> [bike:1, bike:2, bike:3, bike:4]

		/*
		 * 差集: 請注意先後順序
		 */
		Set<String> res16 = jedis.sdiff("bikes:racing:france", "bikes:racing:usa", "bikes:racing:italy");
		System.out.println(res16); // >>> []
		Set<String> res17 = jedis.sdiff("bikes:racing:usa", "bikes:racing:france");
		System.out.println(res17); // >>> [bike:4]
		Set<String> res18 = jedis.sdiff("bikes:racing:france", "bikes:racing:usa");
		System.out.println(res18); // >>> [bike:2, bike:3]
		
		
		/*
		 * 從集合中刪除成員
		 */
		jedis.sadd("bikes:racing:france", "bike:1", "bike:2", "bike:3", "bike:4", "bike:5");

		long res19 = jedis.srem("bikes:racing:france", "bike:1");
		System.out.println(res19); // >>> 1

		String res20 = jedis.spop("bikes:racing:france");
		System.out.println(res20); // >>> bike:3

		Set<String> res21 = jedis.smembers("bikes:racing:france");
		System.out.println(res21); // >>> [bike:2, bike:4, bike:5]

		String res22 = jedis.srandmember("bikes:racing:france");
		System.out.println(res22); // >>> bike:4

	}
}
