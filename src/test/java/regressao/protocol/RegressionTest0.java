package regressao.protocol;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test1() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test1");
        mobi.dayvson.redes.partydj.enums.Message message0 = null;
        try {
            java.lang.String str1 = mobi.dayvson.redes.partydj.models.Protocol.convert(message0);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: null");
        } catch (java.lang.NullPointerException e) {
        }
    }

    @Test
    public void test2() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test2");
        mobi.dayvson.redes.partydj.enums.Message message1 = mobi.dayvson.redes.partydj.models.Protocol.proccess("");
        java.lang.Class<?> wildcardClass2 = message1.getClass();
        java.lang.String str3 = mobi.dayvson.redes.partydj.models.Protocol.convert(message1);
        java.lang.Class<?> wildcardClass4 = message1.getClass();
        java.lang.Class<?> wildcardClass5 = message1.getClass();
        org.junit.Assert.assertTrue("'" + message1 + "' != '" + mobi.dayvson.redes.partydj.enums.Message.UNKNOW + "'", message1.equals(mobi.dayvson.redes.partydj.enums.Message.UNKNOW));
        org.junit.Assert.assertNotNull(wildcardClass2);
        org.junit.Assert.assertTrue("'" + str3 + "' != '" + "unknow" + "'", str3.equals("unknow"));
        org.junit.Assert.assertNotNull(wildcardClass4);
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test3() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test3");
        mobi.dayvson.redes.partydj.enums.Message message1 = mobi.dayvson.redes.partydj.models.Protocol.proccess("hi!");
        java.lang.String str2 = mobi.dayvson.redes.partydj.models.Protocol.convert(message1);
        java.lang.String str3 = mobi.dayvson.redes.partydj.models.Protocol.convert(message1);
        org.junit.Assert.assertTrue("'" + message1 + "' != '" + mobi.dayvson.redes.partydj.enums.Message.UNKNOW + "'", message1.equals(mobi.dayvson.redes.partydj.enums.Message.UNKNOW));
        org.junit.Assert.assertTrue("'" + str2 + "' != '" + "unknow" + "'", str2.equals("unknow"));
        org.junit.Assert.assertTrue("'" + str3 + "' != '" + "unknow" + "'", str3.equals("unknow"));
    }

    @Test
    public void test4() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test4");
        mobi.dayvson.redes.partydj.enums.Message message1 = mobi.dayvson.redes.partydj.models.Protocol.proccess("");
        java.lang.Class<?> wildcardClass2 = message1.getClass();
        java.lang.Class<?> wildcardClass3 = message1.getClass();
        org.junit.Assert.assertTrue("'" + message1 + "' != '" + mobi.dayvson.redes.partydj.enums.Message.UNKNOW + "'", message1.equals(mobi.dayvson.redes.partydj.enums.Message.UNKNOW));
        org.junit.Assert.assertNotNull(wildcardClass2);
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test5() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test5");
        mobi.dayvson.redes.partydj.enums.Message message1 = mobi.dayvson.redes.partydj.models.Protocol.proccess("");
        java.lang.Class<?> wildcardClass2 = message1.getClass();
        java.lang.String str3 = mobi.dayvson.redes.partydj.models.Protocol.convert(message1);
        java.lang.String str4 = mobi.dayvson.redes.partydj.models.Protocol.convert(message1);
        java.lang.Class<?> wildcardClass5 = message1.getClass();
        org.junit.Assert.assertTrue("'" + message1 + "' != '" + mobi.dayvson.redes.partydj.enums.Message.UNKNOW + "'", message1.equals(mobi.dayvson.redes.partydj.enums.Message.UNKNOW));
        org.junit.Assert.assertNotNull(wildcardClass2);
        org.junit.Assert.assertTrue("'" + str3 + "' != '" + "unknow" + "'", str3.equals("unknow"));
        org.junit.Assert.assertTrue("'" + str4 + "' != '" + "unknow" + "'", str4.equals("unknow"));
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test6() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test6");
        java.lang.Object obj0 = new java.lang.Object();
        java.lang.Class<?> wildcardClass1 = obj0.getClass();
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test7() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test7");
        mobi.dayvson.redes.partydj.enums.Message message1 = mobi.dayvson.redes.partydj.models.Protocol.proccess("hi!");
        java.lang.String str2 = mobi.dayvson.redes.partydj.models.Protocol.convert(message1);
        java.lang.Class<?> wildcardClass3 = message1.getClass();
        java.lang.String str4 = mobi.dayvson.redes.partydj.models.Protocol.convert(message1);
        java.lang.Class<?> wildcardClass5 = message1.getClass();
        java.lang.String str6 = mobi.dayvson.redes.partydj.models.Protocol.convert(message1);
        org.junit.Assert.assertTrue("'" + message1 + "' != '" + mobi.dayvson.redes.partydj.enums.Message.UNKNOW + "'", message1.equals(mobi.dayvson.redes.partydj.enums.Message.UNKNOW));
        org.junit.Assert.assertTrue("'" + str2 + "' != '" + "unknow" + "'", str2.equals("unknow"));
        org.junit.Assert.assertNotNull(wildcardClass3);
        org.junit.Assert.assertTrue("'" + str4 + "' != '" + "unknow" + "'", str4.equals("unknow"));
        org.junit.Assert.assertNotNull(wildcardClass5);
        org.junit.Assert.assertTrue("'" + str6 + "' != '" + "unknow" + "'", str6.equals("unknow"));
    }

    @Test
    public void test8() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test8");
        mobi.dayvson.redes.partydj.enums.Message message1 = mobi.dayvson.redes.partydj.models.Protocol.proccess("unknow");
        java.lang.String str2 = mobi.dayvson.redes.partydj.models.Protocol.convert(message1);
        java.lang.String str3 = mobi.dayvson.redes.partydj.models.Protocol.convert(message1);
        org.junit.Assert.assertTrue("'" + message1 + "' != '" + mobi.dayvson.redes.partydj.enums.Message.UNKNOW + "'", message1.equals(mobi.dayvson.redes.partydj.enums.Message.UNKNOW));
        org.junit.Assert.assertTrue("'" + str2 + "' != '" + "unknow" + "'", str2.equals("unknow"));
        org.junit.Assert.assertTrue("'" + str3 + "' != '" + "unknow" + "'", str3.equals("unknow"));
    }
}

