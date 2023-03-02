package dad.LoLstats.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import dad.LoLstats.api.LeagueEntry;
import dad.LoLstats.api.MatchService;
import dad.LoLstats.api.Summoner;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class StatController implements Initializable{

    @FXML private BorderPane view;

    @FXML private ImageView profilePicView, eloView;

    @FXML private Label playerNameLabel, playerLevelLabel, eloLabel, winrateLabel;

    @FXML private PieChart winrateChart;

    @FXML private ListView<GridPane> gamesView;

    @FXML private Button pdfButton, calcButton, logoutButton;

    private Summoner summoner;

    private ArrayList<LeagueEntry> elos;

    private LeagueEntry rankedEntry;

    private String region;

    public static String eloValue;

    private ArrayList<GameController> gList;

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

        App.stage.setResizable(true);
        ImageView calcIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/calc-icon.png")));
        calcIcon.setFitHeight(60);
        calcIcon.setFitWidth(60);

        ImageView pdfIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/pdf-icon.png")));
        pdfIcon.setFitHeight(60);
        pdfIcon.setFitWidth(60);

        calcButton.setGraphic(calcIcon);
        pdfButton.setGraphic(pdfIcon);
        pdfButton.setOnAction(e -> {
            try {
                pdfOnClick();
            } catch (JRException | IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        logoutButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/logout-icon.png"),32,32,true,true)));


        profilePicView.setImage(new Image(getClass().getResourceAsStream(String.format("/assets/profileicon/%s.png",summoner.getProfileIconId()))));

            playerNameLabel.setText(summoner.getName());
            playerLevelLabel.setText("Level " + summoner.getSummonerLevel());
            
            if(elos.size()>0){
                int cont = 0;
                for (int i = 0; i<elos.size();i++) {
                    if(elos.get(i).getQueueType().equals("RANKED_SOLO_5x5")){
                        cont = i;
                        break;
                    }
                }
    
                rankedEntry = elos.get(cont);
                eloValue = rankedEntry.getTier() + " " + rankedEntry.getRank() + " " + rankedEntry.getLeaguePoints() + "LP";
                eloLabel.setText(eloValue);
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
                    
                    ArrayList<GridPane> partidas = new ArrayList<>();
                    gList = new ArrayList<>();
            
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
            gamesView.getSelectionModel().setSelectionMode(null);
            gamesView.setItems(partidasToList);
            gamesView.setPadding(new Insets(0, 0, 0, 0));
            gamesView.setFocusModel(null);
            gamesView.setBorder(Border.EMPTY);
            gamesView.setFocusTraversable(false);
            gamesView.setCellFactory(new Callback<ListView<GridPane>,ListCell<GridPane>>() {
    
                @Override
                public ListCell<GridPane> call(ListView<GridPane> param) {
                    return new ListCell<>(){
                        @Override
                        public void updateItem(GridPane game, boolean empty) {
                            super.updateItem(game, empty);
                                        
                            if (game != null) {
                                setText(null);
                                setGraphic(game);
                                for (GameController juego : gList) {
                                    if(juego.getView().equals(game) && juego.getWin()){
                                        getGraphic().setId("wonGame");
                                        break;
                                    }
                                    else if(juego.getView().equals(game)){
                                        getGraphic().setId("lostGame");
                                        break;
                                    }
                                }
                                    setBorder(Border.EMPTY);
                                    setStyle("-fx-background-color:black;");
                                    setPadding(new Insets(5, 5, 5, 5));
                                    setWidth(game.getWidth());
                                    setHeight(game.getHeight());
                                    setMinHeight(game.getMinHeight());
                                    setMinWidth(game.getMinWidth());
                                    setAlignment(Pos.CENTER);
                            }
                            else {
                                    setText("null");
                                    setGraphic(null);
                            }
                        }
                    };
                }
                            
            });
            return null;
            }
            };        
            new Thread(task).run();;

            App.stage.setWidth(view.getPrefWidth()+40);
            App.stage.setHeight(view.getPrefHeight());
            App.stage.setMinWidth(view.getMinWidth()+40);
            App.stage.setMinHeight(view.getMinHeight()+35);
            }
        
    @FXML private void calcOnClick(){
        if(Objects.isNull(App.calcScene))
        App.calcScene = new Scene(new CalcController(summoner,rankedEntry).getView());
        else if(!this.summoner.equals(CalcController.getSummoner()))
            App.calcScene = new Scene(new CalcController(summoner, rankedEntry).getView());
        if(Objects.isNull(App.statScene))
        App.statScene = new Scene(this.getView());
        App.stage.setScene(App.calcScene);
        App.stage.setWidth(655);
        App.stage.setHeight(550);
        App.stage.setResizable(false);
        App.stage.show();
    }
    
    @FXML private void loginOnClick(){
        App.stage.setScene(App.loginScene);
        App.stage.setWidth(800);
        App.stage.setHeight(576);
        App.stage.setResizable(false);
        App.stage.show();
    }

    @FXML public static void pdfOnClick() throws JRException, IOException{
        
        
        JasperReport report = JasperCompileManager.compileReport(StatController.class.getResourceAsStream("/reports/gamesReport.jrfxml"));
        System.out.println("clicked");
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("sumName", App.summoner.getName());
        parameters.put("sumLvL", App.summoner.getSummonerLevel());
        parameters.put("sumElo", StatController.eloValue);

        JasperPrint print = JasperFillManager.fillReport(report, parameters);

        JasperExportManager.exportReportToPdfFile(print, "pdf/gamePDF.pdf");

        java.awt.Desktop.getDesktop().open(new File("pdf/gamePDF.pdf"));
    }



    private static double getWinrate(int wins, int losses){
        int partidas = wins + losses;
        double vic = (double)wins/partidas;
        return vic*100;
    }

}