package de.twentyeleven.skysail.server.restlet.test;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

import java.util.EnumSet;
import java.util.List;

import org.ops4j.pax.exam.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.testing.utils.PaxExamOptionSet;
import de.twenty11.skysail.common.testing.utils.SkysailCommonOsgiSetup;
import de.twenty11.skysail.server.testing.utils.SkysailServerOsgiSetup;

public class SkysailServerRestletOsgiSetup extends SkysailServerOsgiSetup {

    private static Logger logger = LoggerFactory.getLogger(SkysailCommonOsgiSetup.class.getName());

    @Override
    public List<Option> getOptions(EnumSet<PaxExamOptionSet> optionSets) {

        List<Option> options = super.getOptions(optionSets);

        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.common.restlet", "0.0.1-SNAPSHOT"));

        // skysail.server
        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.server"));

        logger.info("using options from {} for tests", this.getClass());

        return options;
    }

}
