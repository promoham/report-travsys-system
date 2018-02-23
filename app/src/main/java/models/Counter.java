package models;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by eagle on 11/6/2017.
 */
@Entity
public class Counter{
    @Id
    public long id;
    public int placeId;

    public Counter() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }
}
