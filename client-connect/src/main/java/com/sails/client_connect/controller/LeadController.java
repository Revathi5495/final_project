package com.sails.client_connect.controller;

import com.sails.client_connect.dto.LeadDTO;
import com.sails.client_connect.response.ApiResponse;
import com.sails.client_connect.service.LeadService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/leads")
public class LeadController {

    private final LeadService leadService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<LeadDTO>> createLead(
            @Valid @RequestBody LeadDTO leadDTO,
            HttpSession session) {

        // Retrieve the user_id from the session
        Long userId = (Long) session.getAttribute("userId");

        // Set the user_id in the leadDTO
        leadDTO.setUserId(userId);

        LeadDTO createdLead = leadService.createLead(leadDTO);
        ApiResponse<LeadDTO> response = new ApiResponse<>(
                "Lead created successfully",
                HttpStatus.CREATED,
                createdLead
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadDTO> getLeadById(
            @PathVariable Long id,
            HttpSession session) {

        // Retrieve the user_id from the session
        Long userId = (Long) session.getAttribute("userId");

        LeadDTO leadDTO = leadService.getLeadByIdAndUserId(id, userId);
        return new ResponseEntity<>(leadDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LeadDTO>> getAllLeads(HttpSession session) {

        // Retrieve the user_id from the session
        Long userId = (Long) session.getAttribute("userId");

        List<LeadDTO> leads = leadService.getAllLeadsByUserId(userId);
        return new ResponseEntity<>(leads, HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApiResponse<LeadDTO>> updateLead(
            @PathVariable Long id,
            @RequestBody LeadDTO leadDTO,
            HttpSession session) {

        // Retrieve the user_id from the session
        Long userId = (Long) session.getAttribute("userId");
        leadDTO.setUserId(userId);

        LeadDTO updatedLead = leadService.updateLead(id, leadDTO);
        ApiResponse<LeadDTO> response = new ApiResponse<>(
                "Lead updated successfully",
                HttpStatus.OK,
                updatedLead
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLead(
            @PathVariable Long id,
            HttpSession session) {

        // Retrieve the user_id from the session
        Long userId = (Long) session.getAttribute("userId");

        leadService.deleteLead(id, userId);
        ApiResponse<Void> response = new ApiResponse<>(
                "Lead deleted successfully",
                HttpStatus.OK,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
