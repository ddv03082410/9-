package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.entity.StudentCompletedCourse;
import com.example.exception.CustomException;
import com.example.mapper.CourseMapper;
import com.example.mapper.StudentCompletedCourseMapper;
import com.example.mapper.StudentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentCompletedCourseService {

    @Resource
    private StudentCompletedCourseMapper completedCourseMapper;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private StudentMapper studentMapper;

    public List<StudentCompletedCourse> listByStudent(Integer studentId) {
        if (studentId == null) {
            return new ArrayList<>();
        }
        return completedCourseMapper.selectByStudentId(studentId);
    }

    public List<Course> listAvailableCourses(Integer studentId, String keyword) {
        if (studentId == null) {
            return new ArrayList<>();
        }
        String name = StrUtil.trimToNull(keyword);
        return courseMapper.selectForStudent(name, studentId);
    }

    public void addCourses(Integer studentId, List<Integer> courseIds) {
        if (studentId == null) {
            throw new CustomException("学生信息缺失");
        }
        if (CollUtil.isEmpty(courseIds)) {
            return;
        }
        Student student = studentMapper.selectById(studentId);
        if (ObjectUtil.isNull(student)) {
            throw new CustomException("学生不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        for (Integer courseId : courseIds) {
            if (courseId == null) {
                continue;
            }
            Course course = courseMapper.selectById(courseId);
            if (ObjectUtil.isNull(course)) {
                continue;
            }
            if (completedCourseMapper.exists(studentId, courseId) > 0) {
                continue;
            }
            StudentCompletedCourse record = new StudentCompletedCourse();
            record.setStudentId(studentId);
            record.setCourseId(courseId);
            record.setCompletedAt(now);
            completedCourseMapper.insert(record);
        }
        refreshStudentScore(studentId);
    }

    public void deleteById(Integer id) {
        StudentCompletedCourse record = completedCourseMapper.selectById(id);
        if (record == null) {
            return;
        }
        completedCourseMapper.deleteById(id);
        refreshStudentScore(record.getStudentId());
    }

    public void syncScore(Integer studentId) {
        refreshStudentScore(studentId);
    }

    private void refreshStudentScore(Integer studentId) {
        if (studentId == null) {
            return;
        }
        BigDecimal total = completedCourseMapper.selectTotalCredit(studentId);
        int score = total == null ? 0 : total.intValue();
        studentMapper.updateScore(studentId, score);
    }
}
