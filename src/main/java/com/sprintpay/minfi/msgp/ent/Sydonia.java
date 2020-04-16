package com.sprintpay.minfi.msgp.ent;

public class Sydonia {
	
	public String bureau;
	public String niu;
	public String nom;
	public String codeDeclarant;
	public String declarant;
	public String refDeclaration;
	public String dateDeclaration;
	public String refLiquidation;
	public String dateLiquidation;
	public long droitTaxeDouane;
	public long droitScanner;
	public long fraisEForce;
	public int version;
	public String reference;
	public String statut;
	

	public Sydonia(String bureau, String niu, String nom, String codeDeclarant, String declarant, String refDeclaration,
			String dateDeclaration, String refLiquidation, String dateLiquidation, long droitTaxeDouane,
			long droitScanner, long fraisEForce, int version, String reference, String statut) {
		this.bureau = bureau;
		this.niu = niu;
		this.nom = nom;
		this.codeDeclarant = codeDeclarant;
		this.declarant = declarant;
		this.refDeclaration = refDeclaration;
		this.dateDeclaration = dateDeclaration;
		this.refLiquidation = refLiquidation;
		this.dateLiquidation = dateLiquidation;
		this.droitTaxeDouane = droitTaxeDouane;
		this.droitScanner = droitScanner;
		this.fraisEForce = fraisEForce;
		this.version = version;
		this.reference = reference;
		this.statut = statut;
	}


	public String getBureau() {
		return bureau;
	}


	public String getNiu() {
		return niu;
	}


	public String getNom() {
		return nom;
	}


	public String getCodeDeclarant() {
		return codeDeclarant;
	}


	public String getDeclarant() {
		return declarant;
	}


	public String getRefDeclaration() {
		return refDeclaration;
	}


	public String getDateDeclaration() {
		return dateDeclaration;
	}


	public String getRefLiquidation() {
		return refLiquidation;
	}


	public String getDateLiquidation() {
		return dateLiquidation;
	}


	public long getDroitTaxeDouane() {
		return droitTaxeDouane;
	}


	public long getDroitScanner() {
		return droitScanner;
	}


	public long getFraisEForce() {
		return fraisEForce;
	}


	public int getVersion() {
		return version;
	}


	public String getReference() {
		return reference;
	}


	public String getStatut() {
		return statut;
	}

}
