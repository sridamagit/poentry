package com.sridama.bo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sridama.txngw.core.RequestResponse;
import com.sridama.utils.ODBCHelper;

public class BillWisedet {
	static RequestResponse req;
	public BillWisedet(RequestResponse req) {
		this.req = req;
	}
	public RequestResponse fetchBillWisedet() {
		String sql = "SELECT SAKStBillwiseCll.`$SAKStBillDate`, SAKStBillwiseCll.`$SAKStBillRef`, SAKStBillwiseCll.`$SAKStAdvance`, SAKStBillwiseCll.`$SAKStBillOpening`, SAKStBillwiseCll.`$SAKStBillPending`, SAKStBillwiseCll.`$SAKStBillDueon`, SAKStBillwiseCll.`$SAKStBillOverDue`,SAKStBillwiseCll.`$SAKStBillPDC`, SAKStBillwiseCll.`$SAKStBillFinal`, SAKStBillwiseCll.`$SAKStInsNo`, SAKStBillwiseCll.`$SAKStBillDis`, SAKStBillwiseCll.`$SAKStOnAccTxt`, SAKStBillwiseCll.`$SAKStBillOpOnAcc`,SAKStBillwiseCll.`$SAKStBillClOnAcc`,SAKStBillwiseCll.`$SAKStBillOnPdc`,SAKStBillwiseCll.`$SAKStBillOnFinal`, SAKStBillwiseCll.`$SAKStBillFnDate`, SAKStBillwiseCll.`$SAKBillOpTotal`, SAKStBillwiseCll.`$SAKBillPenTotal`, SAKStBillwiseCll.`$SAKBillPosTotal`, SAKStBillwiseCll.`$SAKBillFinalTotal`   FROM "+req.getParam("companyname")+".TallyUser.SAKStBillwiseCll SAKStBillwiseCll where SAKStBillwiseCll.`$SakStParent` = '"+req.getParam("partyname")+"'";
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
				jledDet.put("Ref_No", rs.getString(2));
				jledDet.put("Date", rs.getString(1));
				jledDet.put("advance", rs.getString(3));
				jledDet.put("opamt", rs.getString(4));
				jledDet.put("penamt", rs.getString(5));
				jledDet.put("dueon", rs.getString(6));
				jledDet.put("overdue", rs.getString(7));
				jledDet.put("pdc", rs.getString(8));
				jledDet.put("finalam", rs.getString(9));
				jledDet.put("instno", rs.getString(10));
				jledDet.put("dis", rs.getString(11));
				jledDet.put("onacctext", rs.getString(12));
				jledDet.put("onaccopamt", rs.getString(13));
				jledDet.put("onccclamt", rs.getString(14));
				jledDet.put("onaccpdcamt", rs.getString(15));
				jledDet.put("onaccfinalamt", rs.getString(16));
				jledDet.put("billfinaldate", rs.getString(17));
				jledDet.put("optotal", rs.getString(18));
			    jledDet.put("pendtotal", rs.getString(19));
			    jledDet.put("pdctotal", rs.getString(20));
			    jledDet.put("finaltotal", rs.getString(21));
				System.out.println("here in"+jledDet.toString());
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
	  BillWisedet gl = new BillWisedet(req);
	  gl.fetchBillWisedet();
	}
}
