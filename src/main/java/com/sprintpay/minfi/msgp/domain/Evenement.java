package com.sprintpay.minfi.msgp.domain;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class Evenement<T> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    T valeur;
    private String table_name;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateEvt;
    private String typeEvenement;

    public Evenement() {
    }

    public Evenement(T valeur, String table_name, LocalDateTime dateEvt) {
        this.valeur = valeur;
        this.table_name = table_name;
        this.dateEvt = dateEvt;
    }

    public T getValeur() {
        return valeur;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public LocalDateTime getDateEvt() {
        return dateEvt;
    }

    public void setDateEvt(LocalDateTime dateEvt) {
        this.dateEvt = dateEvt;
    }

    public void setValeur(T valeur) {
        this.valeur = valeur;
    }

    public String getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(String typeEvenement) {
        this.typeEvenement = typeEvenement;
    }
}
