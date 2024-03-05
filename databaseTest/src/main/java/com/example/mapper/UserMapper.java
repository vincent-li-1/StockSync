package com.example.mapper;

import com.example.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper

public interface UserMapper {
    @Select("SELECT * FROM StockSync.users")
    @Results({
            @Result(property = "userId", column = "users_PK"),
            @Result(property = "userName", column = "users_Name")
    })
    List<User> findAll();

    @Select("SELECT * FROM StockSync.users WHERE users_PK = #{id}")
    @Results({
            @Result(property = "userId", column = "users_PK"),
            @Result(property = "userName", column = "users_Name")
    })
    List<User> findById(@Param("id") int id);
}
