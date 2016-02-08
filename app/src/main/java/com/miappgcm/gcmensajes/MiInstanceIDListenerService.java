package com.miappgcm.gcmensajes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by javier on 06/02/2016.
 */
public class MiInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";

    /**
     * Se llama cuando Gcm servers actualizan el registration token, principalemnte por motivos  de seguridad
     */
    @Override
    public void onTokenRefresh() {
        //obtener nuevamente el token y enviarlo a la aplicacion servidor
        RegitroGcmcAsyncTask regitroGcmcAsyncTask = new RegitroGcmcAsyncTask();
        regitroGcmcAsyncTask.execute();
    }

    private class RegitroGcmcAsyncTask extends AsyncTask<String , String, Object> {

        private ProgressDialog mProgressDialog = new ProgressDialog(MiInstanceIDListenerService.this);
        Context context = MiInstanceIDListenerService.this;

        @Override
        protected Object doInBackground(String ... params) {

            try {

                publishProgress("Obteniendo Registration Token en GCM Servers...");
                String registrationToken = Utilidades.ObtenerRegistrationTokenEnGcm(getApplicationContext());

                publishProgress("Enviando Registration a mi aplicacion servidor...");
                String respuesta = Utilidades.RegistrarseEnAplicacionServidor(getApplicationContext(),registrationToken, MainActivity.userid);
                return respuesta;
            }
            catch (Exception ex){
                return ex;
            }
        }

        protected void onProgressUpdate(String... progress) {
            Toast.makeText(getApplicationContext(),progress[0],Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Object result)
        {
            if(result instanceof  String)
            {
                String resultado = (String)result;
                Toast.makeText(getApplicationContext(), "Registro exitoso. " + resultado, Toast.LENGTH_SHORT).show();
            }
            else if (result instanceof Exception)//Si el resultado es una Excepcion..hay error
            {
                Exception ex = (Exception) result;
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
