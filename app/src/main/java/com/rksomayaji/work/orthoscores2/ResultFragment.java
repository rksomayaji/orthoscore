package com.rksomayaji.work.orthoscores2;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rksomayaji.work.orthoscores2.helper.TestXMLParserHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sushanth on 9/15/16.
 *
 * ResultFragment.java
 * --------------------
 * Displays result calculated in the respective test in the TestFragment.java. Details of the test
 * result is displayed after retrieving from the respective test xml file in the asset/tests folder.
 *
 */

public class ResultFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        final int score = args.getInt(OrthoScores.RESULT);
        int test = args.getInt(OrthoScores.TEST_NUMBER);
        View v = inflater.inflate(R.layout.result_layout, container, false);
        TextView textView = (TextView) v.findViewById(R.id.result_text);
        TextView detailedResult = (TextView) v.findViewById(R.id.detailed_result);
        ImageButton copyResult = (ImageButton) v.findViewById(R.id.copy_button);

        TestXMLParserHelper helper = new TestXMLParserHelper(getContext());
        ArrayList<String> totals = new ArrayList<>();
        try {
            totals = helper.getTotal(test);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        String total = totals.get(0);
        textView.setText(String.valueOf(score) + "/" + total);
        detailedResult.setText(totals.get(1));

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
