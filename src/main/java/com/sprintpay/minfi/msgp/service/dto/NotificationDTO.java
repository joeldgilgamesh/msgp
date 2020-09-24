package com.sprintpay.minfi.msgp.service.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * A DTO for the {} entity.
 */
public class NotificationDTO {

    private Long id;

    private String message;

    private Long idUser;

    private String serviceExpediteur;

    private String statut;


    private Long typenotificationId;

    private Set parameterValues=new HashSet<>();
    
    public NotificationDTO() {}

    public NotificationDTO(Long id, String message, Long idUser, String serviceExpediteur, String statut,
			Long typenotificationId, Set parameterValues) {
		super();
		this.id = id;
		this.message = message;
		this.idUser = idUser;
		this.serviceExpediteur = serviceExpediteur;
		this.statut = statut;
		this.typenotificationId = typenotificationId;
		this.parameterValues = parameterValues;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getServiceExpediteur() {
        return serviceExpediteur;
    }

    public void setServiceExpediteur(String serviceExpediteur) {
        this.serviceExpediteur = serviceExpediteur;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Long getTypenotificationId() {
        return typenotificationId;
    }

    public void setTypenotificationId(Long typeNotificationId) {
        this.typenotificationId = typeNotificationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (notificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationDTO.getId());
    }

    public Set getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Set parameterValues) {
        this.parameterValues = parameterValues;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", idUser=" + getIdUser() +
            ", serviceExpediteur='" + getServiceExpediteur() + "'" +
            ", statut='" + getStatut() + "'" +
            ", typenotificationId=" + getTypenotificationId() +
            "}";
    }
}
