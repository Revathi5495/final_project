package com.sails.client_connect.service;

import com.sails.client_connect.dto.LeadDTO;
import com.sails.client_connect.entity.Lead;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.exception.UserNotFoundException;
import com.sails.client_connect.mapper.LeadMapper;
import com.sails.client_connect.repository.LeadRepository;
import com.sails.client_connect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeadService {

    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;
    private final UserRepository userRepository;


    public LeadDTO createLead(LeadDTO leadDTO) {
        Lead lead = leadMapper.toEntity(leadDTO);
        User user = userRepository.findById(leadDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        lead.setUser(user);
        Lead savedLead = leadRepository.save(lead);
        return leadMapper.toDto(savedLead);
    }

    public LeadDTO getLeadByIdAndUserId(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return leadRepository.findByIdAndUser(id, user)
                .map(leadMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Lead not found with id " + id));
    }

    public List<LeadDTO> getAllLeadsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return leadRepository.findAllByUser(user).stream()
                .map(leadMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<LeadDTO> getAllLeads() {
        return leadRepository.findAll().stream()
                .map(leadMapper::toDto)
                .collect(Collectors.toList());
    }

    public LeadDTO updateLead(Long id, LeadDTO leadDTO) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id " + id));

        // Update the lead's fields if they are present in the DTO
        if (leadDTO.getFirstName() != null) {
            lead.setFirstName(leadDTO.getFirstName());
        }
        if (leadDTO.getLastName() != null) {
            lead.setLastName(leadDTO.getLastName());
        }
        if (leadDTO.getEmail() != null) {
            lead.setEmail(leadDTO.getEmail());
        }
        if (leadDTO.getPhoneNumber() != null) {
            lead.setPhoneNumber(leadDTO.getPhoneNumber());
        }

        // Update the user if userId is present in the DTO
        if (leadDTO.getUserId() != null) {
            User user = userRepository.findById(leadDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id " + leadDTO.getUserId()));
            lead.setUser(user);
        }

        Lead updatedLead = leadRepository.save(lead);
        return leadMapper.toDto(updatedLead);
    }

    public void deleteLead(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Lead lead = leadRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Lead not found with id " + id + " for user " + userId));
        leadRepository.delete(lead);
    }
}
