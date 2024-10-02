
package example.jedis.types.json;

import java.util.List;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path2;
import utils.RedisUtils;

/**
 * https://redis.io/docs/latest/develop/data-types/json/
 */
public class InventoryExample {
	public static void main(String[] args) {
		JedisPooled jedis = RedisUtils.getJedisPooled();

		String inventory_json = """
				{
				  "inventory": {
				    "mountain_bikes": [
				      {
				        "id": "bike:1",
				        "model": "Phoebe",
				        "description": "This is a mid-travel trail slayer that is a fantastic daily driver or one bike quiver. The Shimano Claris 8-speed groupset gives plenty of gear range to tackle hills and there’s room for mudguards and a rack too.  This is the bike for the rider who wants trail manners with low fuss ownership.",
				        "price": 1920,
				        "specs": { "material": "carbon", "weight": 13.1 },
				        "colors": ["black", "silver"]
				      },
				      {
				        "id": "bike:2",
				        "model": "Quaoar",
				        "description": "Redesigned for the 2020 model year, this bike impressed our testers and is the best all-around trail bike we've ever tested. The Shimano gear system effectively does away with an external cassette, so is super low maintenance in terms of wear and tear. All in all it's an impressive package for the price, making it very competitive.",
				        "price": 2072,
				        "specs": { "material": "aluminium", "weight": 7.9 },
				        "colors": ["black", "white"]
				      },
				      {
				        "id": "bike:3",
				        "model": "Weywot",
				        "description": "This bike gives kids aged six years and older a durable and uberlight mountain bike for their first experience on tracks and easy cruising through forests and fields. A set of powerful Shimano hydraulic disc brakes provide ample stopping ability. If you're after a budget option, this is one of the best bikes you could get.",
				        "price": 3264,
				        "specs": { "material": "alloy", "weight": 13.8 }
				      }
				    ],
				    "commuter_bikes": [
				      {
				        "id": "bike:4",
				        "model": "Salacia",
				        "description": "This bike is a great option for anyone who just wants a bike to get about on With a slick-shifting Claris gears from Shimano’s, this is a bike which doesn’t break the bank and delivers craved performance.  It’s for the rider who wants both efficiency and capability.",
				        "price": 1475,
				        "specs": { "material": "aluminium", "weight": 16.6 },
				        "colors": ["black", "silver"]
				      },
				      {
				        "id": "bike:5",
				        "model": "Mimas",
				        "description": "A real joy to ride, this bike got very high scores in last years Bike of the year report. The carefully crafted 50-34 tooth chainset and 11-32 tooth cassette give an easy-on-the-legs bottom gear for climbing, and the high-quality Vittoria Zaffiro tires give balance and grip.It includes a low-step frame , our memory foam seat, bump-resistant shocks and conveniently placed thumb throttle. Put it all together and you get a bike that helps redefine what can be done for this price.",
				        "price": 3941,
				        "specs": { "material": "alloy", "weight": 11.6 }
				      }
				    ]
				  }
				}
								""";

		// Tests for 'set_bikes' step.

		String res28 = jedis.jsonSet("bikes:inventory", new Path2("$"), inventory_json);
		System.out.println(res28); // >>> OK

		// 依路徑取得特定 value，$表示根路徑

		Object res29 = jedis.jsonGet("bikes:inventory", new Path2("$.inventory.*"));
		System.out.println(res29); // >>> [[{"specs":{"material":"carbon","weight":13.1},"price":1920, ...

		Object res30 = jedis.jsonGet("bikes:inventory", new Path2("$.inventory.mountain_bikes[*].model"));
		System.out.println(res30); // >>> ["Phoebe","Quaoar","Weywot"]

		Object res31 = jedis.jsonGet("bikes:inventory", new Path2("$.inventory[\"mountain_bikes\"][*].model"));
		System.out.println(res31); // >>> ["Phoebe","Quaoar","Weywot"]

		// 列出屬性名稱為 mountain_bikes 下所有的 model

		Object res32 = jedis.jsonGet("bikes:inventory", new Path2("$..mountain_bikes[*].model"));
		System.out.println(res32); // >>> ["Phoebe","Quaoar","Weywot"]

		// 列出特定屬性名稱的 values

		Object res33 = jedis.jsonGet("bikes:inventory", new Path2("$..model"));
		System.out.println(res33); // >>> ["Phoebe","Quaoar","Weywot","Salacia","Mimas"]

		// 從屬性名稱為 mountain_bikes 的陣列中從 index 0 開始取出兩個 model

		Object res34 = jedis.jsonGet("bikes:inventory", new Path2("$..mountain_bikes[0:2].model"));
		System.out.println(res34); // >>> ["Phoebe","Quaoar"]

		// 透過特定的篩選條件拿到單車資訊 (mountain_bikes 價格低於 3000 且重量小於 10)

		Object res35 = jedis.jsonGet("bikes:inventory",
				new Path2("$..mountain_bikes[?(@.price < 3000 && @.specs.weight < 10)]"));
		System.out.println(res35); // >>> [{"specs":{"material":"aluminium","weight":7.9},"price":2072,...

		// 透過特定的篩選條件拿到單車 model (specs.material == 'alloy')

		Object res36 = jedis.jsonGet("bikes:inventory", new Path2("$..[?(@.specs.material == 'alloy')].model"));
		System.out.println(res36); // >>> ["Weywot","Mimas"]

		// 透過特定的篩選條件拿到單車 model (specs.material 含有 al 字樣的，(?i)表示不區分大小寫)

		Object res37 = jedis.jsonGet("bikes:inventory", new Path2("$..[?(@.specs.material =~ '(?i)AL')].model"));
		System.out.println(res37); // >>> ["Quaoar","Weywot","Salacia","Mimas"]

		// 透過特定的篩選條件拿到單車 model (specs.material 含有 regex_pat 屬性的)

		jedis.jsonSet("bikes:inventory", new Path2("$.inventory.mountain_bikes[0].regex_pat"), "\"(?i)al\"");
		jedis.jsonSet("bikes:inventory", new Path2("$.inventory.mountain_bikes[1].regex_pat"), "\"(?i)al\"");
		jedis.jsonSet("bikes:inventory", new Path2("$.inventory.mountain_bikes[2].regex_pat"), "\"(?i)al\"");

		Object res38 = jedis.jsonGet("bikes:inventory",
				new Path2("$.inventory.mountain_bikes[?(@.specs.material =~ @.regex_pat)].model"));
		System.out.println(res38); // >>> ["Quaoar","Weywot"]

		// 拿到所有 price

		Object res39 = jedis.jsonGet("bikes:inventory", new Path2("$..price"));
		System.out.println(res39); // >>> [1920,2072,3264,1475,3941]

		// 拿到所有減去100元的 price

		Object res40 = jedis.jsonNumIncrBy("bikes:inventory", new Path2("$..price"), -100);
		System.out.println(res40); // >>> [1820,1972,3164,1375,3841]

		// 拿到所有加上100元的 price

		Object res41 = jedis.jsonNumIncrBy("bikes:inventory", new Path2("$..price"), 100);
		System.out.println(res41); // >>> [1920,2072,3264,1475,3941]

		// 把所有金額小於2000的單車都變成1500元

		jedis.jsonSet("bikes:inventory", new Path2("$.inventory.*[?(@.price<2000)].price"), 1500);
		Object res42 = jedis.jsonGet("bikes:inventory", new Path2("$..price"));
		System.out.println(res42); // >>> [1500,2072,3264,1500,3941]

		// 把所有金額小於2000的單車都追加一個粉紅色

		List<Long> res43 = jedis.jsonArrAppendWithEscape("bikes:inventory",
				new Path2("$.inventory.*[?(@.price<2000)].colors"), "\"pink\"");
		System.out.println(res43); // >>> [3, 3]

		Object res44 = jedis.jsonGet("bikes:inventory", new Path2("$..[*].colors"));
		System.out.println(res44); // >>> [["black","silver","\"pink\""],["black","white"],["black","silver","\"pink\""]]

	}

}
