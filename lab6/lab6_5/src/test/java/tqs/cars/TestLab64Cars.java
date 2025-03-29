package tqs.cars;

import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

public class TestLab64Cars {
	public static void main(String[] args) {
		SpringApplication
            .from(CarMngrApplication::main)
            .with(TestcontainersConfiguration.class)
        .run(args);
	}
}
