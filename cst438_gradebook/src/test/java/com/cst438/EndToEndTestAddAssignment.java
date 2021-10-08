package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;



@SpringBootTest
public class EndToEndTestAddAssignment {
	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/hollystephens/chromedriver";

	public static final String URL = "https://cst438gradebook-frontend.herokuapp.com/";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final String TEST_ASSIGNMENT_NAME = "Test Assignment - EndToEnd";
	public static final int COURSE_ID = 99999;
	public static final int SLEEP_DURATION = 1000; // 1 second.

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;


	@Test
	public void addAssignmentTest() throws Exception {
		
		// database clean up for assignment	
		Assignment a = null;
		do {
			a = assignmentRepository.findByName(TEST_ASSIGNMENT_NAME);
			if(a != null)
				assignmentRepository.delete(a);
		} while (a != null);
		
		
		// database clean up and setup for course		
		Course c = null;
		do {
			c = courseRepository.findByCourse_id(COURSE_ID);
			if(c != null)
				courseRepository.delete(c);
		} while (c != null);
		
		c = new Course();
		c.setCourse_id(COURSE_ID);
		c.setInstructor(TEST_INSTRUCTOR_EMAIL);
		c.setSemester("Fall");
		c.setYear(2021);
		c.setTitle("Test Course");
		courseRepository.save(c);
		String courseIdString = "" + COURSE_ID;
		

		// set the driver location and start driver
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);

		try {
			// locate 'New Assignment' button and click
			driver.findElement(By.xpath("//a[span='New Assignment']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// locate input element for assignment for 'Course Id' and add test course id
			driver.findElement(By.xpath("//input[@name='courseId']")).sendKeys(courseIdString);
		 
			// locate input element for assignment for 'Assignment Name' and add "Test Assignment"
			driver.findElement(By.xpath("//input[@name='assignmentName']")).sendKeys(TEST_ASSIGNMENT_NAME);
		
			
			// locate input element for assignment for 'Due Date' and add today's date
			driver.findElement(By.xpath("//input[@name='dueDate']")).sendKeys("2021-10-05");
						
			
			// Locate submit button and click
			driver.findElement(By.xpath("//input[@value='Submit']")).click();
			Thread.sleep(SLEEP_DURATION);

			// verify that assignment has been added to database
			a = assignmentRepository.findByName(TEST_ASSIGNMENT_NAME);
			assertEquals("0", ("" + a.getNeedsGrading()));

		} catch (Exception ex) {
			throw ex;
		} finally {
			// clean up database.
			if (a != null) assignmentRepository.delete(a);
			courseRepository.delete(c);

			driver.quit();
		}

	}

}
