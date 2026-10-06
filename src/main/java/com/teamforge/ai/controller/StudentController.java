package com.teamforge.ai.controller;

import com.teamforge.ai.dto.StudentDTO;
import com.teamforge.ai.dto.StudentFormDTO;
import com.teamforge.ai.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        List<StudentDTO> students = studentService.getAllStudents(search, role, status);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentFormDTO form) {
        StudentDTO created = studentService.createStudent(form);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/import-csv")
    public ResponseEntity<Map<String, Object>> importCsv(@RequestBody Map<String, String> payload) {
        String csv = payload.get("csvData");
        Map<String, Object> result = studentService.importStudentsFromCsv(csv);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentFormDTO form) {
        StudentDTO updated = studentService.updateStudent(id, form);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Student deleted successfully");
        return ResponseEntity.ok(res);
    }
}
