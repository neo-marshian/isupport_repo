/*-------------------------------------------------------------------*/
/*                                                                   */
/* Copyright IBM Corp. 2013 All Rights Reserved                      */
/*                                                                   */
/*-------------------------------------------------------------------*/
/*                                                                   */
/*        NOTICE TO USERS OF THE SOURCE CODE EXAMPLES                */
/*                                                                   */
/* The source code examples provided by IBM are only intended to     */
/* assist in the development of a working software program.          */
/*                                                                   */
/* International Business Machines Corporation provides the source   */
/* code examples, both individually and as one or more groups,       */
/* "as is" without warranty of any kind, either expressed or         */
/* implied, including, but not limited to the warranty of            */
/* non-infringement and the implied warranties of merchantability    */
/* and fitness for a particular purpose. The entire risk             */
/* as to the quality and performance of the source code              */
/* examples, both individually and as one or more groups, is with    */
/* you. Should any part of the source code examples prove defective, */
/* you (and not IBM or an authorized dealer) assume the entire cost  */
/* of all necessary servicing, repair or correction.                 */
/*                                                                   */
/* IBM does not warrant that the contents of the source code         */
/* examples, whether individually or as one or more groups, will     */
/* meet your requirements or that the source code examples are       */
/* error-free.                                                       */
/*                                                                   */
/* IBM may make improvements and/or changes in the source code       */
/* examples at any time.                                             */
/*                                                                   */
/* Changes may be made periodically to the information in the        */
/* source code examples; these changes may be reported, for the      */
/* sample code included herein, in new editions of the examples.     */
/*                                                                   */
/* References in the source code examples to IBM products, programs, */
/* or services do not imply that IBM intends to make these           */
/* available in all countries in which IBM operates. Any reference   */
/* to the IBM licensed program in the source code examples is not    */
/* intended to state or imply that IBM's licensed program must be    */
/* used. Any functionally equivalent program may be used.            */
/*-------------------------------------------------------------------*/
package com.tcs.bluemix.isupport;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class User {


	protected void login(HttpServletRequest request, HttpServletResponse response)
 throws Exception {
		System.out.println("----Login Servlet doPost-----");
		Connection conn = null;
		Statement stmt = null;
		String sqlStatement = null;
		String tableName = "USER14281.MYTABLE";
		PrintWriter pw = response.getWriter();
		JSONObject obj = new JSONObject();
		obj.put("err_msg","");
		obj.put("user_id", request.getParameter("user_id"));
		conn = Util.getConnection();
		stmt = conn.createStatement();
		// Execute some SQL statements on the table: Insert, Select and Delete

		sqlStatement = "SELECT * FROM " + tableName + " WHERE USER_ID='" + request.getParameter("user_id") + "'";
		ResultSet rs = stmt.executeQuery(sqlStatement);
		System.out.println("Query-->" + sqlStatement);
		// Process the result set
		String userName;
		response.setContentType("application/json");
		response.setStatus(200);
		if (rs.next()) {
			userName = rs.getString(2);
			obj.put("name", userName);
			obj.put("age", new Integer("24"));
			// pw.println("<b style='color:green'>Found User: " +
			// userName+"</b>");
			if (null != request.getSession(false)) {
				request.getSession(false).invalidate();
			}
			request.getSession(true).setAttribute("user_json", obj);

		} else {
			obj.put("err_msg", "<b style='color:red'>User ID or Password not valid!</b>");
		}

		// Close the ResultSet
		rs.close();
		stmt.close();
		conn.close();
		pw.write(obj.toString());

		// request.getRequestDispatcher("/home.jsp").forward(request, response);
	}

	protected void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(null != request.getSession(false)){
			request.getSession(false).invalidate();
		} 
		
	}
	
	protected void signup(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("----Signup method-----");
		Connection conn = null;
		Statement stmt =  null;
		String sqlStatement = null;
		String tableName = "USER14281.MYTABLE";
		try {
			String name = request.getParameter("name");
			String userId = request.getParameter("username");
			int year = new Integer(request.getParameter("BirthYear"));
			conn = Util.getConnection();	
			stmt =  conn.createStatement();
			sqlStatement = "INSERT INTO " + tableName + " VALUES ('"+userId+"','"+name+"', "+(2016-year)+")";
			stmt.executeUpdate(sqlStatement);
			stmt.close();
			conn.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}

	}
	
	
	

}