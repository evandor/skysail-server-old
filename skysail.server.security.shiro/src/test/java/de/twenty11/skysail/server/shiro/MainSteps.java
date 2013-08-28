import de.twenty11.skysail.server.RestSteps;
import de.twenty11.skysail.server.shiro.AcceptanceTests;
import de.twenty11.skysail.server.shiro.JacksonSteps;
import net.thucydides.core.annotations.Steps;
import org.codehaus.jackson.map.ObjectMapper;
import org.jbehave.core.annotations.*;
import org.restlet.data.Form;
import org.restlet.resource.ClientResource;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class MainSteps extends AcceptanceTests {

    private Form form;
    private ClientResource cr;
    private String result;
    private ObjectMapper mapper = new ObjectMapper();
    private Integer id;
    
    @Steps
    private RestSteps rest;

    @Steps
    private JacksonSteps jackson;

    @Override
    @BeforeScenario
    public void setUp() {
        super.setUp();
    }

    // === GIVEN ===

    @Given("an existing user wants to login")
    public void setResourcePathForPost() {
    }

    // === WHEN ===

    @When("the user submits the login form with the username $username and the password $password")
    public void post(@Named("username") String username, @Named("password") String password) throws Exception {
    	result = rest.login(username, password);
    }

    // === THEN ===

    @Then("the request is successful")
    public void the_request_is_successful() {
        assertThat(result, containsString("\"success\":true"));
    }

}
