package ca.bcit.avoidit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.stream.StreamSupport;

import ca.bcit.avoidit.model.MyPoint;

import static ca.bcit.avoidit.MainActivity.coords;
import static ca.bcit.avoidit.MainActivity.coords2;
import static ca.bcit.avoidit.RouteDetailActivity.list;


public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(list);
        if(ViewRoutesMapActivity.checkIntersects(coords,list.get(0),list.get(1)) || ViewRoutesMapActivity.checkIntersects(coords2,list.get(0),list.get(1))){
            NotificationHelper notificationHelper = new NotificationHelper(context);
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
            notificationHelper.getManager().notify(1, nb.build());
        }
    }
}