package com.sprint.minfi.msgp.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.sprint.minfi.msgp.domain.enumeration.MeansOfPayment;
import com.sprint.minfi.msgp.domain.enumeration.Statut;

/**
 * A DTO for the {@link com.sprint.minfi.msgp.domain.Payment} entity.
 */
public class PaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    private MeansOfPayment meansOfPayment;

    private Statut statut;

    private Double amount;

    private Long idEmission;

    private Long idRecette;

    private Long idOrganisation;


    private Long idTransactionId;

    private Long idDetVersId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MeansOfPayment getMeansOfPayment() {
        return meansOfPayment;
    }

    public void setMeansOfPayment(MeansOfPayment meansOfPayment) {
        this.meansOfPayment = meansOfPayment;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getIdEmission() {
        return idEmission;
    }

    public void setIdEmission(Long idEmission) {
        this.idEmission = idEmission;
    }

    public Long getIdRecette() {
        return idRecette;
    }

    public void setIdRecette(Long idRecette) {
        this.idRecette = idRecette;
    }

    public Long getIdOrganisation() {
        return idOrganisation;
    }

    public void setIdOrganisation(Long idOrganisation) {
        this.idOrganisation = idOrganisation;
    }

    public Long getIdTransactionId() {
        return idTransactionId;
    }

    public void setIdTransactionId(Long transactionId) {
        this.idTransactionId = transactionId;
    }

    public Long getIdDetVersId() {
        return idDetVersId;
    }

    public void setIdDetVersId(Long detailVersementIntermediaireId) {
        this.idDetVersId = detailVersementIntermediaireId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (paymentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", meansOfPayment='" + getMeansOfPayment() + "'" +
            ", statut='" + getStatut() + "'" +
            ", amount=" + getAmount() +
            ", idEmission=" + getIdEmission() +
            ", idRecette=" + getIdRecette() +
            ", idOrganisation=" + getIdOrganisation() +
            ", idTransactionId=" + getIdTransactionId() +
            ", idDetVersId=" + getIdDetVersId() +
            "}";
    }
}
