package com.sails.client_connect.controller;

import com.sails.client_connect.dto.LeadDTO;
import com.sails.client_connect.response.ApiResponse;
import com.sails.client_connect.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadService leadService;

    @Autowired
    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping("/create-lead")
    public ResponseEntity<ApiResponse<LeadDTO>> createLead(@Valid @RequestBody LeadDTO leadDTO) {
        LeadDTO createdLead = leadService.createLead(leadDTO);
        ApiResponse<LeadDTO> response = new ApiResponse<>(
                "Lead created successfully",
                HttpStatus.CREATED,
                createdLead
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<LeadDTO> getLeadById(@PathVariable Long id) {
        LeadDTO leadDTO = leadService.getLeadById(id);
        return new ResponseEntity<>(leadDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LeadDTO>> getAllLeads() {
        List<LeadDTO> leads = leadService.getAllLeads();
        return new ResponseEntity<>(leads, HttpStatus.OK);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<LeadDTO>> updateLead(
            @PathVariable Long id, @Valid @RequestBody LeadDTO leadDTO) {
        LeadDTO updatedLead = leadService.updateLead(id, leadDTO);
        ApiResponse<LeadDTO> response = new ApiResponse<>(
                "Lead updated successfully",
                HttpStatus.OK,
                updatedLead
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        ApiResponse<Void> response = new ApiResponse<>(
                "Lead deleted successfully",
                HttpStatus.OK,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
