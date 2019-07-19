package com.sridama.bo;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.sridama.utils.ODBCHelper;

import com.sridama.txngw.core.RequestResponse;

public class getcompanynames {

	 static RequestResponse req;
	public getcompanynames(RequestResponse req) {
		this.req = req;
	}
	
	public static RequestResponse fetchcompanynames() throws ParseException{
		final String sql = "Select Company.`$Name`, Company.`$_DBName`,Company.`$_SAKMySlNo`,Company.`$_SAKPriceActive` from Company";
		System.out.println(sql);
		JSONObject companyNameSelection = null;
		Connection conn = null;

		Statement stmt = null;

		ResultSet rs = null;

		try
		{

			final JSONArray arr = new JSONArray();
			conn = ODBCHelper.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				if(rs.getString(3).equals("7566916")){
				companyNameSelection = new JSONObject();
				companyNameSelection.put("name", rs.getString(1));
				companyNameSelection.put("dbname", rs.getString(2));
				companyNameSelection.put("pricelevelactive", rs.getString(4));
				arr.add(companyNameSelection);
				}
				else{
					JSONObject obj = new JSONObject();
					obj.put("error","1");
					obj.put("message", "Invalid Tally Serial Number !!");
					return createResponse(obj);
				}
			}
			JSONObject obj = new JSONObject();
			System.out.println(arr);
			obj.put("error", "0");
			obj.put("companyNames", arr);
			return createResponse(obj);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Internal helper method that wraps a given object within a response
	 * object.
	 */
	private static  RequestResponse createResponse(final JSONObject o)
	{
		return new RequestResponse(o.toJSONString());
	}

	/**
	 * Internal helper method that wraps a given object within a response
	 * object.
	 */
	private static RequestResponse createResponse(final JSONArray o)
	{
		return new RequestResponse(o.toJSONString());
	}

	
	public static void main(String args[]) throws Exception{
		RequestResponse rs = fetchcompanynames();
		System.out.println(rs.getParam("request"));
	}
	
			
}
