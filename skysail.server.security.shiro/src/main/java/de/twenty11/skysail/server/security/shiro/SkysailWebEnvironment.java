//package de.twenty11.skysail.server.security.shiro;
//
//import java.util.Map;
//
//import org.apache.shiro.ShiroException;
//import org.apache.shiro.config.Ini;
//import org.apache.shiro.env.DefaultEnvironment;
//import org.apache.shiro.util.CollectionUtils;
//import org.apache.shiro.util.Destroyable;
//import org.apache.shiro.util.Initializable;
//import org.apache.shiro.web.config.WebIniSecurityManagerFactory;
//import org.apache.shiro.web.env.DefaultWebEnvironment;
//import org.apache.shiro.web.filter.mgt.FilterChainResolver;
//import org.apache.shiro.web.mgt.WebSecurityManager;
//import org.hibernate.validator.internal.cfg.context.ConfiguredConstraint;
//import org.restlet.Context;
//
//public class SkysailWebEnvironment extends DefaultWebEnvironment implements Initializable, Destroyable{
//
//    private Ini ini;
//    private Context context;
//
//    public SkysailWebEnvironment(Ini ini, Context context) {
//        this.ini = ini;
//        this.context = context;
//    }
//
//    @Override
//    public void init() throws ShiroException {
//        configure();
//    }
//
//    protected void configure() {
//
//        this.objects.clear();
//
//        WebSecurityManager securityManager = createWebSecurityManager();
//        setWebSecurityManager(securityManager);
//
////        FilterChainResolver resolver = createFilterChainResolver();
////        if (resolver != null) {
////            setFilterChainResolver(resolver);
////        }
//    }
//
//    protected WebSecurityManager createWebSecurityManager() {
//        WebIniSecurityManagerFactory factory;
//        if (CollectionUtils.isEmpty(ini)) {
//            factory = new WebIniSecurityManagerFactory();
//        } else {
//            factory = new WebIniSecurityManagerFactory(ini);
//        }
//
//        WebSecurityManager wsm = (WebSecurityManager)factory.getInstance();
//
//        //SHIRO-306 - get beans after they've been created (the call was before the factory.getInstance() call,
//        //which always returned null.
//        Map<String, ?> beans = factory.getBeans();
//        if (!CollectionUtils.isEmpty(beans)) {
//            this.objects.putAll(beans);
//        }
//
//        return wsm;
//    }
//
//
//
//}
