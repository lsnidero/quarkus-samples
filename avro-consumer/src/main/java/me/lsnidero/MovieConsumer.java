package me.lsnidero;

import me.lsnidero.entity.LocalMovie;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import io.quarkus.logging.Log;
import me.lsnidero.entity.NoSqlMovie;
import me.lsnidero.quarkus.Movie;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;

@ApplicationScoped
@ActivateRequestContext
public class MovieConsumer {


    @Incoming("movies-from-kafka")
    @ReactiveTransactional
    public Uni<LocalMovie> receive(Movie movie) {
        Log.infof("Ho ricevuto il film: %s (%d)", movie.getTitle(), movie.getYear());

        LocalMovie lMovie = new LocalMovie();
        lMovie.code = extractCode(movie.getTitle());
        lMovie.description = "Awesome movie";
        lMovie.year = movie.getYear();
        lMovie.title = movie.getTitle();
        lMovie.whenWatched = randomDate();
        Log.info("Persisted movie on SQL DB");
        persistAlsoInNoSQL(lMovie);
        Log.info("Persisted movie on NO SQL DB");
        return lMovie.persist();
    }

    private void persistAlsoInNoSQL(LocalMovie movie) {
        NoSqlMovie nMovie = new NoSqlMovie();
        nMovie.code = movie.code;
        nMovie.description = movie.description;
        nMovie.title = movie.title;
        nMovie.year = movie.year;
        nMovie.whenWatched = movie.whenWatched;
        nMovie.persist();
    }


    private static String extractCode(String movieTitle) {
        return UUID.randomUUID().toString();
    }

    private static LocalDate randomDate() {
        long minDay = LocalDate.of(1990, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2022, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }
}
