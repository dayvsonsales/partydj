package mobi.dayvson.redes.partydj.models;

import com.google.gson.Gson;

import java.time.Duration;
import java.util.Objects;

public class Video {

    private final String urlId;
    private final String thumbnail;
    private final String videoName;
    private final long durationMilliseconds;

    public Video(String urlId, String thumbnail, String duration, String videoName) {
        this.urlId = urlId;
        this.thumbnail = thumbnail;
        this.durationMilliseconds = Duration.parse(duration).toMillis();
        this.videoName = videoName;
    }

    public long getDurationMilliseconds() {
        return durationMilliseconds;
    }

    public String getUrlId() {
        return urlId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getVideoName() {
        return videoName;
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
