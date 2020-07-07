package com.sprintpay.minfi.msgp.service.dto;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

public class TransactionSSDTO {

    private Long id;

    @Size(max = 50)
    private String numRefTr;

    @Size(max = 10)
    private String codeOpBank;

    @Size(max = 10)
    private String codeInstruction;

    @Size(max = 10)
    private String codeTypeTr;

    private LocalDate dateTransaction;

    @Size(max = 10)
    private String devise;

    private Double montant;

    @Size(max = 100)
    private String instDonOrdre;

    @Size(max = 100)
    private String corExp;

    @Size(max = 100)
    private String intermediaire;

    @Size(max = 100)
    private String instGesCpt;

    @Size(max = 200)
    private String clientBen;

    @Size(max = 200)
    private String infoPai;

    @Size(max = 10)
    private String detailFrais;

    @Size(max = 100)
    private String infoExpDest;

    private Integer sens;

    @Size(max = 5)
    private String codeCc;

    @Size(max = 10)
    private String codePaysEm;

    private LocalDate dateGen;

    @Size(max = 10)
    private String codeValeur;

    @Size(max = 10)
    private String codeParRem;

    private LocalDate datePres;

    private LocalDate datePresApp;

    @Size(max = 10)
    private String nuRem;

    @Size(max = 10)
    private String codeEnreg;

    @Size(max = 10)
    private String codeDevise;

    @Size(max = 10)
    private String numVir;

    @Size(max = 100)
    private String ribDo;

    @Size(max = 200)
    private String nomDo;

    @Size(max = 10)
    private String codeParDest;

    @Size(max = 10)
    private String codePaysDest;

    @Size(max = 100)
    private String ribBenef;

    @Size(max = 100)
    private String nomBenf;

    @Size(max = 100)
    private String numDoP;

    @Size(max = 100)
    private String motifOp;

    private LocalDate dateReg;

    @Size(max = 10)
    private String motifRej;

    @Size(max = 100)
    private String codeAgRem;

    @Size(max = 100)
    private String codeAgDest;

    @Size(max = 100)
    private String numCheq;

    private LocalDate dateEmission;

    private String typeTransaction;

    private ZonedDateTime heureGen;

    @Size(max = 6000)
    private String clientOrdre;


    private Long sourceFileId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumRefTr() {
        return numRefTr;
    }

    public void setNumRefTr(String numRefTr) {
        this.numRefTr = numRefTr;
    }

    public String getCodeOpBank() {
        return codeOpBank;
    }

    public void setCodeOpBank(String codeOpBank) {
        this.codeOpBank = codeOpBank;
    }

    public String getCodeInstruction() {
        return codeInstruction;
    }

    public void setCodeInstruction(String codeInstruction) {
        this.codeInstruction = codeInstruction;
    }

    public String getCodeTypeTr() {
        return codeTypeTr;
    }

    public void setCodeTypeTr(String codeTypeTr) {
        this.codeTypeTr = codeTypeTr;
    }

    public LocalDate getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDate dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getInstDonOrdre() {
        return instDonOrdre;
    }

    public void setInstDonOrdre(String instDonOrdre) {
        this.instDonOrdre = instDonOrdre;
    }

    public String getCorExp() {
        return corExp;
    }

    public void setCorExp(String corExp) {
        this.corExp = corExp;
    }

    public String getIntermediaire() {
        return intermediaire;
    }

    public void setIntermediaire(String intermediaire) {
        this.intermediaire = intermediaire;
    }

    public String getInstGesCpt() {
        return instGesCpt;
    }

    public void setInstGesCpt(String instGesCpt) {
        this.instGesCpt = instGesCpt;
    }

    public String getClientBen() {
        return clientBen;
    }

    public void setClientBen(String clientBen) {
        this.clientBen = clientBen;
    }

    public String getInfoPai() {
        return infoPai;
    }

    public void setInfoPai(String infoPai) {
        this.infoPai = infoPai;
    }

    public String getDetailFrais() {
        return detailFrais;
    }

    public void setDetailFrais(String detailFrais) {
        this.detailFrais = detailFrais;
    }

    public String getInfoExpDest() {
        return infoExpDest;
    }

    public void setInfoExpDest(String infoExpDest) {
        this.infoExpDest = infoExpDest;
    }

    public Integer getSens() {
        return sens;
    }

    public void setSens(Integer sens) {
        this.sens = sens;
    }

    public String getCodeCc() {
        return codeCc;
    }

    public void setCodeCc(String codeCc) {
        this.codeCc = codeCc;
    }

    public String getCodePaysEm() {
        return codePaysEm;
    }

    public void setCodePaysEm(String codePaysEm) {
        this.codePaysEm = codePaysEm;
    }

    public LocalDate getDateGen() {
        return dateGen;
    }

    public void setDateGen(LocalDate dateGen) {
        this.dateGen = dateGen;
    }

    public String getCodeValeur() {
        return codeValeur;
    }

    public void setCodeValeur(String codeValeur) {
        this.codeValeur = codeValeur;
    }

    public String getCodeParRem() {
        return codeParRem;
    }

    public void setCodeParRem(String codeParRem) {
        this.codeParRem = codeParRem;
    }

    public LocalDate getDatePres() {
        return datePres;
    }

    public void setDatePres(LocalDate datePres) {
        this.datePres = datePres;
    }

    public LocalDate getDatePresApp() {
        return datePresApp;
    }

    public void setDatePresApp(LocalDate datePresApp) {
        this.datePresApp = datePresApp;
    }

    public String getNuRem() {
        return nuRem;
    }

    public void setNuRem(String nuRem) {
        this.nuRem = nuRem;
    }

    public String getCodeEnreg() {
        return codeEnreg;
    }

