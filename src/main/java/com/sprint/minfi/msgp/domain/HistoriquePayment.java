package com.sprint.minfi.msgp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A HistoriquePayment.
 */
@Entity
@Table(name = "historique_payment")
public class HistoriquePayment implements Serializable {

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
