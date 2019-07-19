package com.sridama.bo;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sridama.utils.ODBCHelper;
import com.sridama.txngw.core.RequestResponse;

public class makeSalesorderTrans {

	
	private RequestResponse req;
	
	
	
	public makeSalesorderTrans(RequestResponse req){
		this.req = req;
	}
	
	public RequestResponse makeSale() {
		// TODO Auto-generated method stub

		try
		{
			System.out.println(req.getParam("request"));
			final String json_from_and = req.getParam("request");
			final JSONParser parser = new JSONParser();
			final JSONObject o = (JSONObject) parser.parse(json_from_and);

			//TODO Rotate the array number of times to frame the xml to tally and hit the request 

			// {"success ":"0","failure":"1"}

			final String Url = "http://127.0.0.1:9000/";

			final String SOAPAction = "";

			/*
			 * request xml will be framed based on the incoming JSON
			 */
			final String saleXml = createSalesOrder(o);

			System.out.println(saleXml);

			// Create the connection where we're going to send the file.
			final URL url = new URL(Url);
			final URLConnection connection = url.openConnection();
			final HttpURLConnection httpConn = (HttpURLConnection) connection;

			final ByteArrayInputStream bin = new ByteArrayInputStream(saleXml.getBytes());
			final ByteArrayOutputStream bout = new ByteArrayOutputStream();

			// Copy the SOAP file to the open connection.

			copy(bin, bout);

			final byte[] b = bout.toByteArray();

			// Set the appropriate HTTP parameters.
			httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConn.setRequestProperty("SOAPAction", SOAPAction);
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);

			// Everything's set up; send the XML that was read in to b.
			final OutputStream out = httpConn.getOutputStream();
			out.write(b);
			out.close();

			// Read the response and write it to standard out.

			final InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
			final BufferedReader in = new BufferedReader(isr);

			String inputLine;

			final StringBuilder xmlResponse = new StringBuilder();

			while ((inputLine = in.readLine()) != null)
			{
				final StringTokenizer tokens = new StringTokenizer(inputLine);
				while (tokens.hasMoreTokens())
				{
					//	System.out.println("----->" + );
					xmlResponse.append(tokens.nextToken());
				}

			}

			System.out.println("Response from tally===>");
			System.out.println(xmlResponse);

			String data = "";
			String message = "";
			try
			{

				final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				final StringBuilder sb = new StringBuilder();

				//sb.append("<?xml version=\"1.0\"?>\n");
				//	sb.append("<RESPONSE>\n" + "<CREATED>0</CREATED>\n" + " <ALTERED>0</ALTERED>\n" + "<LASTVCHID>0</LASTVCHID>\n" + "<LASTMID>0</LASTMID>\n" + "<COMBINED>0</COMBINED>\n" + "<IGNORED>0</IGNORED>\n" + "<ERRORS>1</ERRORS>\n" + "</RESPONSE>");
				sb.append("inputLine");

				final Document doc = dBuilder.parse(new ByteArrayInputStream(xmlResponse.toString().getBytes("utf-8")));

				//optional, but recommended
				doc.getDocumentElement().normalize();

				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

				final NodeList nList = doc.getElementsByTagName("RESPONSE");

				System.out.println("----------------------------");

				for (int temp = 0; temp < nList.getLength(); temp++)
				{

					final Node nNode = nList.item(temp);

					System.out.println("\nCurrent Element :" + nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE)
					{

						JSONObject ob = new JSONObject();
						
						final Element eElement = (Element) nNode;

						System.out.println("ERROR id : " + eElement.getElementsByTagName("ERRORS").item(0).getTextContent());
						data = eElement.getElementsByTagName("ERRORS").item(0).getTextContent();
						
						if (!data.equals("0")){
							message = eElement.getElementsByTagName("LINEERROR").item(0).getTextContent();
							System.out.println("in error");
							ob.put("response_code", eElement.getElementsByTagName("ERRORS").item(0).getTextContent());
							ob.put("message", message);
							return createResponse(ob);
						}
						/*	final NodeList nList1 = doc.getElementsByTagName("firstname");
							System.out.println(nList1.getLength());
							if (nList1.getLength() != 0)
							{
								System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
							}
							System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
							System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
							System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());*/
						
					}

				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}

			finally
			{
				in.close();
			}

			final JSONObject result = new JSONObject();

			if (data.equals("0"))
			{
				result.put("response_code", "0");
				System.out.println("here success");
			}
			else
			{
				System.out.println("message------->    " + message);
				result.put("response_code", message);
			}

			return createResponse(result);

		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
   
	

	/*
	 * convert xml into bytes
	 */
	public static void copy(final InputStream in, final OutputStream out) throws IOException
	{

		// do not allow other threads to read from the
		// input or write to the output while copying is
		// taking place

		synchronized (in)
		{
			synchronized (out)
			{

				final byte[] buffer = new byte[256];
				while (true)
				{
					final int bytesRead = in.read(buffer);
					if (bytesRead == -1)
					{
						break;
					}
					out.write(buffer, 0, bytesRead);
				}
			}
		}
	}
	
	public String createSalesOrder(final Object obj) throws ParseException{


		final JSONObject jsonObject = (JSONObject) obj;
		
		JSONObject obinvoice = (JSONObject) jsonObject.get("invoice");
		JSONObject obcustomer = (JSONObject) jsonObject.get("customer");
		//JSONObject obsalesled = (JSONObject) jsonObject.get("salesled");
		JSONObject execdetails = (JSONObject) jsonObject.get("executivedetails");
		System.out.println("sssshhhhhhhhhhhh came to here"+execdetails);
        String isexecutive = "";
        String execname = "";
        		
		String custName = "";
		String salesLedger = "";
		String notes = "";
		Double tAmount = 0.0d;
		Double totalTax = 0.0d;
		String pricelevel = "";
		String company = "";
		String date = "";
		String vchtype = "Sales Order";
		String time = "";
		String orderno = getOrderno();

		isexecutive = execdetails.get("isexecutive").toString();
		execname = org.apache.commons.lang.StringEscapeUtils.escapeHtml(execdetails.get("name").toString());
		notes = org.apache.commons.lang.StringEscapeUtils.escapeHtml(obcustomer.get("notes").toString());
		if (jsonObject.get("Date") != null)
		{
			date = (String) jsonObject.get("Date");
		}else{
			String pattern = "yyyyMMdd";
			String timepattern = "HH.mm";
			//sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timepattern);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
			simpleTimeFormat.setTimeZone(TimeZone.getTimeZone("IST"));
			
			date = simpleDateFormat.format(new Date());
			System.out.println(date);
			//date = "20160401";
			time = simpleTimeFormat.format(new Date());
			System.out.println(time);
		}

		if (jsonObject.get("company") != null)
		{
			company = org.apache.commons.lang.StringEscapeUtils.escapeHtml((String) jsonObject.get("company"));
		}
		if (obcustomer.get("name") != null)
		{
			custName = org.apache.commons.lang.StringEscapeUtils.escapeHtml((String) obcustomer.get("name"));
		}
		if (obcustomer.get("pricelevel") != null){
			pricelevel = org.apache.commons.lang.StringEscapeUtils.escapeHtml((String) obcustomer.get("pricelevel"));
		}
		if (obinvoice.get("final_total") != null)
		{
			tAmount = (Double.parseDouble(obinvoice.get("final_total").toString()));
		}
		
			salesLedger = ODBCHelper.salesLedger;
		     
		
		System.out.println("FBDHB" + vchtype);
		final JSONArray itemList = (JSONArray) jsonObject.get("items");
		final Iterator<Object> iterator = itemList.iterator();

		final StringBuilder loopBuilder = new StringBuilder();
		final int flag = 0;
		while (iterator.hasNext())
		{
			final JSONParser parser = new JSONParser();
			final Object obj1 = parser.parse(iterator.next().toString());
			final JSONObject jsonObject1 = (JSONObject) obj1;
			String itemName = "";
			int quantity = 0;
			Double rate = 0.0d;
			Double MRP = 0.0d;
			String unit = "";
			if (jsonObject1.get("name") != null)
			{
				itemName = org.apache.commons.lang.StringEscapeUtils.escapeHtml(jsonObject1.get("name").toString());
			}
			if (jsonObject1.get("qty") != null)
			{
				quantity = Integer.parseInt(jsonObject1.get("qty").toString());
			}
			if (jsonObject1.get("rate") != null)
			{
				rate = Double.parseDouble(jsonObject1.get("rate").toString());
			}
			if (jsonObject1.get("rate") != null)
			{
				MRP = quantity * Double.parseDouble(jsonObject1.get("rate").toString());
			}
			if (jsonObject1.get("unit") != null)
			{
				unit = org.apache.commons.lang.StringEscapeUtils.escapeHtml(jsonObject1.get("unit").toString());
			}

			System.out.println("Units>>>>" + unit);
			System.out.println("Units>>>>" + rate);
			System.out.println("Units>>>>" + MRP);
			System.out.println("Units>>>>" + itemName);

			loopBuilder.append("<ALLINVENTORYENTRIES.LIST>");
			loopBuilder.append("<STOCKITEMNAME>" + itemName + "</STOCKITEMNAME>");
			loopBuilder.append("<ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>");
			loopBuilder.append("<ISLASTDEEMEDPOSITIVE>No</ISLASTDEEMEDPOSITIVE>");
			loopBuilder.append("<RATE>" + rate + "/" + unit + "</RATE>");
			loopBuilder.append("<AMOUNT>" + MRP + "</AMOUNT>");
			loopBuilder.append("<ACTUALQTY>" + quantity + " " + unit + "</ACTUALQTY>");
			loopBuilder.append("<BILLEDQTY>" + quantity + " " + unit + "</BILLEDQTY>");
			loopBuilder.append("<BATCHALLOCATIONS.LIST>");
			loopBuilder.append("<BATCHNAME>Primary Batch</BATCHNAME>");
			loopBuilder.append("<ORDERNO>" + orderno + "</ORDERNO>");
			loopBuilder.append("<AMOUNT>" + MRP + "</AMOUNT>");
			loopBuilder.append("<ACTUALQTY>" + quantity + " " + unit + "</ACTUALQTY>");
			loopBuilder.append("<BILLEDQTY>" + quantity + " " + unit + "</BILLEDQTY>");
			loopBuilder.append("<ORDERDUEDATE>"+date+"</ORDERDUEDATE>");
			loopBuilder.append("</BATCHALLOCATIONS.LIST>");
			loopBuilder.append("<ACCOUNTINGALLOCATIONS.LIST>");
			loopBuilder.append("<LEDGERNAME>" + salesLedger + "</LEDGERNAME>");
			loopBuilder.append("<ISDEEMEDPOSITIVE>No</ISDEEMEDPOSITIVE>");
			loopBuilder.append("<LEDGERFROMITEM>No</LEDGERFROMITEM>");
			loopBuilder.append("<REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>");
			loopBuilder.append("<ISPARTYLEDGER>No</ISPARTYLEDGER>");
			loopBuilder.append("<ISLASTDEEMEDPOSITIVE>No</ISLASTDEEMEDPOSITIVE>");
			loopBuilder.append("<AMOUNT>" + MRP + "</AMOUNT>");
			loopBuilder.append("</ACCOUNTINGALLOCATIONS.LIST>");
			loopBuilder.append("</ALLINVENTORYENTRIES.LIST>");
			
		}
		final StringBuilder sb = new StringBuilder();

		final double d = totalTax + tAmount;
		final DecimalFormat df = new DecimalFormat("0.00");
		System.out.println(df.format(d));

		sb.append("<ENVELOPE>");
		sb.append("<HEADER>");
		sb.append("<TALLYREQUEST>Import Data</TALLYREQUEST>");
		sb.append("</HEADER>");
		sb.append("<BODY>");
		sb.append("<IMPORTDATA>");
		sb.append("<REQUESTDESC>");
		sb.append("<REPORTNAME>All Masters</REPORTNAME>");
		sb.append("<STATICVARIABLES>");
		sb.append("<SVCURRENTCOMPANY>" + company + "</SVCURRENTCOMPANY>");
		sb.append("</STATICVARIABLES>");
		sb.append("</REQUESTDESC>");
		sb.append("<REQUESTDATA>");
		sb.append("<TALLYMESSAGE xmlns:UDF=\"TallyUDF\">");
		sb.append("<VOUCHER VCHTYPE=\"" + vchtype + "\" ACTION=\"Create\" OBJVIEW=\"Invoice Voucher View\">");
		sb.append("<DATE>" + date + "</DATE>");
		sb.append("<SAKSOVCHENTIME>"+time+"</SAKSOVCHENTIME>");
		/*user defined guid*/
		sb.append("<PARTYNAME>" + custName + "</PARTYNAME>");
		sb.append("<PRICELEVEL>"+ pricelevel +"</PRICELEVEL>");
		sb.append("<VOUCHERTYPENAME>" + vchtype + "</VOUCHERTYPENAME>");
		sb.append("<VOUCHERNUMBER>"+orderno+"</VOUCHERNUMBER>");
		sb.append("<SAKSOVCHEXEST>"+isexecutive+"</SAKSOVCHEXEST>");
		sb.append("<SAKSOVCHEXENAME>"+execname+"</SAKSOVCHEXENAME>");
		sb.append("<NARRATION>"+notes+"</NARRATION>");
		sb.append("<REFERENCE>" + orderno + "</REFERENCE>");
		sb.append("<PARTYLEDGERNAME>" + custName + "</PARTYLEDGERNAME>");
		sb.append("<BASICBASEPARTYNAME>" + custName + "</BASICBASEPARTYNAME>");
		sb.append("<PERSISTEDVIEW>Invoice Voucher View</PERSISTEDVIEW>");
		sb.append("<BASICBUYERNAME>" + custName + "</BASICBUYERNAME>");
		sb.append("<ISOPTIONAL>Yes</ISOPTIONAL>");
		sb.append("<EFFECTIVEDATE>" + date + "</EFFECTIVEDATE>");
		sb.append("<ISINVOICE>No</ISINVOICE>");
		sb.append("<LEDGERENTRIES.LIST>");
		sb.append("<LEDGERNAME>" + custName + "</LEDGERNAME>");
		sb.append("<ISDEEMEDPOSITIVE>Yes</ISDEEMEDPOSITIVE>");
		sb.append("<LEDGERFROMITEM>No</LEDGERFROMITEM>");
		sb.append("<REMOVEZEROENTRIES>No</REMOVEZEROENTRIES>");
		sb.append("<ISPARTYLEDGER>Yes</ISPARTYLEDGER>");
		sb.append("<ISLASTDEEMEDPOSITIVE>Yes</ISLASTDEEMEDPOSITIVE>");
		sb.append("<AMOUNT>-" +  tAmount + "</AMOUNT>");
		sb.append("</LEDGERENTRIES.LIST>");
		sb.append(loopBuilder.toString());
		/*Should start from here*/
		
		sb.append("</VOUCHER>");
		sb.append("</TALLYMESSAGE>");
		sb.append("</REQUESTDATA>");
		sb.append("</IMPORTDATA>");
		sb.append("</BODY>");
		sb.append("</ENVELOPE>");

		return sb.toString();

	
	}
	
	private String getOrderno() throws ParseException
	{
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "";
		sql = "SELECT VoucherType.`$_SAKSOOnPrefix`, VoucherType.`$_SAKSOOnVchNo`  FROM " + req.getParam("companydb") + ".TallyUser.VoucherType VoucherType WHERE (VoucherType.`$Parent` Like 'Sales Order')";
			

		try
		{
			System.out.println(sql);
			conn = ODBCHelper.getConnection();
			String voucherNo = "";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				if(rs.getString(1) == null){
					voucherNo = rs.getString(2);
				}else{
				voucherNo = rs.getString(1)+rs.getString(2);
				}
			}
			//System.out.println("vn" + voucherNo);
			
			return voucherNo;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (conn != null)
					conn.close();
			}
			catch (final SQLException e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public static void main(String args[]) throws ParseException{
		RequestResponse req = new RequestResponse();
		req.setParam("companydb", "SridamaBusinessSolutio");
		makeSalesorderTrans ms = new makeSalesorderTrans(req);
		System.out.println(ms.getOrderno());
		
		
		
		
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
	
}

