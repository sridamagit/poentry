package com.sridama.bo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sridama.txngw.core.RequestResponse;
import com.sridama.utils.ODBCHelper;

public class getPricelevels {
	static RequestResponse req;

	public getPricelevels(RequestResponse req) {
		this.req = req;
	}
	public RequestResponse fetchPriceLevels()throws Exception {
		// TODO Auto-generated method stub
		JSONObject jcustDet = null;
		Statement st = null;

		Connection conn;
		try
		{
			final JSONArray arr = new JSONArray();
			JSONObject result = null;
			conn = ODBCHelper.getConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT SAKAACrpPriveLevel.`$SAKPriceLevel` FROM "+req.getParam("companydb")+".TallyUser.SAKAACrpPriveLevel");
			while(rs.next()){
				result = new JSONObject();
				result.put("name", rs.getString(1));
				arr.add(result);
			}
			final JSONObject obj1 = new JSONObject();
			obj1.put("salesleds", arr);
			System.out.println(obj1.toJSONString());
			return createResponse(obj1);
		} catch (Exception e){
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
