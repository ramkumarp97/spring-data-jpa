package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository studentRepository,
			StudentIdCardRepository studentIdCardRepository) {
		return args -> {

			String firstName = "Ram";
			String lastName = "Kumar";
			int age = 18;
			long id = 2L;
			String email = String.format("%s.%s@prkcode.com", firstName, lastName);
			Student student = new Student(firstName, lastName, email, age);

			student.addBook(new Book("Clean Code", LocalDateTime.now().minusDays(4)));

			student.addBook(new Book("Think and Grow Rich", LocalDateTime.now()));

			student.addBook(new Book("Spring Data JPA", LocalDateTime.now().minusYears(1)));

			StudentIdCard studentIdCard = new StudentIdCard("123456789", student);

			student.setStudentIdCard(studentIdCard);

			student.addEnrolment(new Enrolment(new EnrolmentId(1L, 1L), student, new Course("Computer Science", "IT"),
					LocalDateTime.now()));

			student.addEnrolment(new Enrolment(new EnrolmentId(1L, 2L), student,
					new Course("Amigoscode Spring Data JPA", "IT"), LocalDateTime.now().minusDays(18)));

			student.addEnrolment(new Enrolment(new EnrolmentId(1L, 2L), student, new Course("Spring Data JPA", "IT"),
					LocalDateTime.now().minusDays(18)));

			studentRepository.save(student);

			studentRepository.findById(1L).ifPresent(s -> {
				System.out.println("fetch book lazy...");
				List<Book> books = student.getBooks();
				books.forEach(book -> {
					System.out.println(s.getFirstName() + " borrowed " + book.getBookName());
				});
			});

			studentRepository.findStudentByEmail(email).ifPresent(s -> {
				System.out.println(s.getEmail() + " is the emailID of " + s.getFirstName());

			});
			
			studentIdCardRepository.findById(id).ifPresentOrElse((s -> {
				System.out.println(s.getCardNumber() +" is the card having the id number " +s.getId());
			}), () -> System.out.println("There is no person for id "+ id));
		};
	}
}
