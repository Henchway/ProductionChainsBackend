package chains.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import chains.timeline.GameTimeline;
import chains.utility.Statistics;


//public class Main extends Application {
//
//    @Override
//        public void start(Stage primaryStage) throws Exception{
//            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("GUI.fxml")));
//            primaryStage.setTitle("Hello World");
//            primaryStage.setScene(new Scene(root, 800, 600));
//            primaryStage.show();
//}
//
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}


@SpringBootApplication(scanBasePackages = "chains")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    @Bean
    public GameTimeline gameTimeline() {
        return new GameTimeline();
    }

    @Bean
    public Statistics statistics() {
        return new Statistics();
    }

}
