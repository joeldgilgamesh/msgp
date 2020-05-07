package com.sprintpay.minfi.msgp.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprintpay.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.repository.PaymentRepository;
import com.sprintpay.minfi.msgp.service.PaymentSpecialServices;
import com.sprintpay.minfi.msgp.service.dto.AddedParamsPaymentDTO;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;

@Service
@Transactional
public class PaymentSpecialServicesImpl implements PaymentSpecialServices {
	
	
	private PaymentRepository paymentRepository;
	
	public PaymentSpecialServicesImpl(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}
	
	public Map<String, String> buildRequest(String debitInfo, Double amount, String provider, String code) {
		
		Map<String, String> request = new HashMap<String, String>();
		
		if (provider == "MOBILE_MONEY" || provider == "ORANGE_MONEY" || provider == "YUP" || provider == "EXPRESS_UNION") {
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
		
		return request;
	}
	
	public Map<String, String> buildRequestBank(String debitInfo, String code, String niu, String libelleEmision, Double amount, String referenceEmission) {
		Map<String, String> request = new HashMap<String, String>();
		
		request.put("contribuableId", niu);
		request.put("libelle", libelleEmision);
		request.put("montant", amount.toString());
		request.put("moyenPaiementId", "afrilandcmr");
		request.put("referenceEmission", referenceEmission);
		request.put("clientId", "");
    	request.put("clientToken", "");
    	request.put("phone", debitInfo);
    	request.put("orderId", code);
		
		return request;
	}
	
	public Map<String, String> buildRequestBankUBA(String debitInfo, String code, Double amount, String email, String firstname, String lastname) {
		Map<String, String> request = new HashMap<String, String>();
		
		request.put("clientId", "");
    	request.put("clientToken", "");
    	request.put("phone", debitInfo);
    	request.put("orderId", code);
    	request.put("uniqueId", "");
    	request.put("amount", amount.toString());
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
		
		case "UBA":
			result = "uba";
			break;
			
		case "AFRILAND":
			result = "afrilandcmr";
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

	@Override
	public ResponseEntity<Map<String, Object>> validationEffectuerPaiementEnterDatasEmission() {
		// TODO Auto-generated method stub
		return null;
	}

}
