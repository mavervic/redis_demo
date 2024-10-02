package example.jedis.types.string;

import redis.clients.jedis.JedisPooled;
import java.util.UUID;
import utils.RedisUtils;

public class CaptchaExample {

	public static void main(String[] args) {
		generateAndStoreCaptcha("member:01");

	}

	public static void generateAndStoreCaptcha(String memberId) {
		JedisPooled jedis = RedisUtils.getJedisPooled();
		// 生成6位隨機驗證碼
		String captcha = UUID.randomUUID().toString().substring(0, 6);
		System.out.println("生成的驗證碼為: " + captcha);

		// 設定1分鐘自動刪除這個驗證碼
		long expireSeconds = 60;
		String key = "captcha:" + memberId;
		jedis.set(key, captcha);
		jedis.expire(key, expireSeconds);

		System.out.println("驗證碼已存儲，將在 " + expireSeconds + " 秒後過期");
	}
}