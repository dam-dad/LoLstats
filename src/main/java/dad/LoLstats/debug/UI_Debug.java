package dad.LoLstats.debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import dad.LoLstats.api.LoLService;
import dad.LoLstats.api.Summoner;
import dad.LoLstats.api.SummonerService;
import dad.LoLstats.api.mastery.Champion;
import dad.LoLstats.api.mastery.Mastery;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UI_Debug extends Application{

    Pane uiPane;

    @Override
    public void start(Stage stage) throws Exception {
        
        uiPane = new Pane();

        setBackground();


        Scene scene = new Scene(uiPane, 600, 400);
        stage.setTitle("DEBUG UI");
        stage.setScene(scene);
        stage.show();
        
    }
    public void setBackground() throws Exception{
        String API_KEY = "RGAPI-778df47e-14ae-4125-ac0c-a561836e1fba";

        SummonerService s = new SummonerService(API_KEY, "euw1");

        Summoner summ = s.getSummoner("GeckoDeMalAgüero");

        LoLService ls = new LoLService();

        System.out.println("--GETTING CHAMPS--");
        ArrayList<Mastery> masteries = s.getMasteries(summ.getId());
        Map<String, Champion> champions = ls.getChampionsData(ls.getLastVersion()).getChampions();
        Champion bestChamp = null;
        for (Champion champion : champions.values()) {
            if(champion.getKey().equals(masteries.get(0).getChampionId().toString())){
                bestChamp = champion;
                break;
            }
        }
        System.out.println("BEST CHAMP IS " + bestChamp.getName());
        System.out.println("--FINISHED GETTING CHAMPS--");
        if(bestChamp != null){
            // uiPane.setStyle("-fx-background-image: url(" + getClass().getResource("https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Aatrox_1.jpg") +"); -fx-background-repeat: stretch;-fx-background-size: 900 506;-fx-background-position: center center;");
            BackgroundImage bi = new BackgroundImage(new Image(String.format("https://ddragon.leagueoflegends.com/cdn/img/champion/splash/%s_0.jpg","Ekko")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(800, 400, false, false, false, false));
            uiPane.setBackground(new Background(bi));

        }


    }
    public static void main(String[] args){
        launch(args);
    }
}
