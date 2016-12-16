package com.rksomayaji.work.orthopedicscores.helper;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.rksomayaji.work.orthopedicscores.AboutActivity;
import com.rksomayaji.work.orthopedicscores.OrthoScores;
import com.rksomayaji.work.orthopedicscores.R;

/**
 * Created by sushanth on 10/21/16.
 */

public class DownloadBroadcastReceiverHelper extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int notID = intent.getIntExtra(OrthoScores.NOTIFICATION_ID,1);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sPref.edit();

        switch (action){
            case OrthoScores.DOWNLOAD_UPDATE:
                Uri address = Uri.parse(intent.getStringExtra(OrthoScores.TAG_OR_URL));
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                long downloadID = manager.enqueue(new DownloadManager.Request(address));

                nm.cancel(notID);
                Log.i("BR", "Downloading " + String.valueOf(downloadID));
                break;
            case OrthoScores.IGNORE_UPDATE:
                String tag = intent.getStringExtra(OrthoScores.TAG_OR_URL);
                edit.putString(OrthoScores.IGNORE_TAG, tag);
                edit.apply();

                nm.cancel(notID);
                Log.i("BR", "Ignoring");
                break;
            case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                Log.i("DwnldMgr","Downloaded");

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,0));
                DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor cursor = dm.query(query);
                NotificationCompat.Builder downloadedNotification = new NotificationCompat.Builder(context);

                if(cursor.moveToFirst()){
                    if(cursor.getCount() > 0){
                        if(cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL){
                            Uri file = dm.getUriForDownloadedFile(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,0));
                            Intent installIntent = new Intent(Intent.ACTION_VIEW)
                                    .setDataAndType(file,"application/vnd.android.package-archive");
                            PendingIntent pi = PendingIntent.getActivity(context,0,installIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                            downloadedNotification.setContentTitle("Update Download Complete")
                                    .setContentText("Click to install update")
                                    .setContentIntent(pi)
                                    .setSmallIcon(R.drawable.ic_info_black_24dp);
                            Log.i("DwnldMgr",file.getPath());
                        }
                    }
                }
                else{
                    Log.i("DwnldMgr","Download failed");

                    Intent installFailure = new Intent(context, AboutActivity.class);
                    installFailure.putExtra(OrthoScores.NOTIFICATION,true);
                    PendingIntent pi = PendingIntent.getActivity(context,0,installFailure,PendingIntent.FLAG_UPDATE_CURRENT);

                    downloadedNotification.setContentTitle("Update Download Failed")
                            .setContentText("Download Failed...")
                            .setContentIntent(pi)
                            .setSmallIcon(R.drawable.ic_info_black_24dp);

                }

                nm.notify(notID,downloadedNotification.build());
                break;
        }
    }
}
