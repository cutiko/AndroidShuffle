package cl.cutiko.androidshuffle.models;

/**
 * Created by cutiko on 15-07-16.
 */
public class Song {

    private String id, name;

    public Song(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
