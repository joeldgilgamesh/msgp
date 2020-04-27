package com.sprintpay.minfi.msgp.service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sprintpay.minfi.msgp.domain.HistoriquePayment} entity.
 */
public class HistoriquePaymentDTO implements Serializable {

    private Long id;

    private String status;

    private LocalDateTime dateStatus;


    private Long paymentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(LocalDateTime dateStatus) {
        this.dateStatus = dateStatus;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HistoriquePaymentDTO historiquePaymentDTO = (HistoriquePaymentDTO) o;
        if (historiquePaymentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), historiquePaymentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HistoriquePaymentDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", dateStatus='" + getDateStatus() + "'" +
            ", paymentId=" + getPaymentId() +
            "}";
    }
}
