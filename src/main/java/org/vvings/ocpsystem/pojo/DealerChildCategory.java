package org.vvings.ocpsystem.pojo;

import java.io.Serializable;

public class DealerChildCategory implements Serializable {
    private Integer id;

    private Integer categoryId;

    private Integer dealerId;

    private static final long serialVersionUID = 1L;

    public DealerChildCategory(Integer id, Integer categoryId, Integer dealerId) {
        this.id = id;
        this.categoryId = categoryId;
        this.dealerId = dealerId;
    }

    public DealerChildCategory() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", dealerId=").append(dealerId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}