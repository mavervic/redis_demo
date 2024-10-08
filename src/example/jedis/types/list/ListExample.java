package example.jedis.types.list;

import java.util.List;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.args.ListDirection;
import utils.RedisUtils;

/**
 * https://redis.io/docs/latest/develop/data-types/lists/
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

		/**
		 * pop 會移除取出的元素，lpop 是從左邊取出，rpop 是從右邊取出。
		 */
		long res22 = jedis.rpush("bikes:repairs", "bike:1", "bike:2", "bike:3");
		System.out.println(res22); // >>> 3

		String res23 = jedis.rpop("bikes:repairs");
		System.out.println(res23); // >>> bike:3

		String res24 = jedis.lpop("bikes:repairs");
		System.out.println(res24); // >>> bike:1

		String res25 = jedis.rpop("bikes:repairs");
		System.out.println(res25); // >>> bike:2

		String res26 = jedis.rpop("bikes:repairs");
		System.out.println(res26); // >>> null

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
		// 留下 index 倒數第1,2,3台 的腳踏車
		res28 = jedis.ltrim("bikes:repairs", -3, -1);
		System.out.println(res2); // >>> OK
		// 搜尋腳踏車維修區目前有哪幾台車
		res29 = jedis.lrange("bikes:repairs", 0, -1);
		System.out.println(res29); // >>> [bike:3, bike:4, bike:5]

		/**
		 * brpop 會等待(阻塞)直到有元素可以取出，如果等待時間超過 timeout 則回傳 null
		 */
		long res31 = jedis.rpush("bikes:repairs", "bike:1", "bike:2");
		System.out.println(res31); // >>> 2

		List<String> res32 = jedis.brpop(1, "bikes:repairs");
		System.out.println(res32); // >>> (bikes:repairs, bike:2)

		List<String> res33 = jedis.brpop(1, "bikes:repairs");
		System.out.println(res33); // >>> (bikes:repairs, bike:1)

		List<String> res34 = jedis.brpop(1, "bikes:repairs");
		System.out.println(res34); // >>> null

		/**
		 * 以下是一些特殊情況
		 * 
		 * 例如 key 不存在時的行為
		 * 
		 * 例如 key 的類型不正確時的行為
		 */
		long res35 = jedis.del("new_bikes");
		System.out.println(res35); // >>> 0
		// 例如 key 不存在時的行為
		long res36 = jedis.lpush("new_bikes", "bike:1", "bike:2", "bike:3");
		System.out.println(res36); // >>> 3

		String res37 = jedis.set("new_bikes", "bike:1");
		System.out.println(res37); // >>> OK

		String res38 = jedis.type("new_bikes");
		System.out.println(res38); // >>> string

		// 例如 key 的類型不正確時的行為
		try {
			jedis.lpush("new_bikes", "bike:2", "bike:3");
		} catch (Exception e) {
			e.printStackTrace();
			// >>> redis.clients.jedis.exceptions.JedisDataException:
			// >>> WRONGTYPE Operation against a key holding the wrong kind of value
		}

		/**
		 * 以下在確認 list 為空時相關的行為
		 * 
		 * llen 對於不存在的key回傳0而不是null
		 * 
		 * 當我們從聚合資料類型中刪除元素時，如果值保持為空，則鍵會自動銷毀。
		 * 
		 * 如果目標KEY不存在，則Redis會在新增元素之前建立一個空的Lists。
		 */
		jedis.lpush("bikes:repairs", "bike:1", "bike:2", "bike:3");
		System.out.println(res36); // >>> 3

		boolean res40 = jedis.exists("bikes:repairs");
		System.out.println(res40); // >>> true

		String res41 = jedis.lpop("bikes:repairs");
		System.out.println(res41); // >>> bike:3

		String res42 = jedis.lpop("bikes:repairs");
		System.out.println(res42); // >>> bike:2

		String res43 = jedis.lpop("bikes:repairs");
		System.out.println(res43); // >>> bike:1

		boolean res44 = jedis.exists("bikes:repairs");
		System.out.println(res44); // >>> false

		long res45 = jedis.del("bikes:repairs");
		System.out.println(res45); // >>> 0

		long res46 = jedis.llen("bikes:repairs");
		System.out.println(res46); // >>> 0

		String res47 = jedis.lpop("bikes:repairs");
		System.out.println(res47); // >>> null

		// 一次進5台腳踏車
		long res48 = jedis.lpush("bikes:repairs", "bike:1", "bike:2", "bike:3", "bike:4", "bike:5");
		System.out.println(res48); // >>> 5
		// 留下 index 0 ~ 2 的腳踏車 (可以用在例如部落格的最新文章，例如最新的前三篇)
		String res49 = jedis.ltrim("bikes:repairs", 0, 2);
		System.out.println(res49); // >>> OK
		// 搜尋腳踏車維修區目前有哪幾台車
		List<String> res50 = jedis.lrange("bikes:repairs", 0, -1);
		System.out.println(res50); // >>> [bike:5, bike:4, bike:3]

	}

}
