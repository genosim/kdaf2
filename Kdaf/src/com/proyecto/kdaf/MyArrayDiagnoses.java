package com.proyecto.kdaf;


import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class MyArrayDiagnoses extends ArrayAdapter<Diagnoses> {

	
	private final Context context;
    private final List<Diagnoses> values;
    
    private LayoutInflater inflater;
    
    public MyArrayDiagnoses(Context context, List<Diagnoses> values) {
    	// constructor
        super(context, R.layout.row_search, values);
        this.context = context;
        this.values = values;
        inflater = LayoutInflater.from(context);
	}


    public View getView(final int position, View convertView, ViewGroup parent) 
    {
        // obtenemos el layout
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        
        //rowlist --> cada una de las lineas de la lista
        
        convertView = inflater.inflate(R.layout.row_search, null);
//        View rowView = inflater.inflate(R.layout.row_search, parent, false);
        TextView tvservice = (TextView) convertView.findViewById(R.id.service);
        TextView tvscenario = (TextView) convertView.findViewById(R.id.scenario);
        TextView tvstarted = (TextView) convertView.findViewById(R.id.started_date);
        
     // fijamos los valores de texto
        tvservice.setText(values.get(position).getService_code());
        tvscenario.setText(values.get(position).getScenario_description());
        tvstarted.setText(values.get(position).getStarted_at());
        
        if (values.get(position).getHypotheses().size()==0){
        	Log.e("hypotheses", "0");
        }else{
        	if (values.get(position).getHypotheses().get(0).getType().equals("All_Ok")){
        		
            	if (Float.parseFloat(values.get(position).getHypotheses().get(0).getProbability()) > 0.90){
            		
            		tvservice.setTextColor(Color.parseColor("#00ff00"));
            		tvscenario.setTextColor(Color.parseColor("#00ff00"));
            		tvstarted.setTextColor(Color.parseColor("#00ff00"));
            	}else{
            		tvservice.setTextColor(Color.parseColor("#f58f19"));
            		tvscenario.setTextColor(Color.parseColor("#f58f19"));
            		tvstarted.setTextColor(Color.parseColor("#f58f19"));
            	}
            		
            }else{
            	tvservice.setTextColor(Color.parseColor("#ff0000"));
            	tvscenario.setTextColor(Color.parseColor("#ff0000"));
            	tvstarted.setTextColor(Color.parseColor("#ff0000"));
            }
        }
        
      
        return convertView;
    }


}
