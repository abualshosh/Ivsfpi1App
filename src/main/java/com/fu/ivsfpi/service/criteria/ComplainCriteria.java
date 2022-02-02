package com.fu.ivsfpi.service.criteria;

import com.fu.ivsfpi.domain.enumeration.IdType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link com.fu.ivsfpi.domain.Complain} entity. This class is used
 * in {@link com.fu.ivsfpi.web.rest.ComplainResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /complains?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ComplainCriteria implements Serializable, Criteria {

    /**
     * Class for filtering IdType
     */
    public static class IdTypeFilter extends Filter<IdType> {

        public IdTypeFilter() {}

        public IdTypeFilter(IdTypeFilter filter) {
            super(filter);
        }

        @Override
        public IdTypeFilter copy() {
            return new IdTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter complainNumber;

    private StringFilter ownerName;

    private StringFilter ownerPhone;

    private StringFilter ownerID;

    private IdTypeFilter idType;

    private LongFilter phoneId;

    private Boolean distinct;

    public ComplainCriteria() {}

    public ComplainCriteria(ComplainCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.complainNumber = other.complainNumber == null ? null : other.complainNumber.copy();
        this.ownerName = other.ownerName == null ? null : other.ownerName.copy();
        this.ownerPhone = other.ownerPhone == null ? null : other.ownerPhone.copy();
        this.ownerID = other.ownerID == null ? null : other.ownerID.copy();
        this.idType = other.idType == null ? null : other.idType.copy();
        this.phoneId = other.phoneId == null ? null : other.phoneId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ComplainCriteria copy() {
        return new ComplainCriteria(this);
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

    public UUIDFilter getComplainNumber() {
        return complainNumber;
    }

    public UUIDFilter complainNumber() {
        if (complainNumber == null) {
            complainNumber = new UUIDFilter();
        }
        return complainNumber;
    }

    public void setComplainNumber(UUIDFilter complainNumber) {
        this.complainNumber = complainNumber;
    }

    public StringFilter getOwnerName() {
        return ownerName;
    }

    public StringFilter ownerName() {
        if (ownerName == null) {
            ownerName = new StringFilter();
        }
        return ownerName;
    }

    public void setOwnerName(StringFilter ownerName) {
        this.ownerName = ownerName;
    }

    public StringFilter getOwnerPhone() {
        return ownerPhone;
    }

    public StringFilter ownerPhone() {
        if (ownerPhone == null) {
            ownerPhone = new StringFilter();
        }
        return ownerPhone;
    }

    public void setOwnerPhone(StringFilter ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public StringFilter getOwnerID() {
        return ownerID;
    }

    public StringFilter ownerID() {
        if (ownerID == null) {
            ownerID = new StringFilter();
        }
        return ownerID;
    }

    public void setOwnerID(StringFilter ownerID) {
        this.ownerID = ownerID;
    }

    public IdTypeFilter getIdType() {
        return idType;
    }

    public IdTypeFilter idType() {
        if (idType == null) {
            idType = new IdTypeFilter();
        }
        return idType;
    }

    public void setIdType(IdTypeFilter idType) {
        this.idType = idType;
    }

    public LongFilter getPhoneId() {
        return phoneId;
    }

    public LongFilter phoneId() {
        if (phoneId == null) {
            phoneId = new LongFilter();
        }
        return phoneId;
    }

    public void setPhoneId(LongFilter phoneId) {
        this.phoneId = phoneId;
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
        final ComplainCriteria that = (ComplainCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(complainNumber, that.complainNumber) &&
            Objects.equals(ownerName, that.ownerName) &&
            Objects.equals(ownerPhone, that.ownerPhone) &&
            Objects.equals(ownerID, that.ownerID) &&
            Objects.equals(idType, that.idType) &&
            Objects.equals(phoneId, that.phoneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, complainNumber, ownerName, ownerPhone, ownerID, idType, phoneId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComplainCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (complainNumber != null ? "complainNumber=" + complainNumber + ", " : "") +
            (ownerName != null ? "ownerName=" + ownerName + ", " : "") +
            (ownerPhone != null ? "ownerPhone=" + ownerPhone + ", " : "") +
            (ownerID != null ? "ownerID=" + ownerID + ", " : "") +
            (idType != null ? "idType=" + idType + ", " : "") +
            (phoneId != null ? "phoneId=" + phoneId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
