/**
 * Routines used in new-sale.html file are available here...
 */

/**
 * Gets the next available invoice number for creating a new sale txn. Adds
 * /displays this number in the given element.
 * 
 * @param {Object}
 *            element
 */
function todayd(){
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
	
	var yyyy = today.getFullYear();
	if(dd <10){
		dd = "0"+dd;
	}if(mm < 10){
		mm = "0"+mm;
	}
	
	debugger;
	today = dd+'-'+mm+'-'+yyyy;
	var today1  = new Date(today);
	return today;
}
function todaysdm(){
	var today = new Date();
	var dd = "01";
	var mm = today.getMonth()+1; //January is 0!
	var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
	/*if(dd <10){
		dd = "0"+dd;
	}*/if(mm < 10){
		mm = "0"+mm;
	}
	var yyyy = today.getFullYear();
	
	
	debugger;
	today = dd+'-'+mm+'-'+yyyy;
	var today1  = new Date(today);
	return today;
}
function scrollToAnchor(aid){
    var aTag = $("a[name='"+ aid +"']");
    $('html,body').animate({scrollTop: aTag.offset().top},'slow');
}
function getNextInvoiceNum(element, type) {
	var opcode = '';
	if (type == 'sale') {
		opcode = 4;
	} else {
		opcode = 5;
	}
	$.ajax({
		url : "/eazytrack/EzTrackServlet?opcode=" + opcode + "",
		// url : "responses/fetch-next-invoice.json",
		dataType : "json",
		success : function(data) {
			element.val(data[0].invoicenum); // invoicenum
		}
	});
}

/**
 * Transforms given data to a format (called datum) recognized by typeahead.
 * 
 * @param {Object}
 *            data
 */
function transformCustomers2Datum(data) {
	for (var i = 0; i < data.length; i++) {
		data[i].value = data[i].name;
		data[i].tokens = [ data[i].name, data[i].phone1 ];
	}
	// console.log("transformed data: " + JSON.stringify(data));
	return data;
}

/**
 * Converts items list data to a format recognized by typeahead.
 * 
 * @param {Object}
 *            data
 */
function transformItems2Datum(data) {
	for (var i = 0; i < data.length; i++) {
		data[i].value = data[i].name;
		data[i].tokens = [ data[i].name, data[i].category, data[i].code ];
	}
	console.log("transformed data: " + JSON.stringify(data));
	return data;
}

/**
 * Loads a row with item details.
 * 
 * @param {Object}
 *            datum
 * @param {Object}
 *            row
 */
function populateRow(row, datum) {
	// row.find(".desc span span:first").text(datum.name);
	//row.find(".desc input.desc").typeahead('setQuery', datum.name);
	//row.find(".qty input").val(1);
	console.log(datum.rate);
	row.find(".rate").text(datum.rate);
	if($.cookie("discactive") == "No"){
		row.find(".disc").text("");
	}else{
		if(datum.discount == 0){
			row.find(".disc").text("");
		}else{
			row.find(".disc").text(datum.discount+"%");
		}
		
	}
	if(datum.tax == 0){
		row.find(".tax").text("");
		
	}else{
		row.find(".tax").text(datum.tax+"%");
	}
	
	// row.find(".disc-prcnt").text(datum.discnt_percent);
	//row.find(".disc-prcnt input").val(datum.discnt_percent);

	/*if ($("#cform").is(":checked")) {
		row.find(".vat").text("2");
	} else {
		row.find(".vat").text(datum.vat);
	}*/
	row.data("item-info", datum);
	//updateRow(row);
}

/**
 * Populates the row for given values of invoice
 * 
 * @param {Object}
 *            datum
 * @param {Object}
 *            row
 */
function populateRowEdit(row, datum) {
	// row.find(".desc span span:first").text(datum.name);
	var slno = row.prev().length ? parseInt(row.prev().find("td.sl-no").text()) + 1
			: 1;
	slno = datum.sl_no;
	row.find("td.sl-no").text(slno);
	//row.find(".desc input.desc").typeahead('setQuery', datum.name);
	row.find(".qty input").val(datum.qty);
	row.find(".rate").text(datum.rate);
	// row.find(".disc-prcnt").text(datum.discnt_percent);
	//row.find(".disc-prcnt input").val(datum.discnt_percent);

	/*if ($("#cform").is(":checked")) {
		row.find(".vat").text("2");
	} else {
		row.find(".vat").text(datum.vat);
	}*/
	row.data("item-info", datum);
	updateRow(row);
}

