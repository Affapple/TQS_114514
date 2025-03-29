package tqs.cars;

import org.springframework.boot.SpringApplication;

public class TestLab64Cars {
	public static void main(String[] args) {
		SpringApplication
            .from(CarMngrApplication::main)
            .with(TestcontainersConfiguration.class)
        .run(args);
	}
}
