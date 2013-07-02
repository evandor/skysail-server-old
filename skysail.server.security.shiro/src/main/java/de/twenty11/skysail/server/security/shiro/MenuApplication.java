package de.twenty11.skysail.server.security.shiro;

import de.twenty11.skysail.server.restlet.SkysailApplication;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuApplication extends SkysailApplication {

    private static final Logger logger = LoggerFactory.getLogger(MenuApplication.class);

    @Override
    protected void attach() {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
