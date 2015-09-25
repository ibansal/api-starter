package com.companyname.projectname.repository;

import com.companyname.projectname.dto.Direction;
import com.companyname.projectname.dto.Op;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public abstract class BaseFilterableAndSortableRepository {

    @PersistenceContext
    EntityManager entityManager;

    protected Query getQuery(String baseQuery, List<Triple<String, Op, Object>> filters, List<Pair<String, Direction>> sortBy, boolean countOnly, Class<?> klazz) {
        Map<String, Queue<String>> queryParamMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(filters)) {
            String delim = " AND ";
            for (int i = 0; i < filters.size(); i++) {
                Triple<String, Op, Object> filter = filters.get(i);
                String queryParamKey = "queryParamKey" + i;

                baseQuery = baseQuery + delim + filter.getLeft() + filter.getMiddle().getSign() + ":" +queryParamKey + " ";
                Queue<String> queryParamsKeyQueue = queryParamMap.get(filter.getLeft());
                if(queryParamsKeyQueue == null) {
                    queryParamsKeyQueue = new LinkedList<>();
                }
                queryParamsKeyQueue.add(queryParamKey);
                queryParamMap.put(filter.getLeft(), queryParamsKeyQueue);
            }
        }

        if (!CollectionUtils.isEmpty(sortBy) && !countOnly) {
            String sortSuffix = "";
            String delim = "";
            for (Pair<String, Direction> sort : sortBy) {
                sortSuffix = sortSuffix + delim + sort.getLeft() + sort.getRight().getKey();
                delim = ",";
            }
            baseQuery = baseQuery + " ORDER BY " + sortSuffix;
        }

        Query nativeQuery = null;
        if (countOnly) {
            nativeQuery = entityManager.createNativeQuery(baseQuery);
        } else {
            nativeQuery = entityManager.createNativeQuery(baseQuery, klazz);
        }
        if (!CollectionUtils.isEmpty(filters)) {
            for (Triple<String, Op, Object> filter : filters) {
                Queue<String> queryParamsKeyQueue = queryParamMap.get(filter.getLeft());
                String peek = queryParamsKeyQueue.poll();
                if(StringUtils.isNotBlank(peek)) {
                    nativeQuery.setParameter(peek, filter.getRight());
                }
            }
        }
        return nativeQuery;
    }
}
