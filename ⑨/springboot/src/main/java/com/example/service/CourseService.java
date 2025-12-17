package com.example.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Course;
import com.example.mapper.CourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 课程信息业务处理
 */
@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;

    public void add(Course course) {
        course.setAlreadyNum(0);
        if (course.getCredit() == null) {
            course.setCredit(BigDecimal.valueOf(2));
        }
        if (course.getGeneral() == null) {
            course.setGeneral(Boolean.FALSE);
        }
        courseMapper.insert(course);
        savePrerequisites(course);
    }

    public PageInfo<Course> selectPage(Course course, Integer pageNum, Integer pageSize, Integer studentId) {
        List<Course> list;
        PageHelper.startPage(pageNum, pageSize);
        if (ObjectUtil.isNotEmpty(studentId)) {
            list = courseMapper.selectForStudent(course.getName(), studentId);
        } else if (ObjectUtil.isNotEmpty(course.getTeacherId())) {
            Integer teacherId = course.getTeacherId();
            if (ObjectUtil.isNotEmpty(course.getName())) {
                list = courseMapper.selectByNameAndTeacherId(course.getName(), teacherId);
            } else {
                list = courseMapper.selectAllByTeacherId(teacherId);
            }
        } else {
            if (ObjectUtil.isNotEmpty(course.getName())) {
                list = courseMapper.selectByName(course.getName());
            } else {
                list = courseMapper.selectAll();
            }
        }
        enrichCourseMeta(list);
        return PageInfo.of(list);
    }

    public void updateById(Course course) {
        if (course.getCredit() == null) {
            course.setCredit(BigDecimal.valueOf(2));
        }
        if (course.getGeneral() == null) {
            course.setGeneral(Boolean.FALSE);
        }
        courseMapper.updateById(course);
        savePrerequisites(course);
    }

    public void deleteById(Integer id) {
        courseMapper.deletePrerequisitesByCourseId(id);
        courseMapper.deleteById(id);
    }

    public List<Course> selectAll() {
        List<Course> courses = courseMapper.selectAll();
        enrichCourseMeta(courses);
        return courses;
    }

    public List<Integer> selectPrerequisiteIds(Integer courseId) {
        return courseMapper.selectPrerequisiteIds(courseId);
    }

    public List<Course> recommend(Integer studentId) {
        List<Course> courses = courseMapper.selectRecommendCourses(studentId);
        enrichCourseMeta(courses);
        return courses;
    }

    private void savePrerequisites(Course course) {
        if (course.getId() == null) {
            return;
        }
        courseMapper.deletePrerequisitesByCourseId(course.getId());
        if (CollUtil.isNotEmpty(course.getPrerequisiteIds())) {
            courseMapper.insertPrerequisites(course.getId(), course.getPrerequisiteIds());
        }
    }

    private void enrichCourseMeta(List<Course> courses) {
        if (CollUtil.isEmpty(courses)) {
            return;
        }
        for (Course item : courses) {
            List<Integer> ids = courseMapper.selectPrerequisiteIds(item.getId());
            item.setPrerequisiteIds(ids);
            List<String> names = courseMapper.selectPrerequisiteNames(item.getId());
            if (CollUtil.isNotEmpty(names)) {
                item.setPrerequisiteNames(String.join("、", names));
            } else {
                item.setPrerequisiteNames("");
            }
        }
    }
}
