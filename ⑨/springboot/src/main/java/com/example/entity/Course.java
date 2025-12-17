package com.example.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Course {

    private Integer id;
    private String name;
    private String content;
    private BigDecimal credit;
    private Integer teacherId;
    private Integer num;
    private String time;
    private String location;
    private Integer collegeId;
    private Integer alreadyNum;
    private Integer specialityId;
    private Boolean general;

    private String teacherName;
    private String collegeName;
    private String specialityName;

    private Integer studentId;
    private List<Integer> prerequisiteIds = new ArrayList<>();
    private String prerequisiteNames;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(Integer collegeId) {
        this.collegeId = collegeId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Integer getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public Integer getAlreadyNum() {
        return alreadyNum;
    }

    public void setAlreadyNum(Integer alreadyNum) {
        this.alreadyNum = alreadyNum;
    }

    public Boolean getGeneral() {
        return general;
    }

    public void setGeneral(Boolean general) {
        this.general = general;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public List<Integer> getPrerequisiteIds() {
        return prerequisiteIds;
    }

    public void setPrerequisiteIds(List<Integer> prerequisiteIds) {
        if (prerequisiteIds == null) {
            this.prerequisiteIds = new ArrayList<>();
        } else {
            this.prerequisiteIds = prerequisiteIds;
        }
    }

    public String getPrerequisiteNames() {
        return prerequisiteNames;
    }

    public void setPrerequisiteNames(String prerequisiteNames) {
        this.prerequisiteNames = prerequisiteNames;
    }
}
