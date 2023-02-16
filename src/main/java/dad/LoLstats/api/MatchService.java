package dad.LoLstats.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MatchService {
    private static final String BASE_URL = "https://europe.api.riotgames.com/lol/";
    private Gson gson = new Gson();
    private LoLInterface service;
    private String API_KEY;
    public MatchService(String API_KEY){

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

    public ArrayList<String> getGames(String puuid, String API_KEY) throws Exception{
        Response<ArrayList<String>> response = service
                .getGames(puuid, API_KEY)
                .execute();
        assertResponse(response);
        ArrayList<String> history = response.body();
        return history;
    }

    public Match getGame(String matchID) throws Exception{
        Response<Match> response = service
                .getGame(matchID, API_KEY)
                .execute();
        assertResponse(response);
        Match match = response.body();
        return match;
    }

    private void assertResponse(Response<?> response) throws Exception {
		if (!response.isSuccessful()) {
		ErrorMessage error = gson.fromJson(response.errorBody().string(), ErrorMessage.class);
			throw new Exception(error.getMessage());
		}
	}
}
