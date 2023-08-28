package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.model.dto.request.DoctorRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.service.DoctorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Doctor", description = "Endpoint for doctor operations")
@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    private ResponseData responseData;

    @GetMapping
    public ResponseEntity<ResponseData> getAll() {
        responseData = doctorService.getAll();
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getById(@PathVariable String id) throws Exception {
        responseData = doctorService.getById(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData> getByName(@RequestParam String name) {
        responseData = doctorService.getByName(name);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponseData> add(@Valid @RequestBody DoctorRequest request) throws Exception {
        responseData = doctorService.add(request);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> update(@PathVariable String id, @Valid @RequestBody DoctorRequest request) throws Exception {
        responseData = doctorService.update(id, request);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> delete(@PathVariable String id) throws Exception {
        responseData = doctorService.delete(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData> recovery(@PathVariable String id) throws Exception {
        responseData = doctorService.recovery(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }
}
