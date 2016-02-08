package com.miappgcm.gcmensajes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by javier on 05/02/2016.
 */
public class Utilidades {

    final String senderid = "935809847937";
    static public boolean actividadAbierta = true;


    public static AlertDialog MostrarAlertDialog(Context activity, String mensaje, String titulo, int icono) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(mensaje);
        builder1.setIcon(icono);
        builder1.setTitle(titulo);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder1.create();
        return alertDialog;
    }



    public static boolean compruebaPlayServices(Activity context) {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context, 9000).show();
            }
            else {
                Toast.makeText(context, "Dispositivo no soportado", Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }



    public static String RegistrarseEnAplicacionServidor(Context context,String registrationToken, String userid) throws  Exception {
        if (userid.equals("")) userid = "IdUsuario";
        String imei = DameIMEI(context);
        String stringUrl = "http://www.javigomez.net/gcm/registrar.php?imei=" + imei + "&token=" + registrationToken + "&userid=" + userid;

        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000 /* milliseconds */);
        connection.setConnectTimeout(15000 /* milliseconds */);
        connection.setRequestMethod("GET");

        int codigoEstado = connection.getResponseCode();
        if(codigoEstado != 200)
            throw new Exception("Error al procesar registro. Estado Http: " + codigoEstado);

        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

        String respuesta = "",linea;
        while ((linea = bufferedReader.readLine()) != null) {
            respuesta = respuesta + linea;
        }

        bufferedReader.close();
        inputStream.close();

        //respuesta = new JSONObject(respuesta).getString("RegistroGcmResult");
        if(respuesta.equals("REGISTRADO"))
            respuesta = "Este dispositivo ya esta registrado";
        else if(respuesta.equals("OK"))
            respuesta = "El dispositivo se ha registrado con exito";
        else if(!respuesta.equals("OK"))
            throw new Exception("Error al registrar el dispositivo en el servidor: " + respuesta);
        return respuesta;
    }


    public static String DesregistrarseEnAplicacionServidor(Context context) throws  Exception {
        String imei = DameIMEI(context);
        String stringUrl = "http://www.javigomez.net/gcm/desregistrar.php?imei=" + imei;

        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000 /* milliseconds */);
        connection.setConnectTimeout(15000 /* milliseconds */);
        connection.setRequestMethod("POST");

        int codigoEstado = connection.getResponseCode();
        if(codigoEstado != 200)
            throw new Exception("Error al procesar la operacion. Estado Http: " + codigoEstado);

        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

        String respuesta = "",linea;
        while ((linea = bufferedReader.readLine()) != null) {
            respuesta = respuesta + linea;
        }

        bufferedReader.close();
        inputStream.close();

        //respuesta = new JSONObject(respuesta).getString("RegistroGcmResult");
        if(respuesta.equals("NOREGISTRADO"))
            respuesta = "Este dispositivo NO esta registrado";
        else if(respuesta.equals("OK"))
            respuesta = "El dispositivo se ha eliminado del registro con exito";
        else if(!respuesta.equals("OK"))
            throw new Exception("Error al realizar la operacion: " + respuesta);
        return respuesta;
    }



    public static String ObtenerRegistrationTokenEnGcm(Context context) throws  Exception {
        InstanceID instanceID = InstanceID.getInstance(context);

        String token = instanceID.getToken("935809847937",
                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

        return token;
    }



    public static String DameIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static void muestraMensaje(Activity context) {
        Toast.makeText(context, "Esta es un mensaje de prueba", Toast.LENGTH_SHORT).show();
    }
}
