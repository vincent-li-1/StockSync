package stocksync.mapper;

import org.apache.ibatis.annotations.*;
import stocksync.model.Shipment;
import stocksync.model.Warehouse;

import java.util.List;

@Mapper
public interface ShipmentMapper {

    /**
     * Mapper of selecting all shipment with specified sorting and displaying rules
     *
     * @param limit number of entries for selecting
     * @param offset number of entries jumped before selecting
     * @param sortByAsColumnName name of the column that is used for ordering
     * @param sortMethod increasing order or decreasing order
     *
     * @return A List of shipments
     */
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

    /**
     * Mapper of selecting shipment with specified key value and sorting and displaying rules
     *
     * @param limit number of entries for selecting
     * @param offset number of entries jumped before selecting
     * @param sortByAsColumnName name of the column that is used for ordering
     * @param sortMethod increasing order or decreasing order
     * @param searchKeyAsColumnName name of the column that is used for searching
     * @param searchValueWithWildcard value of the column being searched with wildcard
     * @return A list of shipment that satisfy the search value
     */
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

    /**
     * Mapper of getting the count of all shipment
     * @return an integer representing the total number of entries
     */
    @Select("SELECT COUNT(*) FROM StockSync.Shipment")
    int getTotalNumEntries();

    /**
     * Mapper of getting the count of shipment with specified key value
     * @return an integer representing the total number of entries satisfy the search value
     */
    @Select("SELECT COUNT(*) FROM StockSync.Shipment WHERE ${searchKeyAsColumnName} LIKE '${searchValueWithWildcard}'")
    int getSearchNumEntries(@Param("searchKeyAsColumnName") String searchKeyAsColumnName,
                            @Param("searchValueWithWildcard") String searchValueWithWildcard);

    /**
     * Mapper of deleting a shipment with specific shipment id
     * @param shipmentId the shipment id of the shipment to delete
     */
    @Delete("DELETE FROM StockSync.Shipment WHERE shipment_id = #{shipmentId}")
    void deleteShipment(@Param("shipmentId") int shipmentId);

    /**
     * Mapper of inserting a shipment
     * @param newShipment the shipment to insert
     */
    @Options(useGeneratedKeys = true, keyProperty = "shipmentId")
    @Insert("INSERT INTO StockSync.Shipment (shipment_orig,shipment_dst) Values (#{newShipment.warehouseFromId},#{newShipment.warehouseToId})")
    void insertShipment(@Param("newShipment") Shipment newShipment);

    /**
     * Mapper of updating an existing shipment
     * @param updateShipment the shipment to update
     */
    @Update("UPDATE StockSync.Shipment SET shipment_orig = #{updateShipment.warehouseFromId}, shipment_dst = #{updateShipment.warehouseToId} WHERE (shipment_id = #{updateShipment.shipmentId})")
    void updateShipment(@Param("updateShipment") Shipment updateShipment);
}
