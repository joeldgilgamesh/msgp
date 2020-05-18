package com.sprintpay.minfi.msgp.utils;

public class RetPaiFiscalis {

	private String numero_imposition;
	private String montant;
	private String date_paiement;
	private String idshareApplication;
	private String pwd;
	private String imputation;
	private String libelle_imputation;
	private String code_impot;
	private String libelle_impot;
	private String message;
	private String idpaiement;
	private String result;
	private String montant_imputation;
	
	public String getMontant() {
		return montant;
	}
	public void setMontant(String montant) {
		this.montant = montant;
	}
	public String getIdshareApplication() {
		return idshareApplication;
	}
	public void setIdshareApplication(String idshareApplication) {
		this.idshareApplication = idshareApplication;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getImputation() {
		return imputation;
	}
	public void setImputation(String imputation) {
		this.imputation = imputation;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getNumero_imposition() {
		return numero_imposition;
	}
	public void setNumero_imposition(String numero_imposition) {
		this.numero_imposition = numero_imposition;
	}
	public String getIdpaiement() {
		return idpaiement;
	}
	public void setIdpaiement(String idpaiement) {
		this.idpaiement = idpaiement;
	}
	
	public String getDate_paiement() {
		return date_paiement;
	}
	public void setDate_paiement(String date_paiement) {
		this.date_paiement = date_paiement;
	}
	public String getLibelle_imputation() {
		return libelle_imputation;
	}
	public void setLibelle_imputation(String libelle_imputation) {
		this.libelle_imputation = libelle_imputation;
	}
	public String getCode_impot() {
		return code_impot;
	}
	public void setCode_impot(String code_impot) {
		this.code_impot = code_impot;
	}
	public String getLibelle_impot() {
		return libelle_impot;
	}
	public void setLibelle_impot(String libelle_impot) {
		this.libelle_impot = libelle_impot;
	}
	public String getMontant_imputation() {
		return montant_imputation;
	}
	public void setMontant_imputation(String montant_imputation) {
		this.montant_imputation = montant_imputation;
	}
	public RetPaiFiscalis() {
		super();
	}
	public RetPaiFiscalis(String numero_imposition, String montant, String date_paiement, String idshareApplication,
			String pwd, String imputation, String libelle_imputation, String code_impot, String libelle_impot,
			String message, String idpaiement, String result, String montant_imputation) {
		super();
		this.numero_imposition = numero_imposition;
		this.montant = montant;
		this.date_paiement = date_paiement;
		this.idshareApplication = idshareApplication;
		this.pwd = pwd;
		this.imputation = imputation;
		this.libelle_imputation = libelle_imputation;
		this.code_impot = code_impot;
		this.libelle_impot = libelle_impot;
		this.message = message;
		this.idpaiement = idpaiement;
		this.result = result;
		this.montant_imputation = montant_imputation;
	}
	
}

