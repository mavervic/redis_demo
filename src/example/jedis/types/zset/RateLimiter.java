package example.jedis.types.zset;

import java.util.Date;
import java.util.List;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.resps.Tuple;
import utils.RedisUtils;

public class RateLimiter {
	private JedisPooled jedis;

	/** 時間窗口大小（秒），在這個時間窗口內限流 */
	private int windowSize;

	/** 時間窗口內的操作次數限制 */
	private int limit;

	public RateLimiter(int windowSize, int limit) {
		this.jedis = RedisUtils.getJedisPooled();
		this.windowSize = windowSize;
		this.limit = limit;
	}

	public boolean addOperation(String userId, String operationId) {
		long currentTimestamp = System.currentTimeMillis() / 1000;
		String key = "rate_limiter:" + userId;

		// 使用滑動窗口，每次的操作皆會先移除過期的操作記錄
		System.out.println("移除 " + new Date((currentTimestamp - windowSize) * 1000) + " 之前的操作紀錄");
		jedis.zremrangeByScore(key, 0, currentTimestamp - windowSize);

		// 計算當前時間窗口內的操作次數
		long operationCount = jedis.zcount(key, currentTimestamp - windowSize, currentTimestamp);
		System.out.println("近 " + windowSize + " 秒的操作次數為 " + operationCount);
		if (operationCount >= limit) {
			System.out.println("Rate limit exceeded for user: " + userId);
			
			List<Tuple> zrangeWithScores = jedis.zrangeWithScores(key, 0, 1);
			long firstTimestamp = (long) zrangeWithScores.getFirst().getScore();
			System.out.println("最近可操作時間為 " + new Date((firstTimestamp + windowSize) * 1000));
			return false;
		} else {
			// 添加新的操作記錄
			jedis.zadd(key, currentTimestamp, operationId);
			System.out.println("Operation allowed for user: " + userId);
			return true;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		// 模擬Docker Hub限流，設定時間窗口為 6 小時（21600 秒），限流次數為 200 次
		int sixHours = 60 * 60 * 6;
		RateLimiter rateLimiter = new RateLimiter(sixHours, 200);
		// 模擬已經登入的01號用戶
		String userId = "user01";
		for (int i = 0; i < 210; i++) {
			String operationId = "operation" + i;
			boolean allowed = rateLimiter.addOperation(userId, operationId);
			if (!allowed) {
				System.err.println("使用者 " + userId + " 已經到達使用上限");
			}
			Thread.sleep(1000); // 模擬每 1 秒一次操作
		}
	}
}