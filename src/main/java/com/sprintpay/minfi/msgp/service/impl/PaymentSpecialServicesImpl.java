package com.sprintpay.minfi.msgp.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprintpay.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprintpay.minfi.msgp.service.PaymentSpecialServices;
import com.sprintpay.minfi.msgp.service.dto.AddedParamsPaymentDTO;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;

@Service
@Transactional
public class PaymentSpecialServicesImpl implements PaymentSpecialServices {
	
	//provider.matches("MOBILE_MONEY|MOBILE_MONEY2|ORANGE_MONEY|ORANGE_MONEY2|YUP|EXPRESS_UNION|ECOBANK")
	public Map<String, String> buildRequest(String debitInfo, String amount, String provider, String code) {
		
		Map<String, String> request = new HashMap<String, String>();
		request.put("clientId", "");
    	request.put("clientToken", "");
    	request.put("phone", debitInfo);
    	request.put("orderId", code);
    	request.put("amount", amount);
    	request.put("currency", "");
    	request.put("description", "");
    	request.put("companyName", "");
    	request.put("successUrl", "");
    	request.put("failureUrl", "");
    	request.put("notificationUrl", "");
    	request.put("ipAddress", "");
    	
		return request;
	}
	
	//provider == "AFRILAND"
	public Map<String, String> buildRequestAfriland(String debitInfo, String code, String contribuableId, String libelleEmision, String amount, 
			Long referenceEmission/*, String canal*/) {
		
		Map<String, String> request = new HashMap<String, String>();
		request.put("contribuableId", contribuableId);
		request.put("libelle", libelleEmision);
		request.put("amount", amount);
		request.put("moyenPaiementId", "afrilandcmr");
		request.put("referenceEmission", referenceEmission.toString());
		request.put("clientId", "");
    	request.put("clientToken", "");
    	request.put("phone", debitInfo);
    	request.put("orderId", code);
//    	request.put("canal", canal);
		
		return request;
	}
	
	//provider.matches("UBA|ECOBANK2")
	public Map<String, String> buildRequestUBA(String debitInfo, String code, String amount, String email, String firstname, 
			String lastname, String provider, String clientID) {
		
		Map<String, String> request = new HashMap<String, String>();
		request.put("clientId", clientID);
    	request.put("clientToken", "");
    	request.put("phone", debitInfo);
    	request.put("orderId", code);
    	request.put("uniqueId", "");
    	request.put("email", email);
    	request.put("firstname", firstname);
    	request.put("lastname", lastname);
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
    	request.put("amount", amount);
    	
    	if (provider.equals("ecobankcmr2")) request.put("qrCode", "default");
		
		return request;
	}
	
	public Map<String, String> buildRequestWithoutApi(String code, String clientID, 
			String debitInfo, String amount, String firstname, String lastname, String partnerTrxId){
		
		Map<String, String> request = new HashMap<String, String>();
		request.put("clientId", clientID);
    	request.put("phone", debitInfo);
    	request.put("orderId", code);
    	request.put("firstname", firstname);
    	request.put("lastname", lastname);
    	request.put("amount", amount);
    	request.put("partnerTrxId", partnerTrxId);
    	
    	return request;
	}
	
	public Map<String, String> buildRequestUBA(String code, String clientID, 
			String debitInfo, String amount, String firstname, String lastname, String partnerTrxId){
		
		Map<String, String> request = new HashMap<String, String>();
    	request.put("phone", debitInfo);
    	request.put("clientId", clientID);
    	request.put("orderId", code);
    	request.put("firstname", firstname);
    	request.put("lastname", lastname);
    	request.put("amount", amount);
    	request.put("partnerTrxId", partnerTrxId);
    	
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
		
		case "MOBILE_MONEY2":
			result = "mtncmr2";
			break;
			
		case "ORANGE_MONEY":
			result = "orangecmr";
			break;
			
		case "ORANGE_MONEY2":
			result = "orangecmr2";
			break;
			
		case "EXPRESS_UNION":
			result = "eucmr";
			break;
			
		case "EXPRESS_UNION2":
			result = "eucmr2";
			break;	
			
		case "MASTER_CARD":
			result = "card";
			break;
			
		case "YUP":
			result = "yup";
			break;
		
		case "UBA":
			result = "uba";
			break;
			
		case "AFRILAND":
			result = "afrilandcmr";
			break;
		
		case "ECOBANK":
			result = "ecobankcmr";
			break;
			
		case "ECOBANK2":
			result = "ecobankcmr2";
			break;
			
		case "ECOBANK_ACCOUNT":
			result = "ecobankcmr3";
			break;
			
		case "VISION_FINANCE":
			result = "visionfinancecmr";
			break;
			
		case "CCA_BANK":
			result = "ccabankcmr";
			break;

		default:
			result = null;
			break;
		}
		
		return result;
	}

	@Override
	public PaymentDTO constructPaymentDTO(PaymentDTO paymentDTO, Double amount, Long idEmission, Long idOrganisation, Long idRecette,
			String meansOfPayment) {
		// TODO Auto-generated method stub
		paymentDTO.setAmount(amount);
		paymentDTO.setIdEmission(idEmission);
		paymentDTO.setIdOrganisation(idOrganisation);
		paymentDTO.setIdRecette(idRecette);
		paymentDTO.setMeansOfPayment(MeansOfPayment.valueOf(meansOfPayment));
		return paymentDTO;
	}

	@Override
	public AddedParamsPaymentDTO constructAddedParamsPaymentDTO(AddedParamsPaymentDTO addedParamsPaymentDTO, String email, String firstname, String lastname) {
		// TODO Auto-generated method stub
		addedParamsPaymentDTO.setEmail(email);
		addedParamsPaymentDTO.setFirstname(firstname);
		addedParamsPaymentDTO.setLastname(lastname);
		return addedParamsPaymentDTO;
	}

}
