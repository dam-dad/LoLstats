package dad.LoLstats.objs;

import java.util.List;

import dad.LoLstats.api.GameInfo;

public class GameInfoProvider {
    
    static List<GameInfo> games;

    public static List<GameInfo> getGames(){
        return games;
    }


}
