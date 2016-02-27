package yan.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ConditionUtils {
	private String sql;
	private String orderBy = "";
	private List<String> conditions = new ArrayList<String>();

	public ConditionUtils(String sql) {
		this.sql = sql;
	}

	public void addConditionByAnd(Condition condition) {
		conditions.add(combinationCondition(condition, "and"));
	}

	public void addConditionByOr(Condition condition) {
		conditions.add(combinationCondition(condition, "or"));
	}

	public void addOrderBy(String sortName, String order) {
		this.orderBy = " order by " + sortName + " " + order;
	}

	public String getSqlString() {
		return sql + combinationConditions() + orderBy;
	}

	private String combinationCondition(Condition condition, String contact) {
		List<String> conditionList = condition.getConditionList();
		if (conditionList.size() == 0)
			return null;
		return StringUtils.join(conditionList, " " + contact + " ");
	}

	private String combinationConditions() {
		if (conditions.size() == 0)
			return null;
		return " where " + StringUtils.join(conditions, " and ");
	}

}
