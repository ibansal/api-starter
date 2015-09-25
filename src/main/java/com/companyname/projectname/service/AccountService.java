package com.companyname.projectname.service;

import com.companyname.projectname.exception.InvalidTransactionDataException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ishan.bansal on 6/24/15.
 */

@Service
public class AccountService {

    @Value("${aerospike.db.name}")
    private String databaseName;

    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

    private static Logger transactionLogger = LoggerFactory.getLogger("transactions");


    @Transactional(rollbackFor=Exception.class)
    public void checkTransactionData(String accountId, String amount, String userId) throws InvalidTransactionDataException {
        if (StringUtils.isBlank(accountId) || StringUtils.isBlank(amount) || StringUtils.isBlank(userId)) {
            throw new InvalidTransactionDataException("Data must not be blank" + " accountid " + accountId + " amountStr " + amount + " userID " + userId);
        }
    }

}
