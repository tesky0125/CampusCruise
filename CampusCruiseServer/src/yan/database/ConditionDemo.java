package yan.database;

public class ConditionDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sql = "SELECT ID,AddrID,AddrName,Latitude,Longitude FROM CampusAddr";

		Condition condition = new Condition();
		condition.equal("AddrName", "'Someone like you'");
		condition.like("AddrID", "'%0102%'");

		ConditionUtils cu = new ConditionUtils(sql);

		cu.addConditionByOr(condition);

		System.out.println(cu.getSqlString());
		// clear condition
		condition.cleanCondition();

		condition.between("ID", "2", "4");
		condition.grater_equal("AddrID", "2");

		cu.addConditionByAnd(condition);

		System.out.println(cu.getSqlString());

	}
}
