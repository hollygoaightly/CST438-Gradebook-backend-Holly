package com.cst438.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EnrollmentRepository extends CrudRepository <Enrollment, Integer> {
	
	@Query("select a from Enrollment a where a.studentEmail = :studentEmail")
	Enrollment findByEmail(@Param("studentEmail") String studentEmail);

}
