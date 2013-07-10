package test.de.twenty11.skysail.server.menu.domain;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

public class ShiroBaseTest {

    @Before
    public void setUp() throws Exception {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
    }
    
    @Test
    public void setup_works() {
        // no assertions
    }
    
    @Test
    public void test_current_user_is_not_authenticated() throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        assertThat(currentUser.isAuthenticated(), is(false));
    }

    @Test(expected = IncorrectCredentialsException.class)
    public void test_user_cannot_log_in_with_wrong_credentials() throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "wrongPW");
        currentUser.login(token);
    }

    @Test
    public void test_user_can_log_in_with_matching_credentials() throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
        currentUser.login(token);
        currentUser.logout();
    }


}
