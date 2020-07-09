package com.sprintpay.minfi.msgp.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sprintpay.minfi.msgp.domain.EmissionHistorique} entity.
 */
public class EmissionHistoriqueDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8266853264124945914L;

	private Long id;

    private String status;

    private Long idEmi;

    private LocalDate dateStatus;


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

    public Long getIdEmi() {
        return idEmi;
    }

    public void setIdEmi(Long idEmi) {
        this.idEmi = idEmi;
    }

    public LocalDate getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(LocalDate dateStatus) {
        this.dateStatus = dateStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmissionHistoriqueDTO emissionHistoriqueDTO = (EmissionHistoriqueDTO) o;
        if (emissionHistoriqueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emissionHistoriqueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmissionHistoriqueDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", idEmi=" + getIdEmi() +
            ", dateStatus='" + getDateStatus() + "'" +
            "}";
    }
}
