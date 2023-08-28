package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.model.dto.request.TreatmentRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.service.TreatmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Treatment", description = "Endpoint for treatment operations")
@RestController
@RequestMapping("/treatment")
public class TreatmentController {
    @Autowired
    private TreatmentService treatmentService;

    private ResponseData responseData;

    @GetMapping
    public ResponseEntity<ResponseData> getAll() {
        responseData = treatmentService.getAll();
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getById(@PathVariable String id) throws Exception {
        responseData = treatmentService.getById(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData> getByPatientName(@RequestParam String name) {
        responseData = treatmentService.getByPatientName(name);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponseData> add(@Valid @RequestBody TreatmentRequest request) throws Exception {
        responseData = treatmentService.add(request);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> update(@PathVariable String id, @Valid @RequestBody TreatmentRequest request) throws Exception {
        responseData = treatmentService.update(id, request);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> delete(@PathVariable String id) throws Exception {
        responseData = treatmentService.delete(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData> recovery(@PathVariable String id) throws Exception {
        responseData = treatmentService.recovery(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }
}
