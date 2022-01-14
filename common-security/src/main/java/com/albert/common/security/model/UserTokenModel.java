package com.albert.common.security.model;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class UserTokenModel {
    // 用户名
    private String username;

    private List<String> grantedAuthorityList;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public List<String> getGrantedAuthorityList() {
        return grantedAuthorityList;
    }

    public void setGrantedAuthorityList(List<String> grantedAuthorityList) {
        this.grantedAuthorityList = grantedAuthorityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTokenModel)) return false;
        UserTokenModel that = (UserTokenModel) o;
        return Objects.equals(username, that.username) && Objects.equals(grantedAuthorityList, that.grantedAuthorityList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, grantedAuthorityList);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserTokenModel.class.getSimpleName() + "[", "]")
                .add("username='" + username + "'")
                .add("grantedAuthorityList=" + grantedAuthorityList)
                .toString();
    }
}
