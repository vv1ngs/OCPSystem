package org.vvings.ocpsystem.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderProduct implements Serializable {
    private Integer id;

    private Integer productId;

    private Integer orderId;

    private Integer count;

    private BigDecimal amount;

    private Integer volume;

    private String note;

    private ProductInfo productInfo;

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }



    private static final long serialVersionUID = 1L;

    public OrderProduct(Integer id, Integer productId, Integer orderId, Integer count, BigDecimal amount, Integer volume, String note) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.count = count;
        this.amount = amount;
        this.volume = volume;
        this.note = note;
    }

    public OrderProduct() {
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productId=").append(productId);
        sb.append(", orderId=").append(orderId);
        sb.append(", count=").append(count);
        sb.append(", amount=").append(amount);
        sb.append(", volume=").append(volume);
        sb.append(", note=").append(note);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}