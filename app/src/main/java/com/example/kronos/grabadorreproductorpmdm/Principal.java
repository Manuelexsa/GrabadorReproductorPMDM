package com.example.kronos.grabadorreproductorpmdm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Principal extends Activity {

    private static final int RECORD=0;
    private ArrayList canciones;
    private ArrayList<String> titulos;
    private Adaptador ad;
    private TextView Titulo;
    private boolean reproduciendo;
    private Button btPlay;
    private int cancionActual;
    private Cursor c;
    private Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        intent = new Intent(Principal.this, ServicioAudio.class);
        btPlay = (Button)findViewById(R.id.btPlay);
        canciones = new ArrayList();
        titulos = new ArrayList(getData());
        reproduciendo = false;
        Titulo = (TextView)findViewById(R.id.tvTitulo);
        ad= new Adaptador(this, R.layout.adapterdetalle,titulos);
        final ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getBaseContext(), canciones.get(i).toString(), Toast.LENGTH_LONG).show();
                Titulo.setText(titulos.get(i).toString());
                Intent intent = new Intent(Principal.this, ServicioAudio.class);
                intent.setAction(ServicioAudio.STOP);
                startService(intent);
                intent = new Intent(Principal.this, ServicioAudio.class);
                intent.putExtra("cancion", canciones.get(i).toString());
                intent.setAction(ServicioAudio.ADD);
                startService(intent);
                intent = new Intent(Principal.this, ServicioAudio.class);
                intent.setAction(ServicioAudio.PLAY);
                startService(intent);
                cancionActual = i;
                reproduciendo = true;
            }
        });
        registerForContextMenu(lv);
    }

    private ArrayList<String> getData() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] proyeccion = null;
        String condicion = null;
        String[] parametros = null;
        String orden = null;
         c = getContentResolver().query(
                uri,
                proyeccion,
                condicion,
                parametros,
                orden);
        String ruta = "";
        String titulo = "";
        c.getColumnNames();
        c.moveToFirst();
        int i = 0;
        ArrayList<String> ts=new ArrayList<String>();
        while(!c.isAfterLast()){
            ruta = c.getString(c.getColumnIndex("_data"));
            titulo = c.getString(c.getColumnIndex("title"));
            Log.v("titulos",titulo);
            canciones.add(ruta);
            ts.add(titulo);
           // titulos.add(titulo);
            c.moveToNext();
            i++;
        }
        return ts;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addSong) {
//            add();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pause(View v){
        if(reproduciendo){
            reproduciendo = false;
            btPlay.setBackground(getResources().getDrawable(R.drawable.rp_play));
        } else {
            reproduciendo = true;
            btPlay.setBackground(getResources().getDrawable(R.drawable.rp_pause));
        }
        intent.setAction(ServicioAudio.PAUSE);
        startService(intent);
    }

    public void next(View v){
        if(cancionActual<canciones.size()-1){
            cancionActual++;
            Titulo.setText(titulos.get(cancionActual).toString());

            intent.setAction(ServicioAudio.STOP);
            startService(intent);
            intent.putExtra("cancion", canciones.get(cancionActual).toString());
            intent.setAction(ServicioAudio.ADD);
            startService(intent);
            intent.setAction(ServicioAudio.PLAY);
            startService(intent);
            reproduciendo = true;
        }else{
            cancionActual = 0;
            Titulo.setText(titulos.get(0).toString());
            intent.setAction(ServicioAudio.STOP);
            startService(intent);
            intent.putExtra("cancion", canciones.get(0).toString());
            intent.setAction(ServicioAudio.ADD);
            startService(intent);
            intent.setAction(ServicioAudio.PLAY);
            startService(intent);
            reproduciendo = true;
        }
    }

    public void prev(View v){
        if(cancionActual != 0){
            cancionActual--;
            Titulo.setText(titulos.get(cancionActual).toString());
            intent.setAction(ServicioAudio.STOP);
            startService(intent);
            intent.putExtra("cancion", canciones.get(cancionActual).toString());
            intent.setAction(ServicioAudio.ADD);
            startService(intent);
            intent.setAction(ServicioAudio.PLAY);
            startService(intent);
            reproduciendo = true;
        }else{
            cancionActual = canciones.size()-1;
            Titulo.setText(titulos.get(cancionActual).toString());
            intent.setAction(ServicioAudio.STOP);
            startService(intent);
            intent.putExtra("cancion", canciones.get(cancionActual).toString());
            intent.setAction(ServicioAudio.ADD);
            startService(intent);
            intent.setAction(ServicioAudio.PLAY);
            startService(intent);
            reproduciendo = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, ServicioAudio.class));
    }

    public void record(View v){
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, RECORD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == RECORD) {
            Uri uri = data.getData();
            ad.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ad.notifyDataSetChanged();
    }


}