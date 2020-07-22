package chains.application;

import chains.materials.Warehouse;
import chains.occupation.occupations.Farmer;
import chains.timeline.GameTimeline;
import chains.utility.Statistics;
import chains.worker.Worker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ApplicationBeans {

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
