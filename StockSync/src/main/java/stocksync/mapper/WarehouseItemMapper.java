package stocksync.mapper;

import stocksync.model.Warehouse;
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
    List<Warehouse> findBySearch(@Param("limit") int limit,
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
    List<Warehouse> findAll(@Param("limit") int limit,
                            @Param("offset") int offset,
                            @Param("sortByAsColumnName") String sortByAsColumnName,
                            @Param("sortMethod") String sortMethod);

    @Insert("INSERT INTO StockSync.WarehouseItems (ware_items_id,warehouse_id,item_id,quantity) Values (#{newWI.warehouseItemId},#{newWI.warehouseId},#{newWI.itemId},#{newWI.quantity})")
    void insertWarehouseItem(@Param("newWI") Warehouse newWh);

    @Select("SELECT COUNT(*) FROM StockSync.WarehouseItems")
    int getTotalNumEntries();

    @Select("SELECT COUNT(*) FROM StockSync.WarehouseItems WHERE ${searchKeyAsColumnName} LIKE '${searchValueWithWildcard}'")
    int getSearchNumEntries(@Param("searchKeyAsColumnName") String searchKeyAsColumnName,
                            @Param("searchValueWithWildcard") String searchValueWithWildcard);

    @Delete("DELETE FROM StockSync.WarehouseItems WHERE ware_items_id = #{warehouseItemId}")
    void deleteWarehouseItem(@Param("warehouseItemId") int warehouseItemId);

    @Update("UPDATE StockSync.WarehouseItems SET warehouse_id = #{updateWI.warehouseId}, item_id = #{updateWI.itemId}, quantity = #{updateWI.quantity} WHERE (ware_items_id = #{updateWI.warehouseItemId})")
    void updateWarehouseItem(@Param("updateWI") WarehouseItem updateWI);
}