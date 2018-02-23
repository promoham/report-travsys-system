package models;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by eagle on 11/6/2017.
 */
@Entity
public class PC {
    public String name;
    public int counterId;
    @Id
    public long id;

    public PC() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCounterId() {
        return counterId;
    }

    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
