package com.sridama.bo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sridama.txngw.core.RequestResponse;
import com.sridama.utils.ODBCHelper;

public class getLedgerDetails {
	static RequestResponse req;
	public getLedgerDetails(RequestResponse req) {
		this.req = req;
	}
	public static RequestResponse fetchledgerdetails() {
		String to = req.getParam("to").toString();
		String from = req.getParam("from").toString();
		String 	sql = "SELECT SAKSOLedAccMainCll.`$SAKStAccPName`, SAKSOLedAccMainCll.`$SAKStAccDate`, SAKSOLedAccMainCll.`$SAKStAccFromDt`, SAKSOLedAccMainCll.`$SAKStAccToDt`, SAKSOLedAccMainCll.`$SAKStOpeningDr`,SAKSOLedAccMainCll.`$SAKStOpeningCr`, SAKSOLedAccMainCll.`$SAKStAccVchType`, SAKSOLedAccMainCll.`$SAKStAccVchNum`, SAKSOLedAccMainCll.`$SAKStAccLedDrVchAmount`, SAKSOLedAccMainCll.`$SAKStAccLedCrVchAmount`, SAKSOLedAccMainCll.`$SAKStAccVchSLedName`, SAKSOLedAccMainCll.`$SAKStAccTotalDrAmt`, SAKSOLedAccMainCll.`$SAKStAccTotalCrAmt`, SAKSOLedAccMainCll.`$SAKStAccFirstContraAccount`, SAKSOLedAccMainCll.`$SAKStAccTotalLedVchAmt`, SAKSOLedAccMainCll.`$SAKStDrCrs` FROM "+req.getParam("companyname")+".TallyUser.SAKSOLedAccMainCll SAKSOLedAccMainCll WHERE (SAKSOLedAccMainCll.`$SAKStAccPName` = '"+req.getParam("partyname")+"') and SAKSOLedAccMainCll.`$SAKStAccFromDt` <= '"+to+"' and SAKSOLedAccMainCll.`$SAKStAccFromDt` >= '"+from+"'" ;
		;
		
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
				
				jledDet = new JSONObject();
				jledDet.put("openingdr", rs.getString(5));
				jledDet.put("openingcr", rs.getString(6));
				jledDet.put("Date", rs.getString(2));
				jledDet.put("Particulatrs", rs.getString(11));
				jledDet.put("VchType", rs.getString(7));
				jledDet.put("VchNo", rs.getString(8));
				jledDet.put("dbamt", rs.getString(9));
				jledDet.put("cramt", rs.getString(10));
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
		
		getLedgerDetails gl = new getLedgerDetails(r);
	  gl.fetchledgerdetails();
	}
}
