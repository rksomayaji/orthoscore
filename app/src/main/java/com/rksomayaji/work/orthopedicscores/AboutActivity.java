package com.rksomayaji.work.orthopedicscores;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rksomayaji.work.orthopedicscores.helper.HTTPHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class AboutActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    TextView update;
    TextView download;
    TextView updateDetails;
    static String url;
    static String downloadAddress;
    private static final int WRITE_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        PackageInfo pInfo = null;
        url = getString(R.string.url_download);

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(getIntent().getBooleanExtra(OrthoScores.NOTIFICATION,false)) new getUpdate().execute();
        if(getIntent().getBooleanExtra("CHECK_PERMISSION",false)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST_CODE);
            downloadAddress = getIntent().getStringExtra(OrthoScores.TAG_OR_URL);
        }

        assert pInfo != null;
        String versionCode = pInfo.versionName;
        TextView versionCodeView = (TextView) findViewById(R.id.textview_version_code);
        versionCodeView.setText("Version: " + versionCode);

        update = (TextView) findViewById(R.id.textview_update);
        download = (TextView) findViewById(R.id.textview_download);
        updateDetails = (TextView) findViewById(R.id.textview_update_details);

        Button updateButton = (Button) findViewById(R.id.update_button);

        /*TODO
         * - Add download and ignore buttons
         */

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Update","Checking...");
                new getUpdate().execute();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case WRITE_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(checkInternet()) {
                        long t = downloadUpdate(Uri.parse(downloadAddress));
                    }
                }else{
                    Toast.makeText(this,
                            "Kindly give permission for writing on external storage in app settings to download the update",
                            Toast.LENGTH_SHORT)
                            .show();
                }
        }
    }

    private boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean networkIsPresent = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.i("Main", String.valueOf(networkIsPresent));
        return networkIsPresent;
    }

    private long downloadUpdate(Uri address) {
        String destination = OrthoScores.DESTINATION;
        Uri dest = Uri.parse("file://" + destination);

        File file = new File(String.valueOf(dest));
        if(file.exists())file.delete();

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(address);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationUri(dest);

        return manager.enqueue(request);
    }

    private class getUpdate extends AsyncTask<Void,Void,String[]>{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(AboutActivity.this);
        pDialog.setMessage("Checking for new releases...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        HTTPHelper sh = new HTTPHelper();

        String jsonString = sh.makeServiceCall(url);
        String tagName[] = new String[3];

        if(jsonString != null){
            try{
                JSONObject jsonObject = new JSONObject(jsonString);

                Log.i("About",jsonObject.getString("tag_name"));
                tagName[0] = jsonObject.getString("tag_name");
                tagName[2] = jsonObject.getString("body");
                JSONArray assets = jsonObject.getJSONArray("assets");
                JSONObject c = assets.getJSONObject(0);
                tagName[1] = c.getString("browser_download_url");

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return tagName;
    }

    @Override
    protected void onPostExecute(String[] s) {
        super.onPostExecute(s);

        if (pDialog.isShowing()) pDialog.dismiss();

        update.setText("Latest release: " + s[0]);
        download.setText(s[1]);
        updateDetails.setText(s[2]);
    }
}
}
