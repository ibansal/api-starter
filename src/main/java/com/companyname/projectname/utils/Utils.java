package com.companyname.projectname.utils;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


public class Utils {


    public static JSONArray convertToJSONArray(List<String> list) {
        JSONArray result = new JSONArray();
        if (list != null) {
            for (String str : list) {
                result.add(str);
            }
        }
        return result;
    }

    public static TreeMap<Object, Double> getSortedMapByValue(Map<Object, Double> map) {
        ValueComparator vc = new ValueComparator(map);
        TreeMap<Object, Double> sortedMap = new TreeMap<Object, Double>(vc);
        sortedMap.putAll(map);
        return sortedMap;
    }

    public static String getStringParam(JSONObject obj, String param) {
        if (obj == null || obj.isEmpty()) {
            return null;
        }
        Object object = obj.get(param);
        if (object instanceof String) {
            return object.toString();
        }
        return null;
    }

    public static String getTrimmedString(String str, int size) {
        if (StringUtils.isBlank(str)) {
            return "";
        }

        return str.substring(0, ((str.length() <= size) ? str.length() : size) - 1);
    }

    public static String removeQuotes(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }

        return str.replace("\"", "");
    }

    public static String normalizeUrl(String in) {
        if (StringUtils.isBlank(in)) {
            return "";
        }
        in = in.replaceAll("[^a-zA-Z0-9_\\-\\.\\/]", "");
        return in;
    }

    public static String formatDateForLogging(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
        return simpleDateFormat.format(date);
    }

    public static long getRewardsExpiryFromNow(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 180);
        return c.getTimeInMillis();
    }

    public static String add(String... input) {
        BigDecimal result = new BigDecimal("0");
        for(String s: input) {
            if(StringUtils.isNotEmpty(s)) {
                result = result.add(new BigDecimal(s));
            }
        }
        return result.toString();
    }


    public static <T> List<T> getSublist(List<T> allList, int pos, int size){
        if(CollectionUtils.isEmpty(allList)){
            return allList;
        }
        if(allList.size() < pos*size){
            return Collections.EMPTY_LIST;
        }
        int max = pos*size + size;
        if(allList.size() < max){
            max = allList.size();
        }
        List<T> subList = allList.subList(pos * size, max);
        return subList;
    }


    public static String getMonth(Date date) {
        return new SimpleDateFormat("MMMMM").format(date);
    }

    public static int getDayInt(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public static int getMonthInt(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        return month;
    }

    public static int getYearInt(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.YEAR);
        return month;
    }

    public static String getTwoDigitMonth(Integer data) {
        if (data < 10) {
            return "0" + data;
        }
        return data.toString();
    }

    public static void main(String[] args) {
        System.out.println(Long.getLong("0"));
    }
}
