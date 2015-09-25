package com.companyname.projectname.dto;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by vijay.rawat01 on 8/6/15.
 */
public enum Op {
    EQ(" = ") {
        @Override
        public <T extends Comparable> Predicate perform(CriteriaBuilder builder, Root<?> from, String key, T value) {
            return builder.equal(from.get(key), value);
        }
    },
    GT(" > ") {
        @Override
        public <T extends Comparable>  Predicate perform(CriteriaBuilder builder, Root<?> from, String key, T value) {
            return  builder.greaterThan(from.<T>get(key), value);
        }
    },
    GTE(" >= ") {
        @Override
        public <T extends Comparable>  Predicate perform(CriteriaBuilder builder, Root<?> from, String key, T value) {
            return builder.greaterThanOrEqualTo(from.<T>get(key), value);
        }
    },
    LT(" < ") {
        @Override
        public <T extends Comparable>  Predicate perform(CriteriaBuilder builder, Root<?> from, String key, T value) {
            return builder.lessThan(from.<T>get(key), value);
        }
    },
    LTE(" <= ") {
        @Override
        public <T extends Comparable>  Predicate perform(CriteriaBuilder builder, Root<?> from, String key, T value) {
            return builder.lessThanOrEqualTo(from.<T>get(key), value);
        }
    },
    IN(" IN ") {
        @Override
        public <T extends Comparable>  Predicate perform(CriteriaBuilder builder, Root<?> from, String key, T value) {
            Path<Object> objectPath = from.get(key);
            return builder.in(objectPath).value(value);
        }
    },
    LIKE(" LIKE ") {
        @Override
        public  <T extends Comparable> Predicate perform(CriteriaBuilder builder, Root<?> from, String key, T value) {
            return builder.like(from.<String>get(key), (String) value);
        }
    };

    String sign;

    public String getSign() {
        return sign;
    }

    private Op(String sign) {
        this.sign = sign;
    }

    public static Op findOp(String value) {
        for(Op op : Op.values()) {
            if(op.name().equalsIgnoreCase(value)) {
                return op;
            }
        }
        return null;
    }

    abstract public <T extends Comparable> Predicate perform(CriteriaBuilder builder, Root<?> from, String key, T value);
}
