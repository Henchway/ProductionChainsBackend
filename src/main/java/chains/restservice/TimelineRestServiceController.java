package chains.restservice;

import chains.timeline.GameTimeline;
import chains.utility.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

@RestController
@CrossOrigin
public class TimelineRestServiceController {


    private final GameTimeline gameTimeline;
    private final Statistics statistics;

    @Autowired
    public TimelineRestServiceController(GameTimeline gameTimeline, Statistics statistics) {
        this.gameTimeline = gameTimeline;
        this.statistics = statistics;
    }

    @PostConstruct
    @PostMapping("/start")
    public ResponseEntity<Object> startTimeline() {

        gameTimeline.setStatistics(statistics);
        gameTimeline.startPopulation();
        createTimer();
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello";
    }


    void createTimer() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                GameTimeline.setYearsPassed(GameTimeline.getYearsPassed() + 1);
                gameTimeline.processNewYear();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, GameTimeline.getDurationOfYear());

    }

    @GetMapping("/statistics")
    public Statistics retrieveStatistics() {
        statistics.refreshStatistics();
        return statistics;

    }


}
