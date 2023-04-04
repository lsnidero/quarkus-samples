package me.lsnidero.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;

@MongoEntity(collection="TheMovie")
public class NoSqlMovie extends PanacheMongoEntity {

    public String code;
    @BsonProperty("movieTitle")
    public String title;
    public int year;
    public String description;
    @BsonProperty("watchedOn")
    public LocalDate whenWatched;

}
