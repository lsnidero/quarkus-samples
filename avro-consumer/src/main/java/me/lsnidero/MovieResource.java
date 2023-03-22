package me.lsnidero;

import me.lsnidero.entity.LocalMovie;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {

    @GET
    public Uni<List<LocalMovie>> get() {
        return LocalMovie.listAll();
    }

      ;

}