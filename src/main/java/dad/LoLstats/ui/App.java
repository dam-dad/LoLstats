package dad.LoLstats.ui;

import dad.LoLstats.api.Summoner;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class App extends Application{

    public static Stage stage;
    public static String API_KEY;
    public static Summoner summoner;
    public static Scene loginScene, calcScene, statScene;


    @Override public void start(Stage stage) throws Exception {
        App.stage = stage;
        LoginController l = new LoginController();
        stage.setTitle("LoLstats v1.0");
        stage.setScene(new Scene(l.getView()));
        stage.setWidth(800);
        stage.setHeight(576);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
        stage.show();

    }
    public static void main(String[] args){
        launch(args);
    }
}