/**
 * computes calculated fields viz., discount value, amount
 * 
 * @param {Object}
 *            row
 * @param {object}
 *            recalc - boolean indicating whether to recalculate totals.
 */
function updateSlno() {
	debugger;
	var rows = $("table#item-details tbody tr");
	for ( var i = 0; i < rows.length; i++) {
		$(rows[i]).find("td.sl-no").text(i + 1);
	}
}

function updateTotals() {

	debugger;
	var rows = $("table#item-details tbody tr");
	var sum = 0;
	var disc_sum = 0;
	var tax_sum = 0;
	var qty_sum = 0;
	var tot_mrp = 0;
	var tot_tax = 0;
	for ( var i = 0; i < rows.length; i++) {
		tot_mrp += parseFloat($(rows[i]).find("td.qty input").val())
				* parseFloat($(rows[i]).find(".rate").text());
		var ad_disc = 0;
		adisc = isNaN(ad_disc) ? 0 : ad_disc;
		//$(rows[i]).find(".addtnl-disc input").val(Math.round(adisc.toFixed(2)));

		var rowAmount = parseFloat($(rows[i]).find("td.amount").text());
		sum += isNaN(rowAmount) ? 0 : rowAmount;
		if($(rows[i]).find("td.tax").text() == ""){
			var rowTax = 0;
			tot_tax += 0;
		}else{
			var rowTax = parseFloat($(rows[i]).find("td.tax").text());
			tot_tax += rowAmount*rowTax/100;
		}
		
		if($.cookie("discactive") == "Yes"){
			var rowDiscount = parseFloat($(rows[i]).find("td.disc").text());
			disc_sum += 0;
		}else{
			var rowDiscount = 0;
			disc_sum += 0;
		}
		

		var tax_val =0;
		tax_val = isNaN(tax_val) ? 0 : tax_val;
		tax_sum += tax_val;

		var qty_val = parseInt($(rows[i]).find("td.qty input").val());
		qty_val = isNaN(qty_val) ? 0 : qty_val;
		qty_sum += qty_val;

	}

	$("#tot_mrp").text(tot_mrp.toFixed(2));
	$("#tot_tax").text(tot_tax.toFixed(2));
	debugger;
	
	$("#qty-total").text(qty_sum);

	
	var amtWithTaxes = (sum + tax_sum);
	$("#amt-with-taxes").text(amtWithTaxes.toFixed(2));

	var finalAmt = amtWithTaxes ;
	$("#tot_mrp").text(finalAmt.toFixed(2));
	console.log("here"+finalAmt.toFixed(2)+"&&"+qty_sum);
}

function updateRow(row, recalc) {
	
	var quantity = parseInt(row.find(".qty input").val());
	var rate = (parseFloat(row.find(".rate").text())).toFixed(2);
	var discountPercent = 0;
	var discount = 0;
	if($.cookie("discactive") == "No"){
		discount = 0;
	}else{
		if(row.find(".disc").text() == ""){
			discount = 0;
		}else{
		discount = (parseFloat(row.find(".disc").text())).toFixed(2);
		}
	}
	var vatPercent = 0;
	//vatPercent = isNaN(vatPercent) ? 0 : vatPercent;
	var mrp = quantity * rate;
	// var amt = (baseAmt - (discount) + (baseAmt * vatPercent /
	// 100)).toFixed(2);

	var amtBeforeTax = (mrp - (mrp*discount/100));

	row.find(".rate").text(rate);
	row.find(".disc-val").text(discount);
	row.find(".amount").text(amtBeforeTax.toFixed(2));

	if (recalc == undefined || recalc == true) {
		updateTotals();
	}

}



/**
 * Removes all content of a given item row.
 * 
 * @param {Object}
 *            row
 */
