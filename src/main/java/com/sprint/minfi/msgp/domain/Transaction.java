package com.sprint.minfi.msgp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_transaction")
    private String codeTransaction;

    @Column(name = "telephone")
    private String telephone;

    @NotNull
    @Column(name = "msg", nullable = false)
    private String msg;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeTransaction() {
        return codeTransaction;
    }

    public Transaction codeTransaction(String codeTransaction) {
        this.codeTransaction = codeTransaction;
        return this;
    }

    public void setCodeTransaction(String codeTransaction) {
        this.codeTransaction = codeTransaction;
    }

    public String getTelephone() {
        return telephone;
    }

    public Transaction telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMsg() {
        return msg;
    }

    public Transaction msg(String msg) {
        this.msg = msg;
        return this;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Transaction date(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", codeTransaction='" + getCodeTransaction() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", msg='" + getMsg() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
