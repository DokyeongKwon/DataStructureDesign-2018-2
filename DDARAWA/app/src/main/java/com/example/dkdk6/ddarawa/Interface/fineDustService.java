package com.example.dkdk6.ddarawa.Interface;


import com.example.dkdk6.ddarawa.FineDust;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dkdk6 on 2018-09-30.
 */

public interface fineDustService {
    @GET("{Id}")
    Call<FineDust> get_population(@Path("Id") String Id);
}
