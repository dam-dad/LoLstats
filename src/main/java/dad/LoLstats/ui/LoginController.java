package dad.LoLstats.ui;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;

public class LoginController implements Initializable{

    @FXML private BorderPane view;

    @FXML private TextField userInput;

    @FXML private ChoiceBox<String> serverSelector;

    public LoginController(){
        try{
            FXMLLoader l = new FXMLLoader(getClass().getResource("/fxml/loginView.fxml"));
            l.setController(this);
            l.load();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public BorderPane getView(){
        return view;
    }

    public void initialize(URL location, ResourceBundle resources){

        try {
            URL url = new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/Kaisa_0.jpg");
            BufferedImage bfimage = ImageIO.read(url);
            BackgroundImage bImage = new BackgroundImage(SwingFXUtils.toFXImage(bfimage, null), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
            view.setBackground(new Background(bImage));
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String[] servers = {"BR","EUN","EUW","LAN","LAS","NA","RU","OCE"};

        serverSelector.getItems().addAll(servers);

    }

    @FXML private void getUser(){
        
    }

}
