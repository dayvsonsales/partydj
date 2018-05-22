package integracao;

import static org.junit.jupiter.api.Assertions.*;

import mobi.dayvson.redes.partydj.App;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class IntegracaoTest {

    private static final String URL_BASE = "http://localhost:4000";

    private WebDriver webDriver;


    @BeforeEach
    void setUp(){
        System.setProperty("webdriver.gecko.driver", "/Users/dayvsonsales/partydj/geckodriver");
        webDriver = new FirefoxDriver();
    }

    @AfterEach
    void closeDriver(){
        webDriver.close();
    }

    @Test
    void criarSala(){

        webDriver.get("http://localhost:4000");

        WebElement elementNome = webDriver.findElement(By.id("nome"));

        elementNome.sendKeys("Dayvson");

        webDriver.findElement(By.id("criar")).click();

        WebElement elementUsersCount = webDriver.findElement(By.id("users-count"));

        WebElement elementDono = webDriver.findElement(By.id("nome-dono"));

        WebElement elementTokenSala = webDriver.findElement(By.id("numero-token-sala"));

        Integer usuariosEsperado = 1;
        String donoEsperado = "Dayvson";

        assertAll(() -> {
            assertEquals(usuariosEsperado.toString(), elementUsersCount.getText());
            assertEquals(donoEsperado, elementDono.getText());
            assertTrue(!elementTokenSala.getText().equals(""));
        });
    }

    @Test
    void entrarSala(){

        webDriver.get(URL_BASE);

        WebElement elementNome = webDriver.findElement(By.id("nome"));

        elementNome.sendKeys("Dayvson");

        webDriver.findElement(By.id("criar")).click();

        WebElement elementTokenSala = webDriver.findElement(By.id("numero-token-sala"));

        WebDriver newDriver = new FirefoxDriver();
        newDriver.get(URL_BASE);

        WebElement element = newDriver.findElement(By.id("token-sala"));
        element.sendKeys(elementTokenSala.getText());

        elementNome = newDriver.findElement(By.id("nome"));
        elementNome.sendKeys("Maria");

        WebElement elementClickToken = newDriver.findElement(By.id("entrar"));
        elementClickToken.click();

        Integer usuariosEsperado = 2;
        String donoEsperado = "Dayvson";
        String tokenEsperado = elementTokenSala.getText();


        WebElement elementUsersCount = webDriver.findElement(By.id("users-count"));

        WebElement elementDono = webDriver.findElement(By.id("nome-dono"));

        WebElement _elementTokenSala = webDriver.findElement(By.id("numero-token-sala"));


        assertAll(() -> {
            assertEquals(usuariosEsperado.toString(), elementUsersCount.getText());
            assertEquals(donoEsperado, elementDono.getText());
            assertEquals(tokenEsperado, _elementTokenSala.getText());
        });

        newDriver.close();
    }

    @Test
    void enviarMensagemParaSala(){

        webDriver.get(URL_BASE);

        WebElement elementNome = webDriver.findElement(By.id("nome"));

        elementNome.sendKeys("Dayvson");

        webDriver.findElement(By.id("criar")).click();

        WebElement elementTokenSala = webDriver.findElement(By.id("numero-token-sala"));

        WebDriver newDriver = new FirefoxDriver();
        newDriver.get(URL_BASE);

        WebElement element = newDriver.findElement(By.id("token-sala"));
        element.sendKeys(elementTokenSala.getText());

        elementNome = newDriver.findElement(By.id("nome"));
        elementNome.sendKeys("Maria");

        WebElement elementClickToken = newDriver.findElement(By.id("entrar"));
        elementClickToken.click();

        //Dayvson enviando a mensagem
        WebElement text = webDriver.findElement(By.id("enter"));
        text.sendKeys("Oi, tudo bem?");
        text.sendKeys(Keys.ENTER);

        //Maria enviando mensagem de volta
        text = newDriver.findElement(By.id("enter"));
        text.sendKeys("Tudo sim! E com vc?");
        text.sendKeys(Keys.ENTER);

        //chat do Dayvson

        WebElement chat = webDriver.findElement(By.id("chat"));
        List<WebElement> list = chat.findElements(By.tagName("li"));

        String msgDayvson = list.get(1).getText();
        String msgMaria = list.get(2).getText();

        newDriver.close();

        assertAll(() -> {
            assertEquals("Dayvson: Oi, tudo bem?", msgDayvson);
            assertEquals("Maria: Tudo sim! E com vc?", msgMaria);
        });
    }

    @Test
    void pedirVideo(){

        webDriver.get(URL_BASE);

        WebElement elementNome = webDriver.findElement(By.id("nome"));

        elementNome.sendKeys("Dayvson");

        webDriver.findElement(By.id("criar")).click();

        WebElement elementTokenSala = webDriver.findElement(By.id("numero-token-sala"));

        WebDriver newDriver = new FirefoxDriver();
        newDriver.get(URL_BASE);

        WebElement element = newDriver.findElement(By.id("token-sala"));
        element.sendKeys(elementTokenSala.getText());

        elementNome = newDriver.findElement(By.id("nome"));
        elementNome.sendKeys("Maria");

        WebElement elementClickToken = newDriver.findElement(By.id("entrar"));
        elementClickToken.click();

        //Dayvson pedindo música
        WebElement elementPedirMusica = webDriver.findElement(By.className("open-popup"));
        elementPedirMusica.click();

        WebElement elementNomeMusica = webDriver.findElement(By.id("video-search"));
        elementNomeMusica.sendKeys("Mariah Carey - Hero");
        elementNomeMusica.sendKeys(Keys.ENTER);

        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        WebElement elementListVideo = webDriver.findElement(By.id("list-video"));
        List<WebElement> list = elementListVideo.findElements(By.tagName("li"));
        WebElement video = list.get(0);
        WebElement div = video.findElement(By.tagName("div"));
        List<WebElement> divs = div.findElements(By.tagName("div"));
        divs.get(2).findElement(By.tagName("button")).click();

        WebElement nameVideo = webDriver.findElement(By.id("name-video"));
        String nomeDoVideo = nameVideo.findElement(By.tagName("h3")).getText();


        assertAll(() -> {
            assertEquals("Mariah Carey - Hero (Video)", nomeDoVideo);
        });

        newDriver.close();
    }

}
