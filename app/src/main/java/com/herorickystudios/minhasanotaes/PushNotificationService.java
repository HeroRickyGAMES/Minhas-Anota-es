package com.herorickystudios.minhasanotaes;

//Programado por HeroRickyGames

//Serviço de notificações do app!
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String titulo = remoteMessage.getNotification().getTitle();
        String texto = remoteMessage.getNotification().getBody();

        final String CHANNEL_ID = "HANDS_UP_NOTIFICATION";
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Hands Up Notification",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(titulo)
                .setContentText(texto)
                .setSmallIcon(R.drawable.appicon)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1,builder.build());

        super.onMessageReceived(remoteMessage);
    }
}
