package com.sridama.bo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sridama.txngw.core.RequestResponse;
import com.sridama.utils.ODBCHelper;

public class getUsers {
	static RequestResponse req;

	public getUsers(RequestResponse req) {
		this.req = req;
	}
	
	
	
	
	public static RequestResponse fetchUsers() throws Exception{
		
		
		
		

		// TODO Auto-generated method stub

		JSONObject jcustDet = null;
		Statement st = null;

		Connection conn;

		/*final String json_from_and = req.getParam("request");
		final JSONParser parser = new JSONParser();
		final JSONObject obj = (JSONObject) parser.parse(json_from_and);

		final JSONObject jsonObject = obj;*/
		
		

		try
		{
			JSONObject result = null;
			conn = ODBCHelper.getConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT SAKUserID.`$SAKLVINSOLGID` ,SAKUserID.`$SAKLVINSOLGNAME` , SAKUserID.`$SAKLVINSOISEXECU`, SAKUserID.`$SAKLVINSOLedstatus`, SAKUserID.`$SAKLVINSOitemBalance`  FROM " + req.getParam("companydb")
					+ ".TallyUser.SAKUserID where SAKUserID.`$SAKLVINSOLGID` like '"+req.getParam("user")+"' and  SAKUserID.`$SAKLVINSOLedstatus` like 'Active'");
			int executiveuser = 0;
			while (rs.next())
			{
				if(req.getParam("user").equals(rs.getString(1))){
				executiveuser = 1;
				result = new JSONObject();
				result.put("loginname", rs.getString(2) );
				result.put("isexecutive", rs.getString(3) );
				result.put("stockvisibility", rs.getString(5) );
				System.out.println(rs.getString(4));
				   
				System.out.println(result);	
				return createResponse(result);
				}
			}
			if(executiveuser == 0){
				System.out.println("here in nin executive");
				String sql = "SELECT SAKSOLedUserCtrl.`$SAKLVINSOLGID` ,SAKSOLedUserCtrl.`$Name` , SAKSOLedUserCtrl.`$SAKLVINSOLedstatus`, SAKSOLedUserCtrl.`$SAKLVINSOitemBalance`, SAKSOLedUserCtrl.`$SAKPriceLevel` FROM " + req.getParam("companydb")
					+ ".TallyUser.SAKSOLedUserCtrl where SAKSOLedUserCtrl.`$SAKLVINSOLGID` = '"+req.getParam("user")+"' and  SAKSOLedUserCtrl.`$SAKLVINSOLedstatus` = 'Active'";
				System.out.println(sql);
				rs = st.executeQuery(sql);
				while (rs.next())
				{
				if(req.getParam("user").equals(rs.getString(1))){
				result = new JSONObject();
				result.put("loginname", rs.getString(2) );
				result.put("isexecutive", "No" );
				result.put("stockvisibility", rs.getString(4) );
				result.put("pricelevel", rs.getString(5));
				System.out.println(rs.getString(4));
				   
				System.out.println(result);	
				return createResponse(result);
				}
				}

			}			
			JSONObject res = new JSONObject();
			res.put("response_code", "0");
			res.put("response", "Invalid Login");
			System.out.println(res);
			return createResponse(res);

		}
		catch (final Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
		
		
	}
	
	public static void main(String args[]) throws Exception{
		RequestResponse req = new RequestResponse();
		req.setParam("companydb", "SridamaBusinessSolutio");
		req.setParam("user", "Acc");
		getUsers gu = new getUsers(req);
		gu.fetchUsers();
		
	}
	/**
	 * Internal helper method that wraps a given object within a response
	 * object.
	 */
	private static RequestResponse createResponse(final JSONObject o) {
		return new RequestResponse(o.toJSONString());
	}

	/**
	 * Internal helper method that wraps a given object within a response
	 * object.
	 */
	private static RequestResponse createResponse(final JSONArray o) {
		return new RequestResponse(o.toJSONString());
	}

	
}
