package com.example.dkdk6.ddarawa.Interface;

import com.example.dkdk6.ddarawa.FineDust;
import com.example.dkdk6.ddarawa.SeoulFineDust;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by dkdk6 on 2018-10-06.
 */

public interface seoulFineDustService {
    @GET("{Id}")
    Call<SeoulFineDust> get_population(@Path("Id") String Id);
}
