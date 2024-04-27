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
            @Result(property = "warehouseFromId",column = "shipment_orig"),
            @Result(property = "warehouseToId",column = "shipment_dst"),
    })
    List<Shipment> findAll(@Param("limit") int limit,
                           @Param("offset") int offset,
                           @Param("sortByAsColumnName") String sortByAsColumnName,
                           @Param("sortMethod") String sortMethod);

    @Select("SELECT * FROM StockSync.Shipment WHERE ${searchKeyAsColumnName} LIKE '${searchValueWithWildcard}' ORDER BY ${sortByAsColumnName} ${sortMethod} LIMIT #{limit} OFFSET #{offset}")
    @Results({
            @Result(property = "shipmentId", column = "shipment_id"),
            @Result(property = "warehouseFromId",column = "shipment_orig"),
            @Result(property = "warehouseToId",column = "shipment_dst"),
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
    @Options(useGeneratedKeys = true, keyProperty = "shipmentId")
    @Insert("INSERT INTO StockSync.Shipment (shipment_orig,shipment_dst) Values (#{newShipment.warehouseFromId},#{newShipment.warehouseToId})")
    void insertShipment(@Param("newShipment") Shipment newShipment);

    @Update("UPDATE StockSync.Shipment SET shipment_orig = #{updateShipment.warehouseFromId}, shipment_dst = #{updateShipment.warehouseToId} WHERE (shipment_id = #{updateShipment.shipmentId})")
    void updateShipment(@Param("updateShipment") Shipment updateShipment);
}
