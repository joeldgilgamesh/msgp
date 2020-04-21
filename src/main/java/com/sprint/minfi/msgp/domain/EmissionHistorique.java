package com.sprint.minfi.msgp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A EmissionHistorique.
 */
@Entity
@Table(name = "emission_historique")
public class EmissionHistorique extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "id_emi")
    private Long idEmi;

    @Column(name = "date_status")
    private LocalDate dateStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public EmissionHistorique status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getIdEmi() {
        return idEmi;
    }

    public EmissionHistorique idEmi(Long idEmi) {
        this.idEmi = idEmi;
        return this;
    }

    public void setIdEmi(Long idEmi) {
        this.idEmi = idEmi;
    }

    public LocalDate getDateStatus() {
        return dateStatus;
    }

    public EmissionHistorique dateStatus(LocalDate dateStatus) {
        this.dateStatus = dateStatus;
        return this;
    }

    public void setDateStatus(LocalDate dateStatus) {
        this.dateStatus = dateStatus;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmissionHistorique)) {
            return false;
        }
        return id != null && id.equals(((EmissionHistorique) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EmissionHistorique{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", idEmi=" + getIdEmi() +
            ", dateStatus='" + getDateStatus() + "'" +
            "}";
    }
}
