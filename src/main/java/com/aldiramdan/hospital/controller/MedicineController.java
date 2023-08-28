package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.model.dto.request.MedicineRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.service.MedicineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Medicine", description = "Endpoint for medicine operations")
@RestController
@RequestMapping("/medicine")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;

    private ResponseData responseData;

    @GetMapping
    public ResponseEntity<ResponseData> getAll() {
        responseData = medicineService.getAll();
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getById(@PathVariable String id) throws Exception {
        responseData = medicineService.getById(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData> getByName(@RequestParam String name) {
        responseData = medicineService.getByName(name);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponseData> add(@Valid @RequestBody MedicineRequest request) throws Exception {
        responseData = medicineService.add(request);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> update(@PathVariable String id, @Valid @RequestBody MedicineRequest request) throws Exception {
        responseData = medicineService.update(id, request);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> delete(@PathVariable String id) throws Exception {
        responseData = medicineService.delete(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData> recovery(@PathVariable String id) throws Exception {
        responseData = medicineService.recovery(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }
}
