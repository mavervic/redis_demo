package example.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MockDataUtils {

	private MockDataUtils() {
	}

	public static Map<String, Object> getObj() {
		Map<String, Object> obj = new LinkedHashMap<>();
		obj.put("boolean_true", true);
		obj.put("boolean_false", false);
		obj.put("string", "hello world");
		obj.put("number", 123);
		obj.put("empty", null);
		return obj;
	}

	public static List<Object> getObjArray() {
		List<Object> objArray = new ArrayList<>();
		Map<String, Object> personA = new HashMap<>();
		personA.put("name", "vic");
		Map<String, Object> personB = new HashMap<>();
		personB.put("name", "david");
		Map<String, Object> personC = new HashMap<>();
		personC.put("name", "peter");
		objArray.add(personA);
		objArray.add(personB);
		objArray.add(personC);
		return objArray;
	}

	public static List<String> getArray() {
		List<String> array = new ArrayList<>();
		array.add("10");
		array.add("20");
		array.add("30");
		return array;
	}

	public static Map<String, Object> getObjContentArray() {
		Map<String, Object> obj = getObj();
		List<String> array = MockDataUtils.getArray();
		obj.put("array", array);
		return obj;
	}

	public static Map<String, Object> getObjContentArrayAndObjArray() {
		Map<String, Object> objContentArray = getObjContentArray();
		List<Object> objArray = getObjArray();
		objContentArray.put("objArray", objArray);
		return objContentArray;
	}

	public static Map<String, Object> getProductInventory() {
		Map<String, Object> objContentArray = getObjContentArray();
		List<Object> objArray = getObjArray();
		objContentArray.put("objArray", objArray);
		return objContentArray;
	}
	
	public static Person getPerson(String name, Integer age) {
		Person person = new Person(name, age);
		return person;
	}

	static class Person implements Serializable {
		private static final long serialVersionUID = 1L;

		public Person(String name, Integer age) {
			super();
			this.name = name;
			this.age = age;
		}

		private String name;
		private Integer age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		@Override
		public String toString() {
			return "Person [name=" + name + ", age=" + age + "]";
		}
	}
}
