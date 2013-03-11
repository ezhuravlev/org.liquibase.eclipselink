package org.liquibase.eclipselink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import liquibase.database.Database;
import liquibase.database.structure.Column;
import liquibase.database.structure.ForeignKey;
import liquibase.database.structure.Index;
import liquibase.database.structure.PrimaryKey;
import liquibase.database.structure.Table;
import liquibase.diff.DiffStatusListener;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.DatabaseSnapshotGenerator;
import liquibase.util.StringUtils;

import org.eclipse.persistence.config.EntityManagerProperties;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.exceptions.ValidationException;
import org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition;
import org.eclipse.persistence.internal.jpa.EntityManagerFactoryImpl;
import org.eclipse.persistence.platform.database.DatabasePlatform;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.eclipse.persistence.tools.schemaframework.DefaultTableGenerator;
import org.eclipse.persistence.tools.schemaframework.FieldDefinition;
import org.eclipse.persistence.tools.schemaframework.ForeignKeyConstraint;
import org.eclipse.persistence.tools.schemaframework.TableCreator;
import org.eclipse.persistence.tools.schemaframework.TableDefinition;
import org.eclipse.persistence.tools.schemaframework.UniqueKeyConstraint;
import org.liquibase.eclipselink.jdbc.StubConnection;
import org.liquibase.eclipselink.jdbc.StubDriver;

public class EclipseLinkSnapshotGenerator implements DatabaseSnapshotGenerator {
	
	protected ServerSession session;
	
	

	/**
     * @see liquibase.snapshot.DatabaseSnapshotGenerator#supports(liquibase.database.Database)
     */
    @Override
	public boolean supports(Database database) {
		return database instanceof EclipseLinkDatabase;
	}

	/**
     * @see liquibase.snapshot.DatabaseSnapshotGenerator#getPriority(liquibase.database.Database)
     */
    @Override
	public int getPriority(Database database) {
		return PRIORITY_DATABASE;
	}
	
	/**
     * @see liquibase.snapshot.DatabaseSnapshotGenerator#createSnapshot(liquibase.database.Database, java.lang.String, java.util.Set<liquibase.diff.DiffStatusListener>)
     */
    @Override
	public DatabaseSnapshot createSnapshot(Database database, String schema, Set<DiffStatusListener> listeners) throws DatabaseException {

		DatabaseSnapshot snapshot = new DatabaseSnapshot(database, schema);
		
		String url = database.getConnection().getURL();		
		String persistenceUnitName = url.replace(StubConnection.URL_PREFIX, "");
		
		Map<String, String> properties = new HashMap<String, String>();
		
		properties.put(EntityManagerProperties.JDBC_DRIVER, StubDriver.class.getCanonicalName());
		properties.put(PersistenceUnitProperties.JDBC_URL, url);
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
		
		try {
	    	TableCreator creator;
	    	
	    	if(emf instanceof EntityManagerFactoryImpl) {
	    		
	    		session = ((EntityManagerFactoryImpl)emf).getServerSession();
	    		
	    		DefaultTableGenerator gen = new DefaultTableGenerator(session.getProject(), true);
	    		
	    		creator = gen.generateDefaultTableCreator();
	    	} else {
	    		throw new RuntimeException("Only EclipseLink persistence provider supported.");
	    	}
	        
	    	// Tables
	    	for (TableDefinition td : creator.getTableDefinitions()) {	        	
	        	processTable(snapshot, td);
			}
	    	
	    	// FKs
	    	for (TableDefinition td : creator.getTableDefinitions()) {	    		
	    		processTableForeignKeys(snapshot, td);
	    	}	    	
	    	
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
	    	emf.close();
	    	session.disconnect();
	    }
		
    	return snapshot;
	}
	
	private void processTable(DatabaseSnapshot snapshot, TableDefinition td) {
		
		Table table = new Table(td.getName());
        
    	snapshot.getTables().add(table);
		
        //Fields
    	for (FieldDefinition fd : td.getFields()) {
    		
    		Column column = getColumn(td, fd);        		

            column.setTable(table);
            
            table.getColumns().add(column);
    	}
    	
    	// PKs
    	PrimaryKey primaryKey = new PrimaryKey();
    	primaryKey.setTable(table);
    	for (String pkFieldName : td.getPrimaryKeyFieldNames()) {    		
    		primaryKey.getColumnNamesAsList().add(pkFieldName);
    	}        		
        snapshot.getPrimaryKeys().add(primaryKey);
        
        // UniqueConstraints
        for (UniqueKeyConstraint uniqueKey : td.getUniqueKeys()) {
    		
    		Index index = new Index();
            index.setTable(table);
            index.setName(uniqueKey.getName());
            
            for (String fieldName : uniqueKey.getSourceFields()) {                
            	index.getColumns().add(fieldName);
            }

            snapshot.getIndexes().add(index);
    	}
    	
    	// TODO: Indexes
	}
	
