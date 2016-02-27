package yan.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Condition {
	private List<String> ConditionList = new ArrayList<String>();

	public List<String> getConditionList() {
		return ConditionList;
	}

	public void setConditionList(List<String> conditionList) {
		ConditionList = conditionList;
	}

	public void cleanCondition() {
		ConditionList.clear();
	}

	public void between(String name, String min, String max) {
		ConditionList.add(name + " between " + min + " and " + max);
	}

	public void in(String name, String[] values) {
		ConditionList.add(name + " in ( " + StringUtils.join(values, ",")
				+ " )");
	}

	public void like(String name, String value) {
		ConditionList.add(name + " like " + value);
	}

	public void equal(String name, String value) {
		ConditionList.add(name + " = " + value);
	}

	public void not_equal(String name, String value) {
		ConditionList.add(name + " != " + value);
	}

	public void greater_than(String name, String value) {
		ConditionList.add(name + " > " + value);
	}

	public void less_than(String name, String value) {
		ConditionList.add(name + " < " + value);
	}

	public void less_equal(String name, String value) {
		ConditionList.add(name + " <= " + value);
	}

	public void grater_equal(String name, String value) {
		ConditionList.add(name + " >= " + value);
	}

	public void add(String name, String condition, String value) {
		ConditionList.add(name + " " + condition + " " + value);
	}

}
