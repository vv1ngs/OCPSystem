package org.vvings.ocpsystem.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class DealerChild implements Serializable {
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "真实姓名不能为空")
    private String realname;

    private String password;

    @NotNull(message = "角色不能为空")
    private Integer roleId;

    private Integer parentId;
    @NotNull(message = "性别不能为空")
    private Boolean sex;



    private String phone;

    private String email;

    private String note;

    private Boolean status;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }



    private static final long serialVersionUID = 1L;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public DealerChild(Integer id, @NotBlank(message = "用户名不能为空") String username, @NotBlank(message = "真实姓名不能为空") String realname, String password, @NotNull(message = "角色不能为空") Integer roleId, Integer parentId, @NotNull(message = "性别不能为空") Boolean sex, String phone, String email, String note, Boolean status) {
        this.id = id;
        this.username = username;
        this.realname = realname;
        this.password = password;
        this.roleId = roleId;
        this.parentId = parentId;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
        this.note = note;
        this.status = status;
    }

    public DealerChild() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DealerChild that = (DealerChild) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}