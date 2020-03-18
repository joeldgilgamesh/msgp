package com.sprint.minfi.msgp.domain;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A DetailVersementIntermediaire.
 */
@Entity
@Table(name = "detail_versement_intermediaire")
public class DetailVersementIntermediaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_versment")
    private String numeroVersment;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "montant")
    private Double montant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroVersment() {
        return numeroVersment;
    }

    public DetailVersementIntermediaire numeroVersment(String numeroVersment) {
        this.numeroVersment = numeroVersment;
        return this;
    }

    public void setNumeroVersment(String numeroVersment) {
        this.numeroVersment = numeroVersment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public DetailVersementIntermediaire date(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getMontant() {
        return montant;
    }

    public DetailVersementIntermediaire montant(Double montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailVersementIntermediaire)) {
            return false;
        }
        return id != null && id.equals(((DetailVersementIntermediaire) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DetailVersementIntermediaire{" +
            "id=" + getId() +
            ", numeroVersment='" + getNumeroVersment() + "'" +
            ", date='" + getDate() + "'" +
            ", montant=" + getMontant() +
            "}";
    }
}
