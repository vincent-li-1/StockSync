package com.example.mapper;

import com.example.model.Warehouse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WarehouseMapper {
    @Select("SELECT * FROM StockSync.Warehouse")
    @Results({
            @Result(property = "warehouseId", column = "warehouse_id"),
            @Result(property = "warehouseName",column = "warehouse_name"),
            @Result(property = "warehouseAddress",column = "warehouse_address")
    })
    List<Warehouse> findAll();

    @Insert("INSERT INTO StockSync.Warehouse (warehouse_id,warehouse_name,warehouse_address) Values (#{newWh.id},#{newWh.name},#{newWh.address})")
    void insertWarehouse(@Param("newWh") Warehouse newWh);
}
