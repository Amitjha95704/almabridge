//
//package com.almabridge.controller;
//
//import com.almabridge.dto.auth.UserProfileResponse;
//import com.almabridge.service.InstitutionUserService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/admin/students")
//@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
//public class AdminStudentController {
//
//    private final InstitutionUserService institutionUserService;
//
//    @GetMapping
//    public List<UserProfileResponse> getAllStudents() {
//
//        return institutionUserService
//                .getStudentsOfCurrentInstitution();
//    }
//
//    @GetMapping("/{studentId}")
//    public UserProfileResponse getStudentById(
//            @PathVariable Long studentId) {
//
//        return institutionUserService.getStudentById(studentId);
//    }
//}



package com.almabridge.controller;

import com.almabridge.dto.admin.AdminStudentPageResponse;
import com.almabridge.dto.admin.AdminStudentResponse;
import com.almabridge.dto.admin.StudentStatusRequest;
import com.almabridge.service.admin.AdminStudentManagementService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/students")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminStudentController {

    private final AdminStudentManagementService studentService;

    // Get all students with pagination
    @GetMapping
    public ResponseEntity<AdminStudentPageResponse> getAllStudents(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                studentService.getStudents(keyword, page, size)
        );
    }

    // Search students
    @GetMapping("/search")
    public ResponseEntity<AdminStudentPageResponse> searchStudents(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                studentService.getStudents(keyword, page, size)
        );
    }

    // Get student details
    @GetMapping("/{studentId}")
    public ResponseEntity<AdminStudentResponse> getStudentById(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                studentService.getStudentById(studentId)
        );
    }

    // Activate or deactivate student
    @PatchMapping("/{studentId}/status")
    public ResponseEntity<AdminStudentResponse> updateStudentStatus(
            @PathVariable Long studentId,
            @Valid @RequestBody StudentStatusRequest request) {

        return ResponseEntity.ok(
                studentService.updateStudentStatus(
                        studentId,
                        request.enabled()
                )
        );
    }
}
