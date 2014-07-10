<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="utils.Configure"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
    </head>
    <body>
        <%
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        
        boolean ok = false;//(username.equals("admin") && password.equals("123456"));
        
        Connection cnn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        try{
        	String user = "root";
        	String pass = "1234567890";
        	Class.forName("com.mysql.jdbc.Driver").newInstance ();
        	//System.out.println("==>"+Configure.cnnStr);
        	cnn = DriverManager.getConnection(Configure.cnnStr,user,pass);
        	
			stat = cnn.createStatement();
			rs = stat.executeQuery("select * from users_management");
			while(rs.next()){
				String uid = rs.getString("UserID");
				String pwd = rs.getString("Pass");
				if(uid.equals(username) && pwd.equals(password)){
					ok = true;
					break;
				}
			}
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        if(ok)
            {        	
            session.setAttribute("username",username);
            response.sendRedirect("home.jsp");
            }
        else
        	
            response.sendRedirect("ErrorLogin.jsp");
        %>
    </body>
</html>

