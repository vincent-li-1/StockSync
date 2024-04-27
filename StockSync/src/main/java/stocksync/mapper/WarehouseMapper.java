package stocksync.mapper;

import stocksync.model.Warehouse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WarehouseMapper {
    // Get method to use when there are search params provided
    // Use $ for string substitution in MyBatis query. Search value string needs to be wrapped in ''
    @Select("SELECT * FROM StockSync.Warehouse WHERE ${searchKeyAsColumnName} LIKE '${searchValueWithWildcard}' ORDER BY ${sortByAsColumnName} ${sortMethod} LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(property = "warehouseId", column = "warehouse_id"),
            @Result(property = "warehouseName",column = "warehouse_name"),
            @Result(property = "warehouseAddress",column = "warehouse_address"),
	        @Result(property = "warehouseLong", column = "warehouse_long"),
	        @Result(property = "warehouseLat", column = "warehouse_lat")
    })
    List<Warehouse> findBySearch(@Param("limit") int limit, 
                            @Param("offset") int offset, 
                            @Param("sortByAsColumnName") String sortByAsColumnName, 
                            @Param("sortMethod") String sortMethod,
                            @Param("searchKeyAsColumnName") String searchKeyAsColumnName,
                            @Param("searchValueWithWildcard") String searchValueWithWildcard);
                            
    @Select("SELECT * FROM StockSync.Warehouse WHERE warehouse_id = #{id}")
    @Results({
        @Result(property = "warehouseId", column = "warehouse_id"),
        @Result(property = "warehouseName", column = "warehouse_name"),
        @Result(property = "warehouseAddress", column = "warehouse_address"),
        @Result(property = "warehouseLong", column = "warehouse_long"),
        @Result(property = "warehouseLat", column = "warehouse_lat")
    })
    Warehouse findWarehouseById(@Param("id") Long id);


    // Get method to use when there are no search params
    // Use $ for string substitution in MyBatis query
    @Select("SELECT * FROM StockSync.Warehouse ORDER BY ${sortByAsColumnName} ${sortMethod} LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(property = "warehouseId", column = "warehouse_id"),
            @Result(property = "warehouseName",column = "warehouse_name"),
            @Result(property = "warehouseAddress",column = "warehouse_address"),
            @Result(property = "warehouseLong", column = "warehouse_long"),
            @Result(property = "warehouseLat", column = "warehouse_lat")
    })
    List<Warehouse> findAll(@Param("limit") int limit, 
                        @Param("offset") int offset, 
                        @Param("sortByAsColumnName") String sortByAsColumnName, 
                        @Param("sortMethod") String sortMethod);

    @Insert("INSERT INTO StockSync.Warehouse (warehouse_id,warehouse_name,warehouse_address,warehouse_long,warehouse_lat) Values (#{newWh.warehouseId},#{newWh.warehouseName},#{newWh.warehouseAddress},#{newWh.warehouseLong},#{newWh.warehouseLat})")
    void insertWarehouse(@Param("newWh") Warehouse newWh);

    @Select("SELECT COUNT(*) FROM StockSync.Warehouse")
    int getTotalNumEntries();

    @Select("SELECT COUNT(*) FROM StockSync.Warehouse WHERE ${searchKeyAsColumnName} LIKE '${searchValueWithWildcard}'")
    int getSearchNumEntries(@Param("searchKeyAsColumnName") String searchKeyAsColumnName,
                        @Param("searchValueWithWildcard") String searchValueWithWildcard);

    @Delete("DELETE FROM StockSync.Warehouse WHERE warehouse_id = #{deleteWh.warehouseId}")
    void deleteWarehouse(@Param("deleteWh") Warehouse deleteWh);

    @Delete("DELETE FROM StockSync.Warehouse WHERE warehouse_id = #{warehouseId}")
    void deleteWarehouseButton(@Param("warehouseId") int warehouesId);

    @Update("UPDATE StockSync.Warehouse SET warehouse_name = #{updateWh.warehouseName}, warehouse_address = #{updateWh.warehouseAddress}, warehouse_long = #{updateWh.warehouseLong}, warehouse_lat = #{updateWh.warehouseLat} WHERE (warehouse_id = #{updateWh.warehouseId})")
    void updateWarehouse(@Param("updateWh") Warehouse updateWh);
}
