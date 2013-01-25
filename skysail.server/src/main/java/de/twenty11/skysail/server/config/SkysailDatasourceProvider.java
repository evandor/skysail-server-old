package de.twenty11.skysail.server.config;

import javax.sql.DataSource;

import de.twenty11.skysail.server.services.DataSourceProvider;

public class SkysailDatasourceProvider implements DataSourceProvider {

    //private final static Logger logger = LoggerFactory.getLogger(SkysailDatasourceProvider.class);
   
    @Override
    public DataSource get() {
        return ServerConfiguration.getDefaultDS();
    }
    

}
