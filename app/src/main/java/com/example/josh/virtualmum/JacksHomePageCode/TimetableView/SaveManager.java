package com.example.josh.virtualmum.JacksHomePageCode.TimetableView;


import android.content.Context;

//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import Database.Timetable.Timetable;
import Database.VMDbHelper;

public class SaveManager {

    public static String saveSticker(HashMap<Integer, Sticker> stickers){
//        JsonObject obj1 = new JsonObject();
//        JsonArray arr1 = new JsonArray();
//        int[] orders = getSortedKeySet(stickers);
//        for(int i = 0 ; i < orders.length; i++){
//            JsonObject obj2 = new JsonObject();
//            int idx = orders[i];
//            obj2.addProperty("idx",orders[i]);
//            JsonArray arr2 = new JsonArray();//5
//            ArrayList<Schedule> schedules = stickers.get(idx).getSchedules();
//            for(Schedule schedule : schedules){
//                JsonObject obj3 = new JsonObject();
//                obj3.addProperty("classTitle",schedule.title);
//                obj3.addProperty("classPlace",schedule.location);
//                JsonObject obj4 = new JsonObject();//startTime
//                obj4.addProperty("hour",schedule.getStartTime().getHour());
//                obj4.addProperty("minute",schedule.getStartTime().getMinute());
//                obj3.add("startTime",obj4);
//                JsonObject obj5 = new JsonObject();//endtTime
//                obj5.addProperty("hour",schedule.getEndTime().getHour());
//                obj5.addProperty("minute",schedule.getEndTime().getMinute());
//                obj3.add("endTime",obj5);
//                arr2.add(obj3);
//            }
//            obj2.add("schedule",arr2);
//            arr1.add(obj2);
//        }
//        obj1.add("sticker",arr1);
//        return obj1.toString();
        return "t";
    }

    public static HashMap<Integer,Sticker> loadSticker(Context context){
        HashMap<Integer, Sticker> stickers = new HashMap<Integer,Sticker>();
//        JsonParser parser = new JsonParser();
//        JsonObject obj1 = (JsonObject)parser.parse(json);
//        JsonArray arr1 = obj1.getAsJsonArray("sticker");
//        for(int i = 0 ; i < arr1.size(); i++){
//            Sticker sticker = new Sticker();
//            JsonObject obj2 = (JsonObject)arr1.get(i);
//            int idx = obj2.get("idx").getAsInt();
//            JsonArray arr2 = (JsonArray)obj2.get("schedule");
//            for(int k = 0 ; k < arr2.size(); k++){
//                Schedule schedule = new Schedule();
//                JsonObject obj3 = (JsonObject)arr2.get(k);
//                schedule.setTitle(obj3.get("title").getAsString());
//                schedule.setLocation(obj3.get("location").getAsString());
//                Time startTime = new Time();
//                JsonObject obj4 = (JsonObject)obj3.get("startTime");
//                startTime.setHour(obj4.get("hour").getAsInt());
//                startTime.setMinute(obj4.get("minute").getAsInt());
//                Time endTime = new Time();
//                JsonObject obj5 = (JsonObject)obj3.get("endTime");
//                endTime.setHour(obj5.get("hour").getAsInt());
//                endTime.setMinute(obj5.get("minute").getAsInt());
//                schedule.setStartTime(startTime);
//                schedule.setEndTime(endTime);
//                sticker.addSchedule(schedule);
//            }

        VMDbHelper db = new VMDbHelper(context);

        List<Timetable> AllTimetable = db.getTimetable();
       // db.insertTask("Test 1", "310320191200", "100420191200", 4, 2, 40, 0);
        int hours;
        int minutes;
        int idx = 0;
        Sticker sticker = new Sticker();

        for (Timetable t : AllTimetable) {
            Schedule schedule = new Schedule();
//            schedule.setTitle(db.getTask(t.getTaskID()).getName());

            hours = Integer.parseInt(t.getDate().substring(8,10));
            minutes = Integer.parseInt(t.getDate().substring(10,12));
            Time time = new Time();
            time.setHour(hours);
            time.setMinute(minutes);
            schedule.setStartTime(time);

            hours += (int) t.getDuration();
            time.setHour(hours);
            schedule.setEndTime(time);

            idx = t.getId();

            String dtStart = t.getDate();
            SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmm");
            try {
                Date date = format.parse(dtStart);
                schedule.setDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            sticker.addSchedule(schedule);

        }

        db.closeDB();
        stickers.put(idx,sticker);



        return stickers;
    }


    static private int[] getSortedKeySet(HashMap<Integer, Sticker> stickers){
        int[] orders = new int[stickers.size()];
        int i = 0;
        for(int key : stickers.keySet()){
            orders[i++] = key;
        }
        Arrays.sort(orders);
        return orders;
    }
}
