package com.sridama.bo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sridama.txngw.core.RequestResponse;
import com.sridama.utils.ODBCHelper;

public class BillWisedetTotals {
	static RequestResponse req;
	public BillWisedetTotals(RequestResponse req) {
		this.req = req;
	}
	public RequestResponse fetchBillWisedetTotals() {
		String sql = "SELECT SAKStBillTotalColl.`$SAKStBillOpTotal`, SAKStBillTotalColl.`$SAKStBillPenTotal`, SAKStBillTotalColl.`$SAKStBillPDCTotal`, SAKStBillTotalColl.`$SAKStBillFinalTotal` FROM "+req.getParam("companyname")+".TallyUser.SAKStBillTotalColl SAKStBillTotalColl where SAKStBillTotalColl.`$Parent` = '"+req.getParam("partyname")+"'";
		System.out.println(sql);
		//sql = "SELECT LedUnderSalesIncGroup.`$Name`, AllTaxClassification.`$name`, LedUnderSalesIncGroup.`$TaxClassificationName` FROM Sridamaco.TallyUser.LedUnderSalesIncGroup where LedUnderSalesIncGroup.`$Name` like '%Inter%'";
		JSONArray arr = new JSONArray();
		JSONObject jledDet = null;
		Connection conn = null;

		Statement stmt = null;

		ResultSet rs = null;

		try
		{

			
			conn = ODBCHelper.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while (rs.next())
			{
				System.out.println("here");
			    jledDet = new JSONObject();
			    jledDet.put("optotal", rs.getString(1));
			    jledDet.put("pendtotal", rs.getString(2));
			    jledDet.put("pdctotal", rs.getString(3));
			    jledDet.put("finaltotal", rs.getString(4));
				arr.add(jledDet);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		 return createResponse(arr);
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
	public static void main(String argss[]){
		
	 String s = "{\"from\":\"\",\"to\":\"\",\"partyname\":\"Ashok\",\"companyname\":\"Sridamaco\"}";
	 RequestResponse r = new RequestResponse(s);
	  BillWisedetTotals gl = new BillWisedetTotals(r);
	  gl.fetchBillWisedetTotals();
	}
}
