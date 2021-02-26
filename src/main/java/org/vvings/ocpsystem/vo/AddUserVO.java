package org.vvings.ocpsystem.vo;

import org.vvings.ocpsystem.pojo.operationUser;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author vvings
 * @Date 2021/1/5 17:15
 * @Version 1.0
 */
public class AddUserVO {

    Integer role;

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

    operationUser user;

    public AddUserVO(Integer role, Integer id, @NotBlank(message = "用户名不能为空") String username, String password, @NotBlank(message = "真实姓名不能为空") String realname, Boolean status, Integer institutionId, String phone, String email, @NotNull(message = "性别不能空") Boolean sex, Date createTime, Date updateTime, operationUser user) {
        this.role = role;
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
        this.user = user;
    }

    public AddUserVO() {
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
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
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
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public AddUserVO(Integer rid, operationUser user) {
        this.role = rid;
        this.user = user;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public operationUser getUser() {
        operationUser operationUser=new operationUser();
        if (this.sex!=null){
            operationUser.setSex(this.sex);
        }
        if (this.id!=null){
            operationUser.setId(this.id);
        }
        if (this.username!=null){
            operationUser.setUsername(this.username);
        }
        if (this.password!=null){
            operationUser.setPassword(password);
        }
        if (this.status!=null){
            operationUser.setStatus(status);
        }
        if (this.realname!=null){
            operationUser.setRealname(realname);
        }
        if (this.email!=null){
            operationUser.setEmail(email);
        }
        if (this.phone!=null){
            operationUser.setPhone(phone);
        }
        if (this.institutionId!=null){
            operationUser.setInstitutionId(institutionId);
        }
        return operationUser;
    }

    public void setUser(operationUser user) {
        this.user = user;
    }
}
