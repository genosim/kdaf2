package com.proyecto.kdaf;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class Launch extends Activity {

	private List<String> servicesCode = new ArrayList<String>();
	private List<String> scenariosCode = new ArrayList<String>();
	public static final String ARCHIVO_PREFS = "myPrefs";
	private LaunchAsync launchAsync;
	private String username, password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		
		// creamos un editor para poder acceder a las shared preferences
        SharedPreferences settings = getSharedPreferences(ARCHIVO_PREFS, Context.MODE_PRIVATE);
        // comprobamos si existen los valores.
        username = settings.getString("username", null);
        password = settings.getString("password", null);
        
		final Spinner spinService = (Spinner) findViewById(R.id.spinnerService);
		final Spinner spinScenario = (Spinner)findViewById(R.id.spinnerScenario);
		

		final ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_spinner, servicesCode);
		final ArrayAdapter<String> spinner_adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_spinner, scenariosCode);
		
		Button launch = (Button) findViewById(R.id.buttonLaunch);
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.setBasicAuth(username, password);
		
		client.get("http://81.45.23.75:8000/services/expanded/", new JsonHttpResponseHandler() {
		
			public void onSuccess(JSONObject response) {
				JSONArray jArrayserv = null;

				
				try {
					jArrayserv = response.getJSONArray("results");
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				System.out.println("*****JARRAY    1*****"+jArrayserv.length());
				
				
				for(int i=0;i<jArrayserv.length();i++){
					JSONObject json_data;
					try {
						json_data = jArrayserv.getJSONObject(i);
						

						servicesCode.add(json_data.getString("code"));

	
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				}
		

				//Añadimos el layout para el menú y se lo damos al spinner
				spinner_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
				spinService.setAdapter(spinner_adapter);
				
			}
			
		});
		
		

		
		spinService.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.e("seleccion", String.valueOf(arg2));
				Log.e("services", servicesCode.get(arg2));
				if (!scenariosCode.isEmpty())
						scenariosCode.clear();
				
				Log.e("url", "http://81.45.23.75:8000/services/"+servicesCode.get(arg2)+"/");
				AsyncHttpClient client2 = new AsyncHttpClient();
				client2.setBasicAuth(username, password);
				
				client2.get("http://81.45.23.75:8000/services/"+servicesCode.get(arg2)+"/", new JsonHttpResponseHandler() {
				
					public void onSuccess(JSONObject response2) {
						JSONArray jArrayscen = null;

						
						try {
							jArrayscen = response2.getJSONArray("scenarios");
							
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
						System.out.println("*****JARRAY     2*****"+jArrayscen.length());
						
						
						for(int i=0;i<jArrayscen.length();i++){
							JSONObject json_data2;
							try {
								json_data2 = jArrayscen.getJSONObject(i);
								

								scenariosCode.add(json_data2.getString("code"));

			
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							
						}
						
						//Añadimos el layout para el menú y se lo damos al spinner
						spinner_adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
						spinScenario.setAdapter(spinner_adapter2);
						
					}
					
				});
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		

		launch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				lanzarTarea();
				
				
				
				
			}
		});
		
		
		
	}
	
	public void lanzarTarea(){
		
		JSONObject json = new JSONObject();
		JSONObject parameters = new JSONObject();
		JSONObject observations = new JSONObject();
		try {
			
			parameters.put("phone", "9124523");
			observations.put("manifestation", "SLOW");
			json.put("observations", observations);
			json.put("parameters", parameters);
			json.put("service", "sm2m");
			json.put("scenario", "WebLogin");
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Log.e("json", json.toString());
		
		StringEntity entity = null;
		try {
			entity = new StringEntity(json.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
//		comprobarEstado("24");
        
        AsyncHttpClient client = new AsyncHttpClient();
		client.setBasicAuth(username, password);
        client.post(this, "http://81.45.23.75:8000/diagnoses/", entity, "application/json",new JsonHttpResponseHandler() {
                

			public void onSuccess(JSONObject response) {
				String id = "";
				try {
					id = response.getString("id");
					Log.e("id", id);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				comprobarEstado(id);
				
			
			}
	
		});
		
	}
	
	public void comprobarEstado(String id){
		launchAsync = new LaunchAsync(this, id, username, password);
		launchAsync.execute();
	}
}
