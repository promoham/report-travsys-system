package models;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by eagle on 11/6/2017.
 */
@Entity
public class Place{
    @Id
    public long id;
    public String name;

    public Place() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
