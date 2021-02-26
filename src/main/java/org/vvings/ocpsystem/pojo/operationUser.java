package org.vvings.ocpsystem.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class operationUser implements Serializable {
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String username;

    private String password;
    @NotBlank(message = "真实姓名不能为空")
    private String realname;

    private Boolean status;
    private Integer institutionId;


    private String phone;

    private String email;
    @NotNull(message = "性别不能空")
    private Boolean sex;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public operationUser(Integer id, String username, String password, String realname, Boolean status, Integer institutionId,  String phone, String email, Boolean sex, Date createTime, Date updateTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.realname = realname;
        this.status = status;
        this.institutionId = institutionId;

        this.phone = phone;
        this.email = email;
        this.sex = sex;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public operationUser() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
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
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", realname=").append(realname);
        sb.append(", status=").append(status);
        sb.append(", institutionId=").append(institutionId);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", sex=").append(sex);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
    @Override
    public boolean equals(Object obj) {
        if (this ==obj){
            return true;
        }
        if (obj==null){
            return false;
        }
        if (obj instanceof operationUser){
            operationUser user=(operationUser) obj;
            if (user.getId().equals(this.getId())){
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