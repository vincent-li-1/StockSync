package stocksync.controller;
import org.apache.ibatis.annotations.Delete;
import stocksync.mapper.WarehouseMapper;
import stocksync.model.Warehouse;
import stocksync.model.ItemDetailsDTO;
import stocksync.service.WarehouseService;
import stocksync.model.WarehouseItem;
import stocksync.service.WarehouseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.lang.Math;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "homepage"; 
    }
}
