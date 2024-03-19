package stocksync.mapper;

import stocksync.model.Item;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItemMapper {
    @Select("SELECT * FROM StockSync.Item LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(property = "itemId", column = "item_id"),
            @Result(property = "itemName",column = "item_name"),
            @Result(property = "itemSize", column = "item_size"),
            @Result(property = "itemPrice", column = "item_price")
    })
    List<Item> find(int limit, int offset);

    @Insert("INSERT INTO StockSync.Item (item_id,item_name,item_size,item_price) Values (#{newIt.itemId},#{newIt.itemName},#{newIt.itemSize},#{newIt.itemPrice})")
    void insertItem(@Param("newIt") Item newIt);

    @Select("SELECT COUNT(*) FROM StockSync.Item")
    int getTotalNumEntries();

    @Delete("DELETE FROM StockSync.Item WHERE item_id = #{deleteIt.itemId}")
    void deleteItem(@Param("deleteIt") Item deleteIt);

    @Update("UPDATE StockSync.Item SET item_name = #{updateIt.itemName}, item_size = #{updateIt.itemSize}, item_price = #{updateIt.itemPrice} WHERE (item_id = #{updateIt.itemId})")
    void updateItem(@Param("updateIt") Item updateIt);
}
