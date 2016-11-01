package com.rksomayaji.work.orthoscores2.helper;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.rksomayaji.work.orthoscores2.TestQuestion;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sushanth on 10/15/16.
 */

public class TestXMLParserHelper {

    TestQuestion question;
    String[] response;
    int[] values;
    ArrayList<String[]> responses;
    private Context context;
    private static final String ns = null;

    public TestXMLParserHelper(Context applicationContext){
        context = applicationContext;
    }

    public ArrayList<String> getTestList(){
        ArrayList<InputStream> tests = getAssetFiles();

        ArrayList<String> testList = new ArrayList<>();
        try{
            for(InputStream test : tests){
                String testName = getTestName(test);
                testList.add(testName);
            }
        } catch (XmlPullParserException | IOException e){
            e.printStackTrace();
        }

        Log.i("XML Parser", "Available tests " + String.valueOf(testList.size()));
        return testList;
    }

    private String getTestName(InputStream test) throws XmlPullParserException , IOException{
        XmlPullParser parser = Xml.newPullParser();
        String name = "";
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(test,null);
            parser.nextTag();

            while (parser.next() != XmlPullParser.END_TAG){
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String nameTag = parser.getName();
                if (nameTag.equals("name")){
                    parser.require(XmlPullParser.START_TAG,ns,"name");
                    if (parser.next() == XmlPullParser.TEXT) {
                        name = parser.getText();
                        parser.nextTag();
                    }
                    parser.require(XmlPullParser.END_TAG,ns,"name");
                }
                else skip(parser);
            }
        } finally {
            test.close();
        }

        return name;
    }

    private ArrayList<InputStream> getAssetFiles(){
        ArrayList<InputStream> testAssets = new ArrayList<>();
        try {
            String[] fileList = context.getApplicationContext().getAssets().list("test");
            for (String path : fileList){
                Log.i("XML Parser","Test " + path);
                testAssets.add(context.getApplicationContext().getAssets().open("test/" + path));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testAssets;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
