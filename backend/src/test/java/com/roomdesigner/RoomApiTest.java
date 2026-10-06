package com.roomdesigner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RoomApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void roomCrudAndRoomFurnitureLifecycleWorks() throws Exception {
        String roomResponse = mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"Studio","width":5,"length":4,"floorColor":"#d9c9aa","wallColor":"#f6f1e8"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Studio"))
            .andReturn().getResponse().getContentAsString();
        String roomId = com.jayway.jsonpath.JsonPath.read(roomResponse, "$.id").toString();

        mockMvc.perform(get("/api/rooms/{id}", roomId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Studio"))
            .andExpect(jsonPath("$.furniture").doesNotExist());

        String furnitureResponse = mockMvc.perform(post("/api/rooms/{roomId}/furniture", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"catalogId":"sofa","x":1,"y":1,"rotation":0,"color":"#d27757"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Sofa"))
            .andReturn().getResponse().getContentAsString();
        String furnitureId = com.jayway.jsonpath.JsonPath.read(furnitureResponse, "$.id").toString();

        mockMvc.perform(get("/api/rooms/{roomId}/furniture", roomId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].catalogId").value("sofa"))
            .andExpect(jsonPath("$[0].name").value("Sofa"));

        mockMvc.perform(put("/api/rooms/{roomId}/furniture/{furnitureId}", roomId, furnitureId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"catalogId":"sofa","x":2,"y":1.5,"rotation":90,"color":"#c86f50"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rotation").value(90))
            .andExpect(jsonPath("$.x").value(2.0));

        mockMvc.perform(put("/api/rooms/{id}", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"Reading Room","width":6,"length":5,"floorColor":"#d9c9aa","wallColor":"#f6f1e8"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Reading Room"));

        mockMvc.perform(delete("/api/rooms/{roomId}/furniture/{furnitureId}", roomId, furnitureId))
            .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/rooms/{id}", roomId))
            .andExpect(status().isNoContent());
    }

    @Test
    void furnitureCatalogueAndNotFoundResponsesAreCorrect() throws Exception {
        mockMvc.perform(get("/api/furniture"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("sofa"));

        mockMvc.perform(get("/api/furniture/{id}", "bed"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Bed"));

        mockMvc.perform(get("/api/rooms/99999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Room not found"));

        mockMvc.perform(get("/api/rooms/99999/furniture"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Room not found"));

        mockMvc.perform(get("/api/furniture/unknown-type"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Furniture type not found"));
    }

    @Test
    void invalidPayloadsReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"","width":1,"length":40,"floorColor":"red","wallColor":"#ffffff"}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.fields").exists());

        mockMvc.perform(post("/api/rooms/99999/furniture")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"catalogId":"chair","x":1,"y":1,"rotation":0,"color":"#ff0000"}
                    """))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Room not found"));
    }
}
