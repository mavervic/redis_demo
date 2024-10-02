package example.jedis.types.string;

import redis.clients.jedis.JedisPooled;
import utils.RedisUtils;

public class InventorySystem {
	private JedisPooled jedis;

	public InventorySystem() {
		this.jedis = RedisUtils.getJedisPooled();
	}

	// 添加庫存
	public void addStock(String itemId, int quantity) {
		System.out.println("增加 " + quantity + " 單位庫存到商品 " + itemId);
		long curr = jedis.incrBy("inventory:" + itemId, quantity);
		System.out.println("商品 " + itemId + " 的目前庫存: " + curr);
	}

	// 減少庫存
	public boolean removeStock(String itemId, int quantity) {
		String key = "inventory:" + itemId;
		int currentStock = Integer.parseInt(jedis.get(key));

		if (currentStock >= quantity) {
			System.out.println("從商品 " + itemId + " 中移除 " + quantity + " 單位");
			long curr = jedis.decrBy(key, quantity);
			System.out.println("商品 " + itemId + " 的目前庫存: " + curr);
			return true;
		} else {
			System.out.println("商品 " + itemId + " 的庫存不足");
			return false;
		}
	}

	// 獲取庫存
	public int getStock(String itemId) {
		String stock = jedis.get("inventory:" + itemId);
		return stock == null ? 0 : Integer.parseInt(stock);
	}

	public static void main(String[] args) {
		InventorySystem inventorySystem = new InventorySystem();

		String itemId = "iPhone8";

		// 添加庫存
		inventorySystem.addStock(itemId, 100);

		// 獲取庫存
		int stock = inventorySystem.getStock(itemId);
		System.out.println("商品 " + itemId + " 的當前庫存: " + stock);

		// 減少庫存
		boolean success = inventorySystem.removeStock(itemId, 30);
		if (success) {
			stock = inventorySystem.getStock(itemId);
			System.out.println("移除後商品 " + itemId + " 的當前庫存: " + stock);
		}

		// 嘗試減少超過現有庫存的數量
		success = inventorySystem.removeStock(itemId, 80);
		if (!success) {
			stock = inventorySystem.getStock(itemId);
			System.out.println("移除失敗後商品 " + itemId + " 的當前庫存: " + stock);
		}
	}
}