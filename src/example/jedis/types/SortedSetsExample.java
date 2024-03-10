package example.jedis.types;

import java.util.HashMap;
import java.util.List;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.resps.Tuple;
import utils.RedisUtils;

public class SortedSetsExample {
	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();

		
		/*
		 * 添加賽車手們在第一場比賽中獲得的分數
		 */
		long res1 = jedis.zadd("racer_scores", 10d, "Norem");
		System.out.println(res1); // >>> 1

		long res2 = jedis.zadd("racer_scores", 12d, "Castilla");
		System.out.println(res2); // >>> 1

		long res3 = jedis.zadd("racer_scores", new HashMap<String, Double>() {
			{
				put("Sam-Bodden", 8d);
				put("Royce", 10d);
				put("Ford", 6d);
				put("Prickett", 14d);
				put("Castilla", 12d);
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
		System.out.println(res6); // [[Ford,6.0], [Sam-Bodden,9.0], [Norem,10.0], [Royce,10.0], [Castilla,12.0], [Prickett,14.0]]

		
		/*
		 * 取得 -無限小 ~ 10分的選手們
		 */
		List<String> res7 = jedis.zrangeByScore("racer_scores", Double.MIN_VALUE, 10d);
		System.out.println(res7); // >>> [Ford, Sam-Bodden, Norem, Royce]

		
		
		/*
		 * 篩選掉分數 -無限小 ~ 9分的選手們
		 */
		long res8 = jedis.zrem("racer_scores", "Castilla");
		System.out.println(res8); // >>> 1

		long res9 = jedis.zremrangeByScore("racer_scores", Double.MIN_VALUE, 9d);
		System.out.println(res9); // >>> 2

		List<String> res10 = jedis.zrange("racer_scores", 0, -1);
		System.out.println(res10); // >>> [Norem, Royce, Prickett]

		
		/*
		 * 取得元素所在位置 (由前至後，或由後至前)
		 */
		long res11 = jedis.zrank("racer_scores", "Norem");
		System.out.println(res11); // >>> 0

		long res12 = jedis.zrevrank("racer_scores", "Norem");
		System.out.println(res12); // >>> 2

		
		/*
		 * redis 對於同分項會依照字母順序做排序
		 */
		long res13 = jedis.zadd("lexicographical_scores", new HashMap<String, Double>() {
			{
				put("a", 0d);
				put("e", 0d);
				put("A", 0d);
				put("d", 0d);
				put("b", 0d);
				put("c", 0d);
				put("aAa", 0d);
			}
		});
		System.out.println(res13); // >>> 7

		List<String> res14 = jedis.zrange("lexicographical_scores", 0, -1);
		System.out.println(res14); // >>> [A, a, aAa, b, c, d, e]

		// 搜尋開頭為特定字串的元素
		List<String> res15 = jedis.zrangeByLex("lexicographical_scores", "[A", "[L");
		System.out.println(res15); // >>> [A]

		
		
		
		
		
		/*
		 * FB 遊戲排行榜
		 * 
		 * 如果我們知道賽車手的新分數，我們可以直接透過ZADD命令更新分數
		 * 
		 * 反過來說，如果我們只知道得分不知道總分，則我們可以使用ZINCRBY命令。
		 */
		long res16 = jedis.zadd("fb_game_scores", 100d, "Wood");
		System.out.println(res16); // >>> 1
		long res17 = jedis.zadd("fb_game_scores", 100d, "Henshaw");
		System.out.println(res17); // >>> 1
		long res18 = jedis.zadd("fb_game_scores", 100d, "Henshaw");
		System.out.println(res18); // >>> 0

		double res19 = jedis.zincrby("fb_game_scores", 50d, "Wood");
		System.out.println(res19); // >>> 150.0
		double res20 = jedis.zincrby("fb_game_scores", 50d, "Henshaw");
		System.out.println(res20); // >>> 200.0
	}
}
