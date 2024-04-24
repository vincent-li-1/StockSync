package stocksync.mapper;

import org.apache.ibatis.annotations.*;
import stocksync.model.Shipment;
import stocksync.model.Warehouse;

import java.util.List;

@Mapper
public interface ShipmentMapper {

    @Select("SELECT * FROM StockSync.Shipment ORDER BY ${sortByAsColumnName} ${sortMethod} LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(property = "shipmentId", column = "shipment_id"),
            @Result(property = "warehouseFromId",column = "warehouse_from_id"),
            @Result(property = "warehouseToId",column = "warehouse_to_id"),
            @Result(property = "shipmentDate", column = "shipment_date"),
            @Result(property = "shipmentStatus", column = "shipment_status")
    })
    List<Shipment> findAll(@Param("limit") int limit,
                           @Param("offset") int offset,
                           @Param("sortByAsColumnName") String sortByAsColumnName,
                           @Param("sortMethod") String sortMethod);

    @Select("SELECT * FROM StockSync.Shipment WHERE ${searchKeyAsColumnName} LIKE '${searchValueWithWildcard}' ORDER BY ${sortByAsColumnName} ${sortMethod} LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(property = "shipmentId", column = "shipment_id"),
            @Result(property = "warehouseFromId",column = "warehouse_from_id"),
            @Result(property = "warehouseToId",column = "warehouse_to_id"),
            @Result(property = "shipmentDate", column = "shipment_date"),
            @Result(property = "shipmentStatus", column = "shipment_status")
    })
    List<Shipment> findBySearch(@Param("limit") int limit,
                                 @Param("offset") int offset,
                                 @Param("sortByAsColumnName") String sortByAsColumnName,
                                 @Param("sortMethod") String sortMethod,
                                 @Param("searchKeyAsColumnName") String searchKeyAsColumnName,
                                 @Param("searchValueWithWildcard") String searchValueWithWildcard);

    @Select("SELECT COUNT(*) FROM StockSync.Shipment")
    int getTotalNumEntries();

    @Select("SELECT COUNT(*) FROM StockSync.Shipment WHERE ${searchKeyAsColumnName} LIKE '${searchValueWithWildcard}'")
    int getSearchNumEntries(@Param("searchKeyAsColumnName") String searchKeyAsColumnName,
                            @Param("searchValueWithWildcard") String searchValueWithWildcard);

    @Delete("DELETE FROM StockSync.Shipment WHERE shipment_id = #{shipmentId}")
    void deleteShipment(@Param("shipmentId") int shipmentId);

    @Insert("INSERT INTO StockSync.Shipment (shipment_id,warehouse_from_id,warehouse_to_id,shipment_data,shipment_status) Values (#{newShipment.shipmentId},#{newShipment.warehouseFromId},#{newShipment.warehouseToId},#{newShipment.shipmentDate},#{newShipment.shipmentStatus})")
    void insertShipment(@Param("newShipment") Shipment newShipment);

    @Update("UPDATE StockSync.Shipment SET shipment_status = #{updateShipment.shipmentStatus}, warehouse_from_id = #{updateShipment.warehouseFromId}, warehouse_to_id = #{updateShipment.warehouseToId}, shipment_date = #{updateShipment.shipmentDate} WHERE (shipment_id = #{updateShipment.shipmentId})")
    void updateShipment(@Param("updateShipment") Shipment updateShipment);
}
