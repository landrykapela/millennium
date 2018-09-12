package tz.co.neelansoft.millennium.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class JSONParser {

    public JSONParser(){

    }

    public String getJSONFromURL(URL url) throws IOException{

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream is = urlConnection.getInputStream();

            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");
            String response = null;
            boolean hasNext = scanner.hasNext();
            if(hasNext){
                response = scanner.next();
            }
            scanner.close();
            return response;
        }
        finally {
            urlConnection.disconnect();
        }
    }

    public List<Course> getCourseListFromJSON(String json,String array_key){
        List<Course> courses = new ArrayList<>();
        String[] keys = new String[]{"id,name,price,subjects"};
        try{
            JSONObject jo = new JSONObject(json);
            JSONArray json_courses = jo.getJSONArray(array_key);
            if(json_courses == null){
                return null;
            }
            else{
                for(int i=0; i < json_courses.length();i++){
                    JSONObject json_course = json_courses.getJSONObject(i);
                    Course course = getCourseFromJSON(json_course, keys);
                    courses.add(course);
                }
            }
        }
        catch (JSONException je){
            je.printStackTrace();
            return null;
        }
        return courses;
    }

    public Course getCourseFromJSON(JSONObject jo, String[] keys){
        Course course = new Course();
        try{
            int id = Integer.parseInt(jo.getString(keys[0]));
            String name = jo.getString(keys[1]);
            String price = jo.getString(keys[2]);
            JSONArray subjects = jo.getJSONArray(keys[3]);
            List<Subject> subjectList = new ArrayList<>();
            if(subjects == null){
                subjectList = null;
            }
            else{
                for(int k=0; k<subjectList.size();k++){
                    JSONObject js = subjects.getJSONObject(k);
                    int cid = Integer.parseInt(js.getString("cid"));
                    String fullname = js.getString("fullname");
                    Subject s = new Subject(cid,fullname);
                    subjectList.add(s);
                }
            }

            course.setId(id);
            course.setName(name);
            course.setPrice(price);
            course.setSubjectList(subjectList);

        }
        catch (JSONException e){

        }
        return course;
    }


}