	private void processTableForeignKeys(DatabaseSnapshot snapshot, TableDefinition td) {
		
		for (ForeignKeyConstraint fk : td.getForeignKeys()) {
			
			ForeignKey foreignKey = new ForeignKey();
			
			foreignKey.setName(fk.getName());
			foreignKey.setPrimaryKeyTable(snapshot.getTable(fk.getTargetTable()));
			foreignKey.setForeignKeyTable(snapshot.getTable(td.getName()));
				    
			// Target fields
            Set<String> columnNames = new HashSet<String>();
            for (String field : fk.getTargetFields()) {
            	columnNames.add(field);
            }
            foreignKey.setPrimaryKeyColumns(StringUtils.join(columnNames, ", "));

            // Source fields
            columnNames.clear();
            for (String field : fk.getSourceFields()) {
            	columnNames.add(field);
            }
            // FIXME: WTF?
            if (columnNames.isEmpty()) {
            	for (String field : fk.getSourceFields()) {
                	columnNames.add(field);
                }
            }
            foreignKey.setForeignKeyColumns(StringUtils.join(columnNames, ", "));
            
            // TODO foreignKey.setDeferrable(null);
            // TODO foreignKey.setInitiallyDeferred();
            
    		snapshot.getForeignKeys().add(foreignKey);
		}
	}
	
	private Column getColumn(TableDefinition td, FieldDefinition fd) {
		
		DatabasePlatform platform = session.getPlatform();
		
		Column column = new Column();
        column.setName(fd.getName());
        
        column.setPrimaryKey(fd.isPrimaryKey());
        column.setUnique(fd.isUnique());
        column.setNullable(fd.shouldAllowNull());
        column.setAutoIncrement(fd.isIdentity() && platform.supportsIdentity());

        // TODO: column.setDefaultValue();
        
        FieldTypeDefinition typeDef = getFieldTypeDefinition(fd);
        
        //column.setDataType(hibernateColumn.getSqlTypeCode(mapping)); //FIXME        
        column.setTypeName(typeDef.getName());
        
        column.setCertainDataType(false);
       
        if ((typeDef.isSizeAllowed()) && ((fd.getSize() != 0) || (typeDef.isSizeRequired()))) {
        	
        	if (fd.getSize() != 0) {
		        column.setColumnSize(fd.getSize());
		    } else {				
	        	column.setColumnSize(typeDef.getDefaultSize());
		    }
        	
		    if (fd.getSubSize() != 0) {
		    	column.setDecimalDigits(fd.getSubSize());
		    } else if (typeDef.getDefaultSubSize() != 0) {
		    	column.setDecimalDigits(typeDef.getDefaultSubSize());
		    }
		}
        
        return column;
	}

	@SuppressWarnings("rawtypes")
	protected FieldTypeDefinition getFieldTypeDefinition(FieldDefinition fieldDefinition) {
		
		/*if(null != fieldDefinition.getTypeDefinition()) {
			return
		}*/
		
		FieldTypeDefinition fieldType;
		
		DatabasePlatform platform = session.getPlatform();		
		Class<?> type = fieldDefinition.getType();
		String typeName = fieldDefinition.getTypeName();
		                
        if (null != type) { //translate Java 'type'
            fieldType = platform.getFieldTypeDefinition(type);
            if (null == fieldType) {
                throw ValidationException.javaTypeIsNotAValidDatabaseType(type);
            }
        } else if (typeName != null) { //translate generic type name
            Map<String, Class> fieldTypes = platform.getClassTypes();
            type = fieldTypes.get(typeName);
            if (null == type) { // if unknown type name, use as it is
                fieldType = new FieldTypeDefinition(typeName);
            } else {
                fieldType = platform.getFieldTypeDefinition(type);
                if (null == fieldType) {
                    throw ValidationException.javaTypeIsNotAValidDatabaseType(type);
                }
            }
        } else {
            // both type and typeName is null
            throw ValidationException.javaTypeIsNotAValidDatabaseType(null);
        }
        
        return fieldType;
	}
    
    

    public Table getDatabaseChangeLogTable(Database database) throws DatabaseException {
        return null;
    }

    public Table getDatabaseChangeLogLockTable(Database database) throws DatabaseException {
        return null;
    }

    public Table getTable(String schemaName, String tableName, Database database) throws DatabaseException {
        return null;
    }

    public Column getColumn(String schemaName, String tableName, String columnName, Database database) throws DatabaseException {
        return null;
    }

    public boolean hasDatabaseChangeLogTable(Database database) {
        return false;
    }

    public boolean hasDatabaseChangeLogLockTable(Database database) {
        return false;
    }

    public boolean hasTable(String schemaName, String tableName, Database database) {
        return false;
    }

    public ForeignKey getForeignKeyByForeignKeyTable(String schemaName, String tableName, String fkName, Database database) throws DatabaseException {
        return null;
    }

    public List<ForeignKey> getForeignKeys(String schemaName, String tableName, Database database) throws DatabaseException {
        return new ArrayList<ForeignKey>();
    }

    public boolean hasIndex(String s, String s1, String s2, Database database, String s3) throws DatabaseException {
        return false;
    }

	public boolean hasView(String arg0, String arg1, Database arg2) {
		return false;
	}
}
