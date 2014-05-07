package com.proyecto.kdaf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import android.widget.TextView;

public class DetailLaunch extends Activity {

	private String posicion;
	private Date fecha_ini, fecha_fin;
	public static final String ARCHIVO_PREFS = "myPrefs";
	private String username, password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		// creamos un editor para poder acceder a las shared preferences
        SharedPreferences settings = getSharedPreferences(ARCHIVO_PREFS, Context.MODE_PRIVATE);
        // comprobamos si existen los valores.
        username = settings.getString("username", null);
        password = settings.getString("password", null);
        
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			posicion = extras.getString("posicion");
		
			Log.e("posicion", posicion);
			
		}
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.setBasicAuth(username, password);
		
		client.get("http://81.45.23.75:8000/diagnoses/"+posicion+"/", new JsonHttpResponseHandler() {
			@Override
		    public void onSuccess(JSONObject response) {
				
				System.out.println(response);
//				JSONObject json = null;
//				try {
//					json = new JSONObject(response);
//				} catch (JSONException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				
				TextView service = (TextView) findViewById(R.id.serviceDet);
				TextView scenario = (TextView) findViewById(R.id.scenarioDet);
				TextView started = (TextView) findViewById(R.id.startedDet);
				TextView finished = (TextView) findViewById(R.id.finishedDet);
				
				
				JSONObject jsonService = null;
				JSONObject jsonScenario = null;
				try {
					jsonService = new JSONObject(response.getString("service"));
					jsonScenario = new JSONObject(response.getString("scenario"));
					
					
					service.setText(jsonService.getString("description"));
					scenario.setText(jsonScenario.getString("description"));
					
					Log.e("service",jsonService.getString("description"));
					Log.e("scenario",jsonScenario.getString("description"));
					
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
		
				
				
				fecha_ini = new Date();
				fecha_fin = new Date();
				
				 try {
					 fecha_ini = toDate(response.getString("started_at"));
					 fecha_fin = toDate(response.getString("finished_at"));
				 } catch (ParseException e) {
					// TODO Auto-generated catch block
				   	 e.printStackTrace();
				 } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				 
				 
				started.setText(fecha_ini.toString());
				finished.setText(fecha_fin.toString());
				
				
				TextView hyp1 = (TextView) findViewById(R.id.hyp1);
				TextView hyp2 = (TextView) findViewById(R.id.hyp2);
				TextView hyp3 = (TextView) findViewById(R.id.hyp3);
				TextView hyp4 = (TextView) findViewById(R.id.hyp4);
				
				TextView prob1 = (TextView) findViewById(R.id.prob1);
				TextView prob2 = (TextView) findViewById(R.id.prob2);
				TextView prob3 = (TextView) findViewById(R.id.prob3);
				TextView prob4 = (TextView) findViewById(R.id.prob4);
				
				JSONArray hypothesesArray;
				try {
					hypothesesArray = response.getJSONArray("hypotheses");
					
					hyp1.setText(hypothesesArray.getJSONObject(0).getString("type"));
					prob1.setText(hypothesesArray.getJSONObject(0).getString("percentage"));
					
					hyp2.setText(hypothesesArray.getJSONObject(1).getString("type"));
					prob2.setText(hypothesesArray.getJSONObject(1).getString("percentage"));
					
					hyp3.setText(hypothesesArray.getJSONObject(2).getString("type"));
					prob3.setText(hypothesesArray.getJSONObject(2).getString("percentage"));
					
					hyp4.setText(hypothesesArray.getJSONObject(3).getString("type"));
					prob4.setText(hypothesesArray.getJSONObject(3).getString("percentage"));
					
					if (jsonService.getString("code").equals("sm2m")){
						TextView hyp5 = (TextView) findViewById(R.id.hyp5);
						TextView prob5 = (TextView) findViewById(R.id.prob5);
						hyp5.setText(hypothesesArray.getJSONObject(4).getString("type"));
						prob5.setText(hypothesesArray.getJSONObject(4).getString("percentage"));
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				

			}
			
		});
	}
	
	/** Transform ISO 8601 string to Date. */
    public Date toDate(String iso8601string)
            throws ParseException {
        
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        
        return date;
    }
}
