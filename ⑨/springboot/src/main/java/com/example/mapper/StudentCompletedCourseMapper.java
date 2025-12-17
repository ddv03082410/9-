package com.example.mapper;

import com.example.entity.StudentCompletedCourse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface StudentCompletedCourseMapper {

    void insert(StudentCompletedCourse record);

    @Delete("delete from student_completed_course where id = #{id}")
    void deleteById(Integer id);

    @Select("select count(1) from student_completed_course where student_id = #{studentId} and course_id = #{courseId}")
    int exists(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);

    @Select("select * from student_completed_course where id = #{id}")
    StudentCompletedCourse selectById(Integer id);

    List<StudentCompletedCourse> selectByStudentId(Integer studentId);

    @Select("select ifnull(sum(course.credit), 0) from student_completed_course scc " +
            "left join course on scc.course_id = course.id where scc.student_id = #{studentId}")
    BigDecimal selectTotalCredit(Integer studentId);
}
