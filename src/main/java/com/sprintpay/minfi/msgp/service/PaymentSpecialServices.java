package com.sprintpay.minfi.msgp.service;

import java.util.Map;

import com.sprintpay.minfi.msgp.service.dto.AddedParamsPaymentDTO;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;

public interface PaymentSpecialServices {

	/**
	 * 
	 * @param debitInfo
	 * @param amount
	 * @return
	 */
	Map<String, String> buildRequest(String debitInfo, String amount, String provider, String code);
	
	/**
	 * 
	 * @param provider
	 * @return
	 */
	String convertProvider(String provider);
	
	/*
	 * 
	 */
//	String codeNext();
	
	/**
	 * 
	 * @param debitInfo
	 * @param code
	 * @param niu
	 * @param libelleEmision
	 * @param amount
	 * @param referenceEmission
	 * @return
	 */
	Map<String, String> buildRequestAfriland(String debitInfo, String code, String niu, String libelleEmision, String amount, 
			Long referenceEmission/*, String canal*/);
	
	/**
	 * 
	 * @param provider
	 * @param code
	 * @param clientID
	 * @param debitInfo
	 * @param amount
	 * @param firstname
	 * @param lastname
	 * @return
	 */
	Map<String, String> buildRequestWithoutApi(String code, String clientID, 
			String debitInfo, String amount, String firstname, String lastname);
	
	/**
	 * 
	 * @param code
	 * @param debitInfo
	 * @param amount
	 * @param firstname
	 * @param lastname
	 * @param partnerTrxId
	 * @return
	 */
	Map<String, String> buildRequestUBA(String code, String clientID, 
			String debitInfo, String amount, String firstname, String lastname, String partnerTrxId);

	/**
	 * 
	 * @param debitInfo
	 * @param code
	 * @param amount
	 * @param email
	 * @param firstname
	 * @param lastname
	 * @return
	 */
	Map<String, String> buildRequestUBA(String debitInfo, String code, String amount, String email, String firstname, String lastname, 
			String provider, String clientID);

	/**
	 * 
	 * @param paymentDTO
	 * @param amount
	 * @param idEmission
	 * @param idOrganisation
	 * @param idRecette
	 * @param meansOfPayment
	 * @return
	 */
	PaymentDTO constructPaymentDTO(PaymentDTO paymentDTO, Double amount, Long idEmission, Long idOrganisation, Long idRecette, String meansOfPayment);

	/**
	 * 
	 * @param addedParamsPaymentDTO
	 * @param email
	 * @param firstname
	 * @param lastname
	 * @return
	 */
	AddedParamsPaymentDTO constructAddedParamsPaymentDTO(AddedParamsPaymentDTO addedParamsPaymentDTO, String email, String firstname, String lastname);

}
