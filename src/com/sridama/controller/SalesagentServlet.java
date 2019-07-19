package com.sridama.controller;

import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.parser.ParseException;

import com.sridama.utils.Utils;
import com.sridama.bo.BillWisedet;
import com.sridama.bo.BillWisedetTotals;
import com.sridama.bo.getCustomers;
import com.sridama.bo.getItems;
import com.sridama.bo.getLedgerDetails;
import com.sridama.bo.getPricelevels;
import com.sridama.bo.getSalesLedgers;
import com.sridama.bo.getUsers;
import com.sridama.bo.getcompanynames;
import com.sridama.bo.makeSalesorderTrans;
import com.sridama.txngw.core.RequestResponse;

/**
 * Servlet implementation class SalesagengServlet
 */
@WebServlet("/SalesagentServlet")
public class SalesagentServlet extends HttpServlet {
	private final static Logger LOGGER = Logger.getLogger(SalesagentServlet.class.getName());
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SalesagentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    @Override
	public void init(final ServletConfig config) throws ServletException
	{
		System.out.println("Log4JInitServlet is initializing log4j");
		final String log4jLocation = config.getInitParameter("log4j-properties-location");
		// to make linux compatable \\ for linux and / for windows
		final String delimeter = config.getInitParameter("delimeter");

		final ServletContext sc = config.getServletContext();

		if (log4jLocation == null)
		{
			System.err.println("*** No log4j-properties-location init param, so initializing log4j with BasicConfigurator");
			BasicConfigurator.configure();
		}
		else
		{
			final String webAppPath = sc.getRealPath(delimeter);
			final String log4jProp = webAppPath + log4jLocation;
			System.setProperty("app.root", webAppPath + "logs");

			System.out.println(webAppPath + "logs");

			final File logFile = new File(log4jProp);
			if (logFile.exists())
			{
				System.out.println("Initializing log4j with: " + log4jProp);
				PropertyConfigurator.configure(log4jProp);
			}
			else
			{
				System.err.println("*** " + log4jProp + " file not found, so initializing log4j with BasicConfigurator");
				BasicConfigurator.configure();
			}
		}
		super.init(config);
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final ServletContext servletContext = request.getSession().getServletContext();
		final String contextPath = servletContext.getRealPath("/");

		LOGGER.debug("context path" + contextPath);

		final RequestResponse req = new RequestResponse(request.getParameter("data"));
		req.setParam("contextPath", contextPath);
		req.setParam("queryString", request.getQueryString());

		// Extract opcode from the request.
		final int opCode = Integer.parseInt(request.getParameter("opcode"));
		RequestResponse res = null;
		switch (opCode) {
		case 1://get all company names
			System.out.println("fetching companies...");
			getcompanynames fn = new getcompanynames(req);
			try {
				res = fn.fetchcompanynames();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		case 2:
			System.out.println("in opcode 2"+request.getParameter("data")+"&"+request.getParameter("type_ahead"));
			if (request.getParameter("type_ahead") != null)
				req.setParam("type_ahead", request.getParameter("type_ahead"));
			if (request.getParameter("data") != null)
				req.setParam("data", request.getParameter("data"));
			
			getItems fi = new getItems(req);
			res = fi.fetchItems();
			break;
			
		case 3:
			System.out.println("in opcode 3"+request.getParameter("data"));
			getSalesLedgers fs = new getSalesLedgers(new RequestResponse(request.getParameter("data")));
			try {
				res = fs.fetchSalesLedgers();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case 4:
			System.out.println("in opcode 4"+request.getParameter("data"));
			if (request.getParameter("type_ahead") != null)
				req.setParam("type_ahead", request.getParameter("type_ahead"));
			if (request.getParameter("data") != null)
				req.setParam("data", request.getParameter("data"));
			getCustomers fc = new getCustomers(req);
			try {
				res = fc.fetchCustomers();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		case 5:
			System.out.println("in opcode 5"+request.getParameter("data"));
			makeSalesorderTrans ms = new makeSalesorderTrans(new RequestResponse(request.getParameter("data")));
			try{
				res = ms.makeSale();
			} catch (Exception e){
				e.printStackTrace();
			}
			break;
			
		case 6:
			System.out.println("in opcode 6"+request.getParameter("data"));
			getUsers gu = new getUsers(new RequestResponse(request.getParameter("data")));
			try{
				res = gu.fetchUsers();
			} catch (Exception e){
				e.printStackTrace();
			}
			break;
		
		case 7:
			System.out.println("in opcode 7"+request.getParameter("data"));
			getLedgerDetails gld = new getLedgerDetails(new RequestResponse(request.getParameter("data")));
			try{
				res = gld.fetchledgerdetails();
			} catch (Exception e){
				e.printStackTrace();
			}
			break;
		case 8:
			System.out.println("in opcode 8"+request.getParameter("data"));
			BillWisedet bld = new BillWisedet(new RequestResponse(request.getParameter("data")));
			try{
				res = bld.fetchBillWisedet();
			} catch (Exception e){
				e.printStackTrace();
			}
			break;		
		case 9:
			System.out.println("in opcode 9"+request.getParameter("data"));
			BillWisedetTotals bldt = new BillWisedetTotals(new RequestResponse(request.getParameter("data")));
			try{
				res = bldt.fetchBillWisedetTotals();
			} catch (Exception e){
				e.printStackTrace();
			}
			break;	
		case 10:
			System.out.println("in opcode 10"+request.getParameter("data"));
			if (request.getParameter("type_ahead") != null)
				req.setParam("type_ahead", request.getParameter("type_ahead"));
			if (request.getParameter("data") != null)
				req.setParam("data", request.getParameter("data"));
			getCustomers gfc = new getCustomers(req);
			try {
				res = gfc.fetchCustomersne();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 11: 
			//System.out.println("in opcode 11"+req.getParam("data"));
			getPricelevels gpl =  new getPricelevels(req);
			try{
				res = gpl.fetchPriceLevels();
			} catch(Exception e){
				e.printStackTrace();
			}
			break;
		default:	
			break;
		}
		LOGGER.debug("sending response json here");
		Utils.sendResponse(response, res.getParam("request"));
	}

}
