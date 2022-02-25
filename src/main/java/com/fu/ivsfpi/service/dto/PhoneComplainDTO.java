package com.fu.ivsfpi.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fu.ivsfpi.domain.Phone;
import com.fu.ivsfpi.domain.enumeration.IdType;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

public class PhoneComplainDTO {

    @Lob
    private String descpcription;

    @NotNull
    private String ownerName;

    @NotNull
    private String ownerPhone;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private String ownerID;

    @Enumerated(EnumType.STRING)
    private IdType idType;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NotNull
    private Set<Phone> phones;

    public PhoneComplainDTO() {}

    public String getDescpcription() {
        return descpcription;
    }

    public void setDescpcription(String descpcription) {
        this.descpcription = descpcription;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public IdType getIdType() {
        return idType;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return (
            "PhoneComplainDTO{" +
            "descpcription='" +
            descpcription +
            '\'' +
            ", ownerName='" +
            ownerName +
            '\'' +
            ", ownerPhone='" +
            ownerPhone +
            '\'' +
            ", ownerID='" +
            ownerID +
            '\'' +
            ", idType=" +
            idType +
            ", phones=" +
            phones +
            '}'
        );
    }
}
