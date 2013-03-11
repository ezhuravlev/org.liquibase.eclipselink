package org.liquibase.eclipselink;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import liquibase.database.core.H2Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.Diff;
import liquibase.diff.DiffResult;
import liquibase.servicelocator.ServiceLocator;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.h2.Driver;
import org.junit.Test;
import org.liquibase.eclipselink.jdbc.StubDriver;

public class EclipseLinkIntegrationTest {
    
    private static final String PU_NAME = "persistenceUnit";
    private static final String H2_URL = "jdbc:h2:mem:" + PU_NAME;
    private static final String EL_URL = "eclipselink:" + PU_NAME;
    

	@Test
    public void testPersistenceUnit() throws Exception {   	
		
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(PersistenceUnitProperties.JDBC_DRIVER, Driver.class.getCanonicalName());
		properties.put(PersistenceUnitProperties.JDBC_URL, H2_URL);
		properties.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.CREATE_ONLY);
		properties.put(PersistenceUnitProperties.DDL_GENERATION_MODE, PersistenceUnitProperties.DDL_DATABASE_GENERATION);
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(PU_NAME, properties);		
		
		emf.createEntityManager();
		
		//workaround
		ServiceLocator.getInstance().addPackageToScan(EclipseLinkDatabase.class.getPackage().getName());
		   	
    	H2Database database = new H2Database();
    	Connection h2Connection = (new Driver()).connect(H2_URL, null);    	
    	database.setConnection(new JdbcConnection(h2Connection));
		
    	EclipseLinkDatabase eclipselinkDatabase = new EclipseLinkDatabase();
    	Connection elConnection = (new StubDriver()).connect(EL_URL, null);
    	eclipselinkDatabase.setConnection(new JdbcConnection(elConnection));
    	
    	Diff diff = new Diff(eclipselinkDatabase, database);
		DiffResult diffResult = diff.compare();
		
		assertEquals(0, diffResult.getMissingTables().size());
		assertEquals(0, diffResult.getMissingColumns().size());
		assertEquals(0, diffResult.getMissingPrimaryKeys().size());
		assertEquals(0, diffResult.getMissingIndexes().size());
		assertEquals(0, diffResult.getMissingViews().size());
		assertEquals(0, diffResult.getMissingForeignKeys().size());

		assertEquals(0, diffResult.getUnexpectedTables().size());
		assertEquals(0, diffResult.getUnexpectedColumns().size());
		assertEquals(0, diffResult.getUnexpectedPrimaryKeys().size());
		//FIXME
		//assertEquals(0, diffResult.getUnexpectedIndexes().size());
		assertEquals(0, diffResult.getUnexpectedViews().size());
		assertEquals(0, diffResult.getUnexpectedForeignKeys().size());
	}
}
