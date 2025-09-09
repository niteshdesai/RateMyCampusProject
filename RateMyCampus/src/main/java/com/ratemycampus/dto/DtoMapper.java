package com.ratemycampus.dto;

import com.ratemycampus.entity.*;

public final class DtoMapper {

    private DtoMapper() {}

    public static Student toStudentEntity(StudentDTO dto) {
        if (dto == null) return null;
        Student s = new Student();
        s.setSid(dto.sid);
        s.setEnrollment(dto.enrollment);
        s.setSname(dto.sname);
        s.setSsem(dto.ssem);
        s.setSsection(dto.ssection);
        s.setSgender(dto.sgender);
        s.setSmobile(dto.smobile);
        s.setScity(dto.scity);
        s.setSimg(dto.simg);
        s.setSemail(dto.semail);
        if (dto.collegeId != null) {
            College c = new College();
            c.setCid(dto.collegeId);
            s.setCollege(c);
        }
        if (dto.departmentId != null) {
            Department d = new Department();
            d.setDeptId(dto.departmentId);
            s.setDepartment(d);
        }
        if (dto.courseId != null) {
            Course course = new Course();
            course.setC_id(dto.courseId);
            s.setCourse(course);
        }
        return s;
    }

    public static StudentDTO toStudentDTO(Student s) {
        if (s == null) return null;
        StudentDTO dto = new StudentDTO();
        dto.sid = s.getSid();
        dto.enrollment = s.getEnrollment();
        dto.sname = s.getSname();
        dto.ssem = s.getSsem();
        dto.ssection = s.getSsection();
        dto.sgender = s.getSgender();
        dto.smobile = s.getSmobile();
        dto.scity = s.getScity();
        dto.simg = s.getSimg();
        dto.semail = s.getSemail();
        dto.collegeId = s.getCollege() != null ? s.getCollege().getCid() : null;
        dto.departmentId = s.getDepartment() != null ? s.getDepartment().getDeptId() : null;
        dto.courseId = s.getCourse() != null ? s.getCourse().getC_id() : null;
        return dto;
    }

    public static Teacher toTeacherEntity(TeacherDTO dto) {
        if (dto == null) return null;
        Teacher t = new Teacher();
        t.setTid(dto.tid);
        t.setTname(dto.tname);
        t.setTsem(dto.tsem);
    t.setRole(dto.role);
        t.setTimg(dto.timg);
        if (dto.collegeId != null) {
            College c = new College();
            c.setCid(dto.collegeId);
            t.setCollege(c);
        }
        if (dto.departmentId != null) {
            Department d = new Department();
            d.setDeptId(dto.departmentId);
            t.setDepartment(d);
        }
        return t;
    }

    public static TeacherDTO toTeacherDTO(Teacher t) {
        if (t == null) return null;
        TeacherDTO dto = new TeacherDTO();
        dto.tid = t.getTid();
        dto.tname = t.getTname();
        dto.tsem = t.getTsem();
    dto.role = t.getRole();
        dto.timg = t.getTimg();
        dto.collegeId = t.getCollege() != null ? t.getCollege().getCid() : null;
        dto.departmentId = t.getDepartment() != null ? t.getDepartment().getDeptId() : null;
        return dto;
    }

    public static Rating toRatingEntity(RatingDTO dto) {
        if (dto == null) return null;
        Rating r = new Rating();
        r.setId(dto.id);
        r.setScore(dto.score);
        if (dto.collegeId != null) {
            College c = new College();
            c.setCid(dto.collegeId);
            r.setCollege(c);
        }
        if (dto.studentId != null) {
            Student s = new Student();
            s.setSid(dto.studentId);
            r.setStudent(s);
        }
        return r;
    }

    public static RatingDTO toRatingDTO(Rating r) {
        if (r == null) return null;
        RatingDTO dto = new RatingDTO();
        dto.id = r.getId();
        dto.score = r.getScore();
        dto.collegeId = r.getCollege() != null ? r.getCollege().getCid() : null;
        dto.studentId = r.getStudent() != null ? r.getStudent().getSid() : null;
        return dto;
    }

    public static RatingTeacher toRatingTeacherEntity(RatingTeacherDTO dto) {
        if (dto == null) return null;
        RatingTeacher r = new RatingTeacher();
        r.setId(dto.id);
        r.setScore(dto.score);
        if (dto.teacherId != null) {
            Teacher t = new Teacher();
            t.setTid(dto.teacherId);
            r.setTeacher(t);
        }
        if (dto.studentId != null) {
            Student s = new Student();
            s.setSid(dto.studentId);
            r.setStudent(s);
        }
        if (dto.courseId != null) {
            Course c = new Course();
            c.setC_id(dto.courseId);
            r.setCourse(c);
        }
        return r;
    }

    public static RatingTeacherDTO toRatingTeacherDTO(RatingTeacher r) {
        if (r == null) return null;
        RatingTeacherDTO dto = new RatingTeacherDTO();
        dto.id = r.getId();
        dto.score = r.getScore();
        dto.teacherId = r.getTeacher() != null ? r.getTeacher().getTid() : null;
        dto.studentId = r.getStudent() != null ? r.getStudent().getSid() : null;
        dto.courseId = r.getCourse() != null ? r.getCourse().getC_id() : null;
        return dto;
    }

