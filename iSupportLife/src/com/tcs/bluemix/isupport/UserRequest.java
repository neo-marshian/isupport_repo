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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@SuppressWarnings("serial")
@MultipartConfig
public class UserRequest extends HttpServlet {


	protected void requestBlood(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("----RequestBlood method-----");
		Connection conn = null;
		String sqlStatement = null;
		PreparedStatement pstmt = null;
		String tableName = "USER14281.USER_REQUEST";
		response.setContentType("application/json");
		response.setStatus(200);
		PrintWriter pw = response.getWriter();
		JSONObject obj = (JSONObject)request.getSession().getAttribute("user_json");
		String name = request.getParameter("fname")+" "+request.getParameter("lname");
		String date = request.getParameter("rqrd_date");
		Part file = request.getPart("uploaded_file");
		System.out.println("File size == "+file.getInputStream().available());
		int age = new Integer(request.getParameter("age"));
		String req_num = UUID.randomUUID().toString();			
		conn = getConnection();				
		sqlStatement = "INSERT INTO " + tableName + " VALUES (?,?,?,?,?,?,?,?)";
		//System.out.println(sqlStatement);
		pstmt=conn.prepareStatement(sqlStatement);			
		pstmt.setString(1, req_num);
		pstmt.setString(2, ""+obj.get("name"));
		SimpleDateFormat in = new SimpleDateFormat("dd/MM/yyyy");
		pstmt.setDate(3, new java.sql.Date(in.parse(date).getTime()));
		pstmt.setString(4, "BLOOD");
		pstmt.setString(5, request.getParameter("comments"));
		pstmt.setBinaryStream(6, file.getInputStream(), file.getInputStream().available());
		pstmt.setString(7, request.getParameter("file_name"));
		pstmt.setString(8, request.getParameter("file_type"));
		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
		obj.put("request_number", req_num);
		pw.write(obj.toString());		

		//request.getRequestDispatcher("/home.jsp").forward(request, response);
	}

	protected void showRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("----------------Inside showRequest-------------");
		Statement stmt =  null;
		String sqlStatement = null;
		String tableName = "USER14281.USER_REQUEST";
		Connection conn = null;
		PrintWriter pw = response.getWriter();
		
		response.setContentType("application/json");
		response.setStatus(200);
		conn = getConnection();	
		stmt =  conn.createStatement();			
		String req_num = request.getParameter("request_number");
		sqlStatement = "SELECT * FROM " + tableName + " WHERE REQ_NUMBER = '"+req_num+"'";
		ResultSet rs = stmt.executeQuery(sqlStatement);
		JSONObject obj =  new JSONObject();
		// Process the result set
		if (rs.next()) {
			obj.put("REQ_NUM",req_num);
			obj.put("REQESTOR", rs.getString(2));
			obj.put("REQ_DATE", rs.getDate(3));
			obj.put("REQ_DETAILS", rs.getString(5));
			obj.put("FILE_NAME", rs.getString(7));
		}
		pw.write(obj.toString());
		// Close the ResultSet
		rs.close();
		stmt.close();
		conn.close();
		
	}
	
	protected void downloadDocument(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("----------------Inside showRequest-------------");
		Statement stmt =  null;
		String sqlStatement = null;
		String tableName = "USER14281.USER_REQUEST";
		Connection conn = null;

		try {
			conn = getConnection();	
			stmt =  conn.createStatement();			
			String req_num = request.getParameter("request_number");
			sqlStatement = "SELECT FILE, FILE_TYPE, FILE_NAME FROM " + tableName + " WHERE REQ_NUMBER = '"+req_num+"'";
			ResultSet rs = stmt.executeQuery(sqlStatement);

			// Process the result set
			if (rs.next()) {
				Blob fileContent = rs.getBlob(1);
				System.out.println("fileContent length==="+fileContent.length());
				OutputStream output = response.getOutputStream();
				// Initialize response.
			    response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
			    response.setContentType(rs.getString(2)); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
			    response.setStatus(200);
			    response.setHeader("Content-disposition", "attachment; filename=\""+rs.getString(3)+"\""); // The Save As popup magic is done here. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.
			    System.out.println(rs.getString(2)+"---"+rs.getString(3));
			    byte []fileContentByte = new byte[fileContent.getBinaryStream().available()];
			    fileContent.getBinaryStream().read(fileContentByte);
			    System.out.println("fileContentByte length==="+fileContentByte.length);
			    // Write file to response.			    
			    output.write(fileContentByte);
			}
			// Close the ResultSet
			rs.close();
			stmt.close();
			conn.close();
						
		} catch (Exception e) {		
			e.printStackTrace();
		} 
	}
	
	
	protected void requestInformation(HttpServletRequest request, HttpServletResponse response)
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