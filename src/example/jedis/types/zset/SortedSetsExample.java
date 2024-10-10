package example.jedis.types.zset;

import java.util.HashMap;
import java.util.List;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.resps.Tuple;
import utils.RedisUtils;

/**
 * https://redis.io/docs/latest/develop/data-types/sorted-sets/
 */
public class SortedSetsExample {
	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();

		/*
		 * 添加賽車手們在第一場比賽中獲得的分數
		 */
		long res1 = jedis.zadd("racer_scores", 10D, "Norem");
		System.out.println(res1); // >>> 1

		long res2 = jedis.zadd("racer_scores", 12D, "Castilla");
		System.out.println(res2); // >>> 1
		// 這邊使用了 Java 8 的新特性，稱為 Double Brace Initialization，總之就是一種初始化實體方式，這邊直接初始化了 map
		long res3 = jedis.zadd("racer_scores", new HashMap<String, Double>() {
			{
				put("Sam-Bodden", 8D);
				put("Royce", 10D);
				put("Ford", 6D);
				put("Prickett", 14D);
				put("Castilla", 12D);
			}
		});
		System.out.println(res3); // >>> 4

		/*
		 * 取得排序
		 * 
		 * 注意ZRANGE順序是從低到高，而ZREVRANGE順序是從高到低
		 */
		List<String> res4 = jedis.zrange("racer_scores", 0, -1);
		System.out.println(res4); // >>> [Ford, Sam-Bodden, Norem, Royce, Castil, Castilla, Prickett]

		List<String> res5 = jedis.zrevrange("racer_scores", 0, -1);
		System.out.println(res5); // >>> [Prickett, Castilla, Castil, Royce, Norem, Sam-Bodden, Ford]

		/*
		 * 同時取得所有排名以及分數
		 */
		List<Tuple> res6 = jedis.zrangeWithScores("racer_scores", 0, -1);
		System.out.println(res6); // [[Ford,6.0], [Sam-Bodden,9.0], [Norem,10.0], [Royce,10.0], [Castilla,12.0],
									// [Prickett,14.0]]

		/*
		 * 取得最小正整數 ~ 10分的選手們
		 */
		List<String> res7 = jedis.zrangeByScore("racer_scores", Double.MIN_VALUE, 10D);
		System.out.println(res7); // >>> [Ford, Sam-Bodden, Norem, Royce]

		/**
		 * 移除掉 Castilla
		 */
		long res8 = jedis.zrem("racer_scores", "Castilla");
		System.out.println(res8); // >>> 1

		/*
		 * 篩選掉最小正整數 ~ 9分的選手們
		 */
		long res9 = jedis.zremrangeByScore("racer_scores", Double.MIN_VALUE, 9D);
		System.out.println(res9); // >>> 2

		/**
		 * 查看篩選後的結果
		 */
		List<String> res10 = jedis.zrange("racer_scores", 0, -1);
		System.out.println(res10); // >>> [Norem, Royce, Prickett]

		/*
		 * 取得元素所在位置 (由前至後)
		 */
		long res11 = jedis.zrank("racer_scores", "Norem");
		System.out.println(res11); // >>> 0

		/*
		 * 取得元素所在位置 (由後至前)，可以用在取得遊戲分數的排名
		 */
		long res12 = jedis.zrevrank("racer_scores", "Norem");
		System.out.println(res12); // >>> 2

		/*
		 * redis 對於同分項會依照字母順序做排序
		 */
		long res13 = jedis.zadd("lexicographical_scores", new HashMap<String, Double>() {
			{
				put("a", 0D);
				put("e", 0D);
				put("A", 0D);
				put("d", 0D);
				put("b", 0D);
				put("c", 0D);
				put("aAa", 0D);
			}
		});
		System.out.println(res13); // >>> 7

		List<String> res14 = jedis.zrange("lexicographical_scores", 0, -1);
		System.out.println(res14); // >>> [A, a, aAa, b, c, d, e]

		/**
		 * 取得字串開頭為 A ~ c 之間的元素
		 * 
		 * 這邊使用了 ZRANGEBYLEX 這個指令，這個指令是用來取得字典序在某個範圍內的元素 (區分大小寫)
		 * 
		 * 第二個參數表示搜尋起點條件
		 * 
		 * 第三個參數表示搜尋終點條件
		 * 
		 * @usageNotes 參考數學領域的區間表示法: `[` 表示包含、`(` 表示不包含
		 */
		List<String> res15 = jedis.zrangeByLex("lexicographical_scores", "[A", "[c");
		System.out.println(res15); // >>> [A, a, aAa, b, c]

		/**
		 * 遊戲計分通常有處理方式
		 * 
		 * 1. 直接覆蓋最新分數
		 * 
		 * 2. 累加分數
		 */
		long res16 = jedis.zadd("video_game_scores", 100D, "Wood");
		System.out.println(res16); // >>> 1
		long res17 = jedis.zadd("video_game_scores", 100D, "Henshaw");
		System.out.println(res17); // >>> 1
		long res18 = jedis.zadd("video_game_scores", 100D, "Henshaw");
		System.out.println(res18); // >>> 0
		// 累加分數
		double res19 = jedis.zincrby("video_game_scores", 50D, "Wood");
		System.out.println(res19); // >>> 150.0
		double res20 = jedis.zincrby("video_game_scores", 100D, "Henshaw");
		System.out.println(res20); // >>> 200.0
	}
}
