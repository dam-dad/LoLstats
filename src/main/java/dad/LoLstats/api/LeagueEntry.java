package dad.LoLstats.api;

public class LeagueEntry {
    private String queueType;
    private String tier;
    private String rank;
    private int leaguePoints;
    public String getQueueType() {
        return queueType;
    }
    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }
    public String getTier() {
        return tier;
    }
    public void setTier(String tier) {
        this.tier = tier;
    }
    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public int getLeaguePoints() {
        return leaguePoints;
    }
    public void setLeaguePoints(int leaguePoints) {
        this.leaguePoints = leaguePoints;
    }
    
}
