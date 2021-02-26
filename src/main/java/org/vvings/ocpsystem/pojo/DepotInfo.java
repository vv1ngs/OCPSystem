package org.vvings.ocpsystem.pojo;

import java.io.Serializable;

public class DepotInfo implements Serializable {
    private Integer id;

    private String name;

    private String phone;

    private String address;

    private String addressinstitution;

    private String note;

    private Boolean isDefault;

    private String uuid;

    private Integer consumerId;

    private static final long serialVersionUID = 1L;

    public DepotInfo(Integer id, String name, String phone, String address, String addressinstitution, String note, Boolean isDefault, String uuid, Integer consumerId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.addressinstitution = addressinstitution;
        this.note = note;
        this.isDefault = isDefault;
        this.uuid = uuid;
        this.consumerId = consumerId;
    }

    public DepotInfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getAddressinstitution() {
        return addressinstitution;
    }

    public void setAddressinstitution(String addressinstitution) {
        this.addressinstitution = addressinstitution == null ? null : addressinstitution.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public Integer getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Integer consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", phone=").append(phone);
        sb.append(", address=").append(address);
        sb.append(", addressinstitution=").append(addressinstitution);
        sb.append(", note=").append(note);
        sb.append(", isDefault=").append(isDefault);
        sb.append(", uuid=").append(uuid);
        sb.append(", consumerId=").append(consumerId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}