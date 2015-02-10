package com.example.kronos.grabadorreproductorpmdm;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kronos on 10/02/2015.
 */
public class Adaptador extends ArrayAdapter<String> {

    private Context contexto;
    private ArrayList<String> lista;
    private int recurso;
    static LayoutInflater i;


    public static class ViewHolder{
        public TextView titulo;
        public int posicion;
    }

    public Adaptador(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.lista=objects;
        this.recurso=resource;
        this.i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.titulo=(TextView)convertView.findViewById(R.id.titulo);
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder)convertView.getTag();
        }
        vh.posicion = position;
        vh.titulo.setText(lista.get(position));
        return convertView;

    }
}
