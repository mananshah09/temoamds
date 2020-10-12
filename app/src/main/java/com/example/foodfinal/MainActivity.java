package com.example.foodfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    List<Foodclass> Foodnames=new ArrayList<Foodclass>();
    EditText editText;
    String search;
    int zb=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.Foodname);
        loaddata();
    }

    private void loaddata() {
        Request request = null;
        if (zb == 0) {
            request = new Request.Builder()
             .url("https://forkify-api.herokuapp.com/api/search?q=pizza")
             .get().build();
        }
        else {
            request = new Request.Builder()
                    .url("https://forkify-api.herokuapp.com/api/search?q=" + search)
                    .get().build();


            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                   // Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Response response) throws IOException {

                    if (!response.isSuccessful()) {
                      //  Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String resFromApi = response.body().string();
                    Log.d("RawData", resFromApi);
                    Log.e("1", resFromApi);

                    try {
                        extractData(resFromApi);
                    } catch (Exception e) {
                        //  Toast.makeText(MainActivity.this, "Response Not in JSON Format", Toast.LENGTH_SHORT).show();
                        Log.d("Failed", e.getMessage());
                    }
                }
            });
        }

    }
    String image_url;
    String resFromApi1;
    void extractData(String resFromApi) throws Exception {
        JSONObject jsonObject1=new JSONObject(resFromApi);
        JSONArray jsonArray=jsonObject1.getJSONArray("recipes");
        Log.d("RawData" , jsonArray.toString());
        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            String food=jsonObject.getString("title");
            String recipe_id=jsonObject.getString("recipe_id");
            Log.d("Foodnames(pizza)", food+"  "+ recipe_id);
            //start
            Request request = new Request.Builder()
                    .url("https://forkify-api.herokuapp.com/api/get?rId=" + recipe_id)
                    .get()
                    .build();
            Log.d("RequestUrl1", "https://forkify-api.herokuapp.com/api/get?rId=" + recipe_id);
            OkHttpClient client = new OkHttpClient();
            try {

                client.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        resFromApi1 = response.body().string();

                        JSONObject myjsonobject = null;
                        try {
                            myjsonobject = new JSONObject(String.valueOf(resFromApi1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject recipe = null;
                        try {
                            recipe = myjsonobject.getJSONObject("recipe");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            image_url = recipe.getString("image_url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }



                });
            } catch (Exception e) {
                e.printStackTrace();}

                //end

            Foodclass singlefood=new Foodclass(food,recipe_id,image_url);
            Foodnames.add(singlefood);

        }
        Log.d("AfterExtracting" , "Done");
        for(int i=0 ; i<Foodnames.size();i++){
            Log.d("checkingList" , Foodnames.get(i).toString());
        }
        Asynchronous asynk=new Asynchronous();
        asynk.execute();
    }

    public void buttonpress(View view) {
        Foodnames.clear();
        editText=findViewById(R.id.edittext1);
         search=editText.getText().toString();
         zb=1;

         loaddata();
    }


    private class Asynchronous extends AsyncTask<String ,Void,List<Foodclass>>{
        @Override
        protected List<Foodclass> doInBackground(String... strings) {
            return Foodnames;
        }

        @Override
        protected void onPostExecute(List<Foodclass> Foodclasses) {
            Log.d("Async" , "InAsynchronous");
            final FoodNamesArrayAdapter cityadapter=new  FoodNamesArrayAdapter(MainActivity.this , Foodclasses);
            listView.setAdapter(cityadapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Foodclass f=(Foodclass)cityadapter.getItem(position);

                    Intent in = new Intent(MainActivity.this ,Recipe.class);
                    in.putExtra("title",f.title);
                    in.putExtra("recipe_id",f.recipe_id);
                    startActivity(in);
                }
            });
        }
    }
}