package com.example.foodfinal;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public  class FoodNamesArrayAdapter extends ArrayAdapter{

    public FoodNamesArrayAdapter(Context context, List<Foodclass> resource) {
        super(context, 0, resource);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent)
    {

        View listItemView=convertview;
        if (listItemView == null){
            listItemView= LayoutInflater.from(getContext()).inflate(
                    R.layout.food_list_adapter,parent,false);
        }
        Foodclass fc= (Foodclass) getItem(position);
        Log.d("TAG", "getView: "+position);

       TextView textView=listItemView.findViewById(R.id.FoodListAdapter);

        Log.d("FoodNamesadapter", fc.title+"  "+fc.title);
        textView.setText(fc.title);

        ImageView img=listItemView.findViewById(R.id.imageview);
        //img.setImageURI(Uri.parse(fc.image_url));
        //Log.d("debug",fc.image_url);
        //Picasso.get().load(fc.image_url).into(img);
        return listItemView;

}
}