package stocksync.mapper;

import stocksync.model.Warehouse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WarehouseMapper {
    @Select("SELECT * FROM StockSync.Warehouse LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(property = "warehouseId", column = "warehouse_id"),
            @Result(property = "warehouseName",column = "warehouse_name"),
            @Result(property = "warehouseAddress",column = "warehouse_address"),
	        @Result(property = "warehouseLong", column = "warehouse_long"),
	        @Result(property = "warehouseLat", column = "warehouse_lat")
    })
    List<Warehouse> find(int limit, int offset);

    @Insert("INSERT INTO StockSync.Warehouse (warehouse_id,warehouse_name,warehouse_address,warehouse_long,warehouse_lat) Values (#{newWh.warehouseId},#{newWh.warehouseName},#{newWh.warehouseAddress},#{newWh.warehouseLong},#{newWh.warehouseLat})")
    void insertWarehouse(@Param("newWh") Warehouse newWh);

    @Select("SELECT COUNT(*) FROM StockSync.Warehouse")
    int getTotalNumEntries();

    @Delete("DELETE FROM StockSync.Warehouse WHERE warehouse_id = #{deleteWh.warehouseId}")
    void deleteWarehoues(@Param("deleteWh") Warehouse deleteWh);

    @Update("UPDATE StockSync.Warehouse SET warehouse_name = #{updateWh.warehouseName}, warehouse_address = #{updateWh.warehouseAddress}, warehouse_long = #{updateWh.warehouseLong}, warehouse_lat = #{updateWh.warehouseLat} WHERE (warehouse_id = #{updateWh.warehouseId})")
    void updateWarehouse(@Param("updateWh") Warehouse updateWh);
}
