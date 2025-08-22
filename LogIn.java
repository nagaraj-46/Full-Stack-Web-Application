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
 * Servlet implementation class LogIn
 */
@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogIn() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String mobileNumber = request.getParameter("MobileNumber");
		String password = request.getParameter("NewPassword");
		PrintWriter pw = response.getWriter();
		//pw.print(mobileNumber+" "+password);
		try {
			database(mobileNumber,password, pw, request, response);
		}catch(Exception e) {
			pw.print(e);
		}
	}
	public static void database(String mobileNumber, String Password, PrintWriter pw, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root", "Nagaraj@123456");
	    try {
	        if(selectMobile(con, mobileNumber) && selectPassword(con, mobileNumber,Password, pw)) {
	        	RequestDispatcher dispatcher = request.getRequestDispatcher("/elotronics.html");
	    	    dispatcher.forward(request, response);
	        } else {
	            pw.print("Invalid mobile number or password");
	            response.setContentType("text/html");
	            RequestDispatcher dispatcher = request.getRequestDispatcher("/wrong.html");
	    	    dispatcher.include(request, response);
	        }
	    } catch (Exception e) {
	        pw.print("Error: " + e.getMessage());
	    } finally {
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
	public static boolean selectPassword(Connection con, String mobileNumber, String password, PrintWriter pw) throws Exception {
	    try {
	        String sql = "SELECT ConfirmPassword FROM userdetails WHERE MobileNumber = ?";
	        PreparedStatement pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, mobileNumber); 
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            String confirmPassword = rs.getString("ConfirmPassword");
	            //pw.print(confirmPassword);
	            if (password.equals(confirmPassword)) {
	                return true;
	            } else {
	                return false; 
	            }
	        } else {
	            pw.print("Mobile number not found...");
	            return false;
	        }
	    } catch (Exception e) {
	        pw.print(e.getMessage());
	        return false;
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
