package tqs.homework.canteen.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private String serverUrl = "localhost:8080";

    @Bean
    public OpenAPI defineOpenAPI() {
        Server server = new Server();
        server.setUrl(serverUrl);

        Info information = new Info()
                .title("Canteen RestApi")
                .version("1.0");

        return new OpenAPI().info(information).servers(List.of(server));
    }
}