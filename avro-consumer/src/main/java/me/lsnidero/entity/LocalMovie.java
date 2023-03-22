package me.lsnidero.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class LocalMovie extends PanacheEntity {
    public String code;
    public String title;
    public int year;
    public String description;
    public LocalDate whenWatched;


}