    public void setCodeEnreg(String codeEnreg) {
        this.codeEnreg = codeEnreg;
    }

    public String getCodeDevise() {
        return codeDevise;
    }

    public void setCodeDevise(String codeDevise) {
        this.codeDevise = codeDevise;
    }

    public String getNumVir() {
        return numVir;
    }

    public void setNumVir(String numVir) {
        this.numVir = numVir;
    }

    public String getRibDo() {
        return ribDo;
    }

    public void setRibDo(String ribDo) {
        this.ribDo = ribDo;
    }

    public String getNomDo() {
        return nomDo;
    }

    public void setNomDo(String nomDo) {
        this.nomDo = nomDo;
    }

    public String getCodeParDest() {
        return codeParDest;
    }

    public void setCodeParDest(String codeParDest) {
        this.codeParDest = codeParDest;
    }

    public String getCodePaysDest() {
        return codePaysDest;
    }

    public void setCodePaysDest(String codePaysDest) {
        this.codePaysDest = codePaysDest;
    }

    public String getRibBenef() {
        return ribBenef;
    }

    public void setRibBenef(String ribBenef) {
        this.ribBenef = ribBenef;
    }

    public String getNomBenf() {
        return nomBenf;
    }

    public void setNomBenf(String nomBenf) {
        this.nomBenf = nomBenf;
    }

    public String getNumDoP() {
        return numDoP;
    }

    public void setNumDoP(String numDoP) {
        this.numDoP = numDoP;
    }

    public String getMotifOp() {
        return motifOp;
    }

    public void setMotifOp(String motifOp) {
        this.motifOp = motifOp;
    }

    public LocalDate getDateReg() {
        return dateReg;
    }

    public void setDateReg(LocalDate dateReg) {
        this.dateReg = dateReg;
    }

    public String getMotifRej() {
        return motifRej;
    }

    public void setMotifRej(String motifRej) {
        this.motifRej = motifRej;
    }

    public String getCodeAgRem() {
        return codeAgRem;
    }

    public void setCodeAgRem(String codeAgRem) {
        this.codeAgRem = codeAgRem;
    }

    public String getCodeAgDest() {
        return codeAgDest;
    }

    public void setCodeAgDest(String codeAgDest) {
        this.codeAgDest = codeAgDest;
    }

    public String getNumCheq() {
        return numCheq;
    }

    public void setNumCheq(String numCheq) {
        this.numCheq = numCheq;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public ZonedDateTime getHeureGen() {
        return heureGen;
    }

    public void setHeureGen(ZonedDateTime heureGen) {
        this.heureGen = heureGen;
    }

    public String getClientOrdre() {
        return clientOrdre;
    }

    public void setClientOrdre(String clientOrdre) {
        this.clientOrdre = clientOrdre;
    }

    public Long getSourceFileId() {
        return sourceFileId;
    }

    public void setSourceFileId(Long sourceFileId) {
        this.sourceFileId = sourceFileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (transactionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", numRefTr='" + getNumRefTr() + "'" +
            ", codeOpBank='" + getCodeOpBank() + "'" +
            ", codeInstruction='" + getCodeInstruction() + "'" +
            ", codeTypeTr='" + getCodeTypeTr() + "'" +
            ", dateTransaction='" + getDateTransaction() + "'" +
            ", devise='" + getDevise() + "'" +
            ", montant=" + getMontant() +
            ", instDonOrdre='" + getInstDonOrdre() + "'" +
            ", corExp='" + getCorExp() + "'" +
            ", intermediaire='" + getIntermediaire() + "'" +
            ", instGesCpt='" + getInstGesCpt() + "'" +
            ", clientBen='" + getClientBen() + "'" +
            ", infoPai='" + getInfoPai() + "'" +
            ", detailFrais='" + getDetailFrais() + "'" +
            ", infoExpDest='" + getInfoExpDest() + "'" +
            ", sens=" + getSens() +
            ", codeCc='" + getCodeCc() + "'" +
            ", codePaysEm='" + getCodePaysEm() + "'" +
            ", dateGen='" + getDateGen() + "'" +
            ", codeValeur='" + getCodeValeur() + "'" +
            ", codeParRem='" + getCodeParRem() + "'" +
            ", datePres='" + getDatePres() + "'" +
            ", datePresApp='" + getDatePresApp() + "'" +
            ", nuRem='" + getNuRem() + "'" +
            ", codeEnreg='" + getCodeEnreg() + "'" +
            ", codeDevise='" + getCodeDevise() + "'" +
            ", numVir='" + getNumVir() + "'" +
            ", ribDo='" + getRibDo() + "'" +
            ", nomDo='" + getNomDo() + "'" +
            ", codeParDest='" + getCodeParDest() + "'" +
            ", codePaysDest='" + getCodePaysDest() + "'" +
            ", ribBenef='" + getRibBenef() + "'" +
            ", nomBenf='" + getNomBenf() + "'" +
            ", numDoP='" + getNumDoP() + "'" +
            ", motifOp='" + getMotifOp() + "'" +
            ", dateReg='" + getDateReg() + "'" +
            ", motifRej='" + getMotifRej() + "'" +
            ", codeAgRem='" + getCodeAgRem() + "'" +
            ", codeAgDest='" + getCodeAgDest() + "'" +
            ", numCheq='" + getNumCheq() + "'" +
            ", dateEmission='" + getDateEmission() + "'" +
            ", typeTransaction='" + getTypeTransaction() + "'" +
            ", heureGen='" + getHeureGen() + "'" +
            ", clientOrdre='" + getClientOrdre() + "'" +
            ", sourceFileId=" + getSourceFileId() +
            "}";
    }
}
