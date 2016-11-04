package com.rksomayaji.work.orthoscores2;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rksomayaji.work.orthoscores2.helper.TestXMLParserHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mainMenu;
    ArrayAdapter<String> menuAdapter;
    ListView menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainMenu = (DrawerLayout) findViewById(R.id.drawer);

        ArrayList<String> testArray = getAvailableTests();
        menuAdapter = new ArrayAdapter<>(this, R.layout.list_layout,testArray);
        menuList = (ListView) findViewById(R.id.test_list);
        menuList.setAdapter(menuAdapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TestFragment fragment = new TestFragment();
                Bundle args = new Bundle();
                args.putInt(OrthoScores.TEST_NUMBER,i);
                fragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .commit();
                mainMenu.closeDrawer(GravityCompat.START);
            }
        });
        if(findViewById(R.id.fragment_container) != null){
            if (savedInstanceState != null) return;

            MainFragment mainFragment = new MainFragment();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container,mainFragment);
            ft.commit();
        }
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
                startActivity(aboutIntent);
                return true;
            case R.id.menu_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
