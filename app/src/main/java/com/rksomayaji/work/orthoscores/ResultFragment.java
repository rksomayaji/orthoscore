package com.rksomayaji.work.orthoscores;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sushanth on 9/15/16.
 */
public class ResultFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle args = getArguments();
        int score = args.getInt(OrthoScores.RESULT);
        String test = args.getString(OrthoScores.TEST);
        String total = "0";
        View v = inflater.inflate(R.layout.result_layout, container, false);
        TextView textView = (TextView) v.findViewById(R.id.result_text);

        switch (test){
            case "WOMAC":
                total = getString(R.string.womac_total);
                break;
            case "Oxford Hip Score":
                total = getString(R.string.ohs_total);
                break;
        }
        textView.setText(String.valueOf(score) + "/" + total);
        return v;
    }
}
