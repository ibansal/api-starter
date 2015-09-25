package com.companyname.projectname.utils;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<Object> {
    Map<Object, Double> map;

    public ValueComparator(Map<Object, Double> base) {
        this.map = base;
    }

    public int compare(Object a, Object b) {
        if (map.get(a) >= map.get(b)) {
            return -1;
        } else {
            return 1;
        }
    }
}


