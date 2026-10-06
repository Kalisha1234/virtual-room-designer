package com.roomdesigner.controller;

import com.roomdesigner.service.FurnitureService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/furniture")
public class FurnitureController {
    private final FurnitureService furnitureService;

    public FurnitureController(FurnitureService furnitureService) {
        this.furnitureService = furnitureService;
    }

    @GetMapping
    public List<Map<String, Object>> getCatalog() {
        return furnitureService.getCatalog();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getCatalogItem(@PathVariable String id) {
        return furnitureService.getCatalogItem(id);
    }
}
