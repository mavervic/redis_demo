package example.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PersonTest {

	public static void main(String[] args) {
		Gson gson = new Gson();

		Person vic = new Person("Vic", 10);
		Person david = new Person("David", 20);
		Person peter = new Person("Peter", null);

		// 把 Java 物件轉成 JSON 格式的字串
		System.out.println(gson.toJson(vic)); // {"name":"Vic","age":10}
		System.out.println(gson.toJson(david)); // {"name":"David","age":20}
		System.out.println(gson.toJson(peter)); // {"name":"Peter"}

		// 把 JSON 格式的字串轉回 Java 物件
		System.out.println(gson.fromJson("{\"name\":\"Vic\",\"age\":10}", Person.class)); // Person [name=Vic, age=10]
		System.out.println(gson.fromJson("{\"name\":\"David\",\"age\":20}", Person.class)); // Person [name=David, age=20]
		System.out.println(gson.fromJson("{\"name\":\"Peter\"}", Person.class)); // Person [name=Peter, age=null]

		// 建立一個字串化必須含有null的gson物件
		Gson gsonWillNull = new GsonBuilder().serializeNulls().create();
		System.out.println(gson.toJson(peter)); // {"name":"Peter"}
		System.out.println(gsonWillNull.toJson(peter)); // {"name":"Peter","age":null}
	}
}
