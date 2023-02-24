package dad.LoLstats.ui;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dad.LoLstats.api.GameInfo;
import dad.LoLstats.api.LeagueEntry;
import dad.LoLstats.api.MatchService;
import dad.LoLstats.api.Summoner;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.FocusModel;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class StatController implements Initializable{

    @FXML private BorderPane view;

    @FXML private ImageView profilePicView, eloView;

    @FXML private Label playerNameLabel, playerLevelLabel, eloLabel, winrateLabel;

    @FXML private PieChart winrateChart;

    @FXML private ListView<GridPane> gamesView;

    private Summoner summoner;

    private ArrayList<LeagueEntry> elos;

    private String region;

    public StatController(ArrayList<LeagueEntry> elos, String region){
        try{
            this.elos = elos;
            summoner = App.summoner;
            getRegion(region);
            FXMLLoader l = new FXMLLoader(getClass().getResource("/fxml/statView.fxml"));
            l.setController(this);
            l.load();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void getRegion(String reg) {

        switch (reg) {
            case "euw1":
                region = "europe";
                break;
            case "eun1":
                region = "europe";
                break;
            case "ru1":
                region = "europe";
                break;
            case "tr1":
                region = "europe";
                break;
            case "br1":
                region = "americas";
                break;

            case "na1":
                region = "americas";
                break;
            case "la1":
                region = "americas";
                break;
            case "la2":
                region = "americas";
                break;
            case "jp1":
                region = "asia";
                break;
            case "kr":
                region = "asia";
                break;
            case "oce":
                region = "sea";
                break;

            default:
                region = "europe";
                break;
        }


    }

    public BorderPane getView(){
        return view;
    }

    public void initialize(URL location, ResourceBundle resources){
        view.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/cursors/normal.png"),128,128,true,true)));
        
        App.stage.setMinWidth(view.getMinWidth()+10);
        App.stage.setMinHeight(view.getMinHeight());

            App.stage.setResizable(true);
            
            profilePicView.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/profileicon/%s.png",summoner.getProfileIconId()))));

            playerNameLabel.setText(summoner.getName());
            playerLevelLabel.setText("nivel " + summoner.getSummonerLevel());
            
            if(elos.size()>0){
                int cont = 0;
                for (int i = 0; i<elos.size();i++) {
                    if(elos.get(i).getQueueType().equals("RANKED_SOLO_5x5")){
                        cont = i;
                        break;
                    }
                }
    
                LeagueEntry rankedEntry = elos.get(cont);
                eloLabel.setText(rankedEntry.getTier() + " " + rankedEntry.getRank() + " " + rankedEntry.getLeaguePoints() + "LP");
                eloView.setImage(new Image(getClass().getResourceAsStream(String.format("/images/%s.png", rankedEntry.getTier().toLowerCase()))));
        
                ObservableList<PieChart.Data> piechartData = FXCollections.observableArrayList(
                    new PieChart.Data("LOSERATE", getWinrate(rankedEntry.getLosses(), rankedEntry.getWins())),
                    new PieChart.Data("a", 0),
                    new PieChart.Data("a", 0),
                    new PieChart.Data("WINRATE", getWinrate(rankedEntry.getWins(), rankedEntry.getLosses())), 
                    new PieChart.Data("a", 0),
                    new PieChart.Data("a", 0)
                );
    
                winrateChart.setData(piechartData);
                winrateChart.setLegendVisible(false);
               
                winrateLabel.setText(String.format("%sW %sL %.2f%%W/R",rankedEntry.getWins(),rankedEntry.getLosses(),getWinrate(rankedEntry.getWins(), rankedEntry.getLosses())));                
                    
            } else{
                
                winrateLabel.setText("");
                eloLabel.setText("UNRANKED");
            
            }

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    
                    view.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/cursors/unavailable.png"),128,128,true,true)));
                    ArrayList<GridPane> partidas = new ArrayList<>();
                    ArrayList<GameController> gList = new ArrayList<>();
            
            try {
                        
                MatchService mService = new MatchService(App.API_KEY, region);
                        
                ArrayList<String> gameIds = mService.getGames(summoner.getPuuid(), App.API_KEY);
                
                for (String s : gameIds) {
                            GameController g = new GameController(mService.getGame(s));
                            if(g.getWin())
                                g.getView().setId("gameWon");
                            else
                                g.getView().setId("gameLost");
                            gList.add(g);
                            partidas.add(g.getView());
                }
            } catch (Exception e) {
                                
                e.printStackTrace();
            }
            
            ObservableList<GridPane> partidasToList = FXCollections.observableArrayList(partidas);
            gamesView.getStylesheets().clear();
            gamesView.getStylesheets().add("/css/gameWonStyle.css");
            gamesView.getSelectionModel().setSelectionMode(null);
            gamesView.setItems(partidasToList);
            gamesView.setFocusModel(null);
            gamesView.setFocusTraversable(false);
            gamesView.setCellFactory(new Callback<ListView<GridPane>,ListCell<GridPane>>() {
    
                            @Override
                            public ListCell<GridPane> call(ListView<GridPane> param) {
                                // TODO Auto-generated method stub
                                return new ListCell<>(){
                                    @Override
                                    public void updateItem(GridPane game, boolean empty) {
                                        super.updateItem(game, empty);
                                        if (empty) {
                                            setText(null);
                                            setGraphic(game);
                                        } else if (game != null) {
                                            setText(null);
                                            setGraphic(game);
                                            setMinHeight(game.getMinHeight());
                                            setMinWidth(game.getMinWidth());
                                            setAlignment(Pos.CENTER);
                                            
                                        } else {
                                            setText("null");
                                            setGraphic(null);
                                        }
                                    }
                                };
                            }
                            
                        });;

                        // for (int i = 0; i< gamesView.getItems().size(); i++) {
                        //     if(gList.get(i).getView().equals(gamesView.getItems().get(i)))
                        //         setTheme(gamesView.getItems().get(i), gList.get(i).getWin());
                        //     else
                        //         System.out.println("no son iguales");
                        // }

                        view.getStylesheets().clear();
                        view.getStylesheets().add("/css/gameWonStyle.css");


                        view.setCursor(new ImageCursor(new Image(getClass().getResourceAsStream("/cursors/normal.png"),128,128,true,true)));
                    return null;
                }
                
            };        
            new Thread(task).run();;
            }
        
    // private void setTheme(GridPane g, boolean win){

    //     if(win){
    //         g.getStylesheets().clear();
    //         g.getStylesheets().add("/css/gameWonStyle.css");
    //         g.setId("root");
    //     }
    //     else{
    //         g.getStylesheets().clear();
    //         g.getStylesheets().add("/css/gameLostStyle.css");
    //         g.setId("root");
    //     }


    // }


    private static double getWinrate(int wins, int losses){
        int partidas = wins + losses;
        double vic = (double)wins/partidas;
        return vic*100;
    }

}