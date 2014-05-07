package com.proyecto.kdaf;



import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LaunchAsync extends AsyncTask<Void, Void,Void> {

	
	Context context;
	ProgressDialog pDialog;
	Boolean respuesta = false;
	String id;
	String estado;
	private String username, password;
	
	public LaunchAsync(Context cont, String id, String username, String password) {
		this.context = cont;
		this.id = id;
		this.username = username;
		this.password = password;
		
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		while (respuesta != true){
			Log.e("respuesta", respuesta.toString());
			comprobarEstado();

		}
		
		return null;
	}
	

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pDialog.dismiss();
		Toast.makeText(context,"Cargado", Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(context, DetailLaunch.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("posicion", id);
		context.startActivity(intent);
		//showDialog(0);
	}



	public void comprobarEstado(){
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.setBasicAuth(username, password);
		
		client.get("http://81.45.23.75:8000/diagnoses/"+id+"/", new AsyncHttpResponseHandler() {
			@Override
		    public void onSuccess(String response) {
		        System.out.println(response);
		        
		        try {
					JSONObject json = new JSONObject(response);
					estado = json.getString("status");
					Log.e("status comprobarestado", json.getString("status"));
					if (estado.contains("terminated")){
						respuesta = true;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Loading...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	
	
	
	

}
