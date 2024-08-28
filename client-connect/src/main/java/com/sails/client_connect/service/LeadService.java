package com.sails.client_connect.service;

import com.sails.client_connect.dto.LeadDTO;
import com.sails.client_connect.entity.Lead;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.mapper.LeadMapper;
import com.sails.client_connect.repository.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//@Transactional
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;

    public LeadDTO createLead(LeadDTO leadDTO) {
        var lead = leadMapper.toEntity(leadDTO);
        var savedLead = leadRepository.save(lead);
        return leadMapper.toDto(savedLead);
    }

    public LeadDTO getLeadById(Long id) {
        return leadRepository.findById(id)
                .map(leadMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("Lead not found with id " + id));
    }

    public List<LeadDTO> getAllLeads() {
        return leadRepository.findAll().stream()
                .map(leadMapper::toDto)
                .collect(Collectors.toList());
    }

    public LeadDTO updateLead(Long id, LeadDTO leadDTO) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Lead not found with id " + id));
        // Update the lead's fields if they are present in the DTO
        if (leadDTO.getFirstName() != null) {
            lead.setFirstName(leadDTO.getFirstName());
        }
        if (leadDTO.getLastName() != null) {
            lead.setLastName(leadDTO.getLastName());
        }
        if (leadDTO.getGender() != null) {
            lead.setGender(leadDTO.getGender());
        }
        if (leadDTO.getEmail() != null) {
            lead.setEmail(leadDTO.getEmail());
        }
        if (leadDTO.getPhoneNumber() != null) {
            lead.setPhoneNumber(leadDTO.getPhoneNumber());
        }
        var updatedLead = leadRepository.save(lead);
        return leadMapper.toDto(updatedLead);
    }

    public void deleteLead(Long id) {
        if (!leadRepository.existsById(id)) {
            throw new UserNotFoundException("Lead not found with id " + id);
        }
        leadRepository.deleteById(id);
    }
}
