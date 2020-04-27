package com.sprintpay.minfi.msgp.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprintpay.minfi.msgp.repository.PaymentRepository;
import com.sprintpay.minfi.msgp.service.PaymentSpecialServices;

@Service
@Transactional
public class PaymentSpecialServicesImpl implements PaymentSpecialServices {
	
	
	private PaymentRepository paymentRepository;
	
	public PaymentSpecialServicesImpl(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}
	
	public Map<String, String> buildRequest(String debitInfo, Double amount, String provider, String code) {
		
		Map<String, String> request = new HashMap<String, String>();
		
		if (provider == "MOBILE_MONEY" || provider == "ORANGE_MONEY" || provider == "YUP") {
			request.put("clientId", "");
	    	request.put("clientToken", "");
	    	request.put("phone", debitInfo);
	    	request.put("orderId", code);
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
	    	request.put("orderId", code);
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
		
		if (provider == "AFRILAND") {
			
		}
		
		return request;
	}
	
	public Map<String, String> buildRequestBank(String niu, String libelleEmision, Double amount, String referenceEmission) {
		Map<String, String> request = new HashMap<String, String>();
		
		request.put("contribuableId", niu);
		request.put("libelle", libelleEmision);
		request.put("montant", amount.toString());
		request.put("moyenPaiementId", "afrilandcmr");
		request.put("referenceEmission", referenceEmission);
		
		return request;
	}

	@Override
	public String convertProvider(String provider) {
		// TODO Auto-generated method stub
		String result = "";
		switch (provider) {
		case "MOBILE_MONEY":
			result = "mtncmr";
			break;
			
		case "ORANGE_MONEY":
			result = "orangecmr";
			break;
			
		case "EXPRESS_UNION":
			result = "eucmr";
			break;
			
		case "MASTER_CARD":
			result = "card";
			break;
			
		case "YUP":
			result = "yup";
			break;

		default:
			result = null;
			break;
		}
		return result;
	}

	@Override
	public String codeNext() {
		// TODO Auto-generated method stub
		System.out.println("------------------------------ lastcode" + this.paymentRepository.countLine());
//		return "code_" + (Integer.parseInt(this.paymentRepository.findLastCode().substring(5)) + 1);
		return "code_" + (this.paymentRepository.getLastId(this.paymentRepository.countLine()) + 1);
	}

}
