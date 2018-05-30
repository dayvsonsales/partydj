package regressao.room;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        mobi.dayvson.redes.partydj.models.Room room1 = new mobi.dayvson.redes.partydj.models.Room("");
        mobi.dayvson.redes.partydj.models.User user2 = null;
        try {
            room1.addUser(user2);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Usuario não pode ser vazio");
        } catch (java.lang.IllegalArgumentException e) {
        }
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        mobi.dayvson.redes.partydj.models.Room room1 = new mobi.dayvson.redes.partydj.models.Room("");
        java.lang.String str2 = room1.getVideoQueueJson();
        mobi.dayvson.redes.partydj.models.Video video3 = room1.nextVideo();
        java.lang.String str4 = room1.getVideoQueueJson();
        mobi.dayvson.redes.partydj.models.Video video5 = room1.nextVideo();
        org.junit.Assert.assertTrue("'" + str2 + "' != '" + "[]" + "'", str2.equals("[]"));
        org.junit.Assert.assertNull(video3);
        org.junit.Assert.assertTrue("'" + str4 + "' != '" + "[]" + "'", str4.equals("[]"));
        org.junit.Assert.assertNull(video5);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        mobi.dayvson.redes.partydj.models.Room room1 = new mobi.dayvson.redes.partydj.models.Room("");
        mobi.dayvson.redes.partydj.models.Video video2 = null;
        room1.sendVideoToEveryoneOnRoom(video2);
        boolean boolean5 = room1.equals((java.lang.Object) 1);
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        mobi.dayvson.redes.partydj.models.Room room1 = new mobi.dayvson.redes.partydj.models.Room("");
        java.lang.String str2 = room1.getVideoQueueJson();
        mobi.dayvson.redes.partydj.models.Video video3 = room1.nextVideo();
        mobi.dayvson.redes.partydj.models.Video video4 = null;
        room1.addVideo(video4);
        java.lang.Class<?> wildcardClass6 = room1.getClass();
        org.junit.Assert.assertTrue("'" + str2 + "' != '" + "[]" + "'", str2.equals("[]"));
        org.junit.Assert.assertNull(video3);
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        mobi.dayvson.redes.partydj.models.Room room1 = new mobi.dayvson.redes.partydj.models.Room("");
        java.lang.String str2 = room1.getVideoQueueJson();
        mobi.dayvson.redes.partydj.models.Video video3 = room1.nextVideo();
        boolean boolean5 = room1.equals((java.lang.Object) 10.0d);
        org.junit.Assert.assertTrue("'" + str2 + "' != '" + "[]" + "'", str2.equals("[]"));
        org.junit.Assert.assertNull(video3);
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        mobi.dayvson.redes.partydj.models.Room room1 = new mobi.dayvson.redes.partydj.models.Room("");
        java.lang.String str2 = room1.getVideoQueueJson();
        mobi.dayvson.redes.partydj.models.Video video3 = room1.nextVideo();
        mobi.dayvson.redes.partydj.models.Video video4 = null;
        room1.sendVideoToEveryoneOnRoom(video4);
        room1.sendNoVideoToEveryoneOnRoom();
        org.junit.Assert.assertTrue("'" + str2 + "' != '" + "[]" + "'", str2.equals("[]"));
        org.junit.Assert.assertNull(video3);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        mobi.dayvson.redes.partydj.models.Room room1 = new mobi.dayvson.redes.partydj.models.Room("");
        java.lang.String str2 = room1.getVideoQueueJson();
        mobi.dayvson.redes.partydj.models.Video video3 = room1.nextVideo();
        java.lang.String str4 = room1.getVideoQueueJson();
        room1.sendNoVideoToEveryoneOnRoom();
        java.util.List<mobi.dayvson.redes.partydj.models.User> userList6 = room1.getUserList();
        org.junit.Assert.assertTrue("'" + str2 + "' != '" + "[]" + "'", str2.equals("[]"));
        org.junit.Assert.assertNull(video3);
        org.junit.Assert.assertTrue("'" + str4 + "' != '" + "[]" + "'", str4.equals("[]"));
        org.junit.Assert.assertNotNull(userList6);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        mobi.dayvson.redes.partydj.models.Room room1 = new mobi.dayvson.redes.partydj.models.Room(" 0 1");
    }
}

