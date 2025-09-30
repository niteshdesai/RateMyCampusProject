package com.ratemycampus.config;

import com.ratemycampus.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public (read-only) endpoints
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers("/upload/**").permitAll()
                        .requestMatchers("/api/colleges/search").permitAll()
                        .requestMatchers("/api/colleges/city/**").permitAll()
                        .requestMatchers("/api/colleges/{id}/departments").permitAll()
                        .requestMatchers("/api/colleges/{id}/ratings").permitAll()
                        .requestMatchers("/api/colleges/{id}/rating-count").permitAll()
                        .requestMatchers("/api/colleges/{id}/student-count").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/colleges/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/courses").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/courses/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/departments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/departments/college/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/departments/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ratings/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ratings/college/{collegeId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ratings/college/{collegeId}/student-count").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ratings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rating-teachers/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/rating-teachers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/students").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/students/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/students/enroll/{enrollmentNumber}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/teachers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/teachers/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/teachers/{teacherId}/courses/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/login").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/colleges/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/students/college/{collegeId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/teachers/college/{collegeId}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/departments/college/{collegeId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/teachers/department/{departmentId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/students/department/{departmentId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/departments/*/students/count").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/departments/*/teachers/count").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/departments/*/courses/count").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/courses/department/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hod/department/{departmentId}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/teachers/{teacherId}/courses")
                        .permitAll()
                        // Protected endpoints
                        .requestMatchers(HttpMethod.POST, "/api/courses").hasAuthority("ROLE_HOD")
                        .requestMatchers(HttpMethod.PUT, "/api/courses/{id}").hasAuthority("ROLE_HOD")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/{id}").hasAuthority("ROLE_HOD")
                        .requestMatchers(HttpMethod.POST, "/api/colleges/addcollege").hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/colleges/{id}").hasAuthority("ROLE_COLLEGE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/colleges/updateCollegeImage/{id}")
                        .hasAuthority("ROLE_COLLEGE_ADMIN")
                        // .requestMatchers(HttpMethod.GET, "/api/hod/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/hod").hasAuthority("ROLE_COLLEGE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/hod/{id}").hasAuthority("ROLE_COLLEGE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/hod/updateDeptAdminImage/{id}")
                        .hasAuthority("ROLE_COLLEGE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/hod/{id}").hasAuthority("ROLE_COLLEGE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/departments").hasAuthority("ROLE_COLLEGE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/departments/{id}").hasAuthority("ROLE_HOD")
                        .requestMatchers(HttpMethod.DELETE, "/api/departments/{id}").hasAuthority("ROLE_COLLEGE_ADMIN")
                        .requestMatchers("/api/ratings/addCollegeRating").hasAuthority("ROLE_STUDENT")
//                        .requestMatchers(HttpMethod.PUT, "/api/ratings/{id}").hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/ratings/{id}").hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/rating-teachers").hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.PUT, "/api/rating-teachers/{id}").hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/rating-teachers/{id}").hasAuthority("ROLE_STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/students").hasAuthority("ROLE_HOD")
                        .requestMatchers(HttpMethod.PUT, "/api/students/{id}").hasAuthority("ROLE_HOD")
                        .requestMatchers(HttpMethod.PUT, "/api/students/updateStudentImage/{id}")
                        .hasAuthority("ROLE_HOD")

                        .requestMatchers(HttpMethod.GET, "/api/students/course/{courseId}")
                        .hasAuthority("ROLE_HOD")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/{id}").hasAuthority("ROLE_HOD")
                        .requestMatchers(HttpMethod.POST, "/api/teachers").hasAuthority("ROLE_HOD")
                        .requestMatchers(HttpMethod.PUT, "/api/teachers/{id}").hasAuthority("ROLE_HOD")
                        .requestMatchers(HttpMethod.PUT, "/api/teachers/updateTeacherImage/{id}")
                        .hasAuthority("ROLE_HOD")

                        .requestMatchers(HttpMethod.DELETE, "/api/teachers{id}").hasAuthority("ROLE_HOD")

                        .requestMatchers(HttpMethod.POST, "/api/teachers/{teacherId}/courses")
                        .hasAuthority("ROLE_HOD")
                        .requestMatchers(HttpMethod.DELETE, "/api/teachers/{teacherId}/courses")
                        .hasAuthority("ROLE_HOD")

                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}