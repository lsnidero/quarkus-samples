package me.lsnidero;

import io.quarkus.logging.Log;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApplicationService {

    public String getSome(){
        Log.info("on app service");
        return "some";
    }
}
