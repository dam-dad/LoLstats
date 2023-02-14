package dad.LoLstats;

import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

public class LoginController implements Initializable{

    @FXML private BorderPane view;

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



    }

}
