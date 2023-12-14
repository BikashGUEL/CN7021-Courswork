package com.cityzen.cityzen.Network;

public class ApiServices {
    @GET("journey/journeyresults/{from}/to/{to}")
    Call<YourResponseType> getJourneyResults(
            @Path("from") String from,
            @Path("to") String to,
            @Query("app_key") String appKey
    );
}
