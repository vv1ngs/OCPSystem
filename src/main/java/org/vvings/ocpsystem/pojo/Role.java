package org.vvings.ocpsystem.pojo;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Role implements Serializable {
    private Integer id;
    @NotBlank(message = "角色名称不能为空")
    private String name;

    private String describe;

    private Boolean status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;



    public Role() {
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Role(Integer id, @NotBlank(message = "角色名称不能为空") String name, String describe, Boolean status, Date createTime, Date updateTime) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
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
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this ==obj){
            return true;
        }
        if (obj==null){
            return false;
        }
        if (obj instanceof Role){
            Role role=(Role) obj;
            if (role.getId().equals(this.getId())){
                return true;
            }

        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}