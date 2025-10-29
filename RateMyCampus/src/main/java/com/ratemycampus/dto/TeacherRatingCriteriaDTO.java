package com.ratemycampus.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeacherRatingCriteriaDTO {
    public Long id;
    public Integer teacherId;
    public Integer studentId;
    public int subjectKnowledge;
    public int communicationSkills;

    // Accept either { "teacherId": 1 } or { "teacher": 1 } or { "teacher": { "tid": 1 } }
    @JsonProperty("teacher")
    public void setTeacher(Object teacher) {
        if (teacher == null) return;
        if (teacher instanceof Number) {
            this.teacherId = ((Number) teacher).intValue();
        } else if (teacher instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) teacher;
            Object v = map.get("tid");
            if (v == null) v = map.get("id");
            if (v instanceof Number) this.teacherId = ((Number) v).intValue();
        }
    }


    @JsonProperty("student")
    public void setStudent(Object student) {
        if (student == null) return;
        if (student instanceof Number) {
            this.studentId = ((Number) student).intValue();
        } else if (student instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) student;
            Object v = map.get("sid");
            if (v == null) v = map.get("id");
            if (v instanceof Number) this.studentId = ((Number) v).intValue();
        }
    }
}
