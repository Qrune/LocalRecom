package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jdt.internal.compiler.env.IGenericField;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
			JSONObject obj = new JSONObject();
			HttpSession session = request.getSession(false);
			if (session == null) {
				response.setStatus(403);
				obj.put("status","Session inValid");
			}else {
				String userId = (String)session.getAttribute("user_id");
				String name = conn.getFullname(userId);
				obj.put("status", "OK");
				obj.put("user_id", userId);
				obj.put("name", name);
			}
			RpcHelper.writeJsonObject(response, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			conn.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJSONObject(request);
			String userId = input.getString("user_id");
			String passwd = input.getString("password");
			JSONObject object = new JSONObject();
			if (connection.verifyLogin(userId, passwd)) {
				HttpSession session = request.getSession();
				session.setAttribute("user_id", userId);
				//set expeied after 600 sec
				session.setMaxInactiveInterval(10 * 60);
				String name = connection.getFullname(userId);
				object.put("status", "OK");
				object.put("user_id", userId);
				object.put("name", name);
				RpcHelper.writeJsonObject(response, object);
			}else {
				response.setStatus(401);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connection.close();
		}
	}

}
