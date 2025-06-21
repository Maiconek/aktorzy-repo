package pl.baranowski.marcin.aktorzy;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AkkaConfig {

    @Bean
    public ActorSystem actorSystem() {
        return ActorSystem.create("SpringAkkaSystem");
    }

    @Bean
    public ActorRef studentActor(ActorSystem actorSystem, RestClient restClient) {
        return actorSystem.actorOf(
                new RoundRobinPool(150).props(Props.create(StudentActor.class, restClient)),
                "studentActorRouter"
        );
    }
}
