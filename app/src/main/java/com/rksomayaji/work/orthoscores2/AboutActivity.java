package com.rksomayaji.work.orthoscores2;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    TextView update;
    TextView download;
    TextView updateDetails;
    static String url;
    String versionCode;

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

        versionCode = pInfo.versionName;
        TextView versionCodeView = (TextView) findViewById(R.id.textview_version_code);
        versionCodeView.setText("Version: " + versionCode);

        update = (TextView) findViewById(R.id.textview_update);
        download = (TextView) findViewById(R.id.textview_download);
        updateDetails = (TextView) findViewById(R.id.textview_update_details);

        Button updateButton = (Button) findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Update","Checking...");
                new getUpdate().execute();
            }
        });
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

        if(!s[0].equals(versionCode)){
            update.setText("Latest release: " + s[0]);
            download.setText(s[1]);
            updateDetails.setText(s[2]);
        }else update.setText("No updates. You are on latest version.");
    }
}
}
