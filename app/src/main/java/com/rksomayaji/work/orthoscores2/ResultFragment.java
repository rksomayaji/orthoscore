package com.rksomayaji.work.orthoscores2;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by sushanth on 9/15/16.
 */
public class ResultFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        final int score = args.getInt(OrthoScores.RESULT);
        String test = args.getString(OrthoScores.TEST);
        String total = "0";
        View v = inflater.inflate(R.layout.result_layout, container, false);
        TextView textView = (TextView) v.findViewById(R.id.result_text);
        TextView detailedResult = (TextView) v.findViewById(R.id.detailed_result);
        ImageButton copyResult = (ImageButton) v.findViewById(R.id.copy_button);

        switch (test){
            case "WOMAC":
                total = getString(R.string.womac_total);
                detailedResult.setText(getText(R.string.womac_details));
                Log.i("Result","WOMAC");
                break;
            case "Oxford Hip Score":
                total = getString(R.string.ohs_total);
                detailedResult.setText(getText(R.string.ohs_details));
                Log.i("Result","OHS");
                break;
        }
        textView.setText(String.valueOf(score) + "/" + total);

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("result",String.valueOf(score));

                clipboard.setPrimaryClip(clip);
                return true;
            }
        });

        copyResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("result",String.valueOf(score));

                clipboard.setPrimaryClip(clip);
            }
        });
        return v;
    }
}
