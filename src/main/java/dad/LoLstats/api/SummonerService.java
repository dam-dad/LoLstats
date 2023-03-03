package dad.LoLstats.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SummonerService {
    
    private String BASE_URL = "https://euw1.api.riotgames.com/lol/";
    private LoLInterface service;
    private String API_KEY;
    public SummonerService(String API_KEY, String region){

        BASE_URL = String.format("https://%s.api.riotgames.com/lol/", region);
        System.out.println(BASE_URL);
        this.API_KEY = API_KEY;

        ConnectionPool pool = new ConnectionPool(1,5,TimeUnit.SECONDS);

        OkHttpClient client = new OkHttpClient.Builder()
                    .connectionPool(pool)
                    .build();

        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        service = retrofit.create(LoLInterface.class);
    }

    public Summoner getSummoner(String summonerName) throws IOException{
        Response<Summoner> response = service
                .getSummoner(summonerName, API_KEY)
                .execute();
        Summoner summoner = response.body();
        return summoner;
    }

    public ArrayList<LeagueEntry> getElos(String summonerId) throws IOException{
        Response<ArrayList<LeagueEntry>> response = service
                .getElos(summonerId, API_KEY)
                .execute();
        ArrayList<LeagueEntry> elos = response.body();
        return elos;
    }
  
	}


