package com.sprintpay.minfi.msgp.service.dto;

import java.io.Serializable;

public class AddedParamsPaymentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9214828071077037842L;
	
	private String email;
	private String firstname;
	private String lastname;
	private String contribuableId;
	private String canal;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getContribuableId() {
		return contribuableId;
	}

	public void setContribuableId(String contribuableId) {
		this.contribuableId = contribuableId;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

}
