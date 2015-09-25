package com.companyname.projectname.repository;

import com.companyname.projectname.constant.DatabaseConstants;
import com.companyname.projectname.domain.Account;
import com.companyname.projectname.dto.AccountInfoDto;
import com.companyname.projectname.dto.Direction;
import com.companyname.projectname.dto.Op;
import com.companyname.projectname.utils.Utils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountRepositoryImpl extends BaseFilterableAndSortableRepository implements AccountRepositoryCustom {
    private static Logger logger = LoggerFactory.getLogger(AccountRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(rollbackFor=Exception.class)
    @Override
    public List<AccountInfoDto> findAllAccountInfo(List<Triple<String, Op, Object>> filters, int pageNumber, int pageSize, List<Pair<String, Direction>> sortBy) {
        Map map = getAccountInfoAndDebitEntries(filters, pageNumber, pageSize, sortBy);
        Map<AccountInfoDto, BigDecimal> loanAmountDeductedMap = new HashMap<>();

        List<AccountInfoDto> accountInfoDtos = Lists.newArrayList(map.keySet());
        for (AccountInfoDto accountInfoDto : accountInfoDtos) {
            accountInfoDto.setAvailableBalance(
                    Utils.add(accountInfoDto.getReward(),
                            accountInfoDto.getTds(),
                            accountInfoDto.getCash(),
                            accountInfoDto.getLoan(),
                            loanAmountDeductedMap.get(accountInfoDto).toString(),
                            "-" + accountInfoDto.getOutstanding()));
        }
        return accountInfoDtos;

    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int findAllAccountInfoCount(Map<String, String> filters, String sortBy) {
        Query accountNativeQuery = getNativeQueryForAccount(filters, sortBy, true);
        BigInteger count = (BigInteger) accountNativeQuery.getSingleResult();
        return count.intValue();
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public int findAllAccountInfoCount(List<Triple<String, Op, Object>> filtersList) {
        String baseQuery = "SELECT count(id) FROM account where 1=1 ";
        Query accountNativeQuery = getQuery(baseQuery, filtersList, Collections.EMPTY_LIST, true, Account.class);
        BigInteger count = (BigInteger) accountNativeQuery.getSingleResult();
        return count.intValue();
    }

    @Transactional(rollbackFor=Exception.class)
    private Map getAccountInfoAndDebitEntries(List<Triple<String, Op, Object>> filters, int pageNumber, int pageSize, List<Pair<String, Direction>> sortBy) {
        String baseQuery = "SELECT * FROM account where 1=1 ";
        Query accountNativeQuery = getQuery(baseQuery, filters, sortBy, false, Account.class);
        accountNativeQuery.setFirstResult(pageNumber * pageSize);
        accountNativeQuery.setMaxResults(pageSize);
        List<Account> accounts = accountNativeQuery.getResultList();
        List<Long> accountIds = getAccountIds(accounts);
        if (CollectionUtils.isEmpty(accountIds)) {
            return Collections.EMPTY_MAP;
        }

        String debitQuery = "select " + DatabaseConstants.ACCOUNT_TABLE_PREFIX + ".id, "
                + DatabaseConstants.ACCOUNT_TABLE_PREFIX + ".name, "
                + DatabaseConstants.ACCOUNT_TABLE_PREFIX + ".outstanding, "
                + DatabaseConstants.ACCOUNT_TABLE_PREFIX + ".accountType, "
                + " from "
                + " account " + DatabaseConstants.ACCOUNT_TABLE_PREFIX
                + DatabaseConstants.ACCOUNT_TABLE_PREFIX + ".id = "
                + " WHERE "
                + DatabaseConstants.ACCOUNT_TABLE_PREFIX + ".id IN :inclList";

        String orderBySuffix = "";
        String delimiter = "";
        for(Pair<String, Direction> pair : sortBy) {
            String tablePrefixedValue = getTablePrefixedValue(debitQuery, pair.getLeft());
            if(StringUtils.isNotBlank(tablePrefixedValue)) {
                orderBySuffix = orderBySuffix + delimiter + tablePrefixedValue + pair.getRight().getKey();
                delimiter = ", ";
            }
        }
        if (StringUtils.isNotBlank(orderBySuffix)) {
            debitQuery = debitQuery + " ORDER BY " + orderBySuffix;
        }
        logger.debug("About to run debitquery query  : " + debitQuery);
        Query nativeQuery = entityManager.createNativeQuery(debitQuery);
        nativeQuery.setParameter("inclList", accountIds);
        //Convert to map
        List<Object[]> resultList = nativeQuery.getResultList();
        if (resultList != null) {
            for (Object[] row : resultList) {
                AccountInfoDto accountInfoDto = new AccountInfoDto();
                accountInfoDto.setId(String.valueOf(row[0]));
                accountInfoDto.setName(String.valueOf(row[1]));
                accountInfoDto.setOutstanding(String.valueOf(row[2]));
                String debitTypeString = String.valueOf(row[4]);
            }
        }
        return Collections.EMPTY_MAP;
    }

    private String getTablePrefixedValue(String debitQuery, String sorter) {
        Pattern p = Pattern.compile(".*[\\s]+([^\\s]+\\.)"+sorter+"[\\s|,]+.*");
        Matcher matcher = p.matcher(debitQuery);
        boolean found = matcher.find();
        if(found) {
            return matcher.group(1) + sorter;
        }
        return null;
    }

    private List<Long> getAccountIds(List<Account> accounts) {
        List<Long> result = new ArrayList<>();
        for(Account account:accounts) {
            result.add(account.getId());
        }
        return result;
    }

    private Query getNativeQueryForAccount(Map<String, String> filters, String sortBy, boolean countOnly) {
        String accountQuery = "";
        if(countOnly) {
            accountQuery = "select count(" + DatabaseConstants.ACCOUNT_TABLE_PREFIX + ".id) "
                    + " FROM "
                    + " account " + DatabaseConstants.ACCOUNT_TABLE_PREFIX;
        } else {
            accountQuery = "select " + DatabaseConstants.ACCOUNT_TABLE_PREFIX + ".id "
                    + " FROM "
                    + " account " + DatabaseConstants.ACCOUNT_TABLE_PREFIX;
        }

        Map<String, String> queryParamMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(filters)) {
            accountQuery = accountQuery + " WHERE ";
            String delim = "";
            Integer i = 1;
            for (String key : filters.keySet()) {
                String queryParam = "query" + i++;
                accountQuery = accountQuery + delim + key + ":" + queryParam + " ";
                delim = " AND ";
                queryParamMap.put(key, queryParam);
            }
        }

        if (StringUtils.isNotBlank(sortBy)) {
            accountQuery = accountQuery + " ORDER BY " + sortBy;
        }
        logger.debug("About to run account query  : " + accountQuery);
        Query accountNativeQuery = entityManager.createNativeQuery(accountQuery);
        if (!CollectionUtils.isEmpty(filters)) {
            for (String filterKey : filters.keySet()) {
                accountNativeQuery.setParameter(queryParamMap.get(filterKey), Lists.newArrayList(filters.get(filterKey).split("\\|")));
            }
        }
        return accountNativeQuery;
    }

}
