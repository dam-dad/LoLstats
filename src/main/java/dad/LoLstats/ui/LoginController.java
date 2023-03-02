package dad.LoLstats.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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

        BackgroundImage bImage = new BackgroundImage(new Image(getClass().getResourceAsStream("/images/Kaisa_0.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        view.setBackground(new Background(bImage));
       

        String[] servers = {"Brassil","North & East Europe","West Europe","North Latin America","South Latin America","North America","Russia","Oceania","Japan","Turkey","Corea"};

        serverSelector.getItems().addAll(servers);
        serverSelector.setValue("Server");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/font/Friz Quadrata Regular.ttf"), 80));
        setApiKey();
    }

    @FXML private void getUser(){
        view.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/cursors/unavailable.png"))));
        String region = getRegion();

        try {
            
            SummonerService summService = new SummonerService(App.API_KEY, region);
            
            Summoner summoner = summService.getSummoner(userInput.getText());

            ArrayList<LeagueEntry> elos = summService.getElos(summoner.getId());
            App.summoner = summoner;
            StatController stat = new StatController(elos, region);
            App.statScene = new Scene(stat.getView());
            App.stage.setScene(App.statScene);
            if(Objects.isNull(App.loginScene))
                App.loginScene = new Scene(this.getView());
            else
                App.loginScene.setRoot(this.view);
            userInput.setText("");
            serverSelector.setValue("Server");
            App.stage.show();

        } catch (Exception e) {
            view.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/cursors/normal.png"))));
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
            case "West Europe":
                retorno = "euw1";
                break;
        
            case "North America":
                retorno = "na1";
                break;
            
            case "North Latin America":
                retorno = "la1";
                break;
            
            case "South Latin America":
                retorno = "la2";
                break;

            case "Oceania":
                retorno = "oce";
                break;

            case "North & South Europe":
                retorno = "eun1";
                break;

            case "Russia":
                retorno = "ru1";
                break;

            case "Turkey":
                retorno = "tr1";
                break;

            case "Corea":
                retorno = "kr";
                break;

            case "Brasil":
                retorno = "br1";
                break;

            case "Japan":
                retorno = "jp1";
                break;

            default:
                retorno = "euw1";
                break;
        }

        return retorno;
    }


    private void setApiKey(){
        try{
        TextInputDialog dialog = new TextInputDialog();
            dialog.getDialogPane().getButtonTypes().clear();
            
            ImageView dView = new ImageView(new Image(getClass().getResourceAsStream("/images/key-icon.png")));
            dView.setFitWidth(120);
            dView.setFitHeight(60);
            dialog.setGraphic(dView);

            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(dView.getImage());
            final ButtonType apiType = new ButtonType("GET",ButtonData.YES);
            ButtonType continuar = new ButtonType("NEXT", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("CANCEL",ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(apiType,continuar,cancelar);
            Button goApiButton = (Button) dialog.getDialogPane().lookupButton(apiType);
            EventHandler<ActionEvent> filter = event -> {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI("https://developer.riotgames.com"));
                    event.consume();
                } catch (IOException | URISyntaxException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    event.consume();
                }
            };

            goApiButton.addEventFilter(ActionEvent.ACTION, filter);
            dialog.setTitle("API KEY");
            dialog.setHeaderText("INSERT API KEY");
            dialog.setContentText("If you don't know how to get it just click in the button below.");
            Optional<String> datosRecogidos = dialog.showAndWait();
            App.API_KEY = datosRecogidos.get();
        } catch(NoSuchElementException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}

