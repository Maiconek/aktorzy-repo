package pl.baranowski.marcin.aktorzy;
import akka.actor.AbstractActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class StudentActor extends AbstractActor {
    private static final Logger log = LoggerFactory.getLogger(StudentActor.class);

    private final RestClient restClient;

    public StudentActor(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, seconds -> {
                    // Przetwarzanie żądania
                    log.info("Processing block request for {} seconds", seconds);
                    ResponseEntity<Void> result = restClient.get()
                            .uri("/students/block/" + seconds)
                            .retrieve()
                            .toBodilessEntity();

                    // Odpowiedź
                    getSender().tell(result.getStatusCode().toString(), getSelf());
                })
                .build();
    }
}

