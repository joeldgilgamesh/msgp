package com.sprintpay.minfi.msgp.utils;

public class RetPaiFiscalis {

    private String codeagence;
    private String codebanque;
    private String datepaiement;
    private String imputation;
    private String libelle_imputation;
    private String message;
    private String montant_imputation;
    private String montantcheque;
    private String numerocheque;
    private String numerocompte;
    private String numerodeclaration;
    private String numeropaiement;
    private String result;

    public RetPaiFiscalis() {
    }

    public RetPaiFiscalis(String codeagence, String codebanque, String datepaiement, String imputation, String libelle_imputation, String message, String montant_imputation, String montantcheque, String numerocheque, String numerocompte, String numerodeclaration, String numeropaiement, String result) {
        this.codeagence = codeagence;
        this.codebanque = codebanque;
        this.datepaiement = datepaiement;
        this.imputation = imputation;
        this.libelle_imputation = libelle_imputation;
        this.message = message;
        this.montant_imputation = montant_imputation;
        this.montantcheque = montantcheque;
        this.numerocheque = numerocheque;
        this.numerocompte = numerocompte;
        this.numerodeclaration = numerodeclaration;
        this.numeropaiement = numeropaiement;
        this.result = result;
    }

    public String getCodeagence() {
        return codeagence;
    }

    public void setCodeagence(String codeagence) {
        this.codeagence = codeagence;
    }

    public String getCodebanque() {
        return codebanque;
    }

    public void setCodebanque(String codebanque) {
        this.codebanque = codebanque;
    }

    public String getDatepaiement() {
        return datepaiement;
    }

    public void setDatepaiement(String datepaiement) {
        this.datepaiement = datepaiement;
    }

    public String getImputation() {
        return imputation;
    }

    public void setImputation(String imputation) {
        this.imputation = imputation;
    }

    public String getLibelle_imputation() {
        return libelle_imputation;
    }

    public void setLibelle_imputation(String libelle_imputation) {
        this.libelle_imputation = libelle_imputation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMontant_imputation() {
        return montant_imputation;
    }

    public void setMontant_imputation(String montant_imputation) {
        this.montant_imputation = montant_imputation;
    }

    public String getMontantcheque() {
        return montantcheque;
    }

    public void setMontantcheque(String montantcheque) {
        this.montantcheque = montantcheque;
    }

    public String getNumerocheque() {
        return numerocheque;
    }

    public void setNumerocheque(String numerocheque) {
        this.numerocheque = numerocheque;
    }

    public String getNumerocompte() {
        return numerocompte;
    }

    public void setNumerocompte(String numerocompte) {
        this.numerocompte = numerocompte;
    }

    public String getNumerodeclaration() {
        return numerodeclaration;
    }

    public void setNumerodeclaration(String numerodeclaration) {
        this.numerodeclaration = numerodeclaration;
    }

    public String getNumeropaiement() {
        return numeropaiement;
    }

    public void setNumeropaiement(String numeropaiement) {
        this.numeropaiement = numeropaiement;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RetPaiFiscalis{" +
            "codeagence='" + codeagence + '\'' +
            ", codebanque='" + codebanque + '\'' +
            ", datepaiement='" + datepaiement + '\'' +
            ", imputation='" + imputation + '\'' +
            ", libelle_imputation='" + libelle_imputation + '\'' +
            ", message='" + message + '\'' +
            ", montant_imputation='" + montant_imputation + '\'' +
            ", montantcheque='" + montantcheque + '\'' +
            ", numerocheque='" + numerocheque + '\'' +
            ", numerocompte='" + numerocompte + '\'' +
            ", numerodeclaration='" + numerodeclaration + '\'' +
            ", numeropaiement='" + numeropaiement + '\'' +
            ", result='" + result + '\'' +
            '}';
    }
}

