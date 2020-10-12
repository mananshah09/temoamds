package com.example.foodfinal;

import android.net.Uri;

public class Foodclass {
    String title;
    String recipe_id;
    String image_url;
    Uri uri;
    Foodclass(String title, String recipe_id, String image_url)
    {
        this.recipe_id=recipe_id;
        this.title=title;
        this.image_url=image_url;
        this.uri=uri;
    }
}