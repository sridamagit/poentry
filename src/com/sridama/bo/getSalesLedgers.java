package com.sridama.bo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sridama.txngw.core.RequestResponse;
import com.sridama.utils.ODBCHelper;

public class getSalesLedgers {

	
	static RequestResponse req;

	public getSalesLedgers(RequestResponse req) {
		this.req = req;
	}
	
	public static RequestResponse fetchSalesLedgers() throws ParseException{
		// TODO Auto-generated method stub

		final JSONObject jsalesLed = null;

		JSONObject jtaxLed = null;
		Connection conn = null;

		Statement stmt = null;

		ResultSet rs = null;

		String sql = "";

		//final String json_from_and = req.getParam("request");
		final JSONParser parser = new JSONParser();
		//final JSONObject obj = (JSONObject) parser.parse(json_from_and);

		//final JSONObject jsonObject = obj;
		String company = "";
		String txnType = "";
		

		
			sql = "SELECT LedUnderSalesIncGroup.`$Name`, AllTaxClassification.`$name`, LedUnderSalesIncGroup.`$TaxClassificationName` FROM "+req.getParam("companydb")+".TallyUser.LedUnderSalesIncGroup";
			System.out.println(sql);
		
		

		try
		{
			final JSONArray arr = new JSONArray();
			conn = ODBCHelper.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				jtaxLed = new JSONObject();
				if (rs.getString(1).equals(null) || rs.getString(3).equals("Not Applicable"))
				{
					
				}
				else
				{
					//System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3));
					jtaxLed.put("name", rs.getString(1));
					jtaxLed.put("value", rs.getDouble(2));
					jtaxLed.put("taxClassificationName", rs.getString(3));
					arr.add(jtaxLed);
				}
				
			}
			final JSONObject obj1 = new JSONObject();
			obj1.put("salesleds", arr);
			System.out.println(obj1.toJSONString());
			return createResponse(obj1);
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
