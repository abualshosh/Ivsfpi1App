package com.fu.ivsfpi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fu.ivsfpi.domain.enumeration.IdType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Type;

/**
 * A Complain.
 */
@Entity
@Table(name = "complain")
public class Complain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "complain_number")
    private UUID complainNumber;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descpcription")
    private String descpcription;

    @NotNull
    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @NotNull
    @Column(name = "owner_phone", nullable = false)
    private String ownerPhone;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private String ownerID;

    @Enumerated(EnumType.STRING)
    @Column(name = "id_type")
    private IdType idType;

    @OneToMany(mappedBy = "complain")
    @JsonIgnoreProperties(value = { "complain" }, allowSetters = true)
    public Set<Phone> phones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Complain id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getComplainNumber() {
        return this.complainNumber;
    }

    public Complain complainNumber(UUID complainNumber) {
        this.setComplainNumber(complainNumber);
        return this;
    }

    public void setComplainNumber(UUID complainNumber) {
        this.complainNumber = complainNumber;
    }

    public String getDescpcription() {
        return this.descpcription;
    }

    public Complain descpcription(String descpcription) {
        this.setDescpcription(descpcription);
        return this;
    }

    public void setDescpcription(String descpcription) {
        this.descpcription = descpcription;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public Complain ownerName(String ownerName) {
        this.setOwnerName(ownerName);
        return this;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return this.ownerPhone;
    }

    public Complain ownerPhone(String ownerPhone) {
        this.setOwnerPhone(ownerPhone);
        return this;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerID() {
        return this.ownerID;
    }

    public Complain ownerID(String ownerID) {
        this.setOwnerID(ownerID);
        return this;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public IdType getIdType() {
        return this.idType;
    }

    public Complain idType(IdType idType) {
        this.setIdType(idType);
        return this;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public Set<Phone> getPhones() {
        return this.phones;
    }

    public void setPhones(Set<Phone> phones) {
        if (this.phones != null) {
            this.phones.forEach(i -> i.setComplain(null));
        }
        if (phones != null) {
            phones.forEach(i -> i.setComplain(this));
        }
        this.phones = phones;
    }

    public Complain phones(Set<Phone> phones) {
        this.setPhones(phones);
        return this;
    }

    public Complain addPhone(Phone phone) {
        this.phones.add(phone);
        phone.setComplain(this);
        return this;
    }

    public Complain removePhone(Phone phone) {
        this.phones.remove(phone);
        phone.setComplain(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Complain)) {
            return false;
        }
        return id != null && id.equals(((Complain) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Complain{" +
            "id=" + getId() +
            ", complainNumber='" + getComplainNumber() + "'" +
            ", descpcription='" + getDescpcription() + "'" +
            ", ownerName='" + getOwnerName() + "'" +
            ", ownerPhone='" + getOwnerPhone() + "'" +
            ", ownerID='" + getOwnerID() + "'" +
            ", idType='" + getIdType() + "'" +
            "}";
    }
}
