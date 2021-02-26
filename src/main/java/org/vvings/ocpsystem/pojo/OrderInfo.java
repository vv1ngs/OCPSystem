package org.vvings.ocpsystem.pojo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class OrderInfo implements Serializable {
    private Integer id;
    private Integer childId;

    @NotNull(message = "请完善订单信息")
    private Integer dealerId;

    private Integer depotId;


    private String orderId;

    @NotNull(message = "请完善订单信息")
    private Integer getProductYear;
    @NotNull(message = "请完善订单信息")
    private Integer getProductMonth;
    @NotNull(message = "请完善订单信息")
    private Integer getProductMonthDetailed;

    private String note;

    private String area;

    private Integer status;

    private String rejectReason;

    private Integer totalVolume;

    private Integer totalCount;

    private BigDecimal totalAmount;

    private String firstVerifyName;

    private Date firstVerifyTime;

    private String reviewVerifyName;

    private Date reviewVerifyTime;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public OrderInfo(Integer id, Integer childId, Integer dealerId, Integer depotId, String orderId, Integer getProductYear, Integer getProductMonth, Integer getProductMonthDetailed, String note, String area, Integer status, String rejectReason, Integer totalVolume, Integer totalCount, BigDecimal totalAmount, String firstVerifyName, Date firstVerifyTime, String reviewVerifyName, Date reviewVerifyTime, Date createTime, Date updateTime) {
        this.id = id;
        this.childId = childId;
        this.dealerId = dealerId;
        this.depotId = depotId;
        this.orderId = orderId;
        this.getProductYear = getProductYear;
        this.getProductMonth = getProductMonth;
        this.getProductMonthDetailed = getProductMonthDetailed;
        this.note = note;
        this.area = area;
        this.status = status;
        this.rejectReason = rejectReason;
        this.totalVolume = totalVolume;
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
        this.firstVerifyName = firstVerifyName;
        this.firstVerifyTime = firstVerifyTime;
        this.reviewVerifyName = reviewVerifyName;
        this.reviewVerifyTime = reviewVerifyTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public OrderInfo() {
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

    public Integer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

    public Integer getDepotId() {
        return depotId;
    }

    public void setDepotId(Integer depotId) {
        this.depotId = depotId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getGetProductYear() {
        return getProductYear;
    }

    public void setGetProductYear(Integer getProductYear) {
        this.getProductYear = getProductYear;
    }

    public Integer getGetProductMonth() {
        return getProductMonth;
    }

    public void setGetProductMonth(Integer getProductMonth) {
        this.getProductMonth = getProductMonth;
    }

    public Integer getGetProductMonthDetailed() {
        return getProductMonthDetailed;
    }

    public void setGetProductMonthDetailed(Integer getProductMonthDetailed) {
        this.getProductMonthDetailed = getProductMonthDetailed;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason == null ? null : rejectReason.trim();
    }

    public Integer getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Integer totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFirstVerifyName() {
        return firstVerifyName;
    }

    public void setFirstVerifyName(String firstVerifyName) {
        this.firstVerifyName = firstVerifyName == null ? null : firstVerifyName.trim();
    }

    public Date getFirstVerifyTime() {
        return firstVerifyTime;
    }

    public void setFirstVerifyTime(Date firstVerifyTime) {
        this.firstVerifyTime = firstVerifyTime;
    }

    public String getReviewVerifyName() {
        return reviewVerifyName;
    }

    public void setReviewVerifyName(String reviewVerifyName) {
        this.reviewVerifyName = reviewVerifyName == null ? null : reviewVerifyName.trim();
    }

    public Date getReviewVerifyTime() {
        return reviewVerifyTime;
    }

    public void setReviewVerifyTime(Date reviewVerifyTime) {
        this.reviewVerifyTime = reviewVerifyTime;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", childId=").append(childId);
        sb.append(", dealerId=").append(dealerId);
        sb.append(", depotId=").append(depotId);
        sb.append(", orderId=").append(orderId);
        sb.append(", getProductYear=").append(getProductYear);
        sb.append(", getProductMonth=").append(getProductMonth);
        sb.append(", getProductMonthDetailed=").append(getProductMonthDetailed);
        sb.append(", note=").append(note);
        sb.append(", area=").append(area);
        sb.append(", status=").append(status);
        sb.append(", rejectReason=").append(rejectReason);
        sb.append(", totalVolume=").append(totalVolume);
        sb.append(", totalCount=").append(totalCount);
        sb.append(", totalAmount=").append(totalAmount);
        sb.append(", firstVerifyName=").append(firstVerifyName);
        sb.append(", firstVerifyTime=").append(firstVerifyTime);
        sb.append(", reviewVerifyName=").append(reviewVerifyName);
        sb.append(", reviewVerifyTime=").append(reviewVerifyTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderInfo orderInfo = (OrderInfo) o;
        return Objects.equals(id, orderInfo.id) &&
                Objects.equals(orderId, orderInfo.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId);
    }
}