    // College
    public static College toCollegeEntity(CollegeDTO dto) {
        if (dto == null) return null;
        College c = new College();
        c.setCid(dto.cid);
        c.setCname(dto.cname);
        c.setCdesc(dto.cdesc);
        c.setCactivity(dto.cactivity);
        c.setAddress(dto.address);
        c.setCimg(dto.cimg);
        c.setEmail(dto.email);
        return c;
    }

    public static CollegeDTO toCollegeDTO(College c) {
        if (c == null) return null;
        CollegeDTO dto = new CollegeDTO();
        dto.cid = c.getCid();
        dto.cname = c.getCname();
        dto.cdesc = c.getCdesc();
        dto.cactivity = c.getCactivity();
        dto.address = c.getAddress();
        dto.cimg = c.getCimg();
        dto.email = c.getEmail();
        return dto;
    }

    // Department
    public static Department toDepartmentEntity(DepartmentDTO dto) {
        if (dto == null) return null;
        Department d = new Department();
        d.setDeptId(dto.deptId);
        d.setDeptName(dto.deptName);
        d.setDesc(dto.desc);
        if (dto.collegeId != null) {
            College c = new College();
            c.setCid(dto.collegeId);
            d.setCollege(c);
        }
        return d;
    }

    public static DepartmentDTO toDepartmentDTO(Department d) {
        if (d == null) return null;
        DepartmentDTO dto = new DepartmentDTO();
        dto.deptId = d.getDeptId();
        dto.deptName = d.getDeptName();
        dto.desc = d.getDesc();
        dto.collegeId = d.getCollege() != null ? d.getCollege().getCid() : null;
        return dto;
    }

    // Course
    public static Course toCourseEntity(CourseDTO dto) {
        if (dto == null) return null;
        Course c = new Course();
        c.setC_id(dto.c_id);
        c.setcName(dto.cName);
        c.setcDuration(dto.cDuration);
        c.setcSince(dto.cSince);
        if (dto.collegeId != null) {
            College col = new College();
            col.setCid(dto.collegeId);
            c.setCollege(col);
        }
        if (dto.departmentId != null) {
            Department d = new Department();
            d.setDeptId(dto.departmentId);
            c.setDepartment(d);
        }
        return c;
    }

    public static CourseDTO toCourseDTO(Course c) {
        if (c == null) return null;
        CourseDTO dto = new CourseDTO();
        dto.c_id = c.getC_id();
        dto.cName = c.getcName();
        dto.cDuration = c.getcDuration();
        dto.cSince = c.getcSince();
        dto.collegeId = c.getCollege() != null ? c.getCollege().getCid() : null;
        dto.departmentId = c.getDepartment() != null ? c.getDepartment().getDeptId() : null;
        return dto;
    }

    // CollegeAdmin
    public static CollegeAdmin toCollegeAdminEntity(CollegeAdminDTO dto) {
        if (dto == null) return null;
        CollegeAdmin a = new CollegeAdmin();
        a.setId(dto.id);
        a.setName(dto.name);
        a.setEmail(dto.email);
        a.setMobile(dto.mobile);
        a.setImagePath(dto.imagePath);
        if (dto.collegeId != null) {
            College c = new College();
            c.setCid(dto.collegeId);
            a.setCollege(c);
        }
        return a;
    }

    public static CollegeAdminDTO toCollegeAdminDTO(CollegeAdmin a) {
        if (a == null) return null;
        CollegeAdminDTO dto = new CollegeAdminDTO();
        dto.id = a.getId();
        dto.name = a.getName();
        dto.email = a.getEmail();
        dto.mobile = a.getMobile();
        dto.imagePath = a.getImagePath();
        dto.collegeId = a.getCollege() != null ? a.getCollege().getCid() : null;
        return dto;
    }

    // DepartmentAdmin
    public static DepartmentAdmin toDepartmentAdminEntity(DepartmentAdminDTO dto) {
        if (dto == null) return null;
        DepartmentAdmin a = new DepartmentAdmin();
        a.setHodId(dto.hodId);
        a.setUsername(dto.username);
        a.setName(dto.name);
        a.setEmail(dto.email);
        a.setDaImg(dto.daImg);
        if (dto.departmentId != null) {
            Department d = new Department();
            d.setDeptId(dto.departmentId);
            a.setDepartment(d);
        }
        if (dto.collegeId != null) {
            College c = new College();
            c.setCid(dto.collegeId);
            a.setCollege(c);
        }
        return a;
    }

    public static DepartmentAdminDTO toDepartmentAdminDTO(DepartmentAdmin a) {
        if (a == null) return null;
        DepartmentAdminDTO dto = new DepartmentAdminDTO();
        dto.hodId = a.getHodId();
        dto.username = a.getUsername();
        dto.name = a.getName();
        dto.email = a.getEmail();
        dto.daImg = a.getDaImg();
        dto.departmentId = a.getDepartment() != null ? a.getDepartment().getDeptId() : null;
        dto.collegeId = a.getCollege() != null ? a.getCollege().getCid() : null;
        return dto;
    }
}


