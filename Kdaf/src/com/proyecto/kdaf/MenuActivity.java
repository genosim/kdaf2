package com.proyecto.kdaf;


import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MenuActivity extends Activity {

	public static final String ARCHIVO_PREFS = "myPrefs";
	public static JSONObject respJSON;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		// creamos un editor para poder acceder a las shared preferences
        SharedPreferences settings = getSharedPreferences(ARCHIVO_PREFS, Context.MODE_PRIVATE);
        // comprobamos si existen los valores.
        String username = settings.getString("username", null);
        String password = settings.getString("password", null);
        
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(username,password);
        client.get("http://81.45.23.75:8000/", new AsyncHttpResponseHandler(){
            

            public void onSuccess(String response) {
                // Successfully got a response
            	Log.e("sucess", response);
            }

        });
        
        Button search = (Button)findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(),Search.class);
				startActivity(intent);
				
	
			}
		});
		

		
		Button launch = (Button)findViewById(R.id.launch);
		launch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(getApplicationContext(),Launch.class);
				startActivity(intent);
				
	
			}
		});
		
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(getApplicationContext(),CambiarSettings.class);
		startActivity(intent);
		finish();
		//Toast.makeText(getApplicationContext(),"PULSADO SETTINGS", Toast.LENGTH_SHORT).show();
		return true;
    }

}
