package com.cst438.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.CourseDTOG;

public class RegistrationServiceREST extends RegistrationService {

	
	RestTemplate restTemplate = new RestTemplate();
	
	@Value("${registration.url}") 
	String registration_url;
	
	public RegistrationServiceREST() {
		System.out.println("REST registration service ");
	}
	
	@Override
	public void sendFinalGrades(int course_id , CourseDTOG courseDTO) { 
		System.out.println("Gradebook- RegistrationServiceREST- sendFinalGrades  \nSending FinalGrades for course " + course_id + " to Register-CourseController - updateCourseGrades ");
		String url = "http://localhost:8080/course/" + course_id;
		System.out.println("url -- " + url);
		RestTemplate httpTemplate = new RestTemplate();
		httpTemplate.put(
				url,   // URL
				courseDTO);   // data to send         
		
	}
}
