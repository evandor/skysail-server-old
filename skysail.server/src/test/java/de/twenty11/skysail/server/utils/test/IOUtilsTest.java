package de.twenty11.skysail.server.utils.test;

import java.util.Map;

import org.junit.Test;

import de.twenty11.skysail.server.utils.IOUtils;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

public class IOUtilsTest {

    @Test
    public void reads_multiple_lines() {
        String input = "/role = admin\n/users= anonymous\n\n";
        Map<String, String> defs = IOUtils.readSecurityDefinitions(input);
        assertThat(defs.size(), is(2));
        assertThat(defs, hasEntry("/role", "admin"));
    }

}
