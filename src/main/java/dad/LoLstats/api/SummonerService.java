package dad.LoLstats.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import retrofit2.Response;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SummonerService {
    
    private static final String BASE_URL = "https://euw1.api.riotgames.com/lol/";
    private Gson gson = new Gson();
    private LoLInterface service;
    private String API_KEY;
    public SummonerService(String API_KEY){

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

    public Summoner getSummoner(String summonerName) throws Exception{
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


