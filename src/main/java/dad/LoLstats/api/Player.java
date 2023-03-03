package dad.LoLstats.api;

import dad.LoLstats.ui.StatController;

/***
 * JavaBean to collect a player's game data for the {@link StatController#pdfOnClick()}
 */
public class Player {
    
    private Integer cs;
    private String kda;
    private Integer damage;
    private String championPlayed;
    private boolean win;
    private String championIcon;
    private String userIcon;
    private String eloIcon;


    public Player(Participant p, Summoner s, LeagueEntry l){
        cs = p.getTotalMinionsKilled();
        kda = String.format("%s/%s/%s", p.getKills(),p.getDeaths(),p.getAssists());
        damage = p.getTotalDamageDealtToChampions();
        championPlayed = p.getChampionName();
        win = p.getWin();
        championIcon = p.getChampionName();
        userIcon = s.getProfileIconId();
        eloIcon = l.getTier().toLowerCase();
    }

    

    public String getChampionIcon() {
        return championIcon;
    }



    public String getUserIcon() {
        return userIcon;
    }



    public String getEloIcon() {
        return eloIcon;
    }



    public Integer getCs() {
        return cs;
    }

    public String getKda() {
        return kda;
    }

    public Integer getDamage() {
        return damage;
    }

    public String getChampionPlayed() {
        return championPlayed;
    }

    public boolean isWin() {
        return win;
    }

}
