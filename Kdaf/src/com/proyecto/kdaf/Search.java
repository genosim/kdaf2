package com.proyecto.kdaf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;


public class Search extends AbstractListViewActivity {

//	private SearchAsyn diagnoses;
	private ListView listDiagnosis;
	public static List<Diagnoses> listado = new ArrayList<Diagnoses>();
	public static final String ARCHIVO_PREFS = "myPrefs";
	private Date fecha_ini, fecha_fin;
	private String next = "http://81.45.23.75:8000/diagnoses/expanded/";
	private String username, password;
	private ProgressDialog pDialog;;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
		
		listado.clear();
		// creamos un editor para poder acceder a las shared preferences
        SharedPreferences settings = getSharedPreferences(ARCHIVO_PREFS, Context.MODE_PRIVATE);
        // comprobamos si existen los valores.
        username = settings.getString("username", null);
        password = settings.getString("password", null);
        
        AsyncHttpClient client = new AsyncHttpClient();
		client.setBasicAuth(username, password);
		
		client.get(next, new JsonHttpResponseHandler() {
			public void onSuccess(JSONObject response) {
				try {
					String count = response.getString("count");
					next = next+"?per_page="+count;
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Log.e("pagina", next);
				leerJson();
			}
		});

        
//        leerJson();
		
//		cargarListview();
				

		}

	public void leerJson(){
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.setBasicAuth(username, password);
		client.get(next, new JsonHttpResponseHandler() {
			@Override
            public void onSuccess(JSONObject response) {
                // Pull out the first event on the public timeline
				JSONArray jArray = null;
				try {
					jArray = response.getJSONArray("results");
					String count = response.getString("count");
//					next = response.getString("next");
					
					Log.e("NEXT", next);
					Log.e("COUNT", String.valueOf(count));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				System.out.println("*****JARRAY*****"+jArray.length());
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data;
					try {
						json_data = jArray.getJSONObject(i);
						Diagnoses myDiagnoses = new Diagnoses();
						 myDiagnoses.setId(json_data.getString("id"));
						 myDiagnoses.setUrl(json_data.getString("url"));
						 myDiagnoses.setScenario_id(json_data.getString("scenario_id"));
						 myDiagnoses.setScenario_url(json_data.getString("scenario_url"));
						 myDiagnoses.setService_url(json_data.getString("service_url"));
						 
						 JSONObject scenarioDict = new JSONObject(json_data.getString("scenario"));
						 myDiagnoses.setScenario_description(scenarioDict.getString("description"));
						 
						 JSONObject serviceDict = new JSONObject(json_data.getString("service"));
						 myDiagnoses.setService_description(serviceDict.getString("description"));
						 myDiagnoses.setService_code(json_data.getString("service_code"));
						 
						 fecha_ini = new Date();
						 try {
							 fecha_ini = toDate(json_data.getString("started_at"));
						 } catch (ParseException e) {
							// TODO Auto-generated catch block
						   	 e.printStackTrace();
						 }
						 
						 //Log.e("fecha",fecha.toString());
						 
						 myDiagnoses.setStarted_at(fecha_ini.toString());

						 fecha_fin = new Date();
						 try {
							 fecha_fin = toDate(json_data.getString("finished_at"));
						 } catch (ParseException e) {
							// TODO Auto-generated catch block
						   	 e.printStackTrace();
						 }
						 
						 //Log.e("fecha",fecha.toString());
						 

						 myDiagnoses.setFinished_at(fecha_fin.toString());
						 myDiagnoses.setStatus(json_data.getString("status"));
						 
						 JSONArray hypothesesArray = json_data.getJSONArray("hypotheses");
						 Log.e("long hypotheses", String.valueOf(hypothesesArray.length()));
						 for(int j=0;j < hypothesesArray.length();j++)
		                    {
		                        JSONObject hypothesesObj = hypothesesArray.getJSONObject(j);

		                        Hypotheses hypotheses = new Hypotheses();
		                        hypotheses.setPercentage(hypothesesObj.getString("percentage"));
		                        hypotheses.setId(hypothesesObj.getString("id"));
		                        hypotheses.setType(hypothesesObj.getString("type"));
		                        hypotheses.setValue(hypothesesObj.getString("value"));
		                        hypotheses.setProbability(hypothesesObj.getString("probability"));
		                     
		                        myDiagnoses.anadirHypotheses(hypotheses);

		                    }
						 
						 listado.add(myDiagnoses);
						 
						
//						Log.e("DIAGNOSES", "id  "+json_data.getString("id")+
//								  ", url  "+json_data.getString("url")+
//								  ", scenario_id  "+json_data.getString("scenario_id")+
//								  ", scenario_url  "+json_data.getString("scenario_url")+
//								  ", service_url  "+json_data.getString("service_url")+
//								  ", service_code  "+json_data.getString("service_code")+
//								  ", started_at  "+json_data.getString("started_at")+
//								  ", finished_at  "+json_data.getString("finished_at")+
//								  ", status  "+json_data.getString("status")
//								 );
//						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				cargarListview();
				
			 }
		});
			
	}
	
	public void cargarListview(){
		Log.e("listado ", String.valueOf(listado.size()));
		
		pDialog.dismiss();
		
		footerView = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
		getListView().addFooterView(footerView, null, false);
		setListAdapter(new MyArrayDiagnoses(getApplicationContext(), getData(0, PAGESIZE)));
		getListView().removeFooterView(footerView);

		getListView().setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1)
			{
				// nothing here
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				if (load(firstVisibleItem, visibleItemCount, totalItemCount))
				{
					loading = true;
					getListView().addFooterView(footerView, null, false);
					(new LoadNextPage()).execute("");
				}
			}
		});

		updateDisplayingTextView();
		
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
    
    /**
	 * Returns the elements in a <b>NEW</b> list.
	 */
	public static List<Diagnoses> getData(int offset, int limit)
	{
		List<Diagnoses> newList = new ArrayList<Diagnoses>(limit);
		int end = offset + limit;
		if (end > listado.size())
		{
			end = listado.size();
		}
		newList.addAll(listado.subList(offset, end));
		return newList;		
	}
	
	
}