function clearRow(row) {
	var slno = row.prev().length ? parseInt(row.prev().find("td.sl-no").text()) + 1
			: 0;
	row.find("td.sl-no").text(slno);
	row.find("td.desc input").val("");
	row.find("td.disc").text("");
	row.find("td.tax").text("");
	row.find("td.qty input").val("");
	row.find("td.rate").text("");
	row.find("td.disc-prcnt input").val("");
	row.find("td.disc-val").text("");
	row.find("td.vat").text("");
	row.find("td.amount").text("");
}

/**
 * Used for binding typeahead functionality for new rows created on the fly.
 * 
 * @param {Object}
 *            element
 */

function bindCustomerTypeahead(element){
	
	element.parent().find("input.tt-hint").remove();
	var val = element.val();
	var url = '/poentry/SalesagentServlet?opcode=4&type_ahead=%QUERY&data={"companydb": "'+$.cookie('companydb')+'"}';
	//var url = 'items.json';

		element
		.typeahead({
			name : "customerList",
			remote : url,
			template : ["<p class='bottom-border category-hint' style='min-width: 230px;'>{{name}}</p>"].join(""),
			header : "<div class='bottom-border hint-header' style='text-align:center'><h5>Party Name </span></h5></div>",
			engine : Hogan,
			limit: 10000000,
			
		});
	$(element).bind('typeahead:selected', function(obj, datum, name) {
		console.log("Item selected."+datum.name);
		debugger;
		$(element).val(datum.value)
		$("#price-level").val(datum.pricelevel);
		$("#price-level").show();
		$("#reload").hide();

		$("#price-level-combo").hide();
		if($("#price-level").val() == "" || $("#price-level").val() == null){
			//fetch combo
			$("#price-level").hide();
			$("#price-level-combo").show();
			var json = new Object();
				json.companydb = $.cookie("companydb");
			$.ajax({
				url : "/poentry/SalesagentServlet?opcode=11",
				//url: "http://127.0.0.1:8090/eazytrack/EzTrackServlet?opcode=0",
				type : "POST",
				dataType : "json",
				timeout : 30000,
				data : { data: JSON.stringify(json) },// half-a-minute for login
				success : function(data) {
					var html = "<option>Select Price Level</option>";
					console.log(data.salesleds);
					for(var i=0; i< data.salesleds.length; i++){
						//console.log(data.salesleds[i]);
						html += "<option>"+data.salesleds[i]["name"]+"</option>";
					}
					console.log(html);
					
					$("#price-level-combo").html(html);
					

				},
				error : function() {

				}
			});
		
		}else{
			bindTypeAhead($("#item-name"));
		}
	});

	
	
	
}
function bindLedgerTypeahead(element){
	
	element.parent().find("input.tt-hint").remove();
	var val = element.val();
	var url = '/poentry/SalesagentServlet?opcode=10&type_ahead=%QUERY&data={"companydb": "'+$.cookie('companydb')+'"}';
	//var url = 'items.json';

		element
		.typeahead({
			name : "ledgerList",
			remote : url,
			template : ["<p class='bottom-border category-hint' style='min-width: 230px;'>{{name}}</p>"].join(""),
			header : "<div class='bottom-border hint-header' style='text-align:center'><h5>Party Name </span></h5></div>",
			engine : Hogan,
			limit: 10000000,
			
		});
	$(element).bind('typeahead:selected', function(obj, datum, name) {
		console.log("Item selected."+datum.name);
		debugger;
		$(element).val(datum.value)
		
	});

	
	
	
}
function getbillwisedetails(){
	$("#loader").show();
	var json = {};
	json.partyname = $("#ledger-name").val();
	json.companyname = $.cookie("companyname");
	$.ajax({
		url : "/poentry/SalesagentServlet?opcode=8",
		//url: "http://127.0.0.1:8090/eazytrack/EzTrackServlet?opcode=0",
		type : "POST",
		dataType : "json",
		timeout : 30000, // half-a-minute for login
		data : {
			data : JSON.stringify(json)
		},
		success : function(data) {
			console.log(data);
			var html = "";
			for(var i=0; i< data.length; i++){
				$("#item-details").show();
				if(data[0].optotal != null){
					if(data[0].optotal > 0){
						$("#optotal").text(data[0].optotal);
					}else{
						$("#optotal").text(data[0].optotal);
					}
				}
				
				$("#pentotal").text(data[0].pendtotal)
					
				}if(data[0].pdctotal != null){
					if(data[0].pdctotal > 0){
						$("#pdctotal").text(data[0].pdctotal)
					}else{
						$("#pdctotal").text(data[0].pdctotal)	
					}
				}if(data[0].finaltotal){
					if(data[0].finaltotal > 0){
						$("#finaltotal").text(data[0].finaltotal)
					}else{
						$("#finaltotal").text(data[0].finaltotal)
					}
				}
				
			

				html += "<tr>";
				html += "<td class='delete'>"+data[i].Date+"</td>";
			    //html += "<td class=\"sl-no darkblue\">"+data[i].Ref_No+"</td>";
				if(data[i].advance != null){
					html += "<td class=\"sl-no\"><span style=\"width:80%;float:left\">"+data[i].Ref_No+"</span><span style=\"width:20%;float:left\" >"+data[i].advance+"</span></td>";
				}else{
					html += "<td class=\"sl-no\"><span style=\"width:80%;float:left\">"+data[i].Ref_No+"</span></td>";
				}
				if(data[i].instno !=null){
					html += "<td class=\"sl-no\">"+data[i].instno+"</td>";
				}else{
					html += "<td class=\"sl-no\"></td>";
				}if(data[i].dis != null){
					html += "<td class=\"sl-no right-align\">"+data[i].dis+"</td>";
				}else{
					html += "<td class=\"sl-no\"></td>";
				}
				
				
				if(data[i].opamt > 0){
				html += "<td class=\"desc right-align \">"+data[i].opamt+" </td>";
				}else{
					html += "<td class=\"qty right-align\">"+data[i].opamt+"</td>"
				}
				if(data[i].penamt > 0){//positive
					html += "<td class=\"qty right-align \">"+data[i].penamt+"</td>";
				}else{//negative
					html += "<td class=\"qty right-align \">"+data[i].penamt+"</td>";
				}
				if(data[i].pdc != 0){
					if(data[i].pdc >0){
						html += "<td class=\"qty right-align \">"+data[i].pdc+"</td>";
					}
					else{
						html += "<td class=\"qty right-align \">"+data[i].pdc*+"</td>";	
					}
				}else{
					html += "<td class=\"sl-no \"></td>";
				}
				if(data[i].finalam != null){
					if(data[i].finalam >0 ){
						html += "<td class=\"qty right-align \">"+data[i].finalam+"</td>";
					}else{
						html += "<td class=\"qty right-align \">"+data[i].finalam+"</td>";
					}
				}else{
					html += "<td class=\"sl-no \"></td>";
				}
				
				html += "<td class=\"qty right-align\">"+data[i].dueon+"</td>";
				html += "<td class=\"qty right-align\">"+data[i].overdue+"</td>";
				html += "</tr>";
				
			}
			if(data[0].onacctext!=""){
				html += "<tr>";
				html += "<td class=\"sl-no\">"+todayd()+"</td>";
				html += "<td class=\"sl-no\"><span style=\"width:75%;float:right\"></span><span style=\"width:25%;float:right\">"+data[0].onacctext+"</span></td>";
				html += "<td></td>";
				html += "<td></td>";
				html += "<td class=\"desc right-align \">"+data[0].onaccopamt+" </td>";
				html += "<td class=\"qty right-align \">"+data[0].onacclamt+"</td>";
				html += "<td class=\"qty right-align \">"+data[0].onaccpdcamt+"</td>";
				html += "<td class=\"qty right-align \">"+data[0].onaccfinalamt+"</td>";
				$("#billdate").html(data[0].billfinaldate)
					html += "<td class=\"qty right-align \"></td>";
					html += "<td class=\"qty right-align \"></td>";
					
				html += "</tr>";
					
			}
			
			$("#item-details tbody").html(html);
			$("#billdate").show();
			$("#loader").hide();
		},
		error : function() {

		}
		///totallss---------------
		
	});
}
function bindLedgerTypeahead1(element){
	
	element.parent().find("input.tt-hint").remove();
	var val = element.val();
	var url = '/poentry/SalesagentServlet?opcode=10&type_ahead=%QUERY&data={"companydb": "'+$.cookie('companydb')+'"}';
	//var url = 'items.json';

		element
		.typeahead({
			name : "ledgerList",
			remote : url,
			template : ["<p class='bottom-border category-hint' style='min-width: 230px;'>{{name}}</p>"].join(""),
			header : "<div class='bottom-border hint-header' style='text-align:center'><h5>Party Name </span></h5></div>",
			engine : Hogan,
			limit: 10000000,
			
		});
	$(element).bind('typeahead:selected', function(obj, datum, name) {
		console.log("Item selected."+datum.name);
		debugger;
		$(element).val(datum.value)
		getbillwisedetails();

	});

	
	
	
}
function postPricelevel(pricelevel){
	var json = new Object();
	json.username = $.cookie('loginid');
	json.companyname = $.cookie('companydb');
	json.pricelevel = pricelevel;
	$.ajax({
		url : "/poentry/SalesagentServlet?opcode=12",
		type : "POST",
		dataType : "json",
		timeout : 20000,
		data : {
			data : JSON.stringify(json)
		},
		success : function(data) {
		
		}
	});
}
function bindTypeAhead(element) {
	element.parent().find("input.tt-hint").remove();
	var pricelevel = $("#price-level").val();
	if($("#price-level").val() == "" || $("#price-level").val() == null){
		pricelevel = $("#price-level-combo option:selected").text()
	}
	var val = element.val();
	console.log(pricelevel);
	postPricelevel(pricelevel);
	var url = '/poentry/SalesagentServlet?opcode=2&type_ahead=%QUERY&data={"companydb": "'+$.cookie('companydb')+'", "pricelevel":"'+pricelevel+'"}';
	//var url = 'items.json';
	if($.cookie('stockvisibility')  == 'No'){
		element
		.typeahead({
			name : "itemsList",
			remote : url,
			template : ["<p class='bottom-border category-hint' style='min-width: 230px;'>{{name}}</p>"].join(""),
			header : "<div class='bottom-border hint-header' style='text-align:center'><h5>Item Name </span></h5></div>",
			engine : Hogan,
			limit: 10000000,
		});
	}else{
	element
			.typeahead({
				name : "itemsList",
				remote : url,
				template : ["<p class='bottom-border category-hint' style='min-width: 230px;'>{{name}}  <span class='pull-right'>  &nbsp;({{qty}}) </span></p>"].join(""),
				header : "<div class='bottom-border hint-header' style='text-align:center'><h5>Item Name </span></h5></div>",
				engine : Hogan,
				limit: 10000000,
			});
	}
	$(element).bind('typeahead:selected', function(obj, datum, name) {
		console.log("Item selected.");
		populateRow($(this).parents("tr"), datum);
		$(this).parents("tr").data("data-filled", true);
		var qtyField = $(this).parents("tr").find("td.qty input");
		qtyField.attr('ext', "true");
		$(this).parents("tr").find("td.qty input").focus().select();

	});

}

