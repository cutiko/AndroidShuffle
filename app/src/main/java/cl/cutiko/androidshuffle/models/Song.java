package cl.cutiko.androidshuffle.models;

/**
 * Created by cutiko on 15-07-16.
 */
public class Song {

    private String name;
    private long id;

    public Song(String name, String id) {
        this.name = name;
        this.id = Long.parseLong(id);
    }

    public String getName() {
        return name;
    }

    public Song(long id) {
        this.id = id;
    }
}
