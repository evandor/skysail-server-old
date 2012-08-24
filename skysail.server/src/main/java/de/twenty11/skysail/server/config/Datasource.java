package de.twenty11.skysail.server.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.services.DataSourceProvider;

public class Datasource implements DataSourceProvider {

    private final static Logger logger = LoggerFactory.getLogger(Datasource.class);
    private BasicDataSource defaultDS;

    @Override
    public DataSource get() {
        return this.defaultDS;
    }
    

}
