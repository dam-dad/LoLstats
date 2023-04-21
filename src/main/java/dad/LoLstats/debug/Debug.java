package dad.LoLstats.debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import dad.LoLstats.api.LoLService;
import dad.LoLstats.api.Summoner;
import dad.LoLstats.api.SummonerService;
import dad.LoLstats.api.mastery.Champion;
import dad.LoLstats.api.mastery.ChampionData;
import dad.LoLstats.api.mastery.Mastery;

public class Debug {
    public static void main(String[] args) throws Exception {
        String API_KEY = "RGAPI-778df47e-14ae-4125-ac0c-a561836e1fba";

        SummonerService s = new SummonerService(API_KEY, "euw1");

        Summoner summ = s.getSummoner("GeckoDeMalAgüero");

        LoLService ls = new LoLService();


        ArrayList<Mastery> masteries = s.getMasteries(summ.getId());
        Map<String, Champion> champions = ls.getChampionsData(ls.getLastVersion()).getChampions();
        Champion bestChamp = null;
        for (Champion champion : champions.values()) {
            if(champion.getKey().equals(masteries.get(0).getChampionId().toString())){
                bestChamp = champion;
                break;
            }
        }
        System.out.println(bestChamp.getName());
    }
}
