package com.rksomayaji.work.orthoscores2.helper;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.rksomayaji.work.orthoscores2.TestQuestion;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sushanth on 10/15/16.
 */

public class TestXMLParserHelper {

    private ArrayList<List<Integer>> values;
    private ArrayList<List<String>> responses;
    private Context context;
    private static final String ns = null;

    public TestXMLParserHelper(Context applicationContext){
        context = applicationContext;
        values = new ArrayList<>();
        responses = new ArrayList<>();
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

    public ArrayList<TestQuestion> getTest(int pos) throws IOException {
        ArrayList<TestQuestion> test = new ArrayList<>();
        ArrayList<InputStream> tests = getAssetFiles();

        XmlPullParser parser = Xml.newPullParser();
        try{
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(tests.get(pos),null);
            parser.nextTag();
            Log.i("XML Parser", parser.getName());

            while (parser.next() != XmlPullParser.END_TAG){
                if(parser.getEventType() != XmlPullParser.START_TAG) continue;

                String tag = parser.getName();
                Log.i("XML Parser", tag);

                switch (tag){
                    case "response":
                        getResponses(parser);
                        break;
                    case "questions":
                        test = getQuestion(parser);
                        break;
                    default:
                        skip(parser);
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return test;
    }

    private ArrayList<TestQuestion> getQuestion(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,ns,"questions");
        ArrayList<TestQuestion> questions = new ArrayList<>();

        while(parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;

            String tag = parser.getName();

            if(tag.equals("text")){
                int response = Integer.parseInt(parser.getAttributeValue(null,"response"));
                Log.i("XML Parser", String.valueOf(response));
                TestQuestion q = new TestQuestion();
                q.setQuestion(readText(parser));
                Log.i("XML Parser", q.getQuestion());
                q.setResponse(responses.get(response));
                q.setValue(values.get(response));
                questions.add(q);
            }
        }
        return questions;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void getResponses(XmlPullParser parser) throws IOException , XmlPullParserException{
        parser.require(XmlPullParser.START_TAG,ns,"response");
        while (parser.next() != XmlPullParser.END_TAG){
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if(name.equals("response-array")){
                responses.add(getArray(parser));
            }
        }
    }

    private List<String> getArray(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<String> array = new ArrayList<>();
        List<Integer> valueArray = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG,ns,"response-array");

        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG) continue;

            String tag = parser.getName();
            if(tag.equals("item")){
                Integer value = Integer.parseInt(parser.getAttributeValue(null,"value"));
                Log.i("XML Parser", String.valueOf(value));
                valueArray.add(value);
                if (parser.next() == XmlPullParser.TEXT) {
                    array.add(parser.getText());
                    Log.i("XML Parser",parser.getText());
                    parser.nextTag();
                }
            }
        }
        values.add(valueArray);

        return array;
    }

    public ArrayList<String> getTotal(int pos) throws XmlPullParserException, IOException {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<InputStream> tests = getAssetFiles();

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(tests.get(pos),null);
        parser.nextTag();

        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG) continue;

            String tag = parser.getName();

            if(tag.equals("total")){
                parser.require(XmlPullParser.START_TAG,ns,"total");
                String total = parser.getAttributeValue(null,"value");
                Log.i("XML Parser total", total);
                result.add(total);
                parser.nextTag();

                String name = parser.getName();
                if(name.equals("details")){
                    parser.require(XmlPullParser.START_TAG,ns,"details");
                    result.add(readText(parser));
                    Log.i("XML Parser total",result.get(1));
                    parser.require(XmlPullParser.END_TAG,ns,"details");
                }

                parser.nextTag();
                parser.require(XmlPullParser.END_TAG,ns,"total");
            }else skip(parser);
        }

        return result;
    }

    private ArrayList<InputStream> getAssetFiles(){
        ArrayList<InputStream> testAssets = new ArrayList<>();
        try {
            String[] fileList = context.getApplicationContext().getAssets().list("test");
            for (String path : fileList){
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
