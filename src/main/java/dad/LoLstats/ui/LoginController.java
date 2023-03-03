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
/***
 * Controller for the LoginScene.
 * Collects the data we need for going further into {@link StatController}.  
 * @author katarem
 * @version 1.0 March 3rd 2023
 */
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

        //Styling
        view.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/cursors/normal.png"),128,128,true,true))); 
        BackgroundImage bImage = new BackgroundImage(new Image(getClass().getResourceAsStream("/images/Kaisa_0.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        view.setBackground(new Background(bImage));
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/font/Friz Quadrata Regular.ttf"), 80));
       

        //Preparing a server list
        String[] servers = {"Brasil","North & East Europe","West Europe","North Latin America","South Latin America","North America","Russia","Oceania","Japan","Turkey","Corea"};
        serverSelector.getItems().addAll(servers);
        serverSelector.setValue("Server");

        //Asking the user for the api key
        setApiKey();
    }

    /***
     * This function will communicate through the services and will lead to the {@link StatController}'s Scene.
     */
    @FXML
    private void getUser(){

        //Getting the region in the correct way
        String region = getRegion();

        try {
            //Initializing the SummonerService
            SummonerService summService = new SummonerService(App.API_KEY, region);

            Summoner summoner = summService.getSummoner(userInput.getText());

            //Setting up the summoner in app so it can be accessed anywhere
            App.summoner = summoner;
            
            //Getting the elos from the SummonerService and then swapping into StatScene.
            ArrayList<LeagueEntry> elos = summService.getElos(summoner.getId());
            
            StatController stat = new StatController(elos, region);
            App.statScene = new Scene(stat.getView());
            App.stage.setScene(App.statScene);
            if(Objects.isNull(App.loginScene))
                App.loginScene = new Scene(this.getView());
            else
                App.loginScene.setRoot(this.view);

            //Cleaning for next user
            userInput.setText("");
            serverSelector.setValue("Server");
            App.stage.show();

        } catch (IOException e1){
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("INVALID API KEY");
            alerta.setContentText("The given api key is wrong/null.");
            alerta.show();
        }
        catch (Exception e) {
            //In case of error, an error dialogpane will be shown.
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("USER NOT FOUND");
            alerta.setContentText("That user doesn't exist in that region.");
            alerta.show();
        }
    }
    /**
	 * Sets the regcode according to the region. 
	 * @param retorno The regcode we get for the {@link SummonerService} usage.
	 */
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

    /***
     * Displays a Dialog to obtain the {@link App#API_KEY} for the application setup.
     */
    private void setApiKey(){
        try{
        TextInputDialog dialog = new TextInputDialog();
            
            //Styling
            ImageView dView = new ImageView(new Image(getClass().getResourceAsStream("/images/key-icon.png")));
            dView.setFitWidth(120);
            dView.setFitHeight(60);
            dialog.setGraphic(dView);

            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(dView.getImage());

            //Preparing buttons
            dialog.getDialogPane().getButtonTypes().clear();
            final ButtonType apiType = new ButtonType("GET",ButtonData.YES);
            ButtonType continuar = new ButtonType("NEXT", ButtonData.OK_DONE);
            ButtonType cancelar = new ButtonType("CANCEL",ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(apiType,continuar,cancelar);
            Button goApiButton = (Button) dialog.getDialogPane().lookupButton(apiType);
            
            //Filter to prevent the window from closing when actioning the button.
            EventHandler<ActionEvent> filter = event -> {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI("https://developer.riotgames.com"));
                    event.consume();
                } catch (IOException | URISyntaxException e1) {
                    event.consume();
                }
            };
            // Chinese "PasswordInputDialog". Better ideas lmk
            dialog.getEditor().textProperty().addListener(e -> {
                if(!dialog.getEditor().getText().isEmpty())
                    dialog.getEditor().setStyle("-fx-background-color:red;-fx-text-fill:red;");
                else
                    dialog.getEditor().setStyle("-fx-background-color:white;");
            });
            goApiButton.addEventFilter(ActionEvent.ACTION, filter);
            //More styling
            dialog.setTitle("API KEY");
            dialog.setHeaderText("INSERT API KEY");
            dialog.setContentText("If you don't know how to get it just click in the button below.");

            //Getting the api key from the dialog and setting it up in App.
            Optional<String> datosRecogidos = dialog.showAndWait();
            App.API_KEY = datosRecogidos.get();
        } catch(NoSuchElementException e){
            //Showing the user they can't let the api key null
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("ERROR");
            alerta.setContentText("The api key can't be null.");
            alerta.show();
        }
    }
}

