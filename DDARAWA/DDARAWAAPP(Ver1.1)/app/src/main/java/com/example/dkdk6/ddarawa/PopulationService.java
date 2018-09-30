package com.example.dkdk6.ddarawa;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by dkdk6 on 2018-09-30.
 */

public interface PopulationService {
        @GET("20180901")
        Call<Population> get_population();
}
