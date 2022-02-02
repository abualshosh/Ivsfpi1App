package com.fu.ivsfpi.service.criteria;

import com.fu.ivsfpi.domain.enumeration.PhoneStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.fu.ivsfpi.domain.Phone} entity. This class is used
 * in {@link com.fu.ivsfpi.web.rest.PhoneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /phones?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PhoneCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PhoneStatus
     */
    public static class PhoneStatusFilter extends Filter<PhoneStatus> {

        public PhoneStatusFilter() {}

        public PhoneStatusFilter(PhoneStatusFilter filter) {
            super(filter);
        }

        @Override
        public PhoneStatusFilter copy() {
            return new PhoneStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter imei;

    private StringFilter imei2;

    private StringFilter brand;

    private StringFilter model;

    private StringFilter color;

    private PhoneStatusFilter status;

    private StringFilter verifedBy;

    private InstantFilter verifedDate;

    private LongFilter complainId;

    private Boolean distinct;

    public PhoneCriteria() {}

    public PhoneCriteria(PhoneCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imei = other.imei == null ? null : other.imei.copy();
        this.imei2 = other.imei2 == null ? null : other.imei2.copy();
        this.brand = other.brand == null ? null : other.brand.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.color = other.color == null ? null : other.color.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.verifedBy = other.verifedBy == null ? null : other.verifedBy.copy();
        this.verifedDate = other.verifedDate == null ? null : other.verifedDate.copy();
        this.complainId = other.complainId == null ? null : other.complainId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PhoneCriteria copy() {
        return new PhoneCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getImei() {
        return imei;
    }

    public StringFilter imei() {
        if (imei == null) {
            imei = new StringFilter();
        }
        return imei;
    }

    public void setImei(StringFilter imei) {
        this.imei = imei;
    }

    public StringFilter getImei2() {
        return imei2;
    }

    public StringFilter imei2() {
        if (imei2 == null) {
            imei2 = new StringFilter();
        }
        return imei2;
    }

    public void setImei2(StringFilter imei2) {
        this.imei2 = imei2;
    }

    public StringFilter getBrand() {
        return brand;
    }

    public StringFilter brand() {
        if (brand == null) {
            brand = new StringFilter();
        }
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
    }

    public StringFilter getModel() {
        return model;
    }

    public StringFilter model() {
        if (model == null) {
            model = new StringFilter();
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public StringFilter getColor() {
        return color;
    }

    public StringFilter color() {
        if (color == null) {
            color = new StringFilter();
        }
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public PhoneStatusFilter getStatus() {
        return status;
    }

    public PhoneStatusFilter status() {
        if (status == null) {
            status = new PhoneStatusFilter();
        }
        return status;
    }

    public void setStatus(PhoneStatusFilter status) {
        this.status = status;
    }

    public StringFilter getVerifedBy() {
        return verifedBy;
    }

    public StringFilter verifedBy() {
        if (verifedBy == null) {
            verifedBy = new StringFilter();
        }
        return verifedBy;
    }

    public void setVerifedBy(StringFilter verifedBy) {
        this.verifedBy = verifedBy;
    }

    public InstantFilter getVerifedDate() {
        return verifedDate;
    }

    public InstantFilter verifedDate() {
        if (verifedDate == null) {
            verifedDate = new InstantFilter();
        }
        return verifedDate;
    }

    public void setVerifedDate(InstantFilter verifedDate) {
        this.verifedDate = verifedDate;
    }

    public LongFilter getComplainId() {
        return complainId;
    }

    public LongFilter complainId() {
        if (complainId == null) {
            complainId = new LongFilter();
        }
        return complainId;
    }

    public void setComplainId(LongFilter complainId) {
        this.complainId = complainId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PhoneCriteria that = (PhoneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imei, that.imei) &&
            Objects.equals(imei2, that.imei2) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(model, that.model) &&
            Objects.equals(color, that.color) &&
            Objects.equals(status, that.status) &&
            Objects.equals(verifedBy, that.verifedBy) &&
            Objects.equals(verifedDate, that.verifedDate) &&
            Objects.equals(complainId, that.complainId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imei, imei2, brand, model, color, status, verifedBy, verifedDate, complainId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhoneCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (imei != null ? "imei=" + imei + ", " : "") +
            (imei2 != null ? "imei2=" + imei2 + ", " : "") +
            (brand != null ? "brand=" + brand + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (color != null ? "color=" + color + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (verifedBy != null ? "verifedBy=" + verifedBy + ", " : "") +
            (verifedDate != null ? "verifedDate=" + verifedDate + ", " : "") +
            (complainId != null ? "complainId=" + complainId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
