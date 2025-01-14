package com.example.kevinyue.wineselector.Element;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.kevinyue.wineselector.Element.Adapter.WineAdapter;
import com.example.kevinyue.wineselector.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class WhiteWineActivity extends AppCompatActivity {
    public Context context;
    public static final String WINE_DETAIL_KEY = "wine";

    private WineAdapter wineAdapter;
    private ListView listView;
    private TextView tvType;
    private TextView tvName;
    private TextView tvCountry;
    private TextView tvDice;
    private TextView tvDescription;
    private TextView tvPrice;
    private ImageView whiteWineImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_wine);

        this.setTitle("White Wine List");

        jsonParser();

        itemWineListenerSelected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionsmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Making available to click on the action bar
        switch (item.getItemId()) {
            // finding activity location menu bar
            case R.id.action_elementactivity:
                Intent elementIntent = new Intent(WhiteWineActivity.this, ElementsActivity.class);
                startActivity(elementIntent);
                return true;
            case R.id.action_foodactivity:
                Intent redIntent = new Intent(WhiteWineActivity.this, FoodListActivity.class);
                startActivity(redIntent);
                return true;
            case R.id.action_redactivity:
                Intent whiteIntent = new Intent(WhiteWineActivity.this, RedWineActivity.class);
                startActivity(whiteIntent);
                return true;
            case R.id.action_roseactivity:
                Intent RoseIntent = new Intent(WhiteWineActivity.this, RoseWineActivity.class);
                startActivity(RoseIntent);
                return true;
            case R.id.action_sparklingactivity:
                Intent sparklingIntent = new Intent(WhiteWineActivity.this, SparklingWineActivity.class);
                startActivity(sparklingIntent);
                return true;
            case R.id.action_newactivity:
                Intent beginnersIntent = new Intent(WhiteWineActivity.this, NewBeginnersActivity.class);
                startActivity(beginnersIntent);
                return true;
            case R.id.action_searchactivity:
                Intent searchIntent = new Intent(WhiteWineActivity.this, SearchWineActivity.class);
                startActivity(searchIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void itemWineListenerSelected() {

        //kan ikke hente ut verdier fra ListView items her, da den prøver å finne f.eks R.id.textType i activity_red_wine.xml,
        //hvor denne ID'en ikke eksisterer, kun ListView med ID redList eksiterer i denne layouten.

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                whiteWineImage = (ImageView) view.findViewById(R.id.whiteTest);
                tvType = (TextView) view.findViewById(R.id.textType);
                tvName = (TextView) view.findViewById(R.id.textName);
                tvCountry = (TextView) view.findViewById(R.id.textCountry);
                tvDice = (TextView) view.findViewById(R.id.textDice);
                tvDescription = (TextView) view.findViewById(R.id.textDescription);
                tvPrice = (TextView) view.findViewById(R.id.textPrice);

                // Creating an instance of Wine class with user input data
                Wine wine = new Wine(tvType.getText().toString(),
                        tvName.getText().toString(),
                        tvCountry.getText().toString(),
                        Integer.parseInt(tvDice.getText().toString()),
                        tvDescription.getText().toString(),
                        tvPrice.getText().toString());

                // Creating an intent to open the WineDetailActivity
                Intent intent = new Intent(getApplicationContext(), WineDetailActivity.class);
                //Log.d("RedWineActivity", wineAdapter.getItem(position).getName());
                // Passing data as a parcelable object to WineDetailActivity
                intent.putExtra(WINE_DETAIL_KEY, (Parcelable) wine);
                //Log.d("RedwineActivity", wine.getName());
                startActivity(intent);
            }
        });
    }

    // Inspiration from https://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
    public String loadJSONAssetFile() {
        String json;

        // Error exception
        try {
            // Open the json file
            InputStream inputStream = getAssets().open("viner.json");

            int size = inputStream.available();

            byte[] buffer = new byte[size];

            // Read the file
            inputStream.read(buffer);

            // Close the file
            inputStream.close();

            // What kind of text the file should read example UTF-8
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            Log.e("log_tag", "Error in JSON file connection" + e.toString());
            return null;
        }
        return json;
    }

    public void jsonParser() {
        ArrayList<Wine> wineList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(loadJSONAssetFile());

            // Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonObject.getJSONArray("wines");

            //Log.d("RedWineType", jsonArray.toString());

            // Repeat array and print the info in JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                // Storing each json item in variable
                String type = object.getString("type");
                String name = object.getString("name");
                String country = object.getString("country");
                int dice = Integer.parseInt(object.optString("dice"));
                String description = object.getString("description");
                String price = object.getString("price");;

                Wine tempWine = new Wine();
                tempWine.setType(type);
                tempWine.setName(name);
                tempWine.setCountry(country);
                tempWine.setDice(dice);
                tempWine.setDescription(description);
                tempWine.setPrice(price);

                if (tempWine.getType().equals("white"))
                    wineList.add(tempWine);

            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error to parsing data from JSON file " + e.toString());
        }

        // Identify and get the listview
        listView = (ListView) findViewById(R.id.whiteList);

        context = this;

        // List out the data with a red wine adapter
        wineAdapter = new WineAdapter(this, wineList);
        // set the adapter
        listView.setAdapter(wineAdapter);
    }
}
