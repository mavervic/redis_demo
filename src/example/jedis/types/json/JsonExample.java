package example.jedis.types.json;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path2;
import utils.RedisUtils;

/**
 * https://redis.io/docs/latest/develop/data-types/json/
 */
public class JsonExample {
	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();

		// 請注意指令如何包含美元符號$ 。這是 JSON 文件中值的路徑（在本例中它僅表示根）。
		String res1 = jedis.jsonSet("bike", new Path2("$"), "\"Hyperion\"");
		System.out.println(res1); // >>> OK

		Object res2 = jedis.jsonGet("bike", new Path2("$"));
		System.out.println(res2); // >>> ["Hyperion"]

		List<Class<?>> res3 = jedis.jsonType("bike", new Path2("$"));
		System.out.println(res3); // >>> [class java.lang.String]

		// 這裡還有一些字串操作。 JSON.STRLEN告訴您字串的長度，您可以使用JSON.STRAPPEND向其附加另一個字串。

		List<Long> res4 = jedis.jsonStrLen("bike", new Path2("$"));
		System.out.println(res4); // >>> [8]

		List<Long> res5 = jedis.jsonStrAppend("bike", new Path2("$"), " (Enduro bikes)");
		System.out.println(res5); // >>> [23]

		Object res6 = jedis.jsonGet("bike", new Path2("$"));
		System.out.println(res6); // >>> ["Hyperion (Enduro bikes)"]

		// 數字可以做加減

		String res7 = jedis.jsonSet("crashes", new Path2("$"), 0);
		System.out.println(res7); // >>> OK

		Object res8 = jedis.jsonNumIncrBy("crashes", new Path2("$"), 1);
		System.out.println(res8); // >>> [1]

		Object res9 = jedis.jsonNumIncrBy("crashes", new Path2("$"), 1.5);
		System.out.println(res9); // >>> [2.5]

		Object res10 = jedis.jsonNumIncrBy("crashes", new Path2("$"), -0.75);
		System.out.println(res10); // >>> [1.75]

		// 這是一個更有趣的範例，其中包含 JSON 陣列和物件

		String res11 = jedis.jsonSet("newbike", new Path2("$"),
				new JSONArray().put("Deimos").put(new JSONObject().put("crashes", 0)).put((Object) null));
		System.out.println(res11); // >>> OK

		Object res12 = jedis.jsonGet("newbike", new Path2("$"));
		System.out.println(res12); // >>> [["Deimos",{"crashes":0},null]]

		Object res13 = jedis.jsonGet("newbike", new Path2("$[1].crashes"));
		System.out.println(res13); // >>> [0]

		// JSON.DEL命令刪除您使用path參數指定的任何 JSON 值。
		long res14 = jedis.jsonDel("newbike", new Path2("$.[-1]"));
		System.out.println(res14); // >>> 1

		Object res15 = jedis.jsonGet("newbike", new Path2("$"));
		System.out.println(res15); // >>> [["Deimos",{"crashes":0}]]

		// 你可以使用專用的 JSON 指令子集來操作陣列：

		String res16 = jedis.jsonSet("riders", new Path2("$"), new JSONArray());
		System.out.println(res16); // >>> OK

		List<Long> res17 = jedis.jsonArrAppendWithEscape("riders", new Path2("$"), "Norem");
		System.out.println(res17); // >>> [1]

		Object res18 = jedis.jsonGet("riders", new Path2("$"));
		System.out.println(res18); // >>> [["Norem"]]

		List<Long> res19 = jedis.jsonArrInsertWithEscape("riders", new Path2("$"), 1, "Prickett", "Royce", "Castilla");
		System.out.println(res19); // >>> [4]

		Object res20 = jedis.jsonGet("riders", new Path2("$"));
		System.out.println(res20);
		// >>> [["Norem","Prickett","Royce","Castilla"]]

		List<Long> res21 = jedis.jsonArrTrim("riders", new Path2("$"), 1, 1);
		System.out.println(res21); // >>> [1]

		Object res22 = jedis.jsonGet("riders", new Path2("$"));
		System.out.println(res22); // >>> [["Prickett"]]

		Object res23 = jedis.jsonArrPop("riders", new Path2("$"));
		System.out.println(res23); // >>> [Prickett]

		Object res24 = jedis.jsonArrPop("riders", new Path2("$"));
		System.out.println(res24); // >>> [null]

		// JSON 物件也有自己的指令：

		String res25 = jedis.jsonSet("bike:1", new Path2("$"),
				new JSONObject().put("model", "Deimos").put("brand", "Ergonom").put("price", 4972));
		System.out.println(res25); // >>> OK

		List<Long> res26 = jedis.jsonObjLen("bike:1", new Path2("$"));
		System.out.println(res26); // >>> [3]

		List<List<String>> res27 = jedis.jsonObjKeys("bike:1", new Path2("$"));
		System.out.println(res27); // >>> [[price, model, brand]]

	}

}
