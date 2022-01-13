package com.albert.common.security.mapper;

import com.albert.common.security.entity.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserRepository {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "version", column = "version"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "createBy", column = "create_by"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "updateBy", column = "update_by")
    })
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    UserEntity findUserEntityByUsername(String username);

    @Select(" SELECT" +
            "  a.id," +
            "  a.username," +
            "  c.role_name my_key," +
            "  'ROLE' my_type " +
            " FROM" +
            "  sys_user a," +
            "  sys_user_role_link b," +
            "  sys_role c " +
            " WHERE" +
            "  a.id = b.user_id " +
            "  AND b.role_id = c.id " +
            "  AND a.username = #{username} UNION" +
            " SELECT" +
            "  a.id," +
            "  a.username," +
            "  c.authority_name my_key," +
            "  'AUTHORITY' my_type " +
            " FROM" +
            "  sys_user a," +
            "  sys_user_authority_link b," +
            "  sys_authority c " +
            " WHERE" +
            "  a.id = b.user_id " +
            "  AND b.authority_id = c.id " +
            "  AND a.username = #{username}")
    List<HashMap<String, String>> findUserRoleAndAuthorityByUsername(@Param("username") String username);
}
