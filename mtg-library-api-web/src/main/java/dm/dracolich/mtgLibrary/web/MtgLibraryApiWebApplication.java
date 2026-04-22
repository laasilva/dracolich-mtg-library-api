package dm.dracolich.mtgLibrary.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"dm.dracolich.mtgLibrary.web", "dm.dracolich.forge",
        "dm.dracolich.mtgLibrary.integration"})
@EnableReactiveMongoRepositories(basePackages = {"dm.dracolich.mtgLibrary.web.repository"})
public class MtgLibraryApiWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(MtgLibraryApiWebApplication.class, args);
    }
}
