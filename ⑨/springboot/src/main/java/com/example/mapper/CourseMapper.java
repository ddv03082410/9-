package com.example.mapper;

import com.example.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseMapper {

    void insert(Course course);

    void updateById(Course course);

    void deleteById(Integer id);

    List<Course> selectAll();

    List<Course> selectByName(String name);

    List<Course> selectByNameAndTeacherId(@Param("name") String name, @Param("teacherId") Integer teacherId);

    List<Course> selectForStudent(@Param("name") String name, @Param("studentId") Integer studentId);

    List<Course> selectAllByTeacherId(Integer teacherId);

    Course selectById(Integer courseId);

    List<Integer> selectPrerequisiteIds(Integer courseId);

    List<String> selectPrerequisiteNames(Integer courseId);

    void deletePrerequisitesByCourseId(Integer courseId);

    void insertPrerequisites(@Param("courseId") Integer courseId, @Param("prerequisiteIds") List<Integer> prerequisiteIds);

    List<Course> selectRecommendCourses(@Param("studentId") Integer studentId);

    @Select("select * from course where name = #{name} limit 1")
    Course selectByExactName(String name);
}
