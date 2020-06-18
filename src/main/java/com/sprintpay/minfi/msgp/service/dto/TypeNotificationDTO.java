package com.sprintpay.minfi.msgp.service.dto;

import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {} entity.
 */
public class TypeNotificationDTO {

    private Long id;

    private String libelle;

    private String titre;

    private String description;

    private String template;

    private String canalNotification;

    private Set parameters;
    
    public TypeNotificationDTO() {}

    public TypeNotificationDTO(Long id,String libelle, String titre, String description, String template,
			String canalNotification, Set parameters) {
		super();
		this.id = id;
		this.libelle = libelle;
		this.titre = titre;
		this.description = description;
		this.template = template;
		this.canalNotification = canalNotification;
		this.parameters = parameters;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getCanalNotification() {
        return canalNotification;
    }

    public void setCanalNotification(String canalNotification) {
        this.canalNotification = canalNotification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TypeNotificationDTO typeNotificationDTO = (TypeNotificationDTO) o;
        if (typeNotificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), typeNotificationDTO.getId());
    }

    public Set getParameters() {
        return parameters;
    }

    public void setParameters(Set parameters) {
        this.parameters = parameters;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TypeNotificationDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", template='" + getTemplate() + "'" +
            ", canalNotification='" + getCanalNotification() + "'" +
            "}";
    }
}
