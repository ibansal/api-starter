package com.companyname.projectname.test;

import com.companyname.projectname.test.config.WebMvcConfig;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by vijay.rawat01 on 7/6/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@WebAppConfiguration
public class BaseTest {
    public static final String SELLER_SUFFIX = "sellerId";
    public static final String ADV_SUFFIX = "adv";

    @Autowired
    ApplicationContext applicationContext;


    public void setUp() {
        dropAllTables();
    }

    public void after() {
    }

    @Test
    public void dummy() {
    }

    private void dropAllTables() {
        try {
//            accountRepository.deleteAllInBatch();
            resetAutoIncrementColumns(applicationContext,  "account");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetAutoIncrementColumns(ApplicationContext applicationContext, String... tableNames) throws SQLException {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        try (Connection dbConnection = dataSource.getConnection()) {
            //Create SQL statements that reset the auto increment columns and invoke
            //the created SQL statements.
            for (String resetSqlArgument : tableNames) {
                try (Statement statement = dbConnection.createStatement()) {
                    statement.execute("ALTER TABLE " + resetSqlArgument + " ALTER COLUMN id RESTART WITH 0");
                }
            }
        }
    }

    protected String getAdvertiserSuffixedId(String adv) {
        return adv + ":" + ADV_SUFFIX;
    }

    protected String getSellerSuffixedId(String seller) {
        return seller + ":" + SELLER_SUFFIX;
    }

    protected void print(JSONObject jsonObject) {
        print(jsonObject.toJSONString());
    }

    protected void print(MvcResult mvcResult) {
        try {
            print(mvcResult.getResponse().getContentAsString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void print(String data) {
        System.out.println("*************");
        System.out.println("*************");
        System.out.println(data);
        System.out.println("*************");
        System.out.println("*************");
    }
}
