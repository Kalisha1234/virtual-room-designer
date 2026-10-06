package com.roomdesigner.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class FurnitureService {
    private static final List<Map<String, Object>> CATALOG = List.of(
        item("sofa", "Sofa", 2.2, 0.95, "#d27757"),
        item("bed", "Bed", 2.0, 1.6, "#758aa0"),
        item("chair", "Chair", 0.75, 0.75, "#cfaa63"),
        item("table", "Table", 1.3, 0.9, "#a87950"),
        item("desk", "Desk", 1.5, 0.7, "#9b795d"),
        item("wardrobe", "Wardrobe", 1.2, 0.65, "#b78358"),
        item("lamp", "Lamp", 0.45, 0.45, "#dfbd64"),
        item("plant", "Plant", 0.65, 0.65, "#668566"),
        item("bookshelf", "Bookshelf", 1.4, 0.35, "#795d49")
    );

    public List<Map<String, Object>> getCatalog() {
        return CATALOG;
    }

    public Map<String, Object> getCatalogItem(String id) {
        return CATALOG.stream().filter(item -> item.get("id").equals(id)).findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Furniture type not found"));
    }

    private static Map<String, Object> item(String id, String name, double width, double length, String color) {
        return Map.of("id", id, "name", name, "width", width, "length", length, "color", color);
    }
}
