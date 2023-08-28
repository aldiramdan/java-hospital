package com.aldiramdan.hospital.controller;

import com.aldiramdan.hospital.model.dto.request.PatientRequest;
import com.aldiramdan.hospital.model.dto.response.ResponseData;
import com.aldiramdan.hospital.service.PatientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Patient", description = "Endpoint for patient operations")
@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    private ResponseData responseData;

    @GetMapping
    public ResponseEntity<ResponseData> getAll() {
        responseData = patientService.getAll();
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getById(@PathVariable String id) throws Exception {
        responseData = patientService.getById(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData> getByName(@RequestParam String name) {
        responseData = patientService.getByName(name);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponseData> add(@Valid @RequestBody PatientRequest request) throws Exception {
        responseData = patientService.add(request);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> update(@PathVariable String id, @Valid @RequestBody PatientRequest request) throws Exception {
        responseData = patientService.update(id, request);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> delete(@PathVariable String id) throws Exception {
        responseData = patientService.delete(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData> recovery(@PathVariable String id) throws Exception {
        responseData = patientService.recovery(id);
        return ResponseEntity.status(responseData.getCode()).body(responseData);
    }
}
