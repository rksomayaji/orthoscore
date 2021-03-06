package com.rksomayaji.work.orthopedicscores;

import android.app.Application;
import android.os.Environment;

/**
 * Created by sushanth on 9/28/16.
 *
 * OrthoScores.java
 * -----------------
 * Describes the various package specific constants.
 *
 */
public class OrthoScores extends Application {
    public static final String RESULT = "RESULT";
    public static final String TEST_NUMBER = "TEST_NUMBER";

    public static final String NOTIFICATION = "NOTIFY";
    public static final String DOWNLOAD_UPDATE = "com.rksomayaji.work.orthopedicscores.DOWNLOAD_UPDATE";
    public static final String IGNORE_UPDATE = "com.rksomayaji.work.orthopedicscores.IGNORE_UPDATE";
    public static final String TAG_OR_URL = "TAG";
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String IGNORE_TAG = "ignore_tag";

    public static final String DESTINATION_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
    public static final String DESTINATION_FILENAME = "OrthopedicScores.apk";

    public static final String DESTINATION = DESTINATION_DIRECTORY + DESTINATION_FILENAME;

}
