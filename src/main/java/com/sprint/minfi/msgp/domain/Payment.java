package com.sprint.minfi.msgp.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sprint.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprint.minfi.msgp.domain.enumeration.Statut;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "means_of_payment")
    private MeansOfPayment meansOfPayment;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", columnDefinition = "varchar(32) default 'DRAFT'")
    private Statut statut;

    @NotNull
    @Column(name = "amount")
    private Double amount;

    @Column(name = "id_emission")
    private Long idEmission;

    @Column(name = "id_recette")
    private Long idRecette;

    @Column(name = "id_organisation")
    private Long idOrganisation;

    @OneToMany(mappedBy = "payment")
    private Set<HistoriquePayment> idHistPays = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("payments")
    private Transaction idTransaction;

    @ManyToOne
    @JsonIgnoreProperties("payments")
    private DetailVersementIntermediaire idDetVers;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Payment code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MeansOfPayment getMeansOfPayment() {
        return meansOfPayment;
    }

    public Payment meansOfPayment(MeansOfPayment meansOfPayment) {
        this.meansOfPayment = meansOfPayment;
        return this;
    }

    public void setMeansOfPayment(MeansOfPayment meansOfPayment) {
        this.meansOfPayment = meansOfPayment;
    }

    public Statut getStatut() {
        return statut;
    }

    public Payment statut(Statut statut) {
        this.statut = statut;
        return this;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Double getAmount() {
        return amount;
    }

    public Payment amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getIdEmission() {
        return idEmission;
    }

    public Payment idEmission(Long idEmission) {
        this.idEmission = idEmission;
        return this;
    }

    public void setIdEmission(Long idEmission) {
        this.idEmission = idEmission;
    }

    public Long getIdRecette() {
        return idRecette;
    }

    public Payment idRecette(Long idRecette) {
        this.idRecette = idRecette;
        return this;
    }

    public void setIdRecette(Long idRecette) {
        this.idRecette = idRecette;
    }

    public Long getIdOrganisation() {
        return idOrganisation;
    }

    public Payment idOrganisation(Long idOrganisation) {
        this.idOrganisation = idOrganisation;
        return this;
    }

    public void setIdOrganisation(Long idOrganisation) {
        this.idOrganisation = idOrganisation;
    }

    public Set<HistoriquePayment> getIdHistPays() {
        return idHistPays;
    }

    public Payment idHistPays(Set<HistoriquePayment> historiquePayments) {
        this.idHistPays = historiquePayments;
        return this;
    }

    public Payment addIdHistPay(HistoriquePayment historiquePayment) {
        this.idHistPays.add(historiquePayment);
        historiquePayment.setPayment(this);
        return this;
    }

    public Payment removeIdHistPay(HistoriquePayment historiquePayment) {
        this.idHistPays.remove(historiquePayment);
        historiquePayment.setPayment(null);
        return this;
    }

    public void setIdHistPays(Set<HistoriquePayment> historiquePayments) {
        this.idHistPays = historiquePayments;
    }

    public Transaction getIdTransaction() {
        return idTransaction;
    }

    public Payment idTransaction(Transaction transaction) {
        this.idTransaction = transaction;
        return this;
    }

    public void setIdTransaction(Transaction transaction) {
        this.idTransaction = transaction;
    }

    public DetailVersementIntermediaire getIdDetVers() {
        return idDetVers;
    }

    public Payment idDetVers(DetailVersementIntermediaire detailVersementIntermediaire) {
        this.idDetVers = detailVersementIntermediaire;
        return this;
    }

    public void setIdDetVers(DetailVersementIntermediaire detailVersementIntermediaire) {
        this.idDetVers = detailVersementIntermediaire;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", meansOfPayment='" + getMeansOfPayment() + "'" +
            ", statut='" + getStatut() + "'" +
            ", amount=" + getAmount() +
            ", idEmission=" + getIdEmission() +
            ", idRecette=" + getIdRecette() +
            ", idOrganisation=" + getIdOrganisation() +
            "}";
    }
}
