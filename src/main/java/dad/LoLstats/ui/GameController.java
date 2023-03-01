package dad.LoLstats.ui;

import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import dad.LoLstats.api.GameInfo;
import dad.LoLstats.api.MatchService;
import dad.LoLstats.api.Participant;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class GameController implements Initializable{

    @FXML private GridPane view;

    @FXML private ImageView champView, summoner1, summoner2, rune1, rune2, item0, item1, item2, item3, item4, item5, item6;

    @FXML private Label gameModeLabel, timeLabel, csLabel, kdaLabel, kdaPropLabel, dateLabel, dmgLabel;

    private GameInfo game;
    private Participant player;
    private Long mins, secs;
    private Integer cs;
    private boolean win;

    public GameController(GameInfo game){
        try{
            this.game = game;
            FXMLLoader l = new FXMLLoader(getClass().getResource("/fxml/gameView.fxml"));
            l.setController(this);
            l.load();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public GridPane getView(){
        return view;
    }

    public boolean getWin(){
        return win;
    }

    public void initialize(URL location, ResourceBundle resources){
        

        List<Participant> participantes = game.getInfo().getParticipants();
        
        for (Participant participant : participantes) {
            if (participant.getSummonerId().equals(App.summoner.getId())) {
                player = participant;
                break;
            }
        }

        this.win = player.getWin();
        

        String fecha = Date.from(Instant.ofEpochMilli(game.getInfo().getGameEndTimestamp())).toString();
        
        String[] fechaValues = fecha.split(" ");

        dateLabel.setText(String.format("%s/%d/%s",fechaValues[2],getMonth(fechaValues[1]),fechaValues[5]));
        
        System.out.println(fecha);

        dmgLabel.setText(player.getTotalDamageDealt() + "DMG");

        champView.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/champion/%s.png",player.getChampionName()))));

        gameModeLabel.setText(getGameMode());
        timeLabel.setText(getTime());
        
        if(player.getItem0()!=0)
            item0.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/item/%s.png",player.getItem0()))));
        if(player.getItem1()!=0)
            item1.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/item/%s.png",player.getItem1()))));
        if(player.getItem2()!=0)
            item2.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/item/%s.png",player.getItem2()))));
        if(player.getItem3()!=0)
            item3.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/item/%s.png",player.getItem3()))));
        if(player.getItem4()!=0)
            item4.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/item/%s.png",player.getItem4()))));
        if(player.getItem5()!=0)
            item5.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/item/%s.png",player.getItem5()))));
        if(player.getItem6()!=0)
            item6.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/item/%s.png",player.getItem6()))));

        summoner1.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/summoner/%s.png", player.getSummoner1Id()))));
        summoner2.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/summoner/%s.png", player.getSummoner2Id()))));

        cs = (player.getTotalMinionsKilled() + player.getNeutralMinionsKilled());
        double cspermin = Math.round((cs*1.0/mins) * 10.0) / 10.0;
        csLabel.setText(String.format("CS %s (%.1f)",cs,cspermin));
        kdaLabel.setText(player.getKills() + "/" + player.getDeaths() + "/" + player.getAssists());
        kdaPropLabel.setText(String.format("%.2f:1 KDA",(((double)player.getKills() + player.getAssists())/player.getDeaths())));

        rune1.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/runes/%s.png", player.getPerks().getStyles().get(0).getSelections().get(0).getPerk()))));
        rune2.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/runes/%s.png", player.getPerks().getStyles().get(1).getStyle()))));

        
    }

    private int getMonth(String string) {
        int month;
        switch (string) {
            case "Jan":
                month = 1;
                break;
            case "Feb":
                month = 2;
                break;
            case "Mar":
                month = 3;
                break;
            case "Apr":
                month = 4;
                break;
            case "May":
                month = 5;
                break;
            case "Jun":
                month = 6;
                break;
            case "Jul":
                month = 7;
                break;
            case "Aug":
                month = 8;
                break;
            case "Sep":
                month = 9;
                break;
            case "Oct":
                month = 10;
                break;
            case "Nov":
                month = 11;
                break;
            case "Dec":
                month = 12;
                break;
            
            
                default:
                month = 0;
                break;
        }

        return month;
    }

    private String getTime() {
        Long time = game.getInfo().getGameDuration();

        mins = TimeUnit.SECONDS.toMinutes(time);

        secs = TimeUnit.SECONDS.toSeconds(time) % 60;

        return String.format(mins + "m " + secs + "s");
    }

    private String getGameMode() {
        String out;
        switch (game.getInfo().getQueueId()) {
            case 900:
                out = "ARURF";
                break;
            case 1900:
                out = "URF";
                break;
            case 420:
                out = "RANKED SOLO/DUO";
                break;
            case 440:
                out = "RANKED FLEX";
                break;
            case 450:
                out = "ARAM";
                break;
            case 700:
                out = "CLASH";
                break;
            case 720:
                out = "ARAM CLASH";
                break;
            case 830:
                out = "COOP INTRO";
                break;
            case 840:
                out = "COOP BEGINNER";
                break;
            case 850:
                out = "COOP INTERMEDIATE";
                break;
            case 400:
                out = "NORMAL DRAFT";
                break;
            case 430:
                out = "NORMAL BLIND";
                break;

            default:
                out = "NULL";
                break;
        }
        
        return out;
    }

}