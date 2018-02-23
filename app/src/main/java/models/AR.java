package models;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by eagle on 11/6/2017.
 */

@Entity
public class AR {
    @Id
    public long id;
    public String code;
    public String name;


    public AR() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
