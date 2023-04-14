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
/***
 * Controller for the CalcScene.
 * Gets the data given from {@link StatController} and calculates the wins needed for a greater elo.  
 * @author katarem
 * @version 1.0 March 3rd 2023
 */
public class CalcController implements Initializable {

    @FXML
    private Pane view;

    @FXML
    private TextField userInput, lpsLabel;

    @FXML
    private ImageView profilePic, eloIcon;

    @FXML
    private Label userAccountLabel, userDataLabel, errorLabel, resultadoLabel;

    @FXML
    private Button backButton;

    @FXML
    private HBox eloDeseado;

    @FXML
    private ChoiceBox<Divisiones> divisionDeseadaChoice;

    static Summoner summoner;

    static LeagueEntry rankedEntry;

    private String selectedElo = "";

    public CalcController(LeagueEntry rankedEntry) {

        CalcController.rankedEntry = rankedEntry;
        CalcController.summoner = App.summoner;
        try {
            FXMLLoader l = new FXMLLoader(getClass().getResource("/fxml/viewMain.fxml"));

            l.setController(this);
            l.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pane getView() {
        return view;
    }
    /***
     * @return Last {@link Summoner} that has been searched.
     */
    public static Summoner getSummoner() {
        return CalcController.summoner;
    }

    public void initialize(URL location, ResourceBundle resources) {
        App.stage.setWidth(view.getPrefWidth()+15);
        App.stage.setHeight(view.getPrefHeight());
        //Getting user data
        getUser();

        //Styling
        view.setCursor(new ImageCursor(
                new Image(getClass().getResourceAsStream("/cursors/normal.png"), 128, 128, true, true)));
                
        ImageView backView = new ImageView(new Image(getClass().getResourceAsStream("/images/back-icon.png")));
        backView.setFitHeight(32);
        backView.setFitWidth(32);
        backButton.setGraphic(backView);
                
        //Setting the returning button
        backButton.setOnAction(e -> goBack());

        //Preparing the elo icons and their handler to work as animated buttons
        String[] lista = { "iron.png", "bronze.png", "silver.png", "gold.png", "platinum.png", "diamond.png",
                "master.png", "grandmaster.png", "challenger.png" };
        for (int i = 0; i < lista.length; i++) {
            ImageView e1 = (ImageView) eloDeseado.getChildren().get(i);
            e1.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + lista[i]))));
        }
        eloDeseado.getChildren().stream().filter(ImageView.class::isInstance).map(ImageView.class::cast).forEach(e -> {
            e.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    eloDeseado.getChildren().stream().filter(ImageView.class::isInstance).map(ImageView.class::cast)
                            .forEach(e -> setDefaultSelected(e));
                    selectedElo = e.getId().toUpperCase();
                    e.setFitWidth(110);
                    e.setFitHeight(110);
                }

            });
        });
        //Adding all the enum values to the division
        divisionDeseadaChoice.getItems().addAll(Divisiones.values());

    }

    /***
     * Gets the user data from {@link Summoner} and {@link rankedEntry}
     */
    private void getUser() {
        //Too fast for a Thread maybe.
        Task<Void> tarea = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                updateMessage("Procesando...");

                try {
                    profilePic.setImage(App.profilePic);
                    userAccountLabel.setText(summoner.getName());
                    eloIcon.setImage(new Image(getClass().getResourceAsStream(
                            String.format("/images/%s.png", rankedEntry.getTier().toLowerCase()))));
                    updateMessage(String.format("%s %s %sLP", rankedEntry.getTier(), rankedEntry.getRank(),
                            rankedEntry.getLeaguePoints()));

                } catch (NullPointerException e) {
                    updateMessage("UNRANKED");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

        };

        userDataLabel.textProperty().bind(tarea.messageProperty());
        ;

        new Thread(tarea).start();
    }

    
    /**
     * Turns the eloIcon to its original state. 
     * @param e
     */
    private void setDefaultSelected(ImageView e) {
        e.setFitHeight(64);
        e.setFitWidth(64);
    }

    @FXML
    private void calcularWins() {
        //As more methods use the errorLabel, priorizing the error color
        errorLabel.setStyle("-fx-text-fill:red;");
        try {

            if (tierErrorCheck() || dataErrorCheck() || eloErrorCheck())
                throw new LpsInvalidosException();
        } catch (LpsInvalidosException e) {
            errorLabel.setText(errorLabel.getText()+"\nCouldn't calculate");
        }

        //Reading the data from the ui.
        String[] data = userDataLabel.getText().split(" ");

        String eloActual = data[0].toUpperCase();

        String divActual = data[1].toUpperCase();
        //Using the points difference of the enums, it's possible to guess how far both elos are
        int eloDiff = (Elos.valueOf(selectedElo).elo - Elos.valueOf(eloActual).elo) / 400;
        double promoWins = Math.floor(eloDiff * 3);

        //Parsing data and cooking some maths
        double lpspergame = Double.parseDouble(lpsLabel.getText());

        int lpsActuales = Integer.parseInt(data[2].replaceAll("[^0-9]", ""));

        int totalActual = Elos.valueOf(eloActual).elo + Divisiones.valueOf(divActual).div + lpsActuales;

        int totalDeseado = Elos.valueOf(selectedElo).elo + divisionDeseadaChoice.getValue().div;

        double winsSinPromo = Math.ceil((totalDeseado - totalActual) / lpspergame);

        int winsTotales = 0;
        //Checks if it's needed to add the promo wins
        if (promoWins > 0)
            winsTotales = (int) winsSinPromo + (int) promoWins;
        else
            winsTotales = (int) winsSinPromo;

        //Showing result!
        resultadoLabel.setText(String.valueOf(winsTotales));
        errorLabel.setStyle("-fx-text-fill:rgb(132, 255, 0);");
        errorLabel.setText("Successful operation!");
    }
    /***
     * Checks if there's errors in the user data
     */
    private boolean tierErrorCheck() {
        try {
            if (userDataLabel.getText().equals("UNRANKED")) {
                errorLabel.setText("This feature can't be used with unranked accounts");
                return true;
            }
            else
                return false;
        } catch (NullPointerException e) {
            errorLabel.setText("User should have been added first");
            return true;
        }
    }
    /***
     * Verifies that lps per game are following the rules
     */
    private boolean dataErrorCheck() {
        try {
            int lpspergame = Integer.parseInt(lpsLabel.getText().replaceAll("[^0-9]", ""));
            if (lpspergame <= 0)
                throw new NumberFormatException();
            else if (lpspergame >= 40)
                throw new LpsInvalidosException();
            else
                return false;
        } catch (NumberFormatException e) {
            errorLabel.setText("The lps per game have to be a positive integer");
            return true;
        } catch (LpsInvalidosException e) {
            errorLabel.setText("The lps per game can't be equal or greater than 40");
            return true;
        } catch (NullPointerException e) {
            errorLabel.setText("The lps per game can't be null");
            return true;
        }
    }
    /***
     * Checks if there is or not a selected elo
     */
    private boolean eloErrorCheck() {
        try {
            if (selectedElo.length() < 1)
                throw new NullEloException();
            else if(Elos.valueOf(selectedElo).elo<Elos.valueOf(userDataLabel.getText().split(" ")[0]).elo)
                throw new InvalidEloException();
            else
                return false;
        } catch (NullEloException e) {
            errorLabel.setText("You have to choose an elo");
            return true;
        } catch (InvalidEloException e) {
            errorLabel.setText("The elo you want to reach has to be higher than actual");
            return true;
        }
    }
    /***
     * Goes back to the previous screen.
     */
    private void goBack() {
        if (Objects.isNull(App.calcScene))
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