package com.sprint.minfi.msgp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A JustificatifPaiement.
 */
@Entity
@Table(name = "justificatif_paiement")
public class JustificatifPaiement extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_paiement")
    private Long idPaiement;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "reference_paiement")
    private String referencePaiement;

    @Column(name = "type_paiement")
    private String typePaiement;

    @Column(name = "nature_recette")
    private String natureRecette;

    @Column(name = "montant")
    private Double montant;

    @Column(name = "nui")
    private String nui;

    @Column(name = "nom_prenom_client")
    private String nomPrenomClient;

    @Column(name = "id_organisation")
    private Long idOrganisation;

    @Column(name = "code_organisation")
    private String codeOrganisation;

    @Column(name = "nom_organisation")
    private String nomOrganisation;

    @Column(name = "exercise")
    private String exercise;

    @Column(name = "mois")
    private String mois;

    @Column(name = "libelle_centre")
    private String libelleCentre;

    @Column(name = "libelle_court_centre")
    private String libelleCourtCentre;

    @Column(name = "ifu")
    private String ifu;

    @Column(name = "raison_sociale")
    private String raisonSociale;

    @Column(name = "sigle")
    private String sigle;

    @Column(name = "code_poste")
    private Long codePoste;

    @Column(name = "code")
    private String code;

    @Column(name = "numero")
    private Long numero;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPaiement() {
        return idPaiement;
    }

    public JustificatifPaiement idPaiement(Long idPaiement) {
        this.idPaiement = idPaiement;
        return this;
    }

    public void setIdPaiement(Long idPaiement) {
        this.idPaiement = idPaiement;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public JustificatifPaiement dateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public JustificatifPaiement referencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
        return this;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public String getTypePaiement() {
        return typePaiement;
    }

    public JustificatifPaiement typePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
        return this;
    }

    public void setTypePaiement(String typePaiement) {
        this.typePaiement = typePaiement;
    }

    public String getNatureRecette() {
        return natureRecette;
    }

    public JustificatifPaiement natureRecette(String natureRecette) {
        this.natureRecette = natureRecette;
        return this;
    }

    public void setNatureRecette(String natureRecette) {
        this.natureRecette = natureRecette;
    }

    public Double getMontant() {
        return montant;
    }

    public JustificatifPaiement montant(Double montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getNui() {
        return nui;
    }

    public JustificatifPaiement nui(String nui) {
        this.nui = nui;
        return this;
    }

    public void setNui(String nui) {
        this.nui = nui;
    }

    public String getNomPrenomClient() {
        return nomPrenomClient;
    }

    public JustificatifPaiement nomPrenomClient(String nomPrenomClient) {
        this.nomPrenomClient = nomPrenomClient;
        return this;
    }

    public void setNomPrenomClient(String nomPrenomClient) {
        this.nomPrenomClient = nomPrenomClient;
    }

    public Long getIdOrganisation() {
        return idOrganisation;
    }

    public JustificatifPaiement idOrganisation(Long idOrganisation) {
        this.idOrganisation = idOrganisation;
        return this;
    }

    public void setIdOrganisation(Long idOrganisation) {
        this.idOrganisation = idOrganisation;
    }

    public String getCodeOrganisation() {
        return codeOrganisation;
    }

    public JustificatifPaiement codeOrganisation(String codeOrganisation) {
        this.codeOrganisation = codeOrganisation;
        return this;
    }

    public void setCodeOrganisation(String codeOrganisation) {
        this.codeOrganisation = codeOrganisation;
    }

    public String getNomOrganisation() {
        return nomOrganisation;
    }

    public JustificatifPaiement nomOrganisation(String nomOrganisation) {
        this.nomOrganisation = nomOrganisation;
        return this;
    }

    public void setNomOrganisation(String nomOrganisation) {
        this.nomOrganisation = nomOrganisation;
    }

    public String getExercise() {
        return exercise;
    }

    public JustificatifPaiement exercise(String exercise) {
        this.exercise = exercise;
        return this;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getMois() {
        return mois;
    }

    public JustificatifPaiement mois(String mois) {
        this.mois = mois;
        return this;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public String getLibelleCentre() {
        return libelleCentre;
    }

    public JustificatifPaiement libelleCentre(String libelleCentre) {
        this.libelleCentre = libelleCentre;
        return this;
    }

    public void setLibelleCentre(String libelleCentre) {
        this.libelleCentre = libelleCentre;
    }

    public String getLibelleCourtCentre() {
        return libelleCourtCentre;
    }

    public JustificatifPaiement libelleCourtCentre(String libelleCourtCentre) {
        this.libelleCourtCentre = libelleCourtCentre;
        return this;
    }

    public void setLibelleCourtCentre(String libelleCourtCentre) {
        this.libelleCourtCentre = libelleCourtCentre;
    }

    public String getIfu() {
        return ifu;
    }

    public JustificatifPaiement ifu(String ifu) {
        this.ifu = ifu;
        return this;
    }

    public void setIfu(String ifu) {
        this.ifu = ifu;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public JustificatifPaiement raisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
        return this;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getSigle() {
        return sigle;
    }

    public JustificatifPaiement sigle(String sigle) {
        this.sigle = sigle;
        return this;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public Long getCodePoste() {
        return codePoste;
    }

    public JustificatifPaiement codePoste(Long codePoste) {
        this.codePoste = codePoste;
        return this;
    }

    public void setCodePoste(Long codePoste) {
        this.codePoste = codePoste;
    }

    public String getCode() {
        return code;
    }

    public JustificatifPaiement code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getNumero() {
        return numero;
    }

    public JustificatifPaiement numero(Long numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JustificatifPaiement)) {
            return false;
        }
        return id != null && id.equals(((JustificatifPaiement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "JustificatifPaiement{" +
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
