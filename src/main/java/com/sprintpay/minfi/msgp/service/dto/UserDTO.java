package com.sprintpay.minfi.msgp.service.dto;

import java.time.Instant;
import java.time.LocalDate;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    private boolean activated = false;
    private String langKey;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
    //private Set<String> authorities;
    private String numeroContrubuable;
    private Long idOrganisation;
    private String phone;
    private String numeroCNIParticulier;
    private LocalDate dateNaissancePArticulier;
    private String lieuNaissancePArticulier;
    private String immatriculationEntreprise;
    private String raisonSocialeEntreprise;
    private String nomGerantEntreprise;
    private LocalDate dateCreationEntreprise;
    //private UserType userType;

    //private Role role;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getNumeroContrubuable() {
        return numeroContrubuable;
    }

    public void setNumeroContrubuable(String numeroContrubuable) {
        this.numeroContrubuable = numeroContrubuable;
    }

    public Long getIdOrganisation() {
        return idOrganisation;
    }

    public void setIdOrganisation(Long idOrganisation) {
        this.idOrganisation = idOrganisation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNumeroCNIParticulier() {
        return numeroCNIParticulier;
    }

    public void setNumeroCNIParticulier(String numeroCNIParticulier) {
        this.numeroCNIParticulier = numeroCNIParticulier;
    }

    public LocalDate getDateNaissancePArticulier() {
        return dateNaissancePArticulier;
    }

    public void setDateNaissancePArticulier(LocalDate dateNaissancePArticulier) {
        this.dateNaissancePArticulier = dateNaissancePArticulier;
    }

    public String getLieuNaissancePArticulier() {
        return lieuNaissancePArticulier;
    }

    public void setLieuNaissancePArticulier(String lieuNaissancePArticulier) {
        this.lieuNaissancePArticulier = lieuNaissancePArticulier;
    }

    public String getImmatriculationEntreprise() {
        return immatriculationEntreprise;
    }

    public void setImmatriculationEntreprise(String immatriculationEntreprise) {
        this.immatriculationEntreprise = immatriculationEntreprise;
    }

    public String getRaisonSocialeEntreprise() {
        return raisonSocialeEntreprise;
    }

    public void setRaisonSocialeEntreprise(String raisonSocialeEntreprise) {
        this.raisonSocialeEntreprise = raisonSocialeEntreprise;
    }

    public String getNomGerantEntreprise() {
        return nomGerantEntreprise;
    }

    public void setNomGerantEntreprise(String nomGerantEntreprise) {
        this.nomGerantEntreprise = nomGerantEntreprise;
    }

    public LocalDate getDateCreationEntreprise() {
        return dateCreationEntreprise;
    }

    public void setDateCreationEntreprise(LocalDate dateCreationEntreprise) {
        this.dateCreationEntreprise = dateCreationEntreprise;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "id=" + id +
            ", login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", numeroContrubuable='" + numeroContrubuable + '\'' +
            ", idOrganisation=" + idOrganisation +
            ", phone='" + phone + '\'' +
            ", numeroCNIParticulier='" + numeroCNIParticulier + '\'' +
            ", dateNaissancePArticulier=" + dateNaissancePArticulier +
            ", lieuNaissancePArticulier='" + lieuNaissancePArticulier + '\'' +
            ", immatriculationEntreprise='" + immatriculationEntreprise + '\'' +
            ", raisonSocialeEntreprise='" + raisonSocialeEntreprise + '\'' +
            ", nomGerantEntreprise='" + nomGerantEntreprise + '\'' +
            ", dateCreationEntreprise=" + dateCreationEntreprise +
            '}';
    }
}
