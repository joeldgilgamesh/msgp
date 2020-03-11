package com.sprint.minfi.msgp.service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sprint.minfi.msgp.domain.Transaction} entity.
 */
public class TransactionDTO implements Serializable {

    private Long id;

    private String codeTransaction;

    private String telephone;

    @NotNull
    private String msg;

    @NotNull
    private LocalDateTime date;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeTransaction() {
        return codeTransaction;
    }

    public void setCodeTransaction(String codeTransaction) {
        this.codeTransaction = codeTransaction;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (transactionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", codeTransaction='" + getCodeTransaction() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", msg='" + getMsg() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
