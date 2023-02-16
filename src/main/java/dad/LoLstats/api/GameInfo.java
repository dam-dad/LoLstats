package dad.LoLstats.api;

public class GameInfo {
    int damage;
    int cs;
    int kills;
    int deaths;
    int assists;
    int champLevel;
    String championName;
    int item0;
    int item1;
    int item2;
    int item3;
    int item4;
    int item5;
    int item6;
    int summoner1Id;
    int summoner2Id;
    String role;
    boolean win;

    long gameDuration;
    String gameMode;
    String gameType;
    String queueType = "NULL";


    public GameInfo(double damage, double cs, double kills, double deaths, double assists,
        double champLevel, String championName, double item0, double item1, double item2, double item3, double item4, double item5,
        double item6, double summoner1Id, double summoner2Id, String role, boolean win) {
        
        this.damage = (int) damage;
        this.cs = (int) cs;
        this.kills = (int) kills;
        this.deaths = (int) deaths;
        this.assists = (int) assists;
        this.champLevel = (int) champLevel;
        this.championName = championName;
        this.item0 = (int) item0;
        this.item1 = (int) item1;
        this.item2 = (int) item2;
        this.item3 = (int) item3;
        this.item4 = (int) item4;
        this.item5 = (int) item5;
        this.item6 = (int) item6;
        this.summoner1Id = (int) summoner1Id;
        this.summoner2Id = (int) summoner2Id;
        this.role = role;
        this.win = win;
    }
    public int getdamage() {
        return damage;
    }
    public void setdamage(int damage) {
        this.damage = damage;
    }
    public int getTotalMinionsKilled() {
        return cs;
    }
    public void setTotalMinionsKilled(int cs) {
        this.cs = cs;
    }
    public int getKills() {
        return kills;
    }
    public void setKills(int kills) {
        this.kills = kills;
    }
    public int getDeaths() {
        return deaths;
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public int getAssists() {
        return assists;
    }
    public void setAssists(int assists) {
        this.assists = assists;
    }
    public int getChampLevel() {
        return champLevel;
    }
    public void setChampLevel(int champLevel) {
        this.champLevel = champLevel;
    }
    public String getChampionName() {
        return championName;
    }
    public void setChampionName(String championName) {
        this.championName = championName;
    }
    public int getItem0() {
        return item0;
    }
    public void setItem0(int item0) {
        this.item0 = item0;
    }
    public int getItem1() {
        return item1;
    }
    public void setItem1(int item1) {
        this.item1 = item1;
    }
    public int getItem2() {
        return item2;
    }
    public void setItem2(int item2) {
        this.item2 = item2;
    }
    public int getItem3() {
        return item3;
    }
    public void setItem3(int item3) {
        this.item3 = item3;
    }
    public int getItem4() {
        return item4;
    }
    public void setItem4(int item4) {
        this.item4 = item4;
    }
    public int getItem5() {
        return item5;
    }
    public void setItem5(int item5) {
        this.item5 = item5;
    }
    public int getItem6() {
        return item6;
    }
    public void setItem6(int item6) {
        this.item6 = item6;
    }
    public int getSummoner1Id() {
        return summoner1Id;
    }
    public void setSummoner1Id(int summoner1Id) {
        this.summoner1Id = summoner1Id;
    }
    public int getSummoner2Id() {
        return summoner2Id;
    }
    public void setSummoner2Id(int summoner2Id) {
        this.summoner2Id = summoner2Id;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public boolean isWin() {
        return win;
    }
    public void setWin(boolean win) {
        this.win = win;
    }
    public long getGameDuration() {
        return gameDuration;
    }
    public void setGameDuration(long gameDuration) {
        this.gameDuration = gameDuration;
    }
    public String getGameMode() {
        return gameMode;
    }
    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }
    public String getGameType() {
        return gameType;
    }
    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getQueueType(){
        return queueType;
    }

    public void setGameData(String gameMode, String gameType, double gameDuration, double queueType){
        this.gameMode = gameMode;
        this.gameType = gameType;
        this.gameDuration = (long)gameDuration;
        setQueueType((int)queueType);
    }

    private void setQueueType(int queue){
        switch (queue) {
            case 420:
                queueType = "RANKED_SOLO";
                break;
        
            case 430:
                queueType = "NORMAL_BLIND";
                break;

            case 440:
                queueType = "RANKED_FLEX";
                break;
            
            case 450:
                queueType = "ARAM";
                break;
            
            case 400:
                queueType = "NORMAL_DRAFT";
                break;

            case 830:
                queueType = "COOP_INTRO";
                break;
            
            case 840:;
                queueType = "COOP_BEGINNER";
                break;

            case 850:
                queueType = "COOP_INTERMEDIATE";
                break;

            case 1900:
                queueType = "URF";
                break;
            
            case 900:
                queueType = "ARURF";
                break;

            default:
                queueType = "UNKNOWN";
                break;
        }
    }

    
}
