package com.example.controller;

import com.example.common.Result;
import com.example.dto.CompletedCourseRequest;
import com.example.service.StudentCompletedCourseService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student/completedCourses")
public class StudentCompletedCourseController {

    @Resource
    private StudentCompletedCourseService completedCourseService;

    @GetMapping
    public Result list(@RequestParam Integer studentId) {
        return Result.success(completedCourseService.listByStudent(studentId));
    }

    @GetMapping("/options")
    public Result options(@RequestParam Integer studentId,
                          @RequestParam(required = false) String name) {
        return Result.success(completedCourseService.listAvailableCourses(studentId, name));
    }

    @PostMapping
    public Result add(@RequestBody CompletedCourseRequest request) {
        completedCourseService.addCourses(request.getStudentId(), request.getCourseIds());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        completedCourseService.deleteById(id);
        return Result.success();
    }
}
