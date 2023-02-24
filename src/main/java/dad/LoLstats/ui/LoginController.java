package dad.LoLstats.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.LoLstats.api.LeagueEntry;
import dad.LoLstats.api.Summoner;
import dad.LoLstats.api.SummonerService;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class LoginController implements Initializable{

    @FXML private BorderPane view;

    @FXML private TextField userInput;

    @FXML private Label title;

    @FXML private ChoiceBox<String> serverSelector;

    public static String selectedServer;

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
        view.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/cursors/normal.png"),128,128,true,true)));
        App.stage.setResizable(false);
        

        BackgroundImage bImage = new BackgroundImage(new Image(getClass().getResourceAsStream("/images/Kaisa_0.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        view.setBackground(new Background(bImage));
       

        String[] servers = {"Brasil","Europa del Norte y Este","Europa del Oeste","Latinoamérica del Norte","Latinoamérica del Sur","América del Norte","Rusia","Oceanía","Japón","Turquía","Corea"};

        serverSelector.getItems().addAll(servers);
        serverSelector.setValue("Server");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/font/Friz Quadrata Regular.ttf"), 80));

        setApiKey();
    }

    @FXML private void getUser(){

        String region = getRegion();

        try {
            
            SummonerService summService = new SummonerService(App.API_KEY, region);
            
            Summoner summoner = summService.getSummoner(userInput.getText());

            ArrayList<LeagueEntry> elos = summService.getElos(summoner.getId());
            App.summoner = summoner;
            StatController stat = new StatController(elos, region);
            App.stage.setScene(new Scene(stat.getView()));


        } catch (Exception e) {
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("ERROR");
            alerta.setContentText(e.getMessage());
            e.printStackTrace();
            alerta.show();
        }
    }

    private String getRegion() {
    
        String retorno;
        switch (serverSelector.getValue()) {
            case "Europa del Oeste":
                retorno = "euw1";
                break;
        
            case "América del Norte":
                retorno = "na1";
                break;
            
            case "Latinoamérica del Norte":
                retorno = "la1";
                break;
            
            case "Latinoamérica del Sur":
                retorno = "la2";
                break;

            case "Oceanía":
                retorno = "oce";
                break;

            case "Europa del Norte y Este":
                retorno = "eun1";
                break;

            case "Rusia":
                retorno = "ru1";
                break;

            case "Turquía":
                retorno = "tr1";
                break;

            case "Corea":
                retorno = "kr";
                break;

            case "Brasil":
                retorno = "br1";
                break;

            case "Japón":
                retorno = "jp1";
                break;

            default:
                retorno = "euw1";
                break;
        }

        return retorno;
    }


    private void setApiKey(){
        TextInputDialog dialog = new TextInputDialog();
        try{
        Hyperlink link = new Hyperlink("aquí");
        link.setOnAction(e -> {
            try {
                goToApi(e);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        dialog.setTitle("INSERTA LA API KEY");
        dialog.setContentText("Si no sabes cómo obtener la tuya pincha " + link.getText());
        Optional<String> datosRecogidos = dialog.showAndWait();
        App.API_KEY = datosRecogidos.get();
    } catch (NoSuchElementException e1) {
        System.exit(1);;
    }
    }

    private void goToApi(ActionEvent e) throws IOException, URISyntaxException{
        java.awt.Desktop.getDesktop().browse(new URI("https://www.developer.riotgames.com"));
    }

}
