package com.rksomayaji.work.orthoscores2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.rksomayaji.work.orthoscores2.R;
import com.rksomayaji.work.orthoscores2.TestQuestion;

import java.util.ArrayList;

/**
 * Created by sushanth on 10/13/16.
 */

public class TestArrayAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TestQuestion> questions;
    private int layout;

    public TestArrayAdapter(Context context, int layout, ArrayList<TestQuestion> questions){
        this.context = context;
        this.questions = questions;
        this.layout = layout;
    }
    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int i) {
        return questions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(layout,viewGroup,false);

        TextView question = (TextView) view.findViewById(R.id.test_question);
        Spinner responseSpinner = (Spinner) view.findViewById(R.id.test_spinner);
        SeekBar responseSeekBar = (SeekBar) view.findViewById(R.id.test_seekbar);

        question.setText(questions.get(pos).getQuestion());
        responseSpinner.setAdapter(new ArrayAdapter<String>(context,
                android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item,
                questions.get(pos).getResponse()));

        responseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                questions.get(pos).setSelectedValue(i);
                Log.i("Test",String.valueOf(i) + " / " + String.valueOf(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i("Test","Please select something.");
            }
        });
        return view;
    }
}
