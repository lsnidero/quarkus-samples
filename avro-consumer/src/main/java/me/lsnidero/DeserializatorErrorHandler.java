package me.lsnidero;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import io.smallrye.reactive.messaging.kafka.DeserializationFailureHandler;
import me.lsnidero.quarkus.Movie;
import org.apache.kafka.common.header.Headers;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
@Identifier("failure-dead-letter") // Name of the failure handler
public class DeserializatorErrorHandler implements DeserializationFailureHandler<Movie> {

    @Inject
    @Channel("dead-letter")
    Emitter<DeadLetterBean> dltEmitter;

    @Override
    public Movie handleDeserializationFailure(String topic, boolean isKey, String deserializer, byte[] data, Exception exception, Headers headers) {
        Log.infof("DLT: %s", exception.getMessage());
        dltEmitter.send(Message.of(new DeadLetterBean(topic, isKey, deserializer, data, exception))
                .withAck(() -> {
                    // Called when the message is acked
                    Log.error("SENT TO DEAD LETTER");
                    return CompletableFuture.completedFuture(null);
                })

                .withNack(throwable -> {
                    // Called when the message is nacked
                    Log.error("ERROR, NOT SENT DEAD LETTER");
                    return CompletableFuture.completedFuture(null);
                }));
        return Movie.newBuilder().setTitle("ERROR").setYear(0).setImdbRate(0).setRottenTomatoesRate(0).build();
    }

    public static class DeadLetterBean {
        public String topic;
        public boolean isKey;
        public String deserializer;
        public byte[] data;
        public Throwable exception;

        public DeadLetterBean(String topic, boolean isKey, String deserializer, byte[] data, Throwable exception) {
            this.topic = topic;
            this.isKey = isKey;
            this.deserializer = deserializer;
            this.data = data;
            this.exception = exception;
        }
    }
}
