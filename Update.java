package Application;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Update
 */
@WebServlet("/Update")
public class Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		//UPDATE userdetails SET ConfirmPassword = 'Gopal@9121', NewPassword = 'Gopal@9121' WHERE MobileNumber = 9121747312;
		String mobileNumber = request.getParameter("mobileNumber");
		String newPassword = request.getParameter("newPassword");
		String comfirmPassword = request.getParameter("ConfirmPassword");
		PrintWriter pw = response.getWriter();
		try {
			database(mobileNumber, newPassword,comfirmPassword,pw,request,response);
		}catch(Exception e) {
			e.getMessage();
		}
	}
	public static void database(String mobileNumber, String newPassword,String ComfiramPassword,PrintWriter pw, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root", "Nagaraj@123456");
	    try {
	        if(selectMobile(con, mobileNumber)){
	        	 String insert = "UPDATE userdetails SET ConfirmPassword = ?, NewPassword = ? WHERE MobileNumber = ?";
	    		 PreparedStatement pstmt = con.prepareStatement(insert);
	    		 pstmt.setString(1,ComfiramPassword);
	             pstmt.setString(2, newPassword);
	             long m = Long.parseLong(mobileNumber);
	             pstmt.setLong(3,m);
	             int x = pstmt.executeUpdate();
	             if(x>0) {
	            	 	//pw.print("User details inserted successfully.");
	            	 	response.setContentType("text/html");
	            	 	RequestDispatcher dispatcher = request.getRequestDispatcher("/final.html");
	            	 	dispatcher.include(request, response);
	            	 	pstmt.close();
	             }else {
	            	 response.setContentType("text/html");
	            	 pw.print("Somthing Worng!!!");
	            	 pstmt.close();
	             }
	       }else {
	    	   response.setContentType("text/html");
	    	   pw.print("<br>");
	    	   pw.print("Mobile Number Not Found!!!");
	    	   RequestDispatcher dispatcher = request.getRequestDispatcher("/wrong.html");
       	 	   dispatcher.include(request, response);
	       }
	    } catch (Exception e) {
	        pw.print("Error: " + e.getMessage());
	    } finally {
	    	pw.close();
			con.close();
	        con.close(); 
	    }
	}
	public static boolean selectMobile(Connection con, String mobileNumber) throws Exception {
		try{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from userdetails");
			long mn = Long.parseLong(mobileNumber);
			while(rs.next())
			{
				long m =rs.getLong(6);
				if(mn==m) {
					return true;
				}
			}
		}catch(Exception e) {
				e.getMessage();
				return false;
		}
		return false;
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
