package com.example.runner;

import com.example.entity.College;
import com.example.entity.Course;
import com.example.entity.Speciality;
import com.example.entity.Teacher;
import com.example.mapper.CollegeMapper;
import com.example.mapper.CourseMapper;
import com.example.mapper.SpecialityMapper;
import com.example.mapper.TeacherMapper;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 初始化跨学院公共课程，避免手工录入
 */
@Component
public class CourseBootstrapRunner implements CommandLineRunner {

    @Resource
    private CollegeMapper collegeMapper;
    @Resource
    private SpecialityMapper specialityMapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private CourseMapper courseMapper;

    @Override
    public void run(String... args) {
        Speciality mathSpeciality = ensureSpeciality("数学学院", "基础数学与建模", "应用数学");
        Speciality musicSpeciality = ensureSpeciality("音乐学院", "音乐与舞蹈艺术中心", "音乐学");
        Speciality marxSpeciality = ensureSpeciality("马克思主义学院", "思想政治教育研究", "马克思主义理论");

        ensureGeneralCourse("高等数学", "系统学习微积分、线性代数与常微分方程等内容，为后续算法与建模课程打下坚实数学基础。",
                BigDecimal.valueOf(4), mathSpeciality, "math_prof", "李数学");
        ensureGeneralCourse("音乐鉴赏", "通过多维度赏析国内外经典音乐作品，培养审美素养与跨学科人文视野。",
                BigDecimal.valueOf(2), musicSpeciality, "music_prof", "陈音悠");
        ensureGeneralCourse("马克思主义原理", "深入理解马克思主义基本理论与新时代中国特色社会主义思想，提升价值判断能力。",
                BigDecimal.valueOf(3), marxSpeciality, "marx_prof", "张思政");
    }

    private Speciality ensureSpeciality(String collegeName, String collegeDesc, String specialityName) {
        College college = collegeMapper.selectByExactName(collegeName);
        if (college == null) {
            college = new College();
            college.setName(collegeName);
            college.setContent(collegeDesc);
            collegeMapper.insert(college);
        }
        Speciality speciality = specialityMapper.selectByExactName(specialityName);
        if (speciality == null) {
            speciality = new Speciality();
            speciality.setName(specialityName);
            speciality.setCollegeId(college.getId());
            specialityMapper.insert(speciality);
        }
        return speciality;
    }

    private void ensureGeneralCourse(String courseName, String content, BigDecimal credit, Speciality speciality,
                                     String teacherUsername, String teacherName) {
        if (courseMapper.selectByExactName(courseName) != null) {
            return;
        }
        Teacher teacher = teacherMapper.selectByUsername(teacherUsername);
        if (teacher == null) {
            teacher = new Teacher();
            teacher.setUsername(teacherUsername);
            teacher.setPassword("123456");
            teacher.setRole("TEACHER");
            teacher.setName(teacherName);
            teacher.setSex("女");
            teacher.setTitle("教授");
            teacher.setSpecialityId(speciality.getId());
            teacherMapper.insert(teacher);
        }

        Course course = new Course();
        course.setName(courseName);
        course.setContent(content);
        course.setCredit(credit);
        course.setTeacherId(teacher.getId());
        course.setNum(200);
        course.setTime("周三 14:00 - 16:00");
        course.setLocation(speciality.getName() + "楼201");
        course.setCollegeId(speciality.getCollegeId());
        course.setSpecialityId(speciality.getId());
        course.setGeneral(Boolean.TRUE);
        course.setAlreadyNum(0);
        course.setPrerequisiteIds(new ArrayList<>());
        courseMapper.insert(course);
    }
}
