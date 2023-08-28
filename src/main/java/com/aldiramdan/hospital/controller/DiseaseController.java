package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.model.dto.request.DiseaseRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.service.DiseaseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Disease", description = "Endpoint for disease operations")
@RestController
@RequestMapping("/disease")
public class DiseaseController {
    @Autowired
    private DiseaseService diseaseService;

    private ResponseData responseData;

    @GetMapping
    public ResponseEntity<ResponseData> getAll() {
        responseData = diseaseService.getAll();
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getById(@PathVariable String id) throws Exception {
        responseData = diseaseService.getById(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData> getByName(@RequestParam String name) {
        responseData = diseaseService.getByName(name);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponseData> add(@Valid @RequestBody DiseaseRequest request) throws Exception {
        responseData = diseaseService.add(request);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> update(@PathVariable String id, @Valid @RequestBody DiseaseRequest request) throws Exception {
        responseData = diseaseService.update(id, request);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> delete(@PathVariable String id) throws Exception {
        responseData = diseaseService.delete(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData> recovery(@PathVariable String id) throws Exception {
        responseData = diseaseService.recovery(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }
}
