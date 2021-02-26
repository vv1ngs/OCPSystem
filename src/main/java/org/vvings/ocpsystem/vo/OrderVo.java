package org.vvings.ocpsystem.vo;

import org.joda.time.DateTime;
import org.vvings.ocpsystem.pojo.OrderProduct;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author vvings
 * @Date 2020/12/22 16:34
 * @Version 1.0
 */
public class OrderVo implements Serializable {
    private Integer id;
    private String orderId;
    private String dataTime;
    private String dealerId;
    private String dealerName;
    private String area;
    private List<OrderProduct>  list;
    private String firstVerifier;
    private String lastVerifier;
    private Integer status;
    private BigDecimal TotalAmount;
    private Integer TotalCount;
    private Integer TotalVolume;
    private String createTime;
    private String rejectReason;

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        TotalAmount = totalAmount;
    }

    public Integer getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(Integer totalCount) {
        TotalCount = totalCount;
    }

    public Integer getTotalVolume() {
        return TotalVolume;
    }

    public void setTotalVolume(Integer totalVolume) {
        TotalVolume = totalVolume;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLastVerifier() {
        return lastVerifier;
    }

    public void setLastVerifier(String lastVerifier) {
        this.lastVerifier = lastVerifier;
    }

    public String getFirstVerifier() {
        return firstVerifier;
    }

    public void setFirstVerifier(String firstVerifier) {
        this.firstVerifier = firstVerifier;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<OrderProduct> getList() {
        return list;
    }

    public void setList(List<OrderProduct> list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderVo orderVo = (OrderVo) o;
        return Objects.equals(orderId, orderVo.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
