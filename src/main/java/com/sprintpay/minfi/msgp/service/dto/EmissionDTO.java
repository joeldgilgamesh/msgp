package com.sprintpay.minfi.msgp.service.dto;

import java.io.Serializable;
import java.util.Objects;

import com.sprintpay.minfi.msgp.domain.enumeration.Nature;
import com.sprintpay.minfi.msgp.domain.enumeration.Statut;

/**
 * A DTO for the {@link com.sprintpay.minfi.msgp.domain.Emission} entity.
 */
public class EmissionDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8630591142965605064L;

	private Long id;

    private String refEmi;

    private Double amount;

    private String codeContribuable;

    private Statut status;

    private Long idOrganisation;

    private Long idTypeEmisId;

    private Nature nature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefEmi() {
        return refEmi;
    }

    public void setRefEmi(String refEmi) {
        this.refEmi = refEmi;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCodeContribuable() {
        return codeContribuable;
    }

    public void setCodeContribuable(String codeContribuable) {
        this.codeContribuable = codeContribuable;
    }

    public Statut getStatus() {
        return status;
    }

    public void setStatus(Statut status) {
        this.status = status;
    }

    public Long getIdOrganisation() {
        return idOrganisation;
    }

    public void setIdOrganisation(Long idOrganisation) {
        this.idOrganisation = idOrganisation;
    }

    public Long getIdTypeEmisId() {
        return idTypeEmisId;
    }

    public void setIdTypeEmisId(Long idTypeEmisId) {
        this.idTypeEmisId = idTypeEmisId;
    }

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmissionDTO emissionDTO = (EmissionDTO) o;
        if (emissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmissionDTO{" +
            "id=" + id +
            ", refEmi='" + refEmi + '\'' +
            ", amount=" + amount +
            ", codeContribuable='" + codeContribuable + '\'' +
            ", status=" + status +
            ", idOrganisation=" + idOrganisation +
            ", idTypeEmisId=" + idTypeEmisId +
            ", nature=" + nature +
            '}';
    }
}
