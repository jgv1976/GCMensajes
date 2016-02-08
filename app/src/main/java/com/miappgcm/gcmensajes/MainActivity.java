package com.miappgcm.gcmensajes;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public TextView tv_userid;
    public static String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     /*   if (Utilidades.compruebaPlayServices(this))
            Toast.makeText(this, "Dispositivo compatible", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "Dispositivo NO compatible", Toast.LENGTH_SHORT).show(); */


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        Utilidades.actividadAbierta = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        Utilidades.actividadAbierta = false;
    }


    public void comprobarConectividad(View v) {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if ((info == null || !info.isConnected() || !info.isAvailable())) {
            //Toast.makeText(MainActivity.this,"Oops! No tienes conexión a internet", Toast.LENGTH_LONG).show();
            Utilidades.MostrarAlertDialog(this, "Oops! No tienes conexión a internet", "Mensaje de GCMensajes", R.drawable.cast_ic_notification_2).show();
        }else {
            Utilidades.MostrarAlertDialog(this, "El dispositivo esta conectado a Internet", "Mensaje de GCMensajes", R.drawable.cast_ic_notification_2).show();
        }
    }

    public void notificacion(View v) {
        //Utilidades.muestraMensaje(this);
        Utilidades.MostrarAlertDialog(this, "esto es un mensaje desde Alert Dialog", "Mensaje desde la aplicacion", R.drawable.cast_ic_notification_2).show();
    }


    public void obtenerToken(View v) {
        String tok="";
        try {
            //tok = Utilidades.ObtenerRegistrationTokenEnGcm(this);
            tok = Utilidades.DameIMEI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Utilidades.MostrarAlertDialog(this, tok, "Token obtenido", R.drawable.cast_ic_notification_2).show();
        //Toast.makeText(MainActivity.this, tok, Toast.LENGTH_SHORT).show();
    }


    public void registrarDispositivo(View v) {
        tv_userid = (TextView) findViewById(R.id.tv_userid);
        userid = tv_userid.getText().toString().trim();
        RegistroGcmcAsyncTask registro = new RegistroGcmcAsyncTask();
        registro.execute();
        //Toast.makeText(MainActivity.this, "esto es mensaje de registrar dispositivo", Toast.LENGTH_SHORT).show();
    }


    public void desregistrarDispositivo(View v) {
        DesregistroGcmcAsyncTask desregistro = new DesregistroGcmcAsyncTask();
        desregistro.execute();
        //Toast.makeText(MainActivity.this, "esto es mensaje de Desregistrar el dispositivo", Toast.LENGTH_SHORT).show();
    }

    private class RegistroGcmcAsyncTask extends AsyncTask<String , String, Object> {

        private ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);
        Context context = MainActivity.this;

        @Override
        protected void onPreExecute() {
/*            mProgressDialog.setMessage("Registrando en aplicacion servidor...");
            mProgressDialog.show();
*/
        }

        @Override
        protected Object doInBackground(String ... params) {

            try {

                publishProgress("Obteniendo Registration Token en GCM Servers...");
                String registrationToken = Utilidades.ObtenerRegistrationTokenEnGcm(getApplicationContext());

                publishProgress("Enviando Registration a mi aplicacion servidor...");
                String respuesta = Utilidades.RegistrarseEnAplicacionServidor(getApplicationContext(),registrationToken, userid);
                return respuesta;
            }
            catch (Exception ex){
                return ex;
            }
        }

        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setMessage(progress[0]);
        }

        @Override
        protected void onPostExecute(Object result)
        {
            mProgressDialog.dismiss();

            if(result instanceof  String)
            {
                String resultado = (String)result;
                Utilidades.MostrarAlertDialog(context, resultado, "GCM", R.drawable.common_google_signin_btn_icon_dark).show();
            }
            else if (result instanceof Exception)//Si el resultado es una Excepcion..hay error
            {
                Exception ex = (Exception) result;
                Utilidades.MostrarAlertDialog(context, ex.getMessage(), "ERROR", R.drawable.cast_ic_notification_2).show();
            }
        }

    }


    private class DesregistroGcmcAsyncTask extends AsyncTask<String , String, Object> {

        private ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);
        Context context = MainActivity.this;

/*
        @Override
        protected void onPreExecute() {
            mProgressDialog.setMessage("Registrando en aplicacion servidor...");
            mProgressDialog.show();
        }
*/
        @Override
        protected Object doInBackground(String ... params) {

            try {

                publishProgress("Enviando Registration a mi aplicacion servidor...");
                String respuesta = Utilidades.DesregistrarseEnAplicacionServidor(getApplicationContext());
                return respuesta;
            }
            catch (Exception ex){
                return ex;
            }
        }

        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setMessage(progress[0]);
        }

        @Override
        protected void onPostExecute(Object result)
        {
            mProgressDialog.dismiss();

            if(result instanceof  String)
            {
                String resultado = (String)result;
                Utilidades.MostrarAlertDialog(context, resultado, "GCM", R.drawable.common_google_signin_btn_icon_dark).show();
            }
            else if (result instanceof Exception)//Si el resultado es una Excepcion..hay error
            {
                Exception ex = (Exception) result;
                Utilidades.MostrarAlertDialog(context, ex.getMessage(), "ERROR", R.drawable.cast_ic_notification_2).show();
            }
        }

    }
}
