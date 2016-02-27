package yan.database;
import java.sql.*;

public class DBHelper {
//	SQL Server 连接字符串	
//	private final String dbDriver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
//	private final String url="jdbc:sqlserver://localhost:1433;databaseName=TeachWeb";
//	private final String userName="sa";
//	private final String password="880125";

//	MySQL 连接字符串
	private final String dbDriver="com.mysql.jdbc.Driver";
	private final String url="jdbc:mysql://localhost:3306/Campus";
	private final String userName="root";
	private final String password="root";
	
    Connection conn;
    //Statement stmt;
    ResultSet rs;
	public DBHelper(){//构造函数初始化驱动
		try{
			Class.forName(dbDriver).newInstance();//注意在默认构造函数中加载驱动的区别
			if(this.createConnection())//把连接数据库过程屏蔽，让程序编写更方便
				System.out.println("提示：数据库驱动加载成功！");	
		}catch(Exception ex){
			System.out.println("提示：数据库驱动加载失败！");//在控制台中判断程序是否执行成功
		}
	}
	public boolean createConnection(){//连接数据库，以被合并到构造函数中
		try{
			conn=DriverManager.getConnection(url,userName,password);
			System.out.println("提示：数据库连接成功！");
		}catch(SQLException e){
			System.out.println("提示：数据库连接失败！");
			return false;
		}
		return true;
	}
    public ResultSet executeQuery(String sql)   {
        Statement stmt = null;
        try {
			if(null==conn){
				this.createConnection();
			}
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql);
			System.out.println("提示：执行查询操作成功！");
      	}
      	catch(SQLException ex){
			System.out.println("提示：执行查询操作失败！");     	
      	}
        /*finally
        {
            try
            {
                if(stmt != null)
                    stmt.close();
                if(conn != null)
              	  conn.close();
            }
            catch(SQLException ex)
            {
              	//System.err.print(ex);
				System.out.println("提示：执行查询时关闭数据库连接失败！");	
            }
        }*/
      	return rs;
    	}
	public int executeUpdate(String sql)  {
	   int count = 0;
	   Statement stmt = null;
	   try
	   {
			if(null==conn){
				this.createConnection();
			}
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			count = stmt.executeUpdate(sql);
			System.out.println("提示：执行更新操作成功！");	
	   }
	   catch(SQLException ex)
	   {
			System.out.println("提示：执行更新操作失败！");		   
	   }
	   /*finally
	   {
          try
          {
              if(stmt != null)
                  stmt.close();
              if(conn != null)
            	  conn.close();
          }
          catch(SQLException ex)
          {
              	//System.err.print(ex);
				System.out.println("提示：执行更新时关闭数据库连接失败！");		
          }
      }*/
      return count;
	}
	public void closeConn()
	{
       try
       {
           if(rs != null )
           {
               rs.close();
               if(conn !=null)
            	   conn.close();
           }
			System.out.println("提示：关闭数据库连接成功！");	
       }
       catch(Exception e)
       {
			System.out.println("提示：关闭数据库连接失败！");	
       }
	}
	public PreparedStatement prepareStmt(String sql) {
		PreparedStatement pstmt = null;
		try {
			if(null==conn){
				this.createConnection();
			}
			pstmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("提示：预编译语句创建失败！");	
		}
		return pstmt;
	}
	public static String toChinese(String strvalue)
    {
          try{
              if(strvalue==null)
                 return null;
              else
              {
                 strvalue = new String(strvalue.getBytes("ISO8859-1"), "GBK");
                 return strvalue;
          }
          }catch(Exception e){
  			System.out.println("提示：语言转换失败！");	
            return null;
          }
    }
}
