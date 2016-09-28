package com.rksomayaji.work.orthoscores;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by sushanth on 9/18/16.
 */
public class OHSFragment extends Fragment {
    int[] spinnerResource = {R.id.ohs_pain_spinner,
            R.id.ohs_diff_wash_spinner,
            R.id.ohs_diff_car_spinner,
            R.id.ohs_diff_socks_spinner,
            R.id.ohs_diff_shop_spinner,
            R.id.ohs_length_walk_spinner,
            R.id.ohs_diff_stairs_spinner,
            R.id.ohs_diff_getup_spinner,
            R.id.ohs_limp_walk_spinner,
            R.id.ohs_sev_pain_spinner,
            R.id.ohs_pain_int_spinner,
            R.id.ohs_pain_night_spinner};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.ohs_fragment,container,false);

        final ArrayList<Spinner> spinners = new ArrayList<>();

        for(int spin : spinnerResource){
            Spinner spinner = (Spinner) v.findViewById(spin);
            spinners.add(spinner);
        }
        Button submitButton = (Button) v.findViewById(R.id.btnSubmit_OHS);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int score = 0;
                for(Spinner spin : spinners){
                    score += spin.getSelectedItemPosition();
                }

                Log.i("OHS",String.valueOf(score));
                ResultFragment result = new ResultFragment();
                Bundle args = new Bundle();

                args.putInt(OrthoScores.RESULT, score);
                args.putString(OrthoScores.TEST, "Oxford Hip Score");
                result.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,result).commit();
            }
        });
        return v;
    }
}
