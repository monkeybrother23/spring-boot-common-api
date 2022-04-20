package com.albert.common.security.mapper;

import com.albert.common.security.entity.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserRepository {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "user_name"),
            @Result(property = "password", column = "user_password"),
            @Result(property = "version", column = "version_no"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "createBy", column = "create_by"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "updateBy", column = "update_by")
    })
    @Select("SELECT id,user_name,user_password,version_no,create_time,create_by,update_time,update_by" +
            " FROM develop.sys_user" +
            " WHERE user_name = #{username}")
    UserEntity findUserEntityByUsername(String username);

    @Select(" SELECT" +
            " a.id," +
            " a.user_name," +
            " c.role_name security_name," +
            " 'ROLE' security_type" +
            " FROM" +
            " develop.sys_user a," +
            " develop.sys_user_role_link b," +
            " develop.sys_role c" +
            " WHERE" +
            " a.id = b.user_id" +
            " AND b.role_id = c.id" +
            " AND a.user_name = #{username}" +
            " UNION" +
            " SELECT" +
            " a.id," +
            " a.user_name ," +
            " c.authority_name security_name," +
            " 'AUTHORITY' security_type" +
            " FROM" +
            " develop.sys_user a," +
            " develop.sys_user_authority_link b," +
            " develop.sys_authority c" +
            " WHERE" +
            " a.id = b.user_id" +
            " AND b.authority_id = c.id" +
            " AND a.user_name = #{username}")
    List<HashMap<String, String>> findUserRoleAndAuthorityByUsername(@Param("username") String username);
}
