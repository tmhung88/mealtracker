package com.mealtracker.request;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class AppRequestBuilders {

    public static AppRequestBuilder get(String urlTemplate) {
        return new AppRequestBuilder(MockMvcRequestBuilders.get(urlTemplate));
    }

    public static AppRequestBuilder post(String urlTemplate) {
        return new AppRequestBuilder(MockMvcRequestBuilders.post(urlTemplate));
    }

    public static AppRequestBuilder patch(String urlTemplate) {
        return new AppRequestBuilder(MockMvcRequestBuilders.patch(urlTemplate));
    }
}
