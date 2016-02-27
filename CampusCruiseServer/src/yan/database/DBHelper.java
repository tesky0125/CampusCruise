package yan.database;
import java.sql.*;

public class DBHelper {
//	SQL Server �����ַ���	
//	private final String dbDriver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
//	private final String url="jdbc:sqlserver://localhost:1433;databaseName=TeachWeb";
//	private final String userName="sa";
//	private final String password="880125";

//	MySQL �����ַ���
	private final String dbDriver="com.mysql.jdbc.Driver";
	private final String url="jdbc:mysql://localhost:3306/Campus";
	private final String userName="root";
	private final String password="root";
	
    Connection conn;
    //Statement stmt;
    ResultSet rs;
	public DBHelper(){//���캯����ʼ������
		try{
			Class.forName(dbDriver).newInstance();//ע����Ĭ�Ϲ��캯���м�������������
			if(this.createConnection())//���������ݿ�������Σ��ó����д������
				System.out.println("��ʾ�����ݿ��������سɹ���");	
		}catch(Exception ex){
			System.out.println("��ʾ�����ݿ���������ʧ�ܣ�");//�ڿ���̨���жϳ����Ƿ�ִ�гɹ�
		}
	}
	public boolean createConnection(){//�������ݿ⣬�Ա��ϲ������캯����
		try{
			conn=DriverManager.getConnection(url,userName,password);
			System.out.println("��ʾ�����ݿ����ӳɹ���");
		}catch(SQLException e){
			System.out.println("��ʾ�����ݿ�����ʧ�ܣ�");
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
			System.out.println("��ʾ��ִ�в�ѯ�����ɹ���");
      	}
      	catch(SQLException ex){
			System.out.println("��ʾ��ִ�в�ѯ����ʧ�ܣ�");     	
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
				System.out.println("��ʾ��ִ�в�ѯʱ�ر����ݿ�����ʧ�ܣ�");	
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
			System.out.println("��ʾ��ִ�и��²����ɹ���");	
	   }
	   catch(SQLException ex)
	   {
			System.out.println("��ʾ��ִ�и��²���ʧ�ܣ�");		   
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
				System.out.println("��ʾ��ִ�и���ʱ�ر����ݿ�����ʧ�ܣ�");		
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
			System.out.println("��ʾ���ر����ݿ����ӳɹ���");	
       }
       catch(Exception e)
       {
			System.out.println("��ʾ���ر����ݿ�����ʧ�ܣ�");	
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
			System.out.println("��ʾ��Ԥ������䴴��ʧ�ܣ�");	
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
  			System.out.println("��ʾ������ת��ʧ�ܣ�");	
            return null;
          }
    }
}
