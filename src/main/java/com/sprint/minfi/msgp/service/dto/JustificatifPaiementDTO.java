package com.sprint.minfi.msgp.service.dto;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sprint.minfi.msgp.domain.JustificatifPaiement} entity.
 */
public class JustificatifPaiementDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1361861848970611798L;

	private Long id;

    private Long idPaiement;

    private LocalDateTime dateCreation;

    private String referencePaiement;

    private String typePaiement;

    private String natureRecette;

    private Double montant;

    private String nui;

    private String nomPrenomClient;

    private Long idOrganisation;

    private String codeOrganisation;

    private String nomOrganisation;

    private String exercise;

    private String mois;

    private String libelleCentre;

    private String libelleCourtCentre;

    private String ifu;

    private String raisonSociale;

    private String sigle;

    private Long codePoste;

    private String code;

    private Long numero;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(Long idPaiement) {
        this.idPaiement = idPaiement;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public String getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

    public String getNatureRecette() {
        return natureRecette;
    }

    public void setNatureRecette(String natureRecette) {
        this.natureRecette = natureRecette;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getNui() {
        return nui;
    }

    public void setNui(String nui) {
        this.nui = nui;
    }

    public String getNomPrenomClient() {
        return nomPrenomClient;
    }

    public void setNomPrenomClient(String nomPrenomClient) {
        this.nomPrenomClient = nomPrenomClient;
    }

    public Long getIdOrganisation() {
        return idOrganisation;
    }

    public void setIdOrganisation(Long idOrganisation) {
        this.idOrganisation = idOrganisation;
    }

    public String getCodeOrganisation() {
        return codeOrganisation;
    }

    public void setCodeOrganisation(String codeOrganisation) {
        this.codeOrganisation = codeOrganisation;
    }

    public String getNomOrganisation() {
        return nomOrganisation;
    }

    public void setNomOrganisation(String nomOrganisation) {
        this.nomOrganisation = nomOrganisation;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public String getLibelleCentre() {
        return libelleCentre;
    }

    public void setLibelleCentre(String libelleCentre) {
        this.libelleCentre = libelleCentre;
    }

    public String getLibelleCourtCentre() {
        return libelleCourtCentre;
    }

    public void setLibelleCourtCentre(String libelleCourtCentre) {
        this.libelleCourtCentre = libelleCourtCentre;
    }

    public String getIfu() {
        return ifu;
    }

    public void setIfu(String ifu) {
        this.ifu = ifu;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public Long getCodePoste() {
        return codePoste;
    }

    public void setCodePoste(Long codePoste) {
        this.codePoste = codePoste;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JustificatifPaiementDTO justificatifPaiementDTO = (JustificatifPaiementDTO) o;
        if (justificatifPaiementDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), justificatifPaiementDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JustificatifPaiementDTO{" +
            "id=" + getId() +
            ", idPaiement=" + getIdPaiement() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", referencePaiement='" + getReferencePaiement() + "'" +
            ", typePaiement='" + getTypePaiement() + "'" +
            ", natureRecette='" + getNatureRecette() + "'" +
            ", montant=" + getMontant() +
            ", nui='" + getNui() + "'" +
            ", nomPrenomClient='" + getNomPrenomClient() + "'" +
            ", idOrganisation=" + getIdOrganisation() +
            ", codeOrganisation='" + getCodeOrganisation() + "'" +
            ", nomOrganisation='" + getNomOrganisation() + "'" +
            ", exercise='" + getExercise() + "'" +
            ", mois='" + getMois() + "'" +
            ", libelleCentre='" + getLibelleCentre() + "'" +
            ", libelleCourtCentre='" + getLibelleCourtCentre() + "'" +
            ", ifu='" + getIfu() + "'" +
            ", raisonSociale='" + getRaisonSociale() + "'" +
            ", sigle='" + getSigle() + "'" +
            ", codePoste=" + getCodePoste() +
            ", code='" + getCode() + "'" +
            ", numero=" + getNumero() +
            "}";
    }
}
