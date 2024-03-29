package dad.LoLstats.api;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dad.LoLstats.api.mastery.ChampionData;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoLService {
    private LoLInterface service;
    private String BASE_URL = "https://ddragon.leagueoflegends.com/";

    public LoLService(){
            //Creating the service
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

        public ChampionData getChampionsData(String version) throws Exception{
            Response<ChampionData> response = service
                    .getChampionsData(version,"en_GB")
                    .execute();
            ChampionData champions = response.body();
            return champions;
        }

    public ArrayList<String> getVersions() throws Exception{

        Response<ArrayList<String>> response = service
                .getVersions()
                .execute();
        ArrayList<String> versions = response.body();
        return versions;
    }


    public String getLastVersion() throws Exception{
        ArrayList<String> versions = getVersions();
        return versions.get(0);
    }

    
}

