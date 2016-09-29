package com.rksomayaji.work.orthoscores2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by sushanth on 9/13/16.
 */
public class WomacFragment extends android.support.v4.app.Fragment {

    Spinner painWalking;
    Spinner painStairs;
    Spinner painNocturnal;
    Spinner painRest;
    Spinner painStanding;
    Spinner stiffnessMorning;
    Spinner stiffnessDayTime;
    Spinner difficultyStairsUp;
    Spinner difficultyStairsDown;
    Spinner difficultySittingChair;
    Spinner difficultyRisingChair;
    Spinner difficultyStanding;
    Spinner difficultyBending;
    Spinner difficultyWalking;
    Spinner difficultyLyingOnBed;
    Spinner difficultyRisingOnBed;
    Spinner difficultyPuttingOnSocks;
    Spinner difficultyTakingOffSocks;
    Spinner difficultyBath;
    Spinner difficultyCar;
    Spinner difficultyToilet;
    Spinner difficultyShopping;
    Spinner difficultyHeavyDomestic;
    Spinner difficultyLightDomestic;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.womac_fragment, container, false);
        Button submitButton = (Button) v.findViewById(R.id.btnSubmit);

        painWalking = (Spinner) v.findViewById(R.id.walking_spinner);
        painStairs = (Spinner) v.findViewById(R.id.stair_spinner);
        painNocturnal = (Spinner) v.findViewById(R.id.nocturnal_spinner);
        painRest = (Spinner) v.findViewById(R.id.rest_spinner);
        painStanding = (Spinner) v.findViewById(R.id.standing_spinner);
        stiffnessMorning = (Spinner) v.findViewById(R.id.mornstiff_spinner);
        stiffnessDayTime = (Spinner) v.findViewById(R.id.daystiff_spinner);
        difficultyStairsUp = (Spinner) v.findViewById(R.id.diffstairsup_spinner);
        difficultyStairsDown = (Spinner) v.findViewById(R.id.diffstairsdown_spinner);
        difficultySittingChair = (Spinner) v.findViewById(R.id.diffsitting_spinner);
        difficultyRisingChair = (Spinner) v.findViewById(R.id.diffrisechair_spinner);
        difficultyStanding = (Spinner) v.findViewById(R.id.diffstand_spinner);
        difficultyBending = (Spinner) v.findViewById(R.id.diffbending_spinner);
        difficultyWalking = (Spinner) v.findViewById(R.id.diffwalkingflat_spinner);
        difficultyLyingOnBed = (Spinner) v.findViewById(R.id.difflying_spinner);
        difficultyRisingOnBed = (Spinner) v.findViewById(R.id.diffbedrising_spinner);
        difficultyPuttingOnSocks = (Spinner) v.findViewById(R.id.diffsockson_spinner);
        difficultyTakingOffSocks = (Spinner) v.findViewById(R.id.diffsocksoff_spinner);
        difficultyBath = (Spinner) v.findViewById(R.id.diffbath_spinner);
        difficultyCar = (Spinner) v.findViewById(R.id.diffcar_spinner);
        difficultyToilet = (Spinner) v.findViewById(R.id.difftoilet_spinner);
        difficultyShopping = (Spinner) v.findViewById(R.id.diffshopping_spinner);
        difficultyHeavyDomestic = (Spinner) v.findViewById(R.id.diffdomheavy_spinner);
        difficultyLightDomestic = (Spinner) v.findViewById(R.id.diffdomlight_spinner);

        final Spinner[] scaleSpinners= {painNocturnal,painRest,painStairs,painStanding,painWalking,
                stiffnessMorning,stiffnessDayTime,
                difficultyBath,difficultyBending,difficultyCar,difficultyShopping,
                difficultyToilet,difficultyWalking,difficultyStanding,
                difficultyHeavyDomestic,difficultyLightDomestic,
                difficultyLyingOnBed,difficultyRisingOnBed,
                difficultyPuttingOnSocks,difficultyTakingOffSocks,
                difficultyRisingChair,difficultySittingChair,
                difficultyStairsDown,difficultyStairsUp,
        };


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResultFragment result = new ResultFragment();
                Bundle args = new Bundle();

                args.putInt(OrthoScores.RESULT, calculateScore(scaleSpinners));
                args.putString(OrthoScores.TEST, "WOMAC");
                result.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,result).commit();
            }
        });
        return v;
    }

    public int calculateScore(Spinner[] spinnerArray){
        int score = 0;
        for (Spinner spin : spinnerArray){
            score += spin.getSelectedItemPosition();
        }
        return score;
    }

}
