package org.vvings.ocpsystem.pojo;

import java.io.Serializable;

public class UserToRole implements Serializable {
    private Integer id;

    private Integer roleId;

    private Integer uid;

    private static final long serialVersionUID = 1L;

    public UserToRole(Integer id, Integer roleId, Integer uid) {
        this.id = id;
        this.roleId = roleId;
        this.uid = uid;
    }

    public UserToRole() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId ;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", roleId=").append(roleId);
        sb.append(", uid=").append(uid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}