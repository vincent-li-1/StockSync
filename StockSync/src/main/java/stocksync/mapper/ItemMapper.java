package stocksync.mapper;

import stocksync.model.Item;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItemMapper {
    @Select("SELECT * FROM StockSync.Item WHERE ${searchKeyAsColumnName} LIKE '${searchValueWithWildcard}' ORDER BY ${sortByAsColumnName} ${sortMethod} LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(property = "itemId", column = "item_id"),
            @Result(property = "itemName",column = "item_name"),
            @Result(property = "itemSize", column = "item_size"),
            @Result(property = "itemPrice", column = "item_price")
    })
    List<Item> findBySearch(@Param("limit") int limit,
                            @Param("offset") int offset,
                            @Param("sortByAsColumnName") String sortByAsColumnName,
                            @Param("sortMethod") String sortMethod,
                            @Param("searchKeyAsColumnName") String searchKeyAsColumnName,
                            @Param("searchValueWithWildcard") String searchValueWithWildcard);


    @Select("SELECT * FROM StockSync.Item ORDER BY ${sortByAsColumnName} ${sortMethod} LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(property = "itemId", column = "item_id"),
            @Result(property = "itemName",column = "item_name"),
            @Result(property = "itemSize",column = "item_size"),
            @Result(property = "itemPrice", column = "item_price"),
    })
    List<Item> findAll(@Param("limit") int limit,
                        @Param("offset") int offset,
                        @Param("sortByAsColumnName") String sortByAsColumnName,
                        @Param("sortMethod") String sortMethod);

    @Insert("INSERT INTO StockSync.Item (item_id,item_name,item_size,item_price) Values (#{newIt.itemId},#{newIt.itemName},#{newIt.itemSize},#{newIt.itemPrice})")
    void insertItem(@Param("newIt") Item newIt);

    @Select("SELECT COUNT(*) FROM StockSync.Item")
    int getTotalNumEntries();
    
    @Select("SELECT COUNT(*) FROM StockSync.Item WHERE ${searchKeyAsColumnName} LIKE '${searchValueWithWildcard}'")
    int getSearchNumEntries(@Param("searchKeyAsColumnName") String searchKeyAsColumnName,
                        @Param("searchValueWithWildcard") String searchValueWithWildcard);
                        
    @Delete("DELETE FROM StockSync.Item WHERE item_id = #{itemId}")
    void deleteItem(@Param("itemId") int itemId);

    @Update("UPDATE StockSync.Item SET item_name = #{updateIt.itemName}, item_size = #{updateIt.itemSize}, item_price = #{updateIt.itemPrice} WHERE (item_id = #{updateIt.itemId})")
    void updateItem(@Param("updateIt") Item updateIt);
}

