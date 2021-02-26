package org.vvings.ocpsystem.pojo;

import java.io.Serializable;

public class RoleAuthority implements Serializable {
    private Integer id;

    private Integer authorityId;

    private Integer roleId;

    private static final long serialVersionUID = 1L;

    public RoleAuthority(Integer id, Integer authorityId, Integer roleId) {
        this.id = id;
        this.authorityId = authorityId;
        this.roleId = roleId;
    }

    public RoleAuthority() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", authorityId=").append(authorityId);
        sb.append(", roleId=").append(roleId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}