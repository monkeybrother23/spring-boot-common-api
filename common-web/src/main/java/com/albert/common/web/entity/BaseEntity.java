package com.albert.common.web.entity;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // version
    private Long version;

    // create_time
    private LocalDateTime createTime;

    // create_by
    private String createBy;

    // updateTime
    private LocalDateTime updateTime;

    // update_by
    private String updateBy;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseEntity)) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(version, that.version)
                && Objects.equals(createTime, that.createTime)
                && Objects.equals(createBy, that.createBy)
                && Objects.equals(updateTime, that.updateTime)
                && Objects.equals(updateBy, that.updateBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, createTime, createBy, updateTime, updateBy);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BaseEntity.class.getSimpleName() + "[", "]")
                .add("version=" + version)
                .add("createTime=" + createTime)
                .add("createBy='" + createBy + "'")
                .add("updateTime=" + updateTime)
                .add("updateBy='" + updateBy + "'")
                .toString();
    }
}
