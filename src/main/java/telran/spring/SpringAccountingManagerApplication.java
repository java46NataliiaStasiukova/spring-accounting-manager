package telran.spring;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import jakarta.annotation.PreDestroy;

@SpringBootApplication
public class SpringAccountingManagerApplication {

	private static final String SHUTDOWN = "shutdown";
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(SpringAccountingManagerApplication.class,  args);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.println("To stop server type " + SHUTDOWN);
			String line = scanner.nextLine();
			if (line.equals(SHUTDOWN)) {
				break;
			}
		}
		ctx.close();
		
	}
	
	@PreDestroy
	void preDestroy() {
		System.out.println("bye - shutdown has been performed");
	}

}