/**
 * Prepares a JSON object consisting of all user entered values for creating a
 * new sale txn.
 */
function prepareSaleJson(action) {
	var result = new Object();

	/** Overall invoice data * */
	result.invoice = new Object();
	//result.invoice.invoice_no = $("#invoice-no").val();
	//result.invoice.invoice_date = convert2outputDt($("#date").text());
	//result.invoice.inter_state = $("#interstate").is(":checked") ? "y" : "n";
	//result.invoice.cform = $("#cform").is(":checked") ? "y" : "n";
	//result.invoice.sub_total = $("#base-total").text();
	//result.invoice.additional = $("#addnl-charges input").val();
	//result.invoice.round_off_disc = $("#roundoff input").val();
	result.invoice.final_total = $("#tot_mrp").text();
	//result.invoice.payment_mode = $("#payment-type").val();

	// specifies the action on the invoice , save , update or reject the invocie
	result.invoice.action = action;
    /** customer data * */
	result.executivedetails = new Object();
	result.customer = new Object();
	result.salesled = new Object();
	result.company =  $.cookie("companydb");
	result.executivedetails.isexecutive = $.cookie("isexecutive");
	result.executivedetails.name = $.cookie("loginname");
	result.customer.name = $("#cust-name").val();
	result.customer.notes = $("#notes").val();
	var pricelevel = $("#price-level").val();
	if($("#price-level").val() == "" || $("#price-level").val() == null){
		pricelevel = $("#price-level-combo option:selected").text()
	}
	result.customer.pricelevel = pricelevel;
	/** items listing * */
	result.items = [];
	$("#item-details tbody tr").each(function() {

		// skipping empty rows
		if ($(this).data("item-info")) {

			var item = new Object();

			item.item_id = $(this).data("item-info").id;
			item.name = $(this).data("item-info").value;
			item.qty = $(this).find("td.qty input").val();
			item.rate = $(this).data("item-info").rate;
			item.disc_percent = parseFloat($(this).find("td.disc").text());
			item.amount = $(this).find("td.amount").text();
			item.dsc_p = $(this).data("item-info").discnt_percent;
			item.vat_p = $(this).data("item-info").vat;
			item.category = $(this).data("item-info").catid;
			item.unit = $(this).data("item-info").units;
			result.items.push(item);
		}
	});
	console.log("Save Button "+JSON.stringify(result));
	return result;
}

