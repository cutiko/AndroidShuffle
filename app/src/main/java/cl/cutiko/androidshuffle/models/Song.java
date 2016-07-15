package cl.cutiko.androidshuffle.models;

/**
 * Created by cutiko on 15-07-16.
 */
public class Song {

    private String name;
    private long id;

    public Song(String id, String name) {
        this.id = Long.parseLong(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Song(long id) {
        this.id = id;
    }
}
