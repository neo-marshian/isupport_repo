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
package com.ibm.bluemix.samples;

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

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("----Upload Servlet doPost-----");
		Connection conn = null;
		Statement stmt =  null;
		String sqlStatement = null;
		String tableName = "USER14281.MYTABLE";
		PrintWriter pw = response.getWriter();
		try {
			conn = getConnection();		
			stmt =  conn.createStatement();
		// Execute some SQL statements on the table: Insert, Select and Delete
System.out.println("attribute------------>"+request.getAttribute("user_id"));
			sqlStatement = "SELECT * FROM " + tableName + " WHERE USER_ID='"+request.getParameter("user_id")+"'";
			ResultSet rs = stmt.executeQuery(sqlStatement);
			System.out.println("Query-->"+sqlStatement);
			// Process the result set
			String userName;
			JSONObject obj = new JSONObject();
			response.setContentType("application/json");
			System.out.println("<<---JSON Handle in Servlet---->");
			response.setStatus(200);
			if (rs.next()) {
				userName = rs.getString(2);
				obj.put("name", userName);
			    pw.write(obj.toString());
				//pw.println("<b style='color:green'>Found User: " + userName+"</b>");
			} else{
				pw.println("<b style='color:red'>User ID or Password not valid!</b>");
			}
					      
			// Close the ResultSet
			rs.close();

		} catch (SQLException e) {
			pw.println("Error executing:" + sqlStatement);
			pw.println("SQL Exception: " + e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pw.flush();
		pw.close();

		//request.getRequestDispatcher("/home.jsp").forward(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		String stdkey = request.getParameter("key");
		stdkey = "%" + stdkey + "%";
		// test purpose		
		System.out.println("----------------Inside doGet from Ajax-------------" + stdkey);
		Statement stmt =  null;
		String sqlStatement = null;
		String tableName = "USER14281.MYTABLE";
		PrintWriter pw = response.getWriter();
		// Execute some SQL statements on the table: Insert, Select and Delete
		try {
			stmt =  conn.createStatement();
			sqlStatement = "INSERT INTO " + tableName + " VALUES (\'js500\',\'John Smith\', 52)";
			stmt.executeUpdate(sqlStatement);

			sqlStatement = "SELECT * FROM " + tableName + " WHERE USER_NAME LIKE \'John%\'";
			ResultSet rs = stmt.executeQuery(sqlStatement);

			// Process the result set
			String userId;
			while (rs.next()) {
				userId = rs.getString(1);
				pw.println("Found Employee: " + userId);
			}
			// Close the ResultSet
			rs.close();

			// Delete the record
			sqlStatement = "DELETE FROM " + tableName + " WHERE USER_NAME = \'John Smith\'";
			stmt.executeUpdate(sqlStatement);
						
		} catch (SQLException e) {
			pw.println("Error executing:" + sqlStatement);
			pw.println("SQL Exception: " + e);
		}

	}
	
	private static Connection getConnection() throws Exception {
		Map<String, String> env = System.getenv();
		System.out.println("<----------------------inside getConnection--------------->");
		if (env.containsKey("VCAP_SERVICES")) {
			// we are running on cloud foundry, let's grab the service details from vcap_services
			JSONParser parser = new JSONParser();
			JSONObject vcap = (JSONObject) parser.parse(env.get("VCAP_SERVICES"));
			JSONObject service = null;
			
			// We don't know exactly what the service is called, but it will contain "sql"
			for (Object key : vcap.keySet()) {
				String keyStr = (String) key;
				if (keyStr.toLowerCase().contains("sql")) {
					service = (JSONObject) ((JSONArray) vcap.get(keyStr)).get(0);
					break;
				}
			}

			if (service != null) {
				JSONObject creds = (JSONObject) service.get("credentials");
				/*String name = (String) creds.get("name");
				String host = (String) creds.get("host");
				Long port = (Long) creds.get("port");*/
				String user = (String) creds.get("username");
				String password = (String) creds.get("password");
				
				String url = (String) creds.get("jdbcurl");
				System.out.println(url);
				Class.forName("com.ibm.db2.jcc.DB2Driver");
				return DriverManager.getConnection(url, user, password);
			}
		}
		
		throw new Exception("No SQL service URL found. Make sure you have bound the correct services to your app.");
	}
	
	

}