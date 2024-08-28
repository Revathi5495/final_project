package com.sails.client_connect.service;

import com.sails.client_connect.dto.RoleDTO;
import com.sails.client_connect.dto.RoleUpdateDto;
import com.sails.client_connect.dto.UserRoleUpdateDto;
import com.sails.client_connect.entity.Role;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.mapper.RoleMapper;
import com.sails.client_connect.mapper.UserMapper;
import com.sails.client_connect.repository.RoleRepository;
import com.sails.client_connect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    public UserRoleUpdateDto updateUserRoles(int userId, Set<RoleUpdateDto> roleUpdateDtos) {

        User user = userRepository.findUserWithRolesById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));


        Set<Role> roles = roleUpdateDtos.stream()
                .map(roleDTO -> roleRepository.findByName(roleDTO.getName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleDTO.getName())))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        User updatedUser = userRepository.save(user);

        return userMapper.toUpdateDto(updatedUser);
    }

    public void deleteRole(Long id) throws RoleNotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));


        for (User user : role.getUsers()) {
            user.getRoles().remove(role);
            userRepository.save(user);
        }

        roleRepository.deleteById(id);
    }

    public RoleDTO getRoleById(Long id) throws RoleNotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
        return roleMapper.toDto(role);
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }
}

