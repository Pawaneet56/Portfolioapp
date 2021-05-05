package com.example.portfolioapp.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({

            "Content_Type:application/json",
            "Authorization:key=AAAAZLL_RcE:APA91bGAkYrpMAzawhZokXtLL4Hn1bCYaGDFIEE8WYh83Lz3IU-RC3MkVlAa_QSQYqBz_PbSRV_3ghO7yH0gvFju-JHqRi_4g2VwJkJFexGkB3-PrguEXlv5X7P9bWxiPICNAQfsSuYL"

    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
