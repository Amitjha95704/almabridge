package com.almabridge.service.institution;

import com.almabridge.dto.institution.InstitutionRequest;
import com.almabridge.dto.institution.InstitutionResponse;

import java.util.List;

public interface InstitutionService {

    InstitutionResponse createInstitution(
            InstitutionRequest request
    );

    InstitutionResponse getInstitutionById(Long id);

    List<InstitutionResponse> getAllInstitutions();

}