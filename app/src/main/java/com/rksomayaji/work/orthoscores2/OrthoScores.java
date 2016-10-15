package com.rksomayaji.work.orthoscores2;

import android.app.Application;

/**
 * Created by sushanth on 9/28/16.
 */
public class OrthoScores extends Application {
    public static final String RESULT = "RESULT";
    public static final String TEST = "TEST";
    public static final String TEST_NUMBER = "TEST_NUMBER";

    public static final int[] TEST_ARRAY = {R.array.test_questions,R.array.test_response,R.array.test_value};
    public static final int[] WOMAC_ARRAY = {};
    public static final int[] OHS_ARRAY = {};

    public static final int[][] TESTS = {WOMAC_ARRAY, OHS_ARRAY, TEST_ARRAY};
}
