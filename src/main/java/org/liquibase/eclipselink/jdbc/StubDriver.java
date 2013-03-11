package org.liquibase.eclipselink.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

public class StubDriver implements Driver {

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
        return new StubConnection(url);
    }

	@Override
	public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith(StubConnection.URL_PREFIX);
    }

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

	@Override
	public int getMajorVersion() {
        return 0;
    }

	@Override
	public int getMinorVersion() {
        return 0;
    }

	@Override
	public boolean jdbcCompliant() {
        return false;
    }
}
