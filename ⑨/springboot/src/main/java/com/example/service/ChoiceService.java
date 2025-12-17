package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Choice;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.ChoiceMapper;
import com.example.mapper.CourseMapper;
import com.example.mapper.StudentCompletedCourseMapper;
import com.example.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 选课信息业务层处理
 */
@Service
public class ChoiceService {

    @Resource
    private ChoiceMapper choiceMapper;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private StudentCompletedCourseMapper completedCourseMapper;

    public void add(Course course) {
        Course dbCourse = courseMapper.selectById(course.getId());
        if (ObjectUtil.isNull(dbCourse)) {
            throw new CustomException("课程不存在");
        }
        Student student = studentMapper.selectById(course.getStudentId());
        if (ObjectUtil.isNull(student)) {
            throw new CustomException("学生不存在");
        }
        boolean generalCourse = Boolean.TRUE.equals(dbCourse.getGeneral());
        if (!generalCourse) {
            if (ObjectUtil.isNotEmpty(student.getCollegeId()) && ObjectUtil.isNotEmpty(dbCourse.getCollegeId())
                    && !student.getCollegeId().equals(dbCourse.getCollegeId())) {
                throw new CustomException("该课程仅对所属学院学生开放");
            }
            if (ObjectUtil.isNotEmpty(student.getSpecialityId()) && ObjectUtil.isNotEmpty(dbCourse.getSpecialityId())
                    && !student.getSpecialityId().equals(dbCourse.getSpecialityId())) {
                throw new CustomException("该课程仅限匹配专业的学生选修");
            }
        }
        if (completedCourseMapper.exists(student.getId(), dbCourse.getId()) > 0) {
            throw new CustomException("您已经完成过该门课程了");
        }
        // 1. 先判断这个课程有没有满员
        if (dbCourse.getNum().equals(dbCourse.getAlreadyNum())) {
            throw new CustomException("该门课已经满员，请选择其他课程");
        }
        // 2. 判断该学生有没有选过该门课
        List<Choice> list = choiceMapper.selectByCourseIdAndStudentId(dbCourse.getId(), course.getStudentId());
        if (CollectionUtil.isNotEmpty(list)) {
            throw new CustomException("您已经选过该门课了，请给其他小伙伴留点机会吧！");
        }
        // 3. 往选课信息表里面插入一条选课记录
        Choice choice = new Choice();
        choice.setName(dbCourse.getName());
        choice.setTeacherId(dbCourse.getTeacherId());
        choice.setStudentId(course.getStudentId());
        choice.setCourseId(dbCourse.getId());
        choiceMapper.insert(choice);
        // 4. 该门课的已选人数要+1
        dbCourse.setAlreadyNum(dbCourse.getAlreadyNum() + 1);
        courseMapper.updateById(dbCourse);
    }

    public PageInfo<Choice> selectPage(Choice choice, Integer pageNum, Integer pageSize) {
        List<Choice> list;
        PageHelper.startPage(pageNum, pageSize);
        if (ObjectUtil.isNotEmpty(choice.getStudentId())) {
            // 说明这是学生登录进行分页查询
            if (ObjectUtil.isNotEmpty(choice.getName())) {
                list = choiceMapper.selectByNameAndStudentId(choice.getName(), choice.getStudentId());
            } else {
                list = choiceMapper.selectAllByStudentId(choice.getStudentId());
            }
        } else if (ObjectUtil.isNotEmpty(choice.getTeacherId())) {
            // 说明这是教师登录进行分页查询
            if (ObjectUtil.isNotEmpty(choice.getName())) {
                list = choiceMapper.selectByNameAndTeacherId(choice.getName(), choice.getTeacherId());
            } else {
                list = choiceMapper.selectAllByTeacherId(choice.getTeacherId());
            }
        } else {
            // 说明这是管理员登录进行分页查询
            if (ObjectUtil.isNotEmpty(choice.getName())) {
                list = choiceMapper.selectByName(choice.getName());
            } else {
                list = choiceMapper.selectAll();
            }
        }
        return PageInfo.of(list);
    }

    public void updateById(Choice choice) {
        choiceMapper.updateById(choice);
    }

    public void deleteById(Integer id) {
        Choice choice = choiceMapper.selectById(id);
        choiceMapper.deleteById(id);
        // 把对应的课程信息里面的已选人数-1
        Course course = courseMapper.selectById(choice.getCourseId());
        course.setAlreadyNum(course.getAlreadyNum() - 1);
        courseMapper.updateById(course);
    }

    public List<Choice> selectAll() {
        return choiceMapper.selectAll();
    }

}
