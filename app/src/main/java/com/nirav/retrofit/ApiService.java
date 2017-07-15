package com.nirav.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Developers on 15-07-2017.
 */

public interface ApiService {


    /*
     Retrofit get annotation with our URL
     And our method that will return us the List of ContactList
     */
    @GET("/json_data.json")
    Call<ContactList> getMyJSON();
}
