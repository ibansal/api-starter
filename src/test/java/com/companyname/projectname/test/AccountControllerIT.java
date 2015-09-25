package com.companyname.projectname.test;

import com.companyname.projectname.repository.AeroSpikeDBManager;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerIT extends BaseTest {

    private static Logger logger = LoggerFactory.getLogger(AccountControllerIT.class);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    AeroSpikeDBManager aeroSpikeDBManager;

    @Value("${aerospike.db.name}")
    private String databaseName;

    @Before
    public void setUp() {
        logger.debug("*** TEST START ***");
        super.setUp();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @After
    public void after() {
        logger.debug("*** TEST COMPLETE ***");
        super.after();
    }

    @Test
    public void shouldTestStatus() throws Exception {
        mockMvc.perform(get("/v1/project-base-path/status")).andExpect(status().isOk());
    }


}
