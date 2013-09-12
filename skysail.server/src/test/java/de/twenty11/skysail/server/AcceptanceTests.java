package de.twenty11.skysail.server;

import de.twenty11.skysail.server.internal.DefaultSkysailApplication;
import de.twenty11.skysail.server.internal.SkysailComponent;
import org.jbehave.core.annotations.BeforeStory;
import org.restlet.Component;
import org.restlet.data.Protocol;

public class AcceptanceTests extends ResourceTestWithUnguardedAppication<DefaultSkysailApplication> {

    private static Component component = new SkysailComponent();

    static {
        component.getServers().add(Protocol.HTTP, TEST_PORT);
        try {
            component.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeStory
    public void setUp() {
        DefaultSkysailApplication application = (DefaultSkysailApplication) setUpApplication(new DefaultSkysailApplication(null));
        //application.setEntityManager(getEmfForTests("NotesPU"));
        component.getDefaultHost().attach(application);
    }

}
