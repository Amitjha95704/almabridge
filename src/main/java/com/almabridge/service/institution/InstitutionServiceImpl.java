package com.almabridge.service.institution;

import com.almabridge.dto.institution.InstitutionRequest;
import com.almabridge.dto.institution.InstitutionResponse;

import com.almabridge.entity.Institution;

import com.almabridge.exception.DuplicateResourceException;
import com.almabridge.exception.ResourceNotFoundException;

import com.almabridge.repository.InstitutionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionRepository institutionRepository;

    @Override
    public InstitutionResponse createInstitution(
            InstitutionRequest request) {

        String code = request.institutionCode()
                .trim()
                .toUpperCase();

        if (institutionRepository.existsByInstitutionCode(code)) {
            throw new DuplicateResourceException(
                    "Institution code already exists"
            );
        }

        Institution institution = new Institution();

        institution.setInstitutionName(
                request.institutionName().trim()
        );

        institution.setInstitutionCode(code);

        institution.setCity(request.city());
        institution.setState(request.state());

        institution.setActive(true);

        Institution saved =
                institutionRepository.save(institution);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InstitutionResponse getInstitutionById(Long id) {

        Institution institution = institutionRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Institution not found with ID: " + id
                        )
                );

        return mapToResponse(institution);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstitutionResponse> getAllInstitutions() {

        return institutionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private InstitutionResponse mapToResponse(
            Institution institution) {

        return new InstitutionResponse(
                institution.getId(),
                institution.getInstitutionName(),
                institution.getInstitutionCode(),
                institution.getCity(),
                institution.getState(),
                institution.getActive()
        );
    }
}