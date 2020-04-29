package com.sprintpay.minfi.msgp.service;

import java.util.Map;

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
}
