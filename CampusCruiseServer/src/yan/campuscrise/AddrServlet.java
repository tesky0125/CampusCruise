package yan.campuscrise;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sdicons.json.mapper.MapperException;

import yan.database.AddrInfo;
import yan.database.JsonUtils;

/**
 * Servlet implementation class AddrServlet
 */
public class AddrServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddrServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//服务器端设置
		request.setCharacterEncoding("UTF-8");
		//String id = request.getParameter("id");
		String id = "010215";
		//客户端设置
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		AddrInfo info = AddrInfo.fromID(id);
		System.out.println(info);
//		System.out.println(info.getSubAddrIDNames());
//		System.out.println(info.getSubAddrName("010201"));
//		System.out.println(info.getSubAddr("010202"));
		List<Class> claList = new ArrayList<Class>();
		claList.add(AddrInfo.class);
		try {
			String json = JsonUtils.simpleObjectToJsonStr(info,claList);
			System.out.println(json);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		/*Map<String,Object> map = new HashMap<String,Object>();
		map.put("key1", 3);
		map.put("key2", "123");
		map.put("key3", info);
		try {
			System.out.println(JsonUtils.simpleMapToJsonStr(map,claList));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}*/
		
		
		/*ClientTicketOrder order = new ClientTicketOrder();    
        order.setClientOrderNo("0812333");     
            
        TicketItem tItem = new TicketItem();    
        tItem.setPnr("UY8YG");    
            
        order.setTicketItems(new TicketItem[]{tItem});          
            
        //JavaBean to JSON     
        try {
			System.out.println(JsonUtils.objectToJsonStr(order, true));
		} catch (MapperException e) {
			e.printStackTrace();
		}*/   

		
		String msg = "Test.";
		//向客户端发送消息
		out.print(msg);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
