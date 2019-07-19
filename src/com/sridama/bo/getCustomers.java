package com.sridama.bo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sridama.utils.*;
import com.sridama.txngw.core.RequestResponse;

public class getCustomers {

	static RequestResponse req;

	public getCustomers(RequestResponse req) {
		this.req = req;
	}
	
	
	
	
	public static RequestResponse fetchCustomers() throws Exception{
		
		
		
		

		// TODO Auto-generated method stub

		JSONObject jcustDet = null;
		Statement st = null;

		Connection conn;

		
		
		

		try
		{
			conn = ODBCHelper.getConnection();
			st = conn.createStatement();
			System.out.println("here"+req.getParam("type_ahead"));
			String sql = "SELECT SAKVINSOOnlnLedName.`$SAKOnLedName`,SAKVINSOOnlnLedName.$SAKPriceLevel  FROM  " + req.getParam("companydb")
					+ ".TallyUser.SAKVINSOOnlnLedName  WHERE  (SAKVINSOOnlnLedName.`$SAKOnLedName` like '%"+req.getParam("type_ahead")+"%')";
			System.out.println(sql);
			final ResultSet rs = st.executeQuery(sql);
			
			final JSONArray arr = new JSONArray();
			while (rs.next())
			{
				jcustDet = new JSONObject();
				if (rs.getString(1).equals(null))
				{

				}
				else
				{
					try
					{
						jcustDet.put("address", "");
					}
					catch (final Exception e)
					{
						jcustDet.put("address", "");
					}
					try
					{
						jcustDet.put("name", rs.getString(1));
						jcustDet.put("value", rs.getString(1));
						jcustDet.put("pricelevel", rs.getString(2));
					}
					catch (final Exception e)
					{
						jcustDet.put("name", "");
						jcustDet.put("value", "");
					}
					try
					{
						jcustDet.put("pincode", "");
					}
					catch (final Exception e)
					{
						jcustDet.put("pincode", "");
					}
					try
					{
						jcustDet.put("state", "");
					}
					catch (final Exception e)
					{
						jcustDet.put("state", "");
					}
					try
					{
						jcustDet.put("tin", "");
					}
					catch (final Exception e)
					{
						jcustDet.put("tin", "0");

					}
					try
					{
						jcustDet.put("Closing_balance", "");
					}
					catch (final Exception e)
					{
						jcustDet.put("Closing_balance", "0");

					}

					arr.add(jcustDet);
				}

			}
			final JSONObject result = new JSONObject();
			//result.put("tallyDataMain", arr);
			 System.out.println("{\"tallyDataMain\":" + arr.toString() + "}");
			return createResponse(arr);

		}
		catch (final Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
		
		
	}
public static RequestResponse fetchCustomersne() throws Exception{
		
		
		
		

		// TODO Auto-generated method stub

		JSONObject jcustDet = null;
		Statement st = null;

		Connection conn;

		
		
		

		try
		{
			conn = ODBCHelper.getConnection();
			st = conn.createStatement();
			System.out.println("here"+req.getParam("type_ahead"));
			String sql = "SELECT SAKVINSOOnlnLedName.`$SAKOnLedName`  FROM  " + req.getParam("companydb")
					+ ".TallyUser.SAKVINSOOnlnLedName  WHERE  (SAKVINSOOnlnLedName.`$SAKOnLedName` like '%"+req.getParam("type_ahead")+"%' and SAKVINSOOnlnLedName.`$SAKExeLoginCtrl` != 'Yes')";
			System.out.println(sql);
			final ResultSet rs = st.executeQuery(sql);
			
			final JSONArray arr = new JSONArray();
			while (rs.next())
			{
				jcustDet = new JSONObject();
				if (rs.getString(1).equals(null))
				{

				}
				else
				{
					try
					{
						jcustDet.put("address", "");
					}
					catch (final Exception e)
					{
						jcustDet.put("address", "");
					}
					try
					{
						jcustDet.put("name", rs.getString(1));
						jcustDet.put("value", rs.getString(1));
					}
					catch (final Exception e)
					{
						jcustDet.put("name", "");
						jcustDet.put("value", "");
					}
					try
					{
						jcustDet.put("pincode", "");
					}
					catch (final Exception e)
					{
						jcustDet.put("pincode", "");
					}
					try
					{
						jcustDet.put("state", "");
					}
					catch (final Exception e)
					{
						jcustDet.put("state", "");
					}
					try
					{
						jcustDet.put("tin", "");
					}
					catch (final Exception e)
					{
						jcustDet.put("tin", "0");

					}
					try
					{
						jcustDet.put("Closing_balance", "");
					}
					catch (final Exception e)
					{
						jcustDet.put("Closing_balance", "0");

					}

					arr.add(jcustDet);
				}

			}
			final JSONObject result = new JSONObject();
			//result.put("tallyDataMain", arr);
			 System.out.println("{\"tallyDataMain\":" + arr.toString() + "}");
			return createResponse(arr);

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
		req.setParam("type_ahead", "acc");
		getCustomers gc = new getCustomers(req);
		gc.fetchCustomers();
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
