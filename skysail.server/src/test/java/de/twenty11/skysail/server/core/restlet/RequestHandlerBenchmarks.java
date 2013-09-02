package de.twenty11.skysail.server.core.restlet;

import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;
import de.twenty11.skysail.server.core.restlet.test.RequestHandlerTest;

public class RequestHandlerBenchmarks {

        // H:\git\skysail-server\skysail.server>mvn exec:java -Dexec.mainClass="com.google.caliper.runner.CaliperMain" -Dexec.args="de.twenty11.skysail.server.core.restlet.RequestHandlerBenchmarks.Benchmark1 -p -v" -Dexec.classpathScope=test

        RequestHandlerTest rht = new RequestHandlerTest();

        @Benchmark
        public void runMe(int reps) {
            for (int i = 0; i < reps; i++) {
                System.out.println("i: " + i);
                rht.entity_is_retrieved_via_requestHandlerChain();
            }
        }

    public static class Benchmark1 {
        @Benchmark void timeNanoTime(int reps) {
            for (int i = 0; i < reps; i++) {
                System.nanoTime();
            }
        }
    }

       /*public static void main(String[] args) {
            //RequestHandlerBenchmarks bm = new RequestHandlerBenchmarks();
            //bm.timeMyOperation(10);
            CaliperMain.main(RequestHandlerBenchmarks.class, args);
        }      */
}
