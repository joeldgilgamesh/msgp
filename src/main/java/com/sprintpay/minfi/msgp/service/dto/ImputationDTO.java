package com.sprintpay.minfi.msgp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sprintpay.minfi.msgq.domain.Imputation} entity.
 */
public class ImputationDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7904887793494930522L;

	private Long id;

    private Long numDeclarationImputation;

    private String operation;

    private String natrureDesDroits;

    private Double montant;


    private Long justificatifPaiementId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumDeclarationImputation() {
        return numDeclarationImputation;
    }

    public void setNumDeclarationImputation(Long numDeclarationImputation) {
        this.numDeclarationImputation = numDeclarationImputation;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getNatrureDesDroits() {
        return natrureDesDroits;
    }

    public void setNatrureDesDroits(String natrureDesDroits) {
        this.natrureDesDroits = natrureDesDroits;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Long getJustificatifPaiementId() {
        return justificatifPaiementId;
    }

    public void setJustificatifPaiementId(Long justificatifPaiementId) {
        this.justificatifPaiementId = justificatifPaiementId;
    }

    public ImputationDTO(Long numDeclarationImputation, String operation, String natrureDesDroits, Double montant) {
        this.numDeclarationImputation = numDeclarationImputation;
        this.operation = operation;
        this.natrureDesDroits = natrureDesDroits;
        this.montant = montant;
    }

    public ImputationDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ImputationDTO imputationDTO = (ImputationDTO) o;
        if (imputationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), imputationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ImputationDTO{" +
            "id=" + getId() +
            ", numDeclarationImputation=" + getNumDeclarationImputation() +
            ", operation='" + getOperation() + "'" +
            ", natrureDesDroits='" + getNatrureDesDroits() + "'" +
            ", montant=" + getMontant() +
            ", justificatifPaiementId=" + getJustificatifPaiementId() +
            "}";
    }
}