/**
 * Prepares a JSON object consisting of all user entered values for creating a
 * new purchase txn.
 */
function preparePurchaseJson(action) {
	var result = new Object();

	/** Overall invoice data * */
	result.invoice = new Object();
	result.invoice.invoice_no = $("#invoice-no1").val();
	result.invoice.pur_inv_no = $("#pur-invoice-no").val();
	result.invoice.invoice_date = convert2outputDt($("#date").text());
	result.invoice.inter_state = $("#interstate").is(":checked") ? "y" : "n";
	result.invoice.cform = $("#cform").is(":checked") ? "y" : "n";
	result.invoice.sub_total = $("#base-total").text();
	result.invoice.additional = $("#addnl-charges input").val();
	result.invoice.round_off_disc = $("#roundoff input").val();
	result.invoice.final_total = $("#grand-total").text();
	result.invoice.payment_mode = $("#payment-type").val();

	result.invoice.pur_inv_date = convert2outputDt1($("#pur-invoice-date")
			.val());
	result.invoice.goods_received_date = convert2outputDt1($("#receipt-date")
			.val());

	// specifies the action on the invoice , save , update or reject the invocie
	result.invoice.action = action;

	/** customer data * */
	result.customer = new Object();
	// Get the customer id, if a seletion was made from typeahead suggestions.
	if ($("#cust-name").data("customer") != undefined) {
		var customer_info = $("#cust-name").data("customer");
		result.customer.cust_id = customer_info.id;
		result.customer.name = customer_info.name;
		result.customer.phone = customer_info.phone1;
	} else {
		result.customer.cust_id = -1;
		result.customer.name = $("#cust-name").val();
		result.customer.phone = $("#cust-phone").val();
	}

	/** items listing * */
	result.items = [];
	$("#item-details tbody tr").each(function() {
		var item = new Object();

		item.item_id = $(this).data("item-info").id;
		item.name = $(this).data("item-info").name;
		item.qty = $(this).find("td.qty input").val();
		item.rate = $(this).data("item-info").rate;
		item.discount = $(this).find("td.disc-val").text();
		item.amount = $(this).find("td.amount").text();
		item.dsc_p = $(this).data("item-info").discnt_percent;
		item.vat_p = $(this).data("item-info").vat;
		item.category = $(this).data("item-info").catid;
		result.items.push(item);
	});

	return result;
}

