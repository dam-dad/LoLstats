package dad.LoLstats.ui;

import dad.LoLstats.api.Summoner;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/***
 * The Application itself that will be switching by the Scene provided by the controllers.
 * @author katarem 
 * @version 1.0 March 3rd 2023
 */
public class App extends Application{

    public static Stage stage;
    public static String API_KEY;
    public static Summoner summoner;
    public static Scene loginScene, calcScene, statScene;
    public static Image profilePic;
    public static String gameVersion;

    @Override public void start(Stage stage) throws Exception {
        App.stage = stage;
        LoginController l = new LoginController();
        stage.setTitle("LoLstats v1.02");
        App.loginScene = new Scene(l.getView());
        stage.setScene(loginScene);
        stage.setWidth(800);
        stage.setHeight(576);
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
        stage.show();

    }
    public static void main(String[] args){
        launch(args);
    }
}
