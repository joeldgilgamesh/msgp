package com.sprint.minfi.msgp.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprint.minfi.msgp.service.PaymentSpecialServices;

@Service
@Transactional
public class PaymentSpecialServicesImpl implements PaymentSpecialServices {
	
	public Map<String, String> buildRequest(String debitInfo, Double amount, String provider) {
		
		Map<String, String> request = new HashMap<String, String>();
		
		if (provider == "MOBILE_MONEY" || provider == "ORANGE_MONEY" || provider == "YUP") {
			request.put("clientId", "");
	    	request.put("clientToken", "");
	    	request.put("phone", debitInfo);
	    	request.put("orderId", "");
	    	request.put("amount", amount.toString());
	    	request.put("currency", "");
	    	request.put("description", "");
	    	request.put("companyName", "");
	    	request.put("successUrl", "");
	    	request.put("failureUrl", "");
	    	request.put("notificationUrl", "");
	    	request.put("ipAddress", "");
		}
		
		if (provider == "UBA") {
			request.put("clientId", "");
	    	request.put("clientToken", "");
	    	request.put("phone", debitInfo);
	    	request.put("orderId", "");
	    	request.put("uniqueId", "");
	    	request.put("amount", amount.toString());
	    	request.put("email", "");
	    	request.put("firstname","");
	    	request.put("lastname","");
	    	request.put("currency", "");
	    	request.put("description", "");
	    	request.put("companyName", "");
	    	request.put("successUrl", "");
	    	request.put("failureUrl", "");
	    	request.put("returnUrl", "");
	    	request.put("transactionid", "");
	    	request.put("ref", "");
	    	request.put("notificationUrl", "");
	    	request.put("ipAddress", "");
		}
		
		return request;
	}

}
