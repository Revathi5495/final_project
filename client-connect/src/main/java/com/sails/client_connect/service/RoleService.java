package com.sails.client_connect.service;

import com.sails.client_connect.dto.RoleDto;
import com.sails.client_connect.entity.Role;
import com.sails.client_connect.entity.RoleName;
import com.sails.client_connect.exception.RoleNotFoundException;
import com.sails.client_connect.mapper.RoleMapper;
import com.sails.client_connect.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDto createRole(RoleDto roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        System.out.println("Role created successfully:");
        Role savedRole = roleRepository.save(role);
        System.out.println("Role created successfully:");
        return roleMapper.toDto(savedRole);
    }

    public RoleDto updateRole(Long id, RoleDto roleDTO) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        existingRole.setName(RoleName.valueOf(roleDTO.getName().name()));  // Handling enum conversion
        System.out.println(" successfully:"+existingRole);
        Role updatedRole = roleRepository.save(existingRole);
        return roleMapper.toDto(updatedRole);
    }

    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RoleNotFoundException("Role not found");
        }
        roleRepository.deleteById(id);
    }

    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
        return roleMapper.toDto(role);
    }

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }
}
