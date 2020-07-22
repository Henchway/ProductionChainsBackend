package chains;

import chains.db.LifestockDbController;
import chains.db.LifestockRepository;
import chains.materials.Warehouse;
import chains.timeline.GameTimeline;
import chains.utility.Statistics;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "chains.db")
@SpringBootApplication
public class Main {

    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(Main.class, args);
//        displayAllBeans();
    }

    public static void displayAllBeans() {
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : allBeanNames) {
            System.out.println(beanName);
        }
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
