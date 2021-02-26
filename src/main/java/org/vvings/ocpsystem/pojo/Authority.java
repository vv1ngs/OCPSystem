package org.vvings.ocpsystem.pojo;

import java.io.Serializable;
import java.util.Objects;

public class Authority implements Serializable {
    private Integer id;

    private String authorityName;

    private String uri;

    private static final long serialVersionUID = 1L;

    public Authority(Integer id, String authorityName, String uri) {
        this.id = id;
        this.authorityName = authorityName;
        this.uri = uri;
    }

    public Authority() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName == null ? null : authorityName.trim();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri == null ? null : uri.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", authorityName=").append(authorityName);
        sb.append(", uri=").append(uri);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return Objects.equals(id, authority.id) &&
                Objects.equals(authorityName, authority.authorityName) &&
                Objects.equals(uri, authority.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorityName, uri);
    }
}