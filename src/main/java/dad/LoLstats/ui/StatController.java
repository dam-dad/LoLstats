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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import dad.LoLstats.api.LeagueEntry;
import dad.LoLstats.api.LoLService;
import dad.LoLstats.api.MatchService;
import dad.LoLstats.api.Player;
import dad.LoLstats.api.Summoner;
import dad.LoLstats.api.SummonerService;
import dad.LoLstats.api.mastery.Champion;
import dad.LoLstats.api.mastery.Mastery;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
/***
 * Controller for the StatScene.
 * Manages the match & rank data for {@link CalcController}, {@link StatController#loginOnClick()} and itself.
 * @author katarem
 * @version 1.0 March 3rd 2023
 */
public class StatController implements Initializable {

	@FXML
	private BorderPane view;

	@FXML
	private ImageView profilePicView, eloView;

	@FXML
	private Label playerNameLabel, playerLevelLabel, eloLabel, winrateLabel;

	@FXML
	private PieChart winrateChart;

	@FXML
	private ListView<GridPane> gamesView;

	@FXML
	private Button pdfButton, calcButton, logoutButton;

	@FXML
	private VBox wBox;

	@FXML
	private Pane header;

	private Summoner summoner;

	private ArrayList<LeagueEntry> elos;

	private LeagueEntry rankedEntry;

	private String region;

	private List<Player> repList;


