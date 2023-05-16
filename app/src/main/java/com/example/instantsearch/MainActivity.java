package com.example.instantsearch;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    private EditText texto;
    ArrayList<String> ListaActualizada = new ArrayList<String>();
    ArrayList<String> ListaPalabras = new ArrayList<String>();
    PublishSubject<String> ps;
    ListView lalista ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ps = PublishSubject.create();
        texto = findViewById(R.id.editTextTEXTO);

        ListaPalabras.add("Lucia");
        ListaPalabras.add("Lucas");
        ListaPalabras.add("Laura");
        ListaPalabras.add("Alfonso");
        ListaPalabras.add("Adrian");
        ListaPalabras.add("Carlos");
        ListaPalabras.add("Claudio");

        lalista = (ListView) findViewById(R.id.lista);
        //Creamos el ArrayAdapter y utilizamos el layout más sencillo que es el que muestra solo un TextView en cada Item
        // y lo indicamos mediante el valor R.layout.activity_main:
        ArrayAdapter eladaptador =
                new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1,ListaPalabras);


        //Obtenemos la referencia del ListView y le asociamos el ArrayAdapter para que pueda recibir los items a mostrar:
        lalista.setAdapter(eladaptador);

        ArrayAdapter eladaptador2 =
                new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1,ListaActualizada);


        Observer<String> obsrvr = new Observer<String>() {
            String palabra;
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(String s) {


                ListaActualizada.clear();
                for (String palabra : ListaPalabras)
                    if (palabra.contains(s)) {
                        ListaActualizada.add(palabra);
                    }
                //Como hacemos una busqueda, cargamos el listView nuevo
                 lalista.setAdapter(eladaptador2);
                eladaptador2.notifyDataSetChanged();


            }
            @Override
            public void onError(Throwable e) {
                palabra = "";
                Log.e("MY-APP", "Error Observer");
                e.printStackTrace();
            }
            @Override
            public void onComplete() {
                //((TextView)findViewById(R.id.textView)).setText(palabra);
            }
        };
        ps.debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(obsrvr);


        texto.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int
                    i1, int i2) {

                if(i2 == 0){//Si se vuelve a la situación original

                    lalista.setAdapter(eladaptador);
                    eladaptador.notifyDataSetChanged();
                } else if (Math.abs(i1 - i2)==1) { //Si hay algun cambio en el editText
                    Log.i("App", "Nuevo texto: "+charSequence);
                    ps.onNext(charSequence.toString());
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


}