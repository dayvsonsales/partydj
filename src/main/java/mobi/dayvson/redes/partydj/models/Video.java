package mobi.dayvson.redes.partydj.models;

import com.google.gson.Gson;

import java.util.Objects;

public class Video {

    private final String urlId;
    private final String thumbnail;

    public Video(String urlId, String thumbnail) {
        this.urlId = urlId;
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(urlId, video.urlId) &&
                Objects.equals(thumbnail, video.thumbnail);
    }

    @Override
    public int hashCode() {

        return Objects.hash(urlId, thumbnail);
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
