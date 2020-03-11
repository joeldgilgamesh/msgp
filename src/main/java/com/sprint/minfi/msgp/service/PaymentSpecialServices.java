package com.sprint.minfi.msgp.service;

import java.util.Map;

public interface PaymentSpecialServices {

	/**
	 * 
	 * @param debitInfo
	 * @param amount
	 * @return
	 */
	public Map<String, String> buildRequest(String debitInfo, Double amount, String provider);
}
