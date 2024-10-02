package example.jedis.types.list;

import java.util.List;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.args.ListDirection;
import utils.RedisUtils;

/**
 * Redis 的 Lists 類似於 LinkedList，常用於隊列(queue)或堆棧(stack)
 */
public class ListExample {
	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();

		/*
		 * 隊列（先進先出）
		 */
		// 腳踏車維修區接了兩台腳踏車準備維修
		long res1 = jedis.lpush("bikes:repairs", "bike:1");
		System.out.println(res1); // >>> 1
		long res2 = jedis.lpush("bikes:repairs", "bike:2");
		System.out.println(res2); // >>> 2
		// 腳踏車維修區把最右側的腳踏車取出來，如果沒車了回傳null
		String res3 = jedis.rpop("bikes:repairs");
		System.out.println(res3); // >>> bike:1
		String res4 = jedis.rpop("bikes:repairs");
		System.out.println(res4); // >>> bike:2
		System.out.println(jedis.rpop("bikes:repairs")); // >>> null

		/*
		 * 堆疊（先進後出）
		 * 
		 */
		// 腳踏車維修區接了兩台腳踏車準備維修
		long res5 = jedis.lpush("bikes:repairs", "bike:1");
		System.out.println(res5); // >>> 1
		long res6 = jedis.lpush("bikes:repairs", "bike:2");
		System.out.println(res6); // >>> 2
		// 腳踏車維修區把最左側的腳踏車取出來，如果沒車了回傳null
		String res7 = jedis.lpop("bikes:repairs");
		System.out.println(res7); // >>> bike:2
		String res8 = jedis.lpop("bikes:repairs");
		System.out.println(res8); // >>> bike:1
		System.out.println(jedis.rpop("bikes:repairs")); // >>> null

		// 目前腳踏車店等候維修的車輛 (回傳list的數量，key不存在回傳0)
		long res9 = jedis.llen("bikes:repairs");
		System.out.println(res9); // >>> 0

		// 腳踏車維修區接了兩台腳踏車準備維修
		long res10 = jedis.lpush("bikes:repairs", "bike:1");
		System.out.println(res10); // >>> 1
		long res11 = jedis.lpush("bikes:repairs", "bike:2");
		System.out.println(res11); // >>> 2
		// 腳踏車維修區把最左側的腳踏車修好了，然後挪動到完成區 (具備原子性，全部移動或是全部不移動)
		String res12 = jedis.lmove("bikes:repairs", "bikes:finished", ListDirection.LEFT, ListDirection.LEFT);
		System.out.println(res12); // >>> bike:2

		// 搜尋腳踏車維修區目前有哪幾台車 (0表示從index幾開始找，-1表示最後的index)
		List<String> res13 = jedis.lrange("bikes:repairs", 0, -1);
		System.out.println(res13); // >>> [bike:1]
		// 搜尋腳踏車完成區目前有哪幾台車 (0表示從index幾開始找，-1表示最後的index)
		List<String> res14 = jedis.lrange("bikes:finished", 0, -1);
		System.out.println(res14); // >>> [bike:2]

		// 腳踏車維修區接了三台腳踏車準備維修
		long res15 = jedis.rpush("bikes:repairs", "bike:1");
		System.out.println(res15); // >>> 1
		long res16 = jedis.rpush("bikes:repairs", "bike:2");
		System.out.println(res16); // >>> 2
		long res17 = jedis.lpush("bikes:repairs", "bike:important_bike");
		System.out.println(res17); // >>> 3
		// 搜尋腳踏車維修區目前有哪幾台車
		List<String> res18 = jedis.lrange("bikes:repairs", 0, -1);
		System.out.println(res18); // >>> [bike:important_bike, bike:1, bike:2]

		// 腳踏車維修區一次接了三台腳踏車準備維修
		long res19 = jedis.rpush("bikes:repairs", "bike:1", "bike:2", "bike:3");
		System.out.println(res19); // >>> 3
		// 腳踏車維修區一次接了兩台腳踏車準備維修
		long res20 = jedis.lpush("bikes:repairs", "bike:important_bike", "bike:very_important_bike");
		System.out.println(res20); // >>> 5
		// 搜尋腳踏車維修區目前有哪幾台車
		List<String> res21 = jedis.lrange("bikes:repairs", 0, -1);
		System.out.println(res21); // >>> [bike:very_important_bike, bike:important_bike, bike:1, bike:2, bike:3]

		// 一次進5台腳踏車
		long res27 = jedis.lpush("bikes:repairs", "bike:1", "bike:2", "bike:3", "bike:4", "bike:5");
		System.out.println(res27); // >>> 5
		// 留下 index 0 ~ 2 的腳踏車 (可以用在例如部落格的最新文章，例如最新的前三篇)
		String res28 = jedis.ltrim("bikes:repairs", 0, 2);
		System.out.println(res28); // >>> OK
		List<String> res29 = jedis.lrange("bikes:repairs", 0, -1);
		System.out.println(res29); // >>> [bike:5, bike:4, bike:3]

		// 一次進5台腳踏車
		res27 = jedis.rpush("bikes:repairs", "bike:1", "bike:2", "bike:3", "bike:4", "bike:5");
		System.out.println(res27); // >>> 5
		// 留下 index -3 ~ -1 的腳踏車
		res28 = jedis.ltrim("bikes:repairs", -3, -1);
		System.out.println(res2); // >>> OK
		res29 = jedis.lrange("bikes:repairs", 0, -1);
		System.out.println(res29); // >>> [bike:3, bike:4, bike:5]

		
		autoCreateKeyExplain(jedis);
		autoRemoveKeyExplain(jedis);
		emptyListLen0(jedis);
	}

	/**
	 * 如果目標KEY不存在，則Redis會在新增元素之前建立一個空的Lists。
	 */
	public static void autoCreateKeyExplain(JedisPooled jedis) {
		long res35 = jedis.del("new_bikes");
		System.out.println(res35); // >>> 0
		long res36 = jedis.lpush("new_bikes", "bike:1", "bike:2", "bike:3");
		System.out.println(res36); // >>> 3
	}

	/**
	 * 當我們從聚合資料類型中刪除元素時，如果值保持為空，則鍵會自動銷毀。Stream 資料類型是此規則的唯一例外。
	 */
	public static void autoRemoveKeyExplain(JedisPooled jedis) {
		long res39 = jedis.lpush("bikes:repairs", "bike:1");
		System.out.println(res39); // >>> 3

		boolean res40 = jedis.exists("bikes:repairs");
		System.out.println(res40); // >>> true
		
		String res43 = jedis.lpop("bikes:repairs");
		System.out.println(res43); // >>> bike:1

		// key 被自動移除了
		boolean res44 = jedis.exists("bikes:repairs");
		System.out.println(res44); // >>> false
	}

	/**
	 * llen 對於不存在的key回傳0而不是null
	 */
	public static void emptyListLen0(JedisPooled jedis) {
		long res45 = jedis.del("bikes:repairs");
		System.out.println(res45); // >>> 0

		long res46 = jedis.llen("bikes:repairs");
		System.out.println(res46); // >>> 0

		String res47 = jedis.lpop("bikes:repairs");
		System.out.println(res47); // >>> null
	}
}
