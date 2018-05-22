package models;

import mobi.dayvson.redes.partydj.enums.Message;
import mobi.dayvson.redes.partydj.models.Protocol;
import mobi.dayvson.redes.partydj.models.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class VideoTest {

    private Video video;

    @BeforeEach
    void setUp(){
        video = new Video("youtube.com", "thumbnail.youtube.com", "PT20M1S", "Teste");
    }

    @Test
    void getDurationMilliseconds(){
        assertEquals(1201000, video.getDurationMilliseconds());
    }

    @Test
    void getUrlId(){
        assertEquals("youtube.com", video.getUrlId());
    }

    @Test
    void getThumbnail(){
        assertEquals("thumbnail.youtube.com", video.getThumbnail());
    }

    @Test
    void getVideoName(){
        assertEquals("Teste", video.getVideoName());
    }

    @Test
    void equalsTeste(){
        Video video2 = new Video("youtube.com", "thumbnail.youtube.com", "PT20M1S", "Teste");

        assertThat(video2, is(video));
    }

    @Test
    void noEqualsTeste(){
        Video video2 = new Video("youtu.com", "thumbnail.youtube.com", "PT20M1S", "Teste");

        assertFalse(video2.equals(video));
    }

    @Test
    void formatoDuracaoErrada(){
        try {
            Video video2 = new Video("youtu.com", "thumbnail.youtube.com", "P0M1S", "Teste");
            fail("Esperado exceção DateTimeFormatException");
        }catch (DateTimeParseException e){

        }
    }

    @Test
    void toStringJsonTest(){
        assertEquals("{\"urlId\":\"youtube.com\",\"thumbnail\":\"thumbnail.youtube.com\",\"videoName\":\"Teste\",\"durationMilliseconds\":1201000}", video.toString());
    }

    @Test
    void hashCodeTest(){
        assertEquals("1781895295", video.hashCode());
    }

}
