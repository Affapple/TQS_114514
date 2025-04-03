package tqs.homework.canteen;

import org.springframework.boot.SpringApplication;

public class TestCanteenApplication {

	public static void main(String[] args) {
		SpringApplication
			.from(CanteenApplication::main)
			.with(TestcontainersConfiguration.class)
		.run(args);
	}
}
