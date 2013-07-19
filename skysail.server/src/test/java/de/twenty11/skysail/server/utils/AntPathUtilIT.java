/*
 * Copyright 2013 graefca.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.twenty11.skysail.server.utils;

import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author graefca
 */
public class AntPathUtilIT {

    AntPathUtil antPathUtil;

    public AntPathUtilIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        antPathUtil = new AntPathUtil();
    }

    @After
    public void tearDown() {
    }

    //com/t?st.jsp - matches com/test.jsp but also com/tast.jsp or com/txst.jsp
    @Test
    public void matches_questionmark() {
        assertThat(antPathUtil.match("/com/t?st.jsp", "/com/test.jsp"), is(true));
    }
}
