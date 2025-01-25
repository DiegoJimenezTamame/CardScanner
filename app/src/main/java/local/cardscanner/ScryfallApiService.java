package local.cardscanner;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;

// ScryfallApiService.java
public interface ScryfallApiService {
    @GET("cards/search")
    Call<SearchResponse> searchCards(
            @Query("q") String query,
            @Query("unique") String unique
    );

    @GET("cards/{setCode}/{collectorNumber}")
    Call<CardResponse> getCardBySetAndNumber(
            @Path("setCode") String setCode,
            @Path("collectorNumber") String collectorNumber
    );

    Call<CardResponse> getCardByName(String cardName);
}

