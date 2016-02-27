package yan.database;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddrInfo {
	public String ID;
	public String Name;
	public double Latitude;
	public double Longitude;
	public List<AddrInfo> listAddrs;

	@Override
	public String toString() {
		return "AddrInfo [ID=" + ID + ", Latitude=" + Latitude + ", Longitude="
				+ Longitude + ", Name=" + Name + ", listAddrs=" + listAddrs
				+ "]";
	}

	public List<Map<String,Object>> getSubAddrIDNames()
	{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (AddrInfo info : listAddrs) {
			 Map<String,Object> map = new HashMap<String,Object>();
			 map.put("id", info.ID);
			 map.put("name", info.Name);
			 list.add(map);
		}
		return list;
	}
	
	public String getSubAddrName(String id)
	{
		for (Map<String,Object> map : this.getSubAddrIDNames()) {
			if(map.get("id").equals(id))
				return (String) map.get("name");
		}
		return null;
	}
	
	public AddrInfo getSubAddr(String id)
	{
		for (AddrInfo info : listAddrs) {
			if(info.ID.equals(id))
				return info;
		}
		return null;
	}
	
	public static AddrInfo fromID(String id){
		AddrInfo info = new AddrInfo();
		
		String sql = "SELECT ID,AddrID,AddrName,Latitude,Longitude FROM CampusAddr";
		Condition condition = new Condition();
		condition.equal("AddrID", "'"+id+"'");
		ConditionUtils cu = new ConditionUtils(sql);
		cu.addConditionByAnd(condition);
		sql = cu.getSqlString();
		System.out.println(sql);
		
		DBHelper dbHeler = new DBHelper();
		try {
			ResultSet rs = dbHeler.executeQuery(sql);
			if(rs.next()){
				info.ID = rs.getString("AddrID");
				info.Name=new String(rs.getString("AddrName").getBytes("ISO8859-1"),"GBK");
				info.Latitude=Double.parseDouble(rs.getString("Latitude"));
				info.Longitude=Double.parseDouble(rs.getString("Longitude"));
				info.listAddrs=getfromID(info.ID);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return info;
	}
	
	
	private static List<AddrInfo> getfromID(String id) {
		List<AddrInfo> subAddrs = new ArrayList<AddrInfo>();
		
		String sql = "SELECT ID,AddrID,AddrName,Latitude,Longitude FROM CampusAddr";
		Condition condition = new Condition();
		condition.like("AddrID", "'"+id+"__'");
		ConditionUtils cu = new ConditionUtils(sql);
		cu.addConditionByAnd(condition);
		sql = cu.getSqlString();
		System.out.println(sql);
		
		DBHelper dbHeler = new DBHelper();
		try {
			ResultSet rs = dbHeler.executeQuery(sql);
			while (rs.next()) {
				AddrInfo info = new AddrInfo();
				info.ID = rs.getString("AddrID");
				info.Name=new String(rs.getString("AddrName").getBytes("ISO8859-1"),"GBK");
				info.Latitude=Double.parseDouble(rs.getString("Latitude"));
				info.Longitude=Double.parseDouble(rs.getString("Longitude"));
				info.listAddrs=getfromID(info.ID);
				subAddrs.add(info);
			}
		} catch (SQLException e) {
			System.out.println("AddrInfo fromSQL Error.");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return subAddrs;
	}
}
