package com.sprint.minfi.msgp.domain;


import javax.persistence.*;

import java.io.Serializable;

import com.sprint.minfi.msgp.domain.enumeration.Statut;

/**
 * A Emission.
 */
@Entity
@Table(name = "emission")
public class Emission extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref_emi")
    private String refEmi;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "code_contribuable")
    private String codeContribuable;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Statut status;

    @Column(name = "id_organisation")
    private Long idOrganisation;

    @Column(name = "id_type_emis_id")
    private Long idTypeEmisId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefEmi() {
        return refEmi;
    }

    public Emission refEmi(String refEmi) {
        this.refEmi = refEmi;
        return this;
    }

    public void setRefEmi(String refEmi) {
        this.refEmi = refEmi;
    }

    public Double getAmount() {
        return amount;
    }

    public Emission amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCodeContribuable() {
        return codeContribuable;
    }

    public Emission codeContribuable(String codeContribuable) {
        this.codeContribuable = codeContribuable;
        return this;
    }

    public void setCodeContribuable(String codeContribuable) {
        this.codeContribuable = codeContribuable;
    }

    public Statut getStatus() {
        return status;
    }

    public Emission status(Statut status) {
        this.status = status;
        return this;
    }

    public void setStatus(Statut status) {
        this.status = status;
    }

    public Long getIdOrganisation() {
        return idOrganisation;
    }

    public Emission idOrganisation(Long idOrganisation) {
        this.idOrganisation = idOrganisation;
        return this;
    }

    public void setIdOrganisation(Long idOrganisation) {
        this.idOrganisation = idOrganisation;
    }

    public Long getIdTypeEmisId() {
        return idTypeEmisId;
    }

    public Emission idTypeEmisId(Long idTypeEmisId) {
        this.idTypeEmisId = idTypeEmisId;
        return this;
    }

    public void setIdTypeEmisId(Long idTypeEmisId) {
        this.idTypeEmisId = idTypeEmisId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Emission)) {
            return false;
        }
        return id != null && id.equals(((Emission) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Emission{" +
            "id=" + getId() +
            ", refEmi='" + getRefEmi() + "'" +
            ", amount=" + getAmount() +
            ", codeContribuable='" + getCodeContribuable() + "'" +
            ", status='" + getStatus() + "'" +
            ", idOrganisation=" + getIdOrganisation() +
            ", idTypeEmisId=" + getIdTypeEmisId() +
            "}";
    }
}
