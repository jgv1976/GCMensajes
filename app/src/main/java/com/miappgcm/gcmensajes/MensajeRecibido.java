package com.miappgcm.gcmensajes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MensajeRecibido extends AppCompatActivity {

    TextView tv_titulo, tv_msj;
    String msj, titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje_recibido);
        tv_titulo = (TextView) findViewById(R.id.tv_titulo);
        tv_msj = (TextView) findViewById(R.id.tv_msj);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            msj = extras.getString("mensaje");
            titulo = extras.getString("titulo");
        }

        tv_titulo.setText(titulo);
        tv_msj.setText(msj);

    }

    public void salir(View v) {
        finish();
    }

}
