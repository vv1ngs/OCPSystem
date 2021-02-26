package org.vvings.ocpsystem.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class ProductInfo implements Serializable {
    private Integer id;

    private Integer productId;

    private String productName;

    private String productModel;

    private Integer productVolume;

    private Integer productCategory;

    private BigDecimal productOpenFare;

    private Date createTime;

    private Date updateTime;

    private Integer dealerId;

    private static final long serialVersionUID = 1L;

    public ProductInfo(Integer id, Integer productId, String productName, String productModel, Integer productVolume, Integer productCategory, BigDecimal productOpenFare, Date createTime, Date updateTime, Integer dealerId) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productModel = productModel;
        this.productVolume = productVolume;
        this.productCategory = productCategory;
        this.productOpenFare = productOpenFare;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.dealerId = dealerId;
    }

    public ProductInfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel == null ? null : productModel.trim();
    }

    public Integer getProductVolume() {
        return productVolume;
    }

    public void setProductVolume(Integer productVolume) {
        this.productVolume = productVolume;
    }

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory ;
    }

    public BigDecimal getProductOpenFare() {
        return productOpenFare;
    }

    public void setProductOpenFare(BigDecimal productOpenFare) {
        this.productOpenFare = productOpenFare;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        sb.append(", productId=").append(productId);
        sb.append(", productName=").append(productName);
        sb.append(", productModel=").append(productModel);
        sb.append(", productVolume=").append(productVolume);
        sb.append(", productCategory=").append(productCategory);
        sb.append(", productOpenFare=").append(productOpenFare);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", dealerId=").append(dealerId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductInfo that = (ProductInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId);
    }
}