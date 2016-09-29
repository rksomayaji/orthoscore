package com.rksomayaji.work.orthoscores;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        ListView testList = (ListView) v.findViewById(R.id.main_list);
        String[] tests = getContext().getResources().getStringArray(R.array.tests);
        ArrayAdapter<String> testArrayAdapter = new ArrayAdapter<>(getContext(),R.layout.list_layout,tests);
        testList.setAdapter(testArrayAdapter);

        testList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        WomacFragment womacFragment = new WomacFragment();

                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,womacFragment)
                                .commit();
                        break;
                    case 1:
                        OHSFragment ohsFragment = new OHSFragment();
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,ohsFragment)
                                .commit();
                        break;
                }
            }
        });
        return v;
    }
}
