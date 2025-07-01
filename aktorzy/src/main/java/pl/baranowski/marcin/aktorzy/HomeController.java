package pl.baranowski.marcin.aktorzy;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.CompletionStage;

@RestController
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final ActorRef studentActor;

    @Autowired
    public HomeController(ActorRef studentActor) {
        this.studentActor = studentActor;
    }

    @GetMapping("/")
    public String hello() {
        return "Hello Marcin";
    }

    @GetMapping("/students/block/{seconds}")
    public CompletionStage<String> student(@PathVariable Integer seconds) {
        log.info("Received request to block for {} seconds", seconds);

        // Wysłanie wiadomości do aktora
        Future<Object> future = Patterns.ask(studentActor, seconds, Timeout.durationToTimeout(Duration.create(60, "seconds")));

        // Konwersja Scala Future na Java CompletionStage
        return scala.compat.java8.FutureConverters.toJava(future).thenApply(Object::toString);
    }
}


