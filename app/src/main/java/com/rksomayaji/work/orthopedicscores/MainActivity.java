package com.rksomayaji.work.orthopedicscores;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rksomayaji.work.orthopedicscores.helper.HTTPHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rksomayaji.work.orthopedicscores.helper.TestXMLParserHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mainMenu;
    ArrayAdapter<String> menuAdapter;
    ListView menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent appShortcutIntent = getIntent();
        int launch = appShortcutIntent.getIntExtra("APP_SHORTCUT",0);
        if (launch == 1){
            Log.i("MAIN", String.valueOf(launch));
            launchTest(appShortcutIntent.getIntExtra(OrthoScores.TEST_NUMBER,0));
        }else {
            Log.i("MAIN", String.valueOf(launch));
            if(findViewById(R.id.fragment_container) != null){
                if (savedInstanceState != null) return;

                MainFragment mainFragment = new MainFragment();

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fragment_container,mainFragment);
                ft.commit();
            }
        }

        mainMenu = (DrawerLayout) findViewById(R.id.drawer);

        ArrayList<String> testArray = getAvailableTests();
        menuAdapter = new ArrayAdapter<>(this, R.layout.list_layout,testArray);
        menuList = (ListView) findViewById(R.id.test_list);
        menuList.setAdapter(menuAdapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                launchTest(i);
                mainMenu.closeDrawer(GravityCompat.START);
            }
        });
        checkForUpdate();
    }

    private void checkForUpdate() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        if(settings.getBoolean("auto_update",true)) new getUpdate().execute();
    }

    private ArrayList<String> getAvailableTests() {
        TestXMLParserHelper helper = new TestXMLParserHelper(this);

        return helper.getTestList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_about:
                Intent aboutIntent = new Intent(this,AboutActivity.class);
                aboutIntent.putExtra(OrthoScores.NOTIFICATION,false);
                startActivity(aboutIntent);
                return true;
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(this,SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchTest (int i){
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putInt(OrthoScores.TEST_NUMBER,i);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .commit();
    }

    private class getUpdate extends AsyncTask<Void,Void,String[]> {

        String url = getString(R.string.url_download);
        int notificationID = 1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("OrthoScores2","Checking for update");
        }

        @Override
        protected String[] doInBackground(Void... voids) {
            HTTPHelper sh = new HTTPHelper();

            String jsonString = sh.makeServiceCall(url);
            String[] tagName = new String[2];

            if(jsonString != null){
                try{
                    JSONObject jsonObject = new JSONObject(jsonString);

                    Log.i("Main",jsonObject.getString("tag_name"));
                    tagName[0] = jsonObject.getString("tag_name");
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
            try{
                String versionInstalled = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String ignoreTag = sp.getString(OrthoScores.IGNORE_TAG,null);
                Log.i("Main", "Ignoring " + ignoreTag);

                if(!s[0].equals(versionInstalled) && !s[0].equals(ignoreTag)) {
                    Intent updateIntent = new Intent(getApplicationContext(),AboutActivity.class);
                    updateIntent.putExtra(OrthoScores.NOTIFICATION,true);

                    PendingIntent updatePI =  PendingIntent.getActivity(getApplicationContext(),
                            0,
                            updateIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent downloadIntent = new Intent();
                    downloadIntent.setAction(OrthoScores.DOWNLOAD_UPDATE);
                    downloadIntent.putExtra(OrthoScores.TAG_OR_URL,s[1]);
                    downloadIntent.putExtra(OrthoScores.NOTIFICATION_ID,notificationID);

                    PendingIntent downloadPI = PendingIntent.getBroadcast(getApplicationContext(),
                            1,
                            downloadIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent ignoreIntent = new Intent();
                    ignoreIntent.setAction(OrthoScores.IGNORE_UPDATE);
                    ignoreIntent.putExtra(OrthoScores.TAG_OR_URL,s[0]);
                    ignoreIntent.putExtra(OrthoScores.NOTIFICATION_ID,notificationID);

                    PendingIntent ignorePI = PendingIntent.getBroadcast(getApplicationContext(),
                            2,
                            ignoreIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder updateNotification = new NotificationCompat.Builder(getApplicationContext());
                    updateNotification.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                            .setContentTitle("Update Available")
                            .setContentText("Version: " + s[0])
                            .setContentIntent(updatePI)
                            .addAction(0,"DOWNLOAD",downloadPI)
                            .addAction(0,"IGNORE",ignorePI)
                            .setAutoCancel(true);

                    NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    notifManager.notify(notificationID,updateNotification.build());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
