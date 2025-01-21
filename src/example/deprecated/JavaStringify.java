package example.deprecated;

import java.util.List;
import java.util.Map;

import utils.MockDataUtils;

public class JavaStringify {

	public static void main(String[] args) {
		// [10, 20, 30]
		List<String> array = MockDataUtils.getArray();
		System.out.println("陣列的字串化: " + array);

		// [{name=vic}, {name=david}, {name=peter}]
		List<Object> objArray = MockDataUtils.getObjArray();
		System.out.println("物件的陣列的的字串化: " + objArray);

		// {boolean_true=true, boolean_false=false, string=hello world, number=123, empty=null}
		Map<String, Object> obj = MockDataUtils.getObj();
		System.out.println("物件的字串化: " + obj);

		// {boolean_true=true, boolean_false=false, string=hello world, number=123, empty=null, array=[10, 20, 30]}
		Map<String, Object> objContentArray = MockDataUtils.getObjContentArray();
		System.out.println("物件含陣列的字串化: " + objContentArray);

		// {boolean_true=true, boolean_false=false, string=hello world, number=123, empty=null, 
		// array=[10, 20, 30],
		// objArray=[{name=vic}, {name=david}, {name=peter}]}
		Map<String, Object> objContentArrayAndObjArray = MockDataUtils.getObjContentArrayAndObjArray();
		System.out.println("物件含陣列含陣列元素是物件的字串化: " + objContentArrayAndObjArray);
	}
}
