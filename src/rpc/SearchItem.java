package rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ExternalApis.TicketMasterAPI;
import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}


		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		
		// term can be empty
		String term= request.getParameter("term");
		String userId = request.getParameter("user_id");
		DBConnection connection = DBConnectionFactory.getConnection();
		Set<String>favIds =  connection.getFavoriteItemIds(userId);
		
		try {
			List<Item> items = connection.searchItems(lat, lon, term);
 		    JSONArray array = new JSONArray();
 		    for (Item item : items) {
 		    	JSONObject obj = item.toJSONObject();
 		    	if (favIds.contains(item.getItemId())) {
 		    		obj.append("favorite", true);
 		    	}
 		    	array.put(obj);
 		    	
 		    }
 		    RpcHelper.writeJsonArray(response, array);
 		 }catch (Exception e) {
 		   e.printStackTrace();
 	     }finally {
 		 connection.close();
 	     }

	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
