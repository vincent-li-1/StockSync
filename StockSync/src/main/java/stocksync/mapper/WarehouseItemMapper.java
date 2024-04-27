package stocksync.mapper;

import org.apache.ibatis.annotations.*;
import stocksync.model.WarehouseItem;

import java.util.List;

@Mapper
public interface WarehouseItemMapper {
    // Get method to use when there are search params provided
    // Use $ for string substitution in MyBatis query. Search value string needs to be wrapped in ''
    @Select("SELECT * FROM StockSync.WarehouseItems WHERE ${searchKeyAsColumnName} LIKE '${searchValueWithWildcard}' ORDER BY ${sortByAsColumnName} ${sortMethod} LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(property = "warehouseItemId", column = "ware_items_id"),
            @Result(property = "itemId",column = "item_id"),
            @Result(property = "warehouseId",column = "warehouse_id"),
            @Result(property = "quantity", column = "quantity")
    })
    List<WarehouseItem> findBySearch(@Param("limit") int limit,
                                 @Param("offset") int offset,
                                 @Param("sortByAsColumnName") String sortByAsColumnName,
                                 @Param("sortMethod") String sortMethod,
                                 @Param("searchKeyAsColumnName") String searchKeyAsColumnName,
                                 @Param("searchValueWithWildcard") String searchValueWithWildcard);


    // Get method to use when there are no search params
    // Use $ for string substitution in MyBatis query
    @Select("SELECT * FROM StockSync.WarehouseItems ORDER BY ${sortByAsColumnName} ${sortMethod} LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(property = "warehouseItemId", column = "ware_items_id"),
            @Result(property = "itemId",column = "item_id"),
            @Result(property = "warehouseId",column = "warehouse_id"),
            @Result(property = "quantity", column = "quantity")
    })
    List<WarehouseItem> findAll(@Param("limit") int limit,
                            @Param("offset") int offset,
                            @Param("sortByAsColumnName") String sortByAsColumnName,
                            @Param("sortMethod") String sortMethod);

    @Options(useGeneratedKeys = true, keyProperty = "warehouseItemId")
    @Insert("INSERT INTO StockSync.WarehouseItems (warehouse_id,item_id,quantity) Values (#{newWI.warehouseId},#{newWI.itemId},#{newWI.quantity})")
    void insertWarehouseItem(@Param("newWI") WarehouseItem newWI);

    @Select("SELECT COUNT(*) FROM StockSync.WarehouseItems")
    int getTotalNumEntries();

    @Select("SELECT COUNT(*) FROM StockSync.WarehouseItems WHERE ${searchKeyAsColumnName} LIKE '${searchValueWithWildcard}'")
    int getSearchNumEntries(@Param("searchKeyAsColumnName") String searchKeyAsColumnName,
                            @Param("searchValueWithWildcard") String searchValueWithWildcard);

    @Delete("DELETE FROM StockSync.WarehouseItems WHERE ware_items_id = #{warehouseItemId}")
    void deleteWarehouseItem(@Param("warehouseItemId") int warehouseItemId);

    @Update("UPDATE StockSync.WarehouseItems SET warehouse_id = #{updateWI.warehouseId}, item_id = #{updateWI.itemId}, quantity = #{updateWI.quantity} WHERE (ware_items_id = #{updateWI.warehouseItemId})")
    void updateWarehouseItem(@Param("updateWI") WarehouseItem updateWI);

    @Update("UPDATE StockSync.WarehouseItems SET quantity = quantity - #{quantity} WHERE warehouse_id = #{warehouseId} AND item_id = #{itemId}")
    void subtractQuantity(@Param("quantity") int quantity,
                        @Param("warehouseId") int warehouseId,
                        @Param("itemId") int itemId);

    @Update("UPDATE StockSync.WarehouseItems SET quantity = quantity + #{quantity} WHERE warehouse_id = #{warehouseId} AND item_id = #{itemId}")
    void addQuantity(@Param("quantity") int quantity,
                          @Param("warehouseId") int warehouseId,
                          @Param("itemId") int itemId);

    @Select("SELECT EXISTS(SELECT 1 FROM StockSync.WarehouseItems WHERE warehouse_id = #{warehouseId} AND item_id = #{itemId})")
    boolean hasItem(@Param("warehouseId") int warehouseId,
                @Param("itemId") int itemId);

}