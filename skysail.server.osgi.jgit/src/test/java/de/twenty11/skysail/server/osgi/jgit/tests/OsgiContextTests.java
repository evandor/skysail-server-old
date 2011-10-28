package de.twenty11.skysail.server.osgi.jgit.tests;

import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Repository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

import de.twenty11.skysail.server.osgi.jgit.service.definition.JGitServiceImpl;

@RunWith(JUnit4TestRunner.class)
public class OsgiContextTests {
    private static Repository testRep;
    private static JGitServiceImpl jgit;

    /**
     * @return the options for the testing framework.
     */
    @Configuration
    public final Option[] config() {
        return options(mavenBundle("de.2011.skysail", "skysail.server.osgi.jgit", "0.0.1-SNAPSHOT"),
                        mavenBundle("org.eclipse.jgit", "org.eclipse.jgit", "1.1.0.201109151100-r"),
                        mavenBundle("org.openengsb.wrapped", "com.jcraft.jsch-all", "0.1.42.w1"),
                        systemProperty("osgi.console").value("6666"),
                        // mavenBundle("org.slf4j", "slf4j-simple", "1.6.1"),
                        // mavenBundle("org.ops4j.pax.exam","pax-exam-junit","2.2.0"),
                        // TODO make maven bundle
                        // bundle("file:///home/carsten/workspaces/skysale2/skysail.server.restlet/src/main/webapp/WEB-INF/eclipse/plugins/freemarker_2.3.18.jar"),
                        // scanDir("/home/carsten/workspaces/skysale2/skysail.server.osgi.ext.freemarker"),
                        junitBundles()
        // equinox().version("3.6.2")
        );
    }

    @BeforeClass
    public static void setUp() throws IOException {
        jgit = new JGitServiceImpl();
        testRep = jgit.createRepository("/home/carsten/temp/dummygit");
        jgit.gitHead(testRep);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        File directory = testRep.getDirectory();
        if (directory.getAbsolutePath().length() < 25)
            throw new Exception ("are you sure what you are doing???");
        delete(directory);
        testRep = null;
    }

    /**
     * @param bc
     *            bundleContext
     * @throws IOException
     *             should not happen
     */
    @Test
    public final void getDefaultTemplate() throws IOException {
        assertTrue(testRep != null);
    }

//    @Test
//    public final void logGitTest() throws IOException, NoHeadException, JGitInternalException {
//        jgit.gitLog(testRep);
//    }

    @Test
    public final void addGitTest() throws IOException, NoFilepatternException {
        DirCache gitAdd = jgit.gitAdd(testRep, "*");
        assertTrue(gitAdd != null);
    }
    
    private static void delete(File f) throws IOException {
        if (f.isDirectory()) {
          for (File c : f.listFiles())
            delete(c);
        }
        if (!f.delete())
          throw new FileNotFoundException("Failed to delete file: " + f);
      }

}