function convert2outputDt(dt) {
	var date = dt.split(":")[1];
	date = date.split("/");
	return date[2].trim() + "-" + date[1].trim() + "-" + date[0].trim();
}

function convert2outputDt1(dt) {
	var date = dt.split("/");
	return date[2].trim() + "-" + date[1].trim() + "-" + date[0].trim();
}

/**
 * Submits the sale txn data to backend url through an ajax call.
 * 
 * @param {Object}
 *            data
 */
function submitSaleData(data) {

	$("#save-new-sale").text("Saving...");
	$("#save-new-sale").prop("disabled", true);
	$
			.ajax({
				url : "/poentry/SalesagentServlet?opcode=5",
				type : "POST",
				dataType : "json",
				timeout : 20000,
				data : {
					data : JSON.stringify(data)
				},
				success : function(data) {
					if(data.response_code == "0"){
					var result = alert("Purchase Order Successfully Updated.");
					location.reload();
					}else if(data.response_code == "1"){
						console.log("save status: " + status);
						alert("Status: "+data.message);
						$("#save-new-sale").prop("disabled", false);
					}
					
				},
				error : function(xhr, status, text) {
					console.log("save status: " + status);
					alert("Please Select valid stock item name");
					$("#save-new-sale").prop("disabled", false);
				},
				complete : function() {
					$("#save-new-sale").text("Save");
				}
			});
}

