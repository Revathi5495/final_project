package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.LeadDTO;
import com.sails.client_connect.entity.Lead;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeadMapper {

    LeadDTO toDto(Lead lead);

    Lead toEntity(LeadDTO leadDTO);
}
