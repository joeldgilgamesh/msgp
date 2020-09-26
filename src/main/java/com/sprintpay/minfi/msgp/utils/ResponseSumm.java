package com.sprintpay.minfi.msgp.utils;

import com.sprintpay.minfi.msgp.domain.enumeration.MeansOfPayment;

public class ResponseSumm {

	private MeansOfPayment meansOfPayment;
	private Double amount;
	
	public MeansOfPayment getMeansOfPayment() {
		return meansOfPayment;
	}
	public void setMeansOfPayment(MeansOfPayment meansOfPayment) {
		this.meansOfPayment = meansOfPayment;
	}
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public ResponseSumm(MeansOfPayment meansOfPayment, Double amount) {
		super();
		this.meansOfPayment = meansOfPayment;
		this.amount = amount;
	}
	
	public ResponseSumm() {
		super();
	}

	
}
