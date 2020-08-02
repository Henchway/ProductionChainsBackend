package chains.restservice;

import chains.timeline.GameTimeline;
import chains.utility.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class TimelineRestServiceController {

    private ApplicationContext applicationContext;
    List<GameTimeline> gameTimelines = new ArrayList<>();

    //    @PostConstruct
    @PostMapping("/start")
    public ResponseEntity<Object> startTimeline() {

        GameTimeline gameTimeline = applicationContext.getBean(GameTimeline.class);
        gameTimeline.startPopulation();
        gameTimelines.add(gameTimeline);
        return new ResponseEntity<>(gameTimeline.getUuid().toString(), HttpStatus.OK);

    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @GetMapping("/statistics")
    public ResponseEntity<Object> retrieveStatistics() {

        List<Statistics> statisticsList = new ArrayList<>();
        gameTimelines.forEach(gameTimeline -> {
            gameTimeline.getStatistics().refreshStatistics();
            statisticsList.add(gameTimeline.getStatistics());
        });
        return new ResponseEntity<>(statisticsList, HttpStatus.OK);
    }

    @GetMapping(value = "/statistics/{id}")
    public ResponseEntity<Object> retrieveStatistics(@PathVariable("id") String id) {
        List<Statistics> statisticsList = new ArrayList<>();
        gameTimelines
                .stream()
                .filter(gameTimeline -> gameTimeline.getUuid().toString().equals(id))
                .forEach(gameTimeline -> {
                    gameTimeline.getStatistics().refreshStatistics();
                    statisticsList.add(gameTimeline.getStatistics());
                });
        return new ResponseEntity<>(statisticsList, HttpStatus.OK);
    }

    @PostMapping("/pause")
    public ResponseEntity<Object> pause() {
        gameTimelines.forEach(GameTimeline::pauseTimer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/pause/{id}")
    public ResponseEntity<Object> pause(@PathVariable("id") String id) {

        gameTimelines
                .stream()
                .filter(gameTimeline -> gameTimeline.getUuid().toString().equals(id))
                .forEach(GameTimeline::pauseTimer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/resume/{id}")
    public ResponseEntity<Object> resume(@PathVariable("id") String id) {

        gameTimelines
                .stream()
                .filter(gameTimeline -> gameTimeline.getUuid().toString().equals(id))
                .forEach(GameTimeline::resumeTimer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/resume")
    public ResponseEntity<Object> resume() {
        gameTimelines.forEach(GameTimeline::resumeTimer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
