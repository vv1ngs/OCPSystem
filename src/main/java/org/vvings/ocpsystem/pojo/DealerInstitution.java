package org.vvings.ocpsystem.pojo;

import java.io.Serializable;

public class DealerInstitution implements Serializable {
    private Integer id;

    private Integer childId;

    private Integer institutionId;

    private static final long serialVersionUID = 1L;

    public DealerInstitution(Integer id, Integer childId, Integer institutionId) {
        this.id = id;
        this.childId = childId;
        this.institutionId = institutionId;
    }

    public DealerInstitution() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", childId=").append(childId);
        sb.append(", institutionId=").append(institutionId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}