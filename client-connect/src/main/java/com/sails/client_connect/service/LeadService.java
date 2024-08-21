package com.sails.client_connect.service;

import com.sails.client_connect.dto.LeadDTO;
import com.sails.client_connect.entity.Lead;
import com.sails.client_connect.mapper.LeadMapper;
import com.sails.client_connect.repository.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LeadService {

    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;

    public LeadService(LeadRepository leadRepository, LeadMapper leadMapper) {
        this.leadRepository = leadRepository;
        this.leadMapper = leadMapper;
    }

    public LeadDTO createLead(LeadDTO leadDTO) {
        var lead = leadMapper.toEntity(leadDTO);
        var savedLead = leadRepository.save(lead);
        return leadMapper.toDto(savedLead);
    }

    public LeadDTO getLeadById(Long id) {
        return leadRepository.findById(id)
                .map(leadMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Lead not found with id " + id));
    }

    public List<LeadDTO> getAllLeads() {
        return leadRepository.findAll().stream()
                .map(leadMapper::toDto)
                .collect(Collectors.toList());
    }

    public LeadDTO updateLead(Long id, LeadDTO leadDTO) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id " + id));
        // Update the lead's fields
        lead.setFirstName(leadDTO.getFirstName());
        lead.setLastName(leadDTO.getLastName());
        lead.setGender(leadDTO.getGender());
        lead.setEmail(leadDTO.getEmail());
        lead.setPhoneNumber(leadDTO.getPhoneNumber());
        var updatedLead = leadRepository.save(lead);
        return leadMapper.toDto(updatedLead);
    }

    public void deleteLead(Long id) {
        if (!leadRepository.existsById(id)) {
            throw new RuntimeException("Lead not found with id " + id);
        }
        leadRepository.deleteById(id);
    }
}