/**
 * Submits the purchase txn data to backend url through an ajax call.
 * 
 * @param {Object}
 *            data
 */
function submitPurchaseData(data) {

	$("#save-new-sale").text("Saving...");
	$("#save-new-sale").prop("disabled", true);
	$.ajax({
		url : "/eazytrack/EzTrackServlet?opcode=21",
		type : "POST",
		dataType : "json",
		timeout : 20000,
		data : {
			data : JSON.stringify(data)
		},
		success : function(data) {
			alert("Purchase txn successfully completed ");
			location.reload();
		},
		error : function(xhr, status, text) {
			console.log("save status: " + status);
			alert("Unable to Save at this point of time. Retry later.");
			$("#save-new-sale").prop("disabled", false);
		},
		complete : function() {
			$("#save-new-sale").text("Save");
		}
	});
}

/**
 * Updates the tax value for cform 2%, non cfom original.
 * 
 * @param {Object}
 *            cform indicates if cform is true or not.
 */
function updateTax(cform) {

	var rows = $("#item-details tbody tr");
	if (cform) {
		for (var i = 0; i < rows.length; i++) {
			if ($(rows[i]).data("item-info") != undefined) {
				$(rows[i]).find("td.vat").text("2");
				updateRow($(rows[i]), false);
			}
		}
	} else {
		// copy the tax value from row's data.
		for (var i = 0; i < rows.length; i++) {
			if ($(rows[i]).data("item-info") != undefined) {
				var taxVal = $(rows[i]).data("item-info").vat;
				$(rows[i]).find("td.vat").text(taxVal);
				updateRow($(rows[i]), false);
			}
		}
	}

	updateTotals();
}

function addNewRow() {
	/**
	 * If the last row in item table is already empty, don't add a new row.
	 */
	var lastRow = $("table#item-details tbody tr:last");
	lastRow.each(function() {
		  var trIsEmpty = true;
		  var tr = $(this);
		 // alert(tr.find("td.desc input").val())
		  if(tr.find("td.qty input").val() == null || tr.find("td.qty input").val() == ''){
			  alert("Last row is already empty")
		  }else{
			    var rowFilled = $("table#item-details tbody tr:last").data("item-info");
				var newRow = $("table#item-details tbody tr:last").clone();
				$("table#item-details tbody").append(newRow);
				clearRow(newRow);
				bindTypeAhead(newRow.find("td.desc input"));
				newRow.find("td.desc input").focus();
		  }

		   
		});

	
	scrollToAnchor('notes');
	
}
function show_hide_column(col_no, do_show) {
	   var tbl = $("table#item-details");
	   if(do_show) {
           $("table#item-details thead tr th.disc").show();
           $("table#item-details tfoot tr td.disc").show();
        } else {
     	   $("table#item-details thead tr th.disc").hide();
     	   $("table#item-details tfoot tr td.disc").hide();
        }
	   $('table#item-details tbody tr').each(function() {
           if(do_show) {
              $(this).find("td.disc").show();
           } else {
        	   $(this).find("td.disc").hide();
           }
       });
	}

function isEmpty(td) {
	        if (td.text == '' || td.text() == ' ' || td.html() == '&nbsp;' || td.is(":not(:visible)")) {
	            return true;
	        }            

	        return false;
}
