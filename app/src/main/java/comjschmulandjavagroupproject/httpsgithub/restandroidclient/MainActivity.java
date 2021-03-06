package comjschmulandjavagroupproject.httpsgithub.restandroidclient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Stuff> stuffList = new ArrayList<>();
    private StuffArrayAdapter stuffArrayAdapter;
    private ListView stuffListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create ArrayAdapter to bind stuffList to the stuffListView
        stuffListView = (ListView) findViewById(R.id.stuffListView);
        stuffArrayAdapter = new StuffArrayAdapter(this, stuffList);
        stuffListView.setAdapter(stuffArrayAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText locationStuffIdText = (EditText) findViewById(R.id.stuffIDEditText);
                URL url = createURL(locationStuffIdText.getText().toString());

                if (url != null){
                    dismissKeyboard(locationStuffIdText);
                    GetStuffTask getStuffTask = new GetStuffTask();
                    getStuffTask.execute(url);
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            R.string.connect_error, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    //programmatically dismiss keyboard when user touches FAB
    private void dismissKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // create web service URL using id specified. if there was an id provided by user
    private URL createURL(String id){
        String baseUrl = getString(R.string.web_service_url);
        String urlString = null;

        try{
            if(id.length()<1){//search field was empty
                urlString = baseUrl;
            }else{//search field has an id
                urlString = baseUrl + URLEncoder.encode(id,"UTF-8");
            }

            return new URL(urlString);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return null; //URL was malformed
    }

    //makes the REST web service call to get stuff data and saves the data to a local HTML file
    private class GetStuffTask extends AsyncTask<URL,Void,JSONArray>{

        @Override
        protected JSONArray doInBackground(URL... params){
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();

                //See 09 JEE_Demo for stackoverflow examples
                connection.addRequestProperty("Accept", "application/json");

                int responce = connection.getResponseCode();

                if (responce == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {
                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e) {
                        Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.read_error, Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    String json = builder.toString();
                    if (!json.startsWith("[")) {
                        json = String.format("[%s]", json);
                    }

                    return new JSONArray(json);
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            R.string.connect_error, Snackbar.LENGTH_LONG).show();
                }
            }catch (Exception e) {
                Snackbar.make(findViewById(R.id.coordinatorLayout),
                        R.string.connect_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }finally {
                connection.disconnect();// close the HttpURLConnection
            }

            return null;
        }

        //process JSON responce and update ListView
        @Override
        protected void onPostExecute(JSONArray stuffs){
            convertJSONtoArrayList(stuffs); //repopulate stuffList
            stuffArrayAdapter.notifyDataSetChanged();// rebind to ListView
            stuffListView.smoothScrollToPosition(0);//scroll to top
        }

        //create Stuff object from JSONArrya containing the stuff records
        private void convertJSONtoArrayList(JSONArray list){
            stuffList.clear();//clear old stuff data

            try{
                //convert each element of list to a stuff object
                for(int i = 0;i<list.length(); ++i){
                    JSONObject stuff = list.getJSONObject(i);//get one

                    // add new Stuff object to stuffList
                    stuffList.add(new Stuff(
                            stuff.getString("id"),
                            stuff.getString("stuff"),
                            stuff.getString("moreStuff")));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
