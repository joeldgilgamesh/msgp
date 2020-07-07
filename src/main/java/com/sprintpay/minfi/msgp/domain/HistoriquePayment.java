package com.sprintpay.minfi.msgp.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A HistoriquePayment.
 */
@Entity
@Table(name = "historique_payment")
public class HistoriquePayment extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "date_status")
    private LocalDateTime dateStatus;

    @ManyToOne
    @JsonIgnoreProperties("idHistPays")
    private Payment payment;

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

    public HistoriquePayment status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateStatus() {
        return dateStatus;
    }

    public HistoriquePayment dateStatus(LocalDateTime dateStatus) {
        this.dateStatus = dateStatus;
        return this;
    }

    public void setDateStatus(LocalDateTime dateStatus) {
        this.dateStatus = dateStatus;
    }

    public Payment getPayment() {
        return payment;
    }

    public HistoriquePayment payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriquePayment)) {
            return false;
        }
        return id != null && id.equals(((HistoriquePayment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "HistoriquePayment{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", dateStatus='" + getDateStatus() + "'" +
            "}";
    }
}
