package com.example.foodfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class Recipe extends AppCompatActivity {
    String foodname;
    String foodid;
    ImageView img;
    String respFromApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent in=getIntent();
        foodname=in.getStringExtra("title");
        foodid=in.getStringExtra("recipe_id");
        img =findViewById(R.id.imageview);
        TextView t12=findViewById(R.id.textview);

        t12.setText(foodname);
        Log.d("heyyy",foodname+"  "+foodid);
        LoadData();
        Log.d("heya","hii");

    }
    private void LoadData() {
        Request request = new Request.Builder()
                .url("https://forkify-api.herokuapp.com/api/get?rId=" + foodid)
                .get()
                .build();
        Log.d("RequestUrl", "https://forkify-api.herokuapp.com/api/get?rId=" + foodid);
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(Recipe.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }
                respFromApi = response.body().string();
                Asynchronous asynk  = new Asynchronous();
                asynk.execute();
            }

        });
    }

    String extractData() throws IOException, JSONException {
        final StringBuffer stringBuffer=new StringBuffer();
        JSONObject myjsonobject = new JSONObject(respFromApi);
        JSONObject recipe = myjsonobject.getJSONObject("recipe");
        JSONArray ingredients = recipe.getJSONArray("ingredients");
        Log.d("serverResponse", "" + ingredients.length());

        for (int z=0;z<ingredients.length();z++)
        {
          stringBuffer.append(" Step "+(z+1)+"  :"+ingredients.getString(z)+"/n");
            stringBuffer.append(System.getProperty("line.separator"));

            Log.d("yupps",ingredients.getString(z));
        }
        Log.d("cheeh",stringBuffer.toString());
        // here
        //ta.setTextSize(25);
        //ta.setBackgroundResource(R.drawable.);
        //end




        final int social_rank = (int)recipe.getDouble("social_rank");
        String image_url = recipe.getString("image_url");
        Log.d("ImageUrl" , image_url);
        URL url = new URL(image_url);

        Log.d("check1", image_url);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView ta=findViewById(R.id.textview3);ta.setText(stringBuffer);
                TextView t1 = findViewById(R.id.textview);
                TextView t2 = findViewById(R.id.textview2);
                t1.setText(foodname);
                Log.d("socialRank", ""+social_rank);
                t2.setText(""+social_rank);

            }
        });
        return image_url;
    }
    void LoadImage(String inp){
        Picasso.get().load(inp).into(img);

    }



    private class Asynchronous extends AsyncTask<String ,Void,String>  {
        @Override
        protected String doInBackground(String... strings) {
            //Whenever we load or scroll on screen this function is automatically
            //called and we return the list of citynames that we loaded in extractData
            //function after this onPostExecute is called
            try {
                return extractData() ;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        public void onPostExecute(String myimg) {
            LoadImage(myimg);


        }

    }

}