package stocksync.mapper;

import stocksync.model.User;
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

    @Insert("INSERT INTO StockSync.users (users_PK,users_Name) values (#{newUser.userId},#{newUser.userName})")
    void insertUser(@Param("newUser") User newUser);
}
