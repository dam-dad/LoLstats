package dad.LoLstats.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class App extends Application{

    @Override public void start(Stage stage) throws Exception {
        LoginController l = new LoginController();
        stage.setTitle("LoLstats v1.0");
        stage.setScene(new Scene(l.getView()));
        stage.show();

    }
    public static void main(String[] args){
        launch(args);
    }
}
