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
    public ActorRef studentActor(ActorSystem actorSystem, RestClient.Builder restClientBuilder) {
        RestClient restClient = restClientBuilder
                .baseUrl("https://vt-app-serviceapp-dmbthkaqe6a3ffh0.polandcentral-01.azurewebsites.net")
                .build();
        return actorSystem.actorOf(
                new RoundRobinPool(5).props(Props.create(StudentActor.class, restClient)),
                "studentActorRouter"
        );    }
}
