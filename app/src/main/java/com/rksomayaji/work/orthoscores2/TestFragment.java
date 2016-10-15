package com.rksomayaji.work.orthoscores2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rksomayaji.work.orthoscores2.adapter.TestArrayAdapter;

import java.util.ArrayList;

/**
 * Created by sushanth on 10/12/16.
 */

public class TestFragment extends Fragment {

    public TestFragment () {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_fragment,container,false);

        TextView testName = (TextView) v.findViewById(R.id.test_name);
        ListView testsList = (ListView) v.findViewById(R.id.question_list);
        Button submitButton = (Button) v.findViewById(R.id.button_submit);

        Bundle args = getArguments();
        final int i = args.getInt(OrthoScores.TEST_NUMBER);
        final String[] tests = getContext().getResources().getStringArray(R.array.tests);

        testName.setAllCaps(true);
        testName.setText(tests[i]);

        final ArrayList<TestQuestion> questions = getTestQuestions(OrthoScores.TESTS[i]);
        TestArrayAdapter testAdapter = new TestArrayAdapter(getContext(),R.layout.test_layout, questions);
        testsList.setAdapter(testAdapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int scores = getTestScore(questions);

                Bundle args = new Bundle();
                args.putString(OrthoScores.TEST,tests[i]);
                args.putInt(OrthoScores.RESULT,scores);

                ResultFragment result = new ResultFragment();
                result.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,result)
                        .commit();
            }
        });

        return v;
    }

    private int getTestScore(ArrayList<TestQuestion> questions) {
        int s = 0;

        for (TestQuestion q : questions) {
            s += q.getSelectedValue();
            Log.i("TEST", String.valueOf(s));
        }
        return s;
    }

    private ArrayList<TestQuestion> getTestQuestions(int[] test) {
        ArrayList<TestQuestion> qs = new ArrayList<>();

        String[] questions = getResources().getStringArray(test[0]);
        for (String q : questions){
            qs.add(new TestQuestion(q,
                    getResources().getStringArray(test[1]),
                    getResources().getIntArray(test[2])));
        }

        return qs;
    }
}
