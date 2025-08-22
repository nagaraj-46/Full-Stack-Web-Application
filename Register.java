package Application;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Register() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String firstName = request.getParameter("firstName");
		String scondName = request.getParameter("scondName");
		String date = request.getParameter("userDate");
		String month = request.getParameter("userMonth");
		String year = request.getParameter("userYear");
		String gender = request.getParameter("gender");
		String mobileNumber = request.getParameter("userMobileNumber");
		String email = request.getParameter("userEmail");
		String newPassword = request.getParameter("newPassword");
		String conformPassword = request.getParameter("conformPassword");
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		pw.print("<br>");
		//pw.print(firstName+" "+scondName+" "+date+" "+month+" "+year+" "+gender+" "+mobileNumber+" "+email+" "+newPassword+" "+conformPassword);
		String fullName = firstName+" "+scondName;
		String cdate = date+"-"+month+"-"+year;
		long cmobileNumber = Long.parseLong(mobileNumber);
		try {
			database(firstName,scondName,fullName,cdate,gender,cmobileNumber,email,newPassword,conformPassword,pw, request,response);
		}catch(SQLException e) {
			pw.print(e);
		}catch(Exception e) {
			e.getMessage();
			pw.print(e);
		    }
	}
	public static void database(String firstName, String secondName, String fullName,String dateOfBirth,String gender, long mobileNumber, String userEmail, String newPassword, String confirmPassword, PrintWriter pw, HttpServletRequest request, HttpServletResponse response)throws Exception {
		//create table userdetails(FirstName char(10), ScoundName char(24), FullName char(40), DataofBirth date, Gender char(6), MobileNumber long, UserEmail char(32), newPassword char(25), ConfirmPassword char(25));
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root", "Nagaraj@123456");
		String insert = "INSERT INTO userdetails(FirstName, ScondName, FullName, DateofBirth, Gender, MobileNumber, UserEmail, NewPassword, ConfirmPassword)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			if(isValidMobileNumer(mobileNumber+"")) {
				if(!selectGmail(con,userEmail,pw)) {
					if(!selectMobile(con,mobileNumber+"",pw)) {
						 PreparedStatement pstmt = con.prepareStatement(insert);
						 pstmt.setString(1, firstName);
				         pstmt.setString(2, secondName);
				         pstmt.setString(3, fullName);
				         pstmt.setString(4, dateOfBirth);
				         pstmt.setString(5, gender);
				         pstmt.setLong(6, mobileNumber);
				         pstmt.setString(7, userEmail);
				         pstmt.setString(8, newPassword);
				         pstmt.setString(9, confirmPassword);
				         int x = pstmt.executeUpdate();
				 		if(x>0) {
				 			//pw.print("User details inserted successfully.");
				 			response.setContentType("text/html");
				             RequestDispatcher dispatcher = request.getRequestDispatcher("/final.html");
				     	    dispatcher.include(request, response);
				 			pstmt.close();
				 		}else {
				 			response.setContentType("text/html");
				             RequestDispatcher dispatcher = request.getRequestDispatcher("/wrong.html");
				     	     dispatcher.include(request, response);
				     	     pstmt.close();
				 		}
					}else {
						response.setContentType("text/html");
						RequestDispatcher dispatcher = request.getRequestDispatcher("/Back2.html");
			     	    dispatcher.include(request, response);
					}	
				}else {
					response.setContentType("text/html");
					RequestDispatcher dispatcher = request.getRequestDispatcher("/Back3.html");
		     	    dispatcher.include(request, response);
				}
			}else {
				response.setContentType("text/html");
				RequestDispatcher dispatcher = request.getRequestDispatcher("/Back1.html");
	     	    dispatcher.include(request, response);
			}
		}catch(Exception e){
			e.getMessage();
		}finally {
			pw.close();
 			con.close();
		}
	}
	public static boolean selectMobile(Connection con, String mobileNumber, PrintWriter pw)throws Exception {
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
	
	public static boolean isValidMobileNumer(String m) {
		if((m.startsWith("9") || m.startsWith("8") || m.startsWith("7") || m.startsWith("6") && m.length()==10)){
			for(int i=0; i<m.length();i++) {
				char ch = m.charAt(i);
				if(ch<'0' && ch>'9') {
					return false;
				}
			}
		}else {
			return false;
		}
		return true;
	}
	
	public static boolean selectGmail(Connection con, String Gmail, PrintWriter pw)throws Exception {
		try{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from userdetails");
			while(rs.next())
			{
				String DataBasemail =rs.getString(7);
				if(Gmail.equalsIgnoreCase(DataBasemail)) {
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
		/*MysqlDataSource mds = new MysqlDataSource();\r\n"
		+ "		mds.setUrl(\"jdbc:mysql://localhost:3306/db1\");\r\n"
		+ "		mds.setUser(\"root\");\r\n"
		+ "		mds.setPassword(\"Venu@9121\");\r\n"
		+ "		Connection con = mds.getConnection();" */
	}

}
