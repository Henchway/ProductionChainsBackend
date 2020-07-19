package chains.application;

import chains.materials.Warehouse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import chains.timeline.GameTimeline;
import chains.utility.Statistics;


@SpringBootApplication(scanBasePackages = "chains")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    @Bean
    public Warehouse warehouse() {
        return new Warehouse();
    }

    @Bean
    public GameTimeline gameTimeline(Warehouse warehouse) {
        return new GameTimeline(warehouse);
    }

    @Bean
    public Statistics statistics(GameTimeline gameTimeline) {
        return new Statistics(gameTimeline);
    }

}