	public StatController(ArrayList<LeagueEntry> elos, String region) {
		try {
			this.elos = elos;
			summoner = App.summoner;
			getRegion(region);
			FXMLLoader l = new FXMLLoader(getClass().getResource("/fxml/statView.fxml"));
			l.setController(this);
			l.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Sets the region according to the regcode 
	 * @param reg The regcode we get for the {@link MatchService} usage.
	 */
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

	public BorderPane getView() {
		return view;
	}

	public void initialize(URL location, ResourceBundle resources) {

		//Styling
		App.stage.setResizable(true);
		
		view.setCursor(new ImageCursor(
				new Image(getClass().getResourceAsStream("/cursors/normal.png"), 128, 128, true, true)));
				
		ImageView calcIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/calc-icon.png")));
		calcIcon.setFitHeight(60);
		calcIcon.setFitWidth(60);

		ImageView pdfIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/pdf-icon.png")));
		pdfIcon.setFitHeight(60);
		pdfIcon.setFitWidth(60);
		
		calcButton.setGraphic(calcIcon);
		pdfButton.setGraphic(pdfIcon);
		
		logoutButton.setGraphic(new ImageView(
			new Image(getClass().getResourceAsStream("/images/logout-icon.png"), 32, 32, true, true)));
		
		try {
			App.gameVersion = new LoLService().getLastVersion();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


 		App.profilePic = new Image(String.format("http://ddragon.leagueoflegends.com/cdn/%s/img/profileicon/%s.png", App.gameVersion, summoner.getProfileIconId()));


		//Get most played champ to show it
		Map<String, Champion> champions;
		Champion mostPlayed = null;
		try {
			champions = new LoLService().getChampionsData(App.gameVersion).getChampions();
			System.out.println(summoner.getId());
			ArrayList<Mastery> bestChamp = new SummonerService(App.API_KEY, App.region).getMasteries(summoner.getId());
			for (Champion champion : champions.values()) {
				if(champion.getKey().equals(bestChamp.get(0).getChampionId().toString())){
					mostPlayed = champion;
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(mostPlayed.getName());
		if(mostPlayed!=null)
		{
			
			BackgroundImage b = new BackgroundImage(new Image(String.format("https://ddragon.leagueoflegends.com/cdn/img/champion/splash/%s_0.jpg", mostPlayed.getName().replaceAll(" ", ""))), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(50, 50, true, true, false, true));
			header.setBackground(new Background(b));
		}

		profilePicView.setImage(App.profilePic);
		
		playerNameLabel.setText(summoner.getName());
		playerLevelLabel.setText("Level " + summoner.getSummonerLevel());

		//Adding function to buttons
		pdfButton.setOnAction(e -> {
			try {
				pdfOnClick();
			} catch (JRException | IOException e1) {
				e1.printStackTrace();
			}
		});
		
		//Checking if the player has elo in RANKED SOLO/DUO
		if (elos.size() > 0) {

			int cont = 0;
			for (int i = 0; i < elos.size(); i++) {
				if (elos.get(i).getQueueType().equals("RANKED_SOLO_5x5")) {
					cont = i;
					break;
				}
			}
		//TRUE -> Preparing rank data & winrate
			rankedEntry = elos.get(cont);
			eloLabel.setText(rankedEntry.getTier() + " " + rankedEntry.getRank() + " " + rankedEntry.getLeaguePoints() + "LP");
			eloView.setImage(new Image(getClass()
			.getResourceAsStream(String.format("/images/%s.png", rankedEntry.getTier().toLowerCase()))));			
			
			//No other way to get the desired colors in the piechart than creating another blank datas.
			ObservableList<PieChart.Data> piechartData = FXCollections.observableArrayList(
				new PieChart.Data("LOSERATE", getWinrate(rankedEntry.getLosses(), rankedEntry.getWins())),
				new PieChart.Data("a", 0), new PieChart.Data("a", 0),
				new PieChart.Data("WINRATE", getWinrate(rankedEntry.getWins(), rankedEntry.getLosses())),
				new PieChart.Data("a", 0), new PieChart.Data("a", 0));
				
			winrateChart.setData(piechartData);
			winrateChart.setLegendVisible(false);
				
			winrateLabel.setText(String.format("%sW %sL %.2f%%W/R", rankedEntry.getWins(), rankedEntry.getLosses(),
			getWinrate(rankedEntry.getWins(), rankedEntry.getLosses())));

		} 
		
		else {
			//FALSE -> Displaying that the player has not enough rank data.
			wBox.setVisible(false);
			winrateLabel.setText("");
			eloView.setImage(new Image(getClass().getResourceAsStream("/images/closeicon.png")));
			eloLabel.setText("UNRANKED");
					
			}
			//List for the pdf exporting.	
			repList = new ArrayList<Player>();
				
			//Making use of a thread to try to improve the speed. If you know a better way lmk.
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {

					ArrayList<GridPane> partidas = new ArrayList<>();
					ArrayList<GameController> gList = new ArrayList<>();

					try {
						//We initialize our matchservice with the data we need for it
						MatchService mService = new MatchService(App.API_KEY, region);
						//This will get us the gameID of the last 20 games of the account. 
						ArrayList<String> gameIds = mService.getGames(summoner.getPuuid(), App.API_KEY);
						//Iterating it we can get each game
						for (String s : gameIds) {
							//Prepares the UI of our game
							GameController g = new GameController(mService.getGame(s));
							//For the pdf exporting
							repList.add(new Player(g.getPlayer(),summoner,rankedEntry));
							//Blue if Win, Red if Lost
							if (g.getWin())
								g.getView().setId("gameWon");
							else
								g.getView().setId("gameLost");
							gList.add(g);
							partidas.add(g.getView());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					//Getting the games into the ListView
					ObservableList<GridPane> partidasToList = FXCollections.observableArrayList(partidas);
					gamesView.getSelectionModel().setSelectionMode(null);
					gamesView.setItems(partidasToList);
					gamesView.setPadding(new Insets(0, 0, 0, 0));
					gamesView.setFocusModel(null);
					gamesView.setBorder(Border.EMPTY);
					gamesView.setFocusTraversable(false);
					//We need custom cells for a graphic Listcell
					gamesView.setCellFactory(new Callback<ListView<GridPane>, ListCell<GridPane>>() {

						@Override
						public ListCell<GridPane> call(ListView<GridPane> param) {
							return new ListCell<>() {
								@Override
								public void updateItem(GridPane game, boolean empty) {
									super.updateItem(game, empty);

									if (game != null) {
										setText(null);
										setGraphic(game);
										for (GameController juego : gList) {
											if (juego.getView().equals(game) && juego.getWin()) {
												getGraphic().setId("wonGame");
												break;
											} else if (juego.getView().equals(game)) {
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
									} else {
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
		new Thread(task).run();

		//Getting the desired size for the window
		App.stage.setWidth(view.getPrefWidth() + 40);
		App.stage.setHeight(view.getPrefHeight());
		App.stage.setMinWidth(view.getMinWidth() + 40);
		App.stage.setMinHeight(view.getMinHeight() + 35);
	}

	/***
	 * Swaps the current Screen to the {@link CalcController}'s Scene
	 */
	@FXML
	private void calcOnClick() {
		if (Objects.isNull(App.calcScene))
			App.calcScene = new Scene(new CalcController(rankedEntry).getView());
		else if (!this.summoner.equals(CalcController.getSummoner()))
			App.calcScene = new Scene(new CalcController(rankedEntry).getView());
		if (Objects.isNull(App.statScene))
			App.statScene = new Scene(this.getView());
		App.stage.setScene(App.calcScene);
		App.stage.setWidth(690);
		App.stage.setHeight(550);
		App.stage.setResizable(false);
		App.stage.show();
	}

	/*** 
	 * Swaps the current Screen to the {@link LoginController}'s Scene
	 * ***/
	@FXML
	private void loginOnClick() {
		App.stage.setScene(App.loginScene);
		App.stage.setWidth(800);
		App.stage.setHeight(576);
		App.stage.setResizable(false);
		App.stage.show();
	}

	
	/**
	 * Prepares the info obtained previously from the player's games and returns a PDF File.
	 * @throws JRException
	 * @throws IOException
	 */
	@FXML
	public void pdfOnClick() throws JRException, IOException {
		//Reading the XML
		JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/gamesReport.jrxml"));
		
		//Preparing the external parameters we have to insert.
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("sumName", App.summoner.getName());
		parameters.put("sumLvL", App.summoner.getSummonerLevel());
		parameters.put("sumElo", String.format("%s %s %sLP", rankedEntry.getTier(), rankedEntry.getRank(),rankedEntry.getLeaguePoints()));

		//Filling the design with the data we provided
		JasperPrint print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(repList));

		File pdf = new File("pdf");
		if (!pdf.exists()) {
			pdf.mkdir();
		}
		//Creates the file and opens it with the system's default pdf viewer.
		JasperExportManager.exportReportToPdfFile(print, "pdf/gamePDF.pdf");

		java.awt.Desktop.getDesktop().open(new File("pdf/gamePDF.pdf"));
	}


	/***
	 * Gets the Winrate from the wins and losses we provide it
	 * @param wins
	 * @param losses
	 * @return winrate
	 */
	private static double getWinrate(int wins, int losses) {
		int partidas = wins + losses;
		double vic = (double) wins / partidas;
		double winrate = vic*100;
		return winrate;
	}

}