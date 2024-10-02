package example.jedis.types.zset;

import redis.clients.jedis.JedisPooled;
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

		// 移除過期的操作記錄
		jedis.zremrangeByScore(key, 0, currentTimestamp - windowSize);

		// 計算當前時間窗口內的操作次數
		long operationCount = jedis.zcount(key, currentTimestamp - windowSize, currentTimestamp);
		if (operationCount >= limit) {
			System.out.println("Rate limit exceeded for user: " + userId);
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