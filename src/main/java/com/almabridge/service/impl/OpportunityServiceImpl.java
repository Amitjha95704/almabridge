
package com.almabridge.service.impl;

import com.almabridge.dto.opportunity.CreateOpportunityRequest;
import com.almabridge.dto.opportunity.OpportunityResponse;
import com.almabridge.dto.opportunity.UpdateOpportunityRequest;
import com.almabridge.entity.Institution;
import com.almabridge.entity.Opportunity;
import com.almabridge.entity.User;
import com.almabridge.enums.OpportunityStatus;
import com.almabridge.enums.OpportunityType;
import com.almabridge.exception.ResourceNotFoundException;
import com.almabridge.repository.OpportunityRepository;
import com.almabridge.repository.UserRepository;
import com.almabridge.service.CurrentUserService;
import com.almabridge.service.OpportunityService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public OpportunityResponse createOpportunity(
            CreateOpportunityRequest request) {

        Long institutionId = getRequiredInstitutionId();

        User admin = userRepository.findById(currentUserService.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin user not found"));

        Opportunity opportunity = new Opportunity();

        opportunity.setInstitution(admin.getInstitution());
        opportunity.setCreatedBy(admin);

        applyRequest(opportunity, request);

        opportunity.setStatus(OpportunityStatus.DRAFT);

        Opportunity savedOpportunity =
                opportunityRepository.save(opportunity);

        return mapToResponse(savedOpportunity);
    }

    @Override
    @Transactional
    public OpportunityResponse updateOpportunity(
            Long opportunityId,
            UpdateOpportunityRequest request) {

        Long institutionId = getRequiredInstitutionId();

        Opportunity opportunity = getAdminOpportunity(
                opportunityId,
                institutionId
        );

        if (opportunity.getStatus() == OpportunityStatus.CLOSED) {
            throw new IllegalArgumentException(
                    "Closed opportunities cannot be updated");
        }

        applyRequest(opportunity, request);

        Opportunity updatedOpportunity =
                opportunityRepository.save(opportunity);

        return mapToResponse(updatedOpportunity);
    }

    @Override
    @Transactional
    public void deleteOpportunity(Long opportunityId) {

        Long institutionId = getRequiredInstitutionId();

        Opportunity opportunity = getAdminOpportunity(
                opportunityId,
                institutionId
        );

        opportunityRepository.delete(opportunity);
    }

    @Override
    @Transactional
    public OpportunityResponse publishOpportunity(Long opportunityId) {

        Long institutionId = getRequiredInstitutionId();

        Opportunity opportunity = getAdminOpportunity(
                opportunityId,
                institutionId
        );

        if (opportunity.getStatus() == OpportunityStatus.CLOSED) {
            throw new IllegalArgumentException(
                    "Closed opportunities cannot be published");
        }

        if (opportunity.getApplicationDeadline() != null
                && !opportunity.getApplicationDeadline()
                        .isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Cannot publish an opportunity with an expired deadline");
        }

        opportunity.setStatus(OpportunityStatus.PUBLISHED);

        Opportunity savedOpportunity =
                opportunityRepository.save(opportunity);

        return mapToResponse(savedOpportunity);
    }

    @Override
    @Transactional
    public OpportunityResponse closeOpportunity(Long opportunityId) {

        Long institutionId = getRequiredInstitutionId();

        Opportunity opportunity = getAdminOpportunity(
                opportunityId,
                institutionId
        );

        opportunity.setStatus(OpportunityStatus.CLOSED);

        Opportunity savedOpportunity =
                opportunityRepository.save(opportunity);

        return mapToResponse(savedOpportunity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityResponse> getAdminOpportunities(
            OpportunityStatus status,
            Pageable pageable) {

        Long institutionId = getRequiredInstitutionId();

        Page<Opportunity> opportunities;

        if (status == null) {
            opportunities = opportunityRepository
                    .findAllByInstitutionId(institutionId, pageable);
        } else {
            opportunities = opportunityRepository
                    .findAllByInstitutionIdAndStatus(
                            institutionId,
                            status,
                            pageable
                    );
        }

        return opportunities.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpportunityResponse> getStudentOpportunities(
            OpportunityType type,
            Pageable pageable) {

        Long institutionId = getRequiredInstitutionId();

        Page<Opportunity> opportunities;

        if (type == null) {
            opportunities = opportunityRepository
                    .findAllByInstitutionIdAndStatusOrderByCreatedAtDesc(
                            institutionId,
                            OpportunityStatus.PUBLISHED,
                            pageable
                    );
        } else {
            opportunities = opportunityRepository
                    .findAllByInstitutionIdAndStatusAndOpportunityTypeOrderByCreatedAtDesc(
                            institutionId,
                            OpportunityStatus.PUBLISHED,
                            type,
                            pageable
                    );
        }

        return opportunities.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OpportunityResponse getStudentOpportunityById(
            Long opportunityId) {

        Long institutionId = getRequiredInstitutionId();

        Opportunity opportunity = opportunityRepository
                .findByIdAndInstitutionId(opportunityId, institutionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Opportunity not found"));

        if (opportunity.getStatus() != OpportunityStatus.PUBLISHED) {
            throw new ResourceNotFoundException(
                    "Opportunity not found");
        }

        return mapToResponse(opportunity);
    }

    private Long getRequiredInstitutionId() {

        Long institutionId = currentUserService.getInstitutionId();

        if (institutionId == null) {
            throw new IllegalStateException(
                    "Your account is not associated with an institution");
        }

        return institutionId;
    }

    private Opportunity getAdminOpportunity(
            Long opportunityId,
            Long institutionId) {

        return opportunityRepository
                .findByIdAndInstitutionId(opportunityId, institutionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Opportunity not found"));
    }

    private void applyRequest(
            Opportunity opportunity,
            CreateOpportunityRequest request) {

        opportunity.setCompanyName(request.companyName().trim());
        opportunity.setJobTitle(request.jobTitle().trim());
        opportunity.setOpportunityType(request.opportunityType());
        opportunity.setDescription(request.description());
        opportunity.setEligibilityCriteria(request.eligibilityCriteria());
        opportunity.setLocation(request.location());
        opportunity.setWorkMode(request.workMode());
        opportunity.setSalary(request.salary());
        opportunity.setApplicationUrl(
                validateApplicationUrl(request.applicationUrl()));
        opportunity.setApplicationDeadline(
                request.applicationDeadline());

        validateDeadline(request.applicationDeadline());
    }

    private void applyRequest(
            Opportunity opportunity,
            UpdateOpportunityRequest request) {

        opportunity.setCompanyName(request.companyName().trim());
        opportunity.setJobTitle(request.jobTitle().trim());
        opportunity.setOpportunityType(request.opportunityType());
        opportunity.setDescription(request.description());
        opportunity.setEligibilityCriteria(request.eligibilityCriteria());
        opportunity.setLocation(request.location());
        opportunity.setWorkMode(request.workMode());
        opportunity.setSalary(request.salary());
        opportunity.setApplicationUrl(
                validateApplicationUrl(request.applicationUrl()));
        opportunity.setApplicationDeadline(
                request.applicationDeadline());

        validateDeadline(request.applicationDeadline());
    }

    private void validateDeadline(LocalDate deadline) {

        if (deadline != null && !deadline.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Application deadline must be a future date");
        }
    }

    private String validateApplicationUrl(String applicationUrl) {

        if (applicationUrl == null || applicationUrl.isBlank()) {
            return null;
        }

        try {
            URI uri = URI.create(applicationUrl.trim());

            String scheme = uri.getScheme();

            if (scheme == null
                    || !(scheme.equalsIgnoreCase("http")
                    || scheme.equalsIgnoreCase("https"))
                    || uri.getHost() == null) {

                throw new IllegalArgumentException(
                        "Application URL must be a valid HTTP or HTTPS URL");
            }

            return uri.toString();

        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Application URL must be a valid HTTP or HTTPS URL");
        }
    }

    private OpportunityResponse mapToResponse(
            Opportunity opportunity) {

        Institution institution = opportunity.getInstitution();
        User creator = opportunity.getCreatedBy();

        return new OpportunityResponse(
                opportunity.getId(),
                institution.getId(),
                opportunity.getCompanyName(),
                opportunity.getJobTitle(),
                opportunity.getOpportunityType(),
                opportunity.getDescription(),
                opportunity.getEligibilityCriteria(),
                opportunity.getLocation(),
                opportunity.getWorkMode(),
                opportunity.getSalary(),
                opportunity.getApplicationUrl(),
                opportunity.getApplicationDeadline(),
                opportunity.getStatus(),
                creator.getId(),
                creator.getFullName(),
                opportunity.getCreatedAt(),
                opportunity.getUpdatedAt()
        );
    }
}
