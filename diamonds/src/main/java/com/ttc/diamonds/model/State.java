package com.ttc.diamonds.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="states")
public class State {

    @Id
    private int id;
    private String abbr;
    private String name;

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", abbr='" + abbr + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return getId() == state.getId() &&
                Objects.equals(getAbbr(), state.getAbbr()) &&
                Objects.equals(getName(), state.getName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getAbbr(), getName());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
