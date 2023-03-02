package dad.LoLstats.ui;

import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import dad.LoLstats.objs.*;
import dad.LoLstats.api.LeagueEntry;
import dad.LoLstats.api.Summoner;
import dad.LoLstats.exceptions.*;

public class CalcController implements Initializable{

    @FXML private Pane view;

    @FXML private TextField userInput, lpsLabel;

    @FXML private ImageView profilePic, eloIcon;

    @FXML private Label userAccountLabel, userDataLabel, errorLabel, resultadoLabel;

    @FXML private Button backButton;

    @FXML private HBox eloDeseado;

    @FXML private ChoiceBox<Divisiones> divisionDeseadaChoice;

    private Summoner summoner;

    private LeagueEntry rankedEntry;


    private String selectedElo = "";

    public CalcController(Summoner summoner, LeagueEntry rankedEntry){

        this.summoner = summoner;
        this.rankedEntry = rankedEntry;

        try{
            FXMLLoader l = new FXMLLoader(getClass().getResource("/fxml/viewMain.fxml"));
            
            l.setController(this);
            l.load();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Pane getView(){
        return view;
    }

    public void initialize(URL location, ResourceBundle resources){

        getUser();
        view.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/cursor/normal.png"),128,128,true,true)));
        backButton.setOnAction(e -> goBack());

        ImageView backView = new ImageView(new Image(getClass().getResourceAsStream("/images/back-icon.png")));
        backView.setFitHeight(32);
        backView.setFitWidth(32);

        backButton.setGraphic(backView);


        String[] lista = {"iron.png","bronze.png","silver.png","gold.png","platinum.png","diamond.png","master.png","grandmaster.png","challenger.png"};
        for (int i = 0; i < lista.length; i++) {
            ImageView e1 = (ImageView)eloDeseado.getChildren().get(i);
            e1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + lista[i]))));
        }
        eloDeseado.getChildren().stream().filter(ImageView.class::isInstance).map(ImageView.class::cast).forEach(e -> {
            e.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    eloDeseado.getChildren().stream().filter(ImageView.class::isInstance).map(ImageView.class::cast).forEach(e -> setDefaultSelected(e));
                    selectedElo = e.getId().toUpperCase();
                    e.setFitWidth(110);
                    e.setFitHeight(110);
                }

            });
        });

        divisionDeseadaChoice.getItems().addAll(Divisiones.values());


    }

    private void getUser(){
        
        Task<Void> tarea = new Task<Void>() {
            
            @Override
            protected Void call() throws Exception {
                
                updateMessage("Procesando...");

                try {
                    profilePic.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/profileIcon/%s.png",summoner.getProfileIconId()))));
                    userAccountLabel.setText(summoner.getName());
                    eloIcon.setImage(new Image(getClass().getResourceAsStream(String.format("/images/%s.png", rankedEntry.getTier().toLowerCase()))));
                    updateMessage(String.format("%s %s %sLP", rankedEntry.getTier(),rankedEntry.getRank(),rankedEntry.getLeaguePoints()));

            } catch(NullPointerException e){
                updateMessage("UNRANKED");
            } catch(Exception e){
                System.out.println(e.getMessage());
            }
                return null;
            }
            
        };
        
        userDataLabel.textProperty().bind(tarea.messageProperty());;

        new Thread(tarea).start();
    }

    private void setDefaultSelected(ImageView e){
        e.setFitHeight(64);
        e.setFitWidth(64);
    }

    @FXML private void calcularWins(){

        try {
            
            if(tierErrorCheck() || dataErrorCheck() || eloErrorCheck())
                    throw new LpsInvalidosException();
        } catch (LpsInvalidosException e) {
            System.out.println("no se pudo calcular");
            e.printStackTrace();
        }

        String[] data = userDataLabel.getText().split(" ");

        String eloActual = data[0].toUpperCase();

        String divActual = data[1].toUpperCase();

        int eloDiff = (Elos.valueOf(selectedElo).elo - Elos.valueOf(eloActual).elo ) / 400;
        double promoWins = Math.floor(eloDiff * 3);
        
        double lpspergame = Double.parseDouble(lpsLabel.getText());
        
        int lpsActuales = Integer.parseInt(data[2].replaceAll("[^0-9]", ""));

        int totalActual = Elos.valueOf(eloActual).elo + Divisiones.valueOf(divActual).div + lpsActuales;

        int totalDeseado = Elos.valueOf(selectedElo).elo + divisionDeseadaChoice.getValue().div;

        double winsSinPromo = Math.ceil((totalDeseado - totalActual)/lpspergame);

        int winsTotales = 0;

        if(promoWins>0)
            winsTotales = (int)winsSinPromo + (int)promoWins;
        else
            winsTotales = (int)winsSinPromo;

        resultadoLabel.setText(String.valueOf(winsTotales));
        errorLabel.setText("Operación realizada con éxito");
    }

    private boolean tierErrorCheck(){
        try {
            if(userDataLabel.getText().equals("UNRANKED")){
                errorLabel.setText("No se puede usar la aplicación con cuentas sin elo");
                return true;
            }
            else
                return false;
        } catch (NullPointerException e) {
            errorLabel.setText("Se debe haber agregado el usuario");
            return true;
        }
    }

    private boolean dataErrorCheck(){
        try {
            int lpspergame = Integer.parseInt(lpsLabel.getText().replaceAll("[^0-9]", ""));
            if(lpspergame<=0)
                throw new NumberFormatException();
            else if(lpspergame>=40)
                throw new LpsInvalidosException();
            else
                return false;
        } catch (NumberFormatException e) {
            errorLabel.setText("Los lp por partida deben ser un número entero positivo");
            return true;
        } catch (LpsInvalidosException e) {
            errorLabel.setText("Los lp por partida no pueden ser igual o mayores a 40");
            return true;
        } catch (NullPointerException e) {
            errorLabel.setText("Los lp por partida no pueden ser nulos");
            return true;
        }
    }

    private boolean eloErrorCheck(){
        try {
            if(selectedElo.length()<1)
                throw new NullEloException();
            else
                return false;
        } catch (NullEloException e) {
            errorLabel.setText("Tienes que elegir un elo");
            return true;
        }
    }

    private void goBack(){
        if(Objects.isNull(App.calcScene))
            App.calcScene = new Scene(this.getView());
        else
        App.calcScene.setRoot(this.getView());
        App.stage.setScene(App.statScene);
        App.stage.setHeight(560);
        App.stage.setWidth(685);
        App.stage.setResizable(true);
        App.stage.show();
    }

}