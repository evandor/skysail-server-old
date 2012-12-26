///**
// *  Copyright 2011 Carsten Gräf
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// * 
// */
//
//package de.twenty11.skysail.server.test;
//
//import static org.junit.Assert.*;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.io.Writer;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.codehaus.jackson.map.ObjectMapper;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.restlet.data.CharacterSet;
//import org.restlet.ext.jackson.JacksonRepresentation;
//
//import de.twenty11.skysail.common.grids.GridData;
//import de.twenty11.skysail.common.grids.RowData;
//import de.twenty11.skysail.common.responses.SkysailResponse;
//import de.twenty11.skysail.common.responses.SkysailSuccessResponse;
//
//public class ResponseTest {
//
//    @BeforeClass
//    public static void setUpBeforeClass() throws Exception {
//    }
//
//    @AfterClass
//    public static void tearDownAfterClass() throws Exception {
//    }
//
//    @Before
//    public void setUp() throws Exception {
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    //@Test
//    public void test() {
//        GridData gridData = new GridData(null);
//        RowData rowData = new RowData(null);
//        List<Object> columnData = new ArrayList<Object>();
//        columnData.add("hier");
//        columnData.add("dort");
////        rowData.setColumnData(columnData );
//        gridData.addRowData(rowData );
//        SkysailResponse<GridData> response = new SkysailSuccessResponse<GridData>(gridData);
//        // setResponseDetails(response);
//        JacksonRepresentation<SkysailResponse<GridData>> rep = new JacksonRepresentation<SkysailResponse<GridData>>(
//                        response);
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            OutputStream outputStream = new ByteArrayOutputStream();
//            Writer writer = new OutputStreamWriter(outputStream,
//                            CharacterSet.ISO_8859_1.getName());
//            
//            mapper.writeValue(writer, rep);
//            System.out.println(rep.getText());
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//}
