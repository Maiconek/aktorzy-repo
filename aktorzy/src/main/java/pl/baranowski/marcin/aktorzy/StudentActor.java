package pl.baranowski.marcin.aktorzy;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

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
                    ActorRef senderRef = getSender();  

                    CompletableFuture
                            .supplyAsync(() -> {
                                try {
                                    ResponseEntity<Void> result = restClient.get()
                                            .uri("/students/block/" + seconds)
                                            .retrieve()
                                            .toBodilessEntity();
                                    return result.getStatusCode().toString();
                                } catch (Exception e) {
                                    return "ERROR: " + e.getMessage(); 
                                }
                            })
                            .thenAccept(response -> senderRef.tell(response, getSelf()));

                })
                .build();
    }
}

