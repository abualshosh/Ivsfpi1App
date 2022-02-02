package com.fu.ivsfpi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fu.ivsfpi.domain.enumeration.PhoneStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Type;

/**
 * A Phone.
 */
@Entity
@Table(name = "phone")
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "imei", nullable = false)
    private String imei;

    @Column(name = "imei_2")
    private String imei2;

    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "color")
    private String color;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descroptions")
    private String descroptions;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PhoneStatus status;

    @Column(name = "verifed_by")
    private String verifedBy;

    @Column(name = "verifed_date")
    private Instant verifedDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "phones" }, allowSetters = true)
    private Complain complain;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Phone id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImei() {
        return this.imei;
    }

    public Phone imei(String imei) {
        this.setImei(imei);
        return this;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImei2() {
        return this.imei2;
    }

    public Phone imei2(String imei2) {
        this.setImei2(imei2);
        return this;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    public String getBrand() {
        return this.brand;
    }

    public Phone brand(String brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public Phone model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return this.color;
    }

    public Phone color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescroptions() {
        return this.descroptions;
    }

    public Phone descroptions(String descroptions) {
        this.setDescroptions(descroptions);
        return this;
    }

    public void setDescroptions(String descroptions) {
        this.descroptions = descroptions;
    }

    public PhoneStatus getStatus() {
        return this.status;
    }

    public Phone status(PhoneStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PhoneStatus status) {
        this.status = status;
    }

    public String getVerifedBy() {
        return this.verifedBy;
    }

    public Phone verifedBy(String verifedBy) {
        this.setVerifedBy(verifedBy);
        return this;
    }

    public void setVerifedBy(String verifedBy) {
        this.verifedBy = verifedBy;
    }

    public Instant getVerifedDate() {
        return this.verifedDate;
    }

    public Phone verifedDate(Instant verifedDate) {
        this.setVerifedDate(verifedDate);
        return this;
    }

    public void setVerifedDate(Instant verifedDate) {
        this.verifedDate = verifedDate;
    }

    public Complain getComplain() {
        return this.complain;
    }

    public void setComplain(Complain complain) {
        this.complain = complain;
    }

    public Phone complain(Complain complain) {
        this.setComplain(complain);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Phone)) {
            return false;
        }
        return id != null && id.equals(((Phone) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Phone{" +
            "id=" + getId() +
            ", imei='" + getImei() + "'" +
            ", imei2='" + getImei2() + "'" +
            ", brand='" + getBrand() + "'" +
            ", model='" + getModel() + "'" +
            ", color='" + getColor() + "'" +
            ", descroptions='" + getDescroptions() + "'" +
            ", status='" + getStatus() + "'" +
            ", verifedBy='" + getVerifedBy() + "'" +
            ", verifedDate='" + getVerifedDate() + "'" +
            "}";
    }
}
