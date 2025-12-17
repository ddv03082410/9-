package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.Student;
import com.example.entity.Teacher;
import com.example.exception.CustomException;
import com.example.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学生信息业务层处理
 */
@Service
public class StudentService {

    @Resource
    private StudentMapper studentMapper;

    public void add(Student student) {
        // 做一些逻辑处理
        Student dbStudent = studentMapper.selectByUsername(student.getUsername());
        if (ObjectUtil.isNotEmpty(dbStudent)) {
            throw new CustomException("用户名已存在");
        }
        if (ObjectUtil.isEmpty(student.getPassword())) {
            student.setPassword("123456");
        }
        if (ObjectUtil.isEmpty(student.getName())) {
            student.setName(student.getUsername());
        }
        student.setRole("STUDENT");
        student.setScore(0);

        studentMapper.insert(student);
    }

    public PageInfo<Student> selectPage(Student student, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        String name = null;
        Integer collegeId = null;
        Integer specialityId = null;
        if (ObjectUtil.isNotEmpty(student)) {
            name = StrUtil.trimToNull(student.getName());
            collegeId = student.getCollegeId();
            specialityId = student.getSpecialityId();
        }
        List<Student> list = studentMapper.selectList(name, collegeId, specialityId);
        return PageInfo.of(list);
    }

    public void updateById(Student student) {
        studentMapper.updateById(student);
    }

    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    /**
     * 登录
     */
    public Student login(Account account) {
        Student dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbStudent)) {
            throw new CustomException("用户不存在");
        }
        if (!account.getPassword().equals(dbStudent.getPassword())) {
            throw new CustomException("账号或密码错误");
        }
        return dbStudent;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        Student student = new Student();
        student.setUsername(account.getUsername());
        student.setPassword(account.getPassword());
        add(student);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Student dbStudent = studentMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbStudent)) {
            throw new CustomException("用户不存在");
        }
        if (!account.getPassword().equals(dbStudent.getPassword())) {
            throw new CustomException("原密码错误");
        }
        dbStudent.setPassword(account.getNewPassword());
        studentMapper.updateById(dbStudent);
    }

    public Student selectById(Integer id) {
        return studentMapper.selectById(id);
    }
}
