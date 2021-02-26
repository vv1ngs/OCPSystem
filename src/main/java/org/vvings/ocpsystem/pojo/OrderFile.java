package org.vvings.ocpsystem.pojo;

import java.io.Serializable;

public class OrderFile implements Serializable {
    private Integer id;

    private String filePath;

    private Integer orderId;

    private static final long serialVersionUID = 1L;

    public OrderFile(Integer id, String filePath, Integer orderId) {
        this.id = id;
        this.filePath = filePath;
        this.orderId = orderId;
    }

    public OrderFile() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", filePath=").append(filePath);
        sb.append(", orderId=").append(orderId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}