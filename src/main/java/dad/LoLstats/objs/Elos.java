package dad.LoLstats.objs;

public enum Elos {
    IRON(0),
    BRONZE(400),
    SILVER(800),
    GOLD(1200),
    PLATINUM(1600),
    DIAMOND(2000),
    MASTER(2400),
    GRANDMASTER(2800),
    CHALLENGER(3200);

    public int elo;

    Elos(int elo){
        this.elo = elo;
    }
}
