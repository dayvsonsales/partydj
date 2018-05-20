package models;

import mobi.dayvson.redes.partydj.enums.Message;
import mobi.dayvson.redes.partydj.models.Protocol;
import mobi.dayvson.redes.partydj.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserTest {

    private User user;

    @BeforeEach
    void setUp(){
        user = new User(null, "Dayvson", null);
    }

    @Test
    void getName(){
        assertEquals("Dayvson", user.getName());
    }

    @Test
    void getWebSocket(){
        assertEquals(null, user.getWebSocket());
    }

    @Test
    void equalsTeste(){
        User user2 = new User(null, "Dayvson", null);

        assertTrue(user2.equals(user));
    }

    @Test
    void noEqualsTeste(){
        User user2 = new User(null, "ayvson", null);

        assertFalse(user2.equals(user));
    }


}
