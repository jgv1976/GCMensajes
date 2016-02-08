package com.miappgcm.gcmensajes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
/**
 * Created by javier on 06/02/2016.
 */
public class MiGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    Activity activity = new Activity();

    /**
     * Cuando el mensage es recibido.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        final String mensaje = data.getString("mensaje");
        final String titulo = data.getString("titulo");
        //Log.d(TAG, "From: " + from);
        //Log.d(TAG, "Message: " + message);

        /**
         * El  mensaje recibido se proceso en este metodo.
         * Ejemplo: - Sincronizar con servidor.
         *     - Almacenar mensajes en base de datos local.
         *     - Actualizar UI.
         *     - Mostrar nofificaciones
         */

        if (Utilidades.actividadAbierta) {

            abreActivity(mensaje, titulo);
/*
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    //Toast.makeText(MiGcmListenerService.this, mensaje, Toast.LENGTH_SHORT).show();
                    mostrarMensaje(mensaje, titulo, R.drawable.cast_ic_notification_2);
                }
            });
*/
        }else {
            this.MostrarNotification(mensaje);
        }
    }

    private void abreActivity(String mensaje, String titulo) {
        Intent i = new Intent(this, MensajeRecibido.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("mensaje", mensaje);
        i.putExtra("titulo", titulo);
        startActivity(i);
    }


    private void MostrarNotification(String message) {

        long[] vibrate = {0,100,200,300};
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
                .setContentTitle("Mensaje GCM")
                .setVibrate(vibrate)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int idNotificacion = 1;
        notificationManager.notify(1, notificationBuilder.build());
    }

    private void mostrarMensaje(String mensaje, String titulo, int icono) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
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
        alertDialog.show();
    }
}
