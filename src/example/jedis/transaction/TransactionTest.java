package example.jedis.transaction;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TransactionTest {

	public static void main(String[] args) {
		Thread thread1 = new Thread(() -> performTransaction("runner 01"));
		Thread thread2 = new Thread(() -> performTransaction("runner 02"));

		thread1.start();
		thread2.start();
	}

	private static void performTransaction(String name) {
		Jedis jedis = new Jedis("localhost", 6379);
		jedis.auth("mypassword");
		String key = "who.win";

		// Set initial value
		jedis.set(key, "initialValue");

		// Watch the key
		jedis.watch(key);
		System.out.println(name + ": 開始watch了");
		// Start a transaction
		Transaction transaction = jedis.multi();

		// Modify the key in the transaction
		transaction.set(key, name + " is Winner");

		// If the key was not modified by another client, execute the transaction
		// If the key was modified, discard the transaction
		List<Object> results = transaction.exec();
		System.out.println(name + ": 執行了");

		if (results == null) {
			System.out.println(name + ": The key was modified by another client. Transaction was discarded.");
		} else {
			System.out.println(name + ": Transaction was executed successfully.");
			jedis.zincrby("win.record", 1, name);
		}

		// Close the connection
		jedis.close();
	}

}
