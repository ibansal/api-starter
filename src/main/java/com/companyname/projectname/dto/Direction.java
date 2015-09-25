package com.companyname.projectname.dto;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

/**
 * Created by vijay.rawat01 on 8/6/15.
 */
public enum Direction {
    ASC(" ASC ") {
        @Override
        public Order perform(CriteriaBuilder criteriaBuilder, Root<?> from, String key) {
            return criteriaBuilder.asc(from.get(key));
        }
    }, DESC(" DESC ") {
        @Override
        public Order perform(CriteriaBuilder criteriaBuilder, Root<?> from, String key) {
            return criteriaBuilder.desc(from.get(key));
        }
    };

    private String key;

    public String getKey() {
        return key;
    }

    Direction(String key) {
        this.key = key;
    }

    public static Direction findDirection(String value) {
        for (Direction direction : Direction.values()) {
            if (direction.name().equalsIgnoreCase(value)) {
                return direction;
            }
        }
        return null;
    }

    public abstract Order perform(CriteriaBuilder criteriaBuilder, Root<?> from, String key);
}
