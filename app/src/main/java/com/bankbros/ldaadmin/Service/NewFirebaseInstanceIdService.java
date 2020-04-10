package com.bankbros.ldaadmin.Service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bankbros.ldaadmin.ChatsFragment;
import com.bankbros.ldaadmin.DashBoardActivity;
import com.bankbros.ldaadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

public class NewFirebaseInstanceIdService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("Token", "sendTokenToServer: "+s);

        sendTokenToServer(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(checkApp())
            return;
        if(FirebaseAuth.getInstance().getCurrentUser()!= null)
        if (remoteMessage.getData() == null || FirebaseAuth.getInstance().getCurrentUser().getEmail() == null) {
            return;
        }
        Log.i("New", "onMessageReceived: "+"Notification");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            sendNotification(remoteMessage);
        }else {
            sendNotification26(remoteMessage);

        }
    }

    public boolean checkApp(){
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);

        // get the info from the currently running task
        List<ActivityManager.RunningTaskInfo> taskInfo = null;
        if (am != null) {
            taskInfo = am.getRunningTasks(1);
        }

        ComponentName componentInfo = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            componentInfo = taskInfo.get(0).topActivity;
        }
        if (componentInfo != null) {
            if (componentInfo.getPackageName().equalsIgnoreCase("com.bankbros.ldaadmin")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public void sendNotification(RemoteMessage remoteMessage){

        Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String content = data.get("content");

        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("Message Notifications", "Message", NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }

            showNotification(title,content);
        }

    }

    public void sendNotification26(RemoteMessage remoteMessage){


        Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String content = data.get("content");


        Intent notificationIntent = new Intent(this, DashBoardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,notificationIntent,0);

        Notification notification = new NotificationCompat.Builder(this,"999")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        notification.notify();



    }

    public void sendTokenToServer(String token){
        Log.d("Token", "sendTokenToServer: "+token);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Admin").child("AdminToken").setValue(token);
 //        Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
//    }
//
//

  public void showNotification(String title,String message){

      Intent notificationIntent = new Intent(getApplicationContext(), DashBoardActivity.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

      NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"Message Notifications")
             .setContentTitle(title)
             .setSmallIcon(R.drawable.ic_launcher_foreground)
             .setAutoCancel(true)
              .setContentIntent(pendingIntent)
             .setContentText(message);


     NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
     managerCompat.notify(999,builder.build());

    }

}
