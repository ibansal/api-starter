package com.companyname.projectname.test;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountRepositoryIT extends BaseTest {

    public static final String ISHAN = "ishan";
    private static Logger logger = LoggerFactory.getLogger(AccountRepositoryIT.class);



    @Before
    public void setUp() {
        logger.debug("*** TEST START ***");
        super.setUp();
    }

    @After
    public void after() {
        logger.debug("*** TEST COMPLETE ***");
        super.after();
    }

}