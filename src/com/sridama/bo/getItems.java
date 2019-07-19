package com.sridama.bo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sridama.txngw.core.RequestResponse;
import com.sridama.utils.ODBCHelper;

public class getItems {

	static RequestResponse req;

	public getItems(RequestResponse req) {
		this.req = req;
	}

	public RequestResponse fetchItems() {
		// TODO Auto-generated method stub

		JSONObject jitemDet = null;

		final String sql = "SELECT SAKItemPriceFinal.`$ClosingBalance`,  SAKItemPriceFinal.`$SAKItemRate`, SAKItemPriceFinal.`$StkItemName`,  SAKItemPriceFinal.`$BaseUnits` FROM "+req.getParam("companydb")+".TallyUser.SAKItemPriceFinal where SAKItemPriceFinal.`$StkItemName` like '%"+req.getParam("type_ahead")+"%' and SAKItemPriceFinal.`$SAKPriceLevel` = '"+req.getParam("pricelevel")+"'";
		System.out.println("items sql is:"+sql);	
		Statement st = null;
		ResultSet rs = null;
		Connection conn;
		
		try {
			conn = ODBCHelper.getConnection();
			st = conn.createStatement();
		
			rs = st.executeQuery(sql);
		
			final JSONArray arr = new JSONArray();
	
			while (rs.next()) {
				jitemDet = new JSONObject();
				if (rs.getString(3).equals(null)) {
	
				} else {
					jitemDet.put("name", rs.getString(3));
					jitemDet.put("value", rs.getString(3));
					jitemDet.put("qty", rs.getInt(1));
					jitemDet.put("rate", rs.getDouble(2));
					jitemDet.put("units", rs.getString(4));
					arr.add(jitemDet);
				}
			}
			/*final JSONObject result = new JSONObject();
			result.put("items", arr);*/
			System.out.println("here"+arr);
			return createResponse(arr);

		} catch (final Exception e) {
			System.out.println("here in catch");
			e.printStackTrace();
		}
		return null;
	}

	
	
	
	public static void main(String args[]){
		RequestResponse req  = new RequestResponse();
		req.setParam("companydb", "SridamaBusinessSolutio");
		req.setParam("type_ahead", "i" );
		getItems gi = new getItems(req);
		gi.fetchItems();
		
		
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
