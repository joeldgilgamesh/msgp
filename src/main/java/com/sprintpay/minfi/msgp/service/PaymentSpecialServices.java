package com.sprintpay.minfi.msgp.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.sprintpay.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;
import com.sprintpay.minfi.msgp.service.dto.AddedParamsPaymentDTO;
import com.sprintpay.minfi.msgp.service.dto.PaymentDTO;

public interface PaymentSpecialServices {

	/**
	 * 
	 * @param debitInfo
	 * @param amount
	 * @return
	 */
	public Map<String, String> buildRequest(String debitInfo, Double amount, String provider, String code);
	
	/**
	 * 
	 * @param provider
	 * @return
	 */
	public String convertProvider(String provider);
	
	/*
	 * 
	 */
	public String codeNext();
	
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
	public Map<String, String> buildRequestBank(String debitInfo, String code, String niu, String libelleEmision, Double amount, String referenceEmission);

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
	public Map<String, String> buildRequestBankUBA(String debitInfo, String code, Double amount, String email, String firstname, String lastname);

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
	public PaymentDTO constructPaymentDTO(PaymentDTO paymentDTO, Double amount, Long idEmission, Long idOrganisation, Long idRecette, String meansOfPayment);

	/**
	 * 
	 * @param addedParamsPaymentDTO
	 * @param email
	 * @param firstname
	 * @param lastname
	 * @return
	 */
	public AddedParamsPaymentDTO constructAddedParamsPaymentDTO(AddedParamsPaymentDTO addedParamsPaymentDTO, String email, String firstname, String lastname);

	public ResponseEntity<Map<String, Object>> validationEffectuerPaiementEnterDatasEmission();
}
