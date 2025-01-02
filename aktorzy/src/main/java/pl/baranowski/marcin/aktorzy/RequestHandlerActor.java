package pl.baranowski.marcin.aktorzy;
import akka.actor.AbstractActor;
import akka.actor.Props;

public class RequestHandlerActor extends AbstractActor {

    public static Props props() {
        return Props.create(RequestHandlerActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    String response = "Processed: " + message;
                    getSender().tell(response, getSelf());
                })
                .build();
    }
}
