package com.sprintpay.minfi.msgp.service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.sprintpay.minfi.msgp.domain.DetailVersementIntermediaire} entity.
 */
public class DetailVersementIntermediaireDTO implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -1142468912269715875L;

	private Long id;

    private String numeroVersment;

    private LocalDateTime date;

    private Double montant;

    private Set<String> paymentRefs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroVersment() {
        return numeroVersment;
    }

    public void setNumeroVersment(String numeroVersment) {
        this.numeroVersment = numeroVersment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Set<String> getPaymentRefs() { return paymentRefs; }

    public void setPaymentRefs(Set<String> paymentRefs) { this.paymentRefs = paymentRefs; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DetailVersementIntermediaireDTO detailVersementIntermediaireDTO = (DetailVersementIntermediaireDTO) o;
        if (detailVersementIntermediaireDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), detailVersementIntermediaireDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DetailVersementIntermediaireDTO{" +
            "id=" + id +
            ", numeroVersment='" + numeroVersment + '\'' +
            ", date=" + date +
            ", montant=" + montant +
            ", paymentRefs=" + paymentRefs +
            '}';
    }
}
