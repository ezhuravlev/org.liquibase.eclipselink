package org.liquibase.eclipselink;

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.liquibase.eclipselink.jdbc.StubConnection;
import org.liquibase.eclipselink.jdbc.StubDriver;


import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.changelog.RanChangeSet;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.structure.DatabaseObject;
import liquibase.exception.DatabaseException;
import liquibase.exception.DatabaseHistoryException;
import liquibase.exception.DateParseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.RollbackImpossibleException;
import liquibase.exception.StatementNotSupportedOnDatabaseException;
import liquibase.exception.UnsupportedChangeException;
import liquibase.sql.visitor.SqlVisitor;
import liquibase.statement.DatabaseFunction;
import liquibase.statement.SqlStatement;

public class EclipseLinkDatabase implements Database {	
	public static final String TYPE_NAME = "eclipselink";	
	public static final String DATABASE_PRODUCT_NAME = "EclipseLink";

    private String defaultSchema;
	private DatabaseConnection connection;

    
	
    public EclipseLinkDatabase() {
    
    }

	@Override
	public DatabaseConnection getConnection() {
		return connection;
	}

	@Override
	public void setConnection(DatabaseConnection conn) {
		this.connection = conn;
	}

	@Override
	public void checkDatabaseChangeLogTable(boolean updateExistingNullChecksums, DatabaseChangeLog databaseChangeLog, String[] contexts) throws DatabaseException {		
	
	}
    
    /**
     * @see liquibase.database.Database#getTypeName()
     */
    @Override
    public String getTypeName() {
    	return TYPE_NAME;
    }

    /**
     * @see liquibase.database.Database#getDatabaseProductName()
     */
    @Override
	public String getDatabaseProductName() {
		return DATABASE_PRODUCT_NAME;
	}

    /**
     * @see liquibase.database.Database#isCorrectDatabaseImplementation(liquibase.database.DatabaseConnection)
     */
    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
    	return DATABASE_PRODUCT_NAME.equals(conn.getDatabaseProductName());
    }

	@Override
	public String getDefaultSchemaName() {
		return defaultSchema;
	}

	@Override
	public void setDefaultSchemaName(String defaultSchema) throws DatabaseException {
		this.defaultSchema = defaultSchema;
	}
    
    /**
     * @see liquibase.database.Database#getDefaultDriver(java.lang.String)
     */
    @Override
    public String getDefaultDriver(String url) {
		if (url.startsWith(StubConnection.URL_PREFIX))
			return StubDriver.class.getCanonicalName();
		else
			return null;
    }

    /**
     * @see liquibase.servicelocator.PrioritizedService#getPriority()
     */
    @Override
    public int getPriority() {
    	return PRIORITY_DATABASE;
    }

    @Override
	public String getDatabaseProductVersion() throws DatabaseException {
        return "N/A";
    }

    @Override
	public boolean requiresUsername() {
        return false;
    }

	@Override
	public boolean requiresPassword() {
		return false;
	}
	
    @Override
	public DatabaseObject[] getContainingObjects() {
        return null;
    }

    public void checkDatabaseChangeLogTable(boolean b, DatabaseChangeLog databaseChangeLog) throws DatabaseException {

    }

    @Override
	public List<DatabaseFunction> getDatabaseFunctions() {
        return new ArrayList<DatabaseFunction>();
    }

    @Override
	public void reset() {
    	
    }

    @Override
	public boolean supportsForeignKeyDisable() {
        return false;
    }

    @Override
	public boolean disableForeignKeyChecks() throws DatabaseException {
        return false;
    }

    @Override
	public void enableForeignKeyChecks() throws DatabaseException {
    }

    @Override
	public boolean getAutoCommitMode() {
        return false;
    }

    @Override
	public boolean isAutoCommit() throws DatabaseException {
        return false;
    }

    @Override
	public void setAutoCommit(boolean b) throws DatabaseException {
        
    }

    @Override
	public boolean supportsDDLInTransaction() {
        return false;
    }

    @Override
	public int getDatabaseMajorVersion() throws DatabaseException {
        return -1;
    }

    @Override
	public int getDatabaseMinorVersion() throws DatabaseException {
        return -1;
    }

    @Override
	public String getDefaultCatalogName() throws DatabaseException {
        return null;
    }

    @Override
	public boolean supportsInitiallyDeferrableColumns() {
        return false;
    }

    @Override
	public boolean supportsSequences() {
        return false;
    }

    @Override
	public boolean supportsAutoIncrement() {
        return false;
    }

    @Override
	public String getDateLiteral(String isoDate) {
        return null;
    }

    @Override
	public String getCurrentDateTimeFunction() {
        return null;
    }

    @Override
	public void setCurrentDateTimeFunction(String function) {

    }

    @Override
	public String getLineComment() {
        return null;
    }

    public String getAutoIncrementClause() {
        return null;
    }

    @Override
	public String getDatabaseChangeLogTableName() {
        return null;
    }

    @Override
	public String getDatabaseChangeLogLockTableName() {
        return null;
    }
    
    /**
     * Does nothing because this is a hibernate database
     * @see liquibase.database.Database#setDatabaseChangeLogLockTableName(java.lang.String)
     */
    @Override
	public void setDatabaseChangeLogLockTableName(String tableName) {
    	
    }

	/**
	 * Does nothing because this is a hibernate database
     * @see liquibase.database.Database#setDatabaseChangeLogTableName(java.lang.String)
     */
    @Override
	public void setDatabaseChangeLogTableName(String tableName) {
    	
    }

	@Override
	public String getConcatSql(String... values) {
        return null;
    }

    @Override
	public void setCanCacheLiquibaseTableInfo(boolean canCacheLiquibaseTableInfo) {
    	
    }

    @Override
	public boolean hasDatabaseChangeLogTable() {
        return false;
    }

    @Override
	public boolean hasDatabaseChangeLogLockTable() {
        return false;
    }

    public void checkDatabaseChangeLogTable() throws DatabaseException {

    }

    @Override
	public void checkDatabaseChangeLogLockTable() throws DatabaseException {

    }

    @Override
	public void dropDatabaseObjects(String schema) throws DatabaseException {

    }

    @Override
	public void tag(String tagString) throws DatabaseException {

    }

    @Override
	public boolean doesTagExist(String tag) throws DatabaseException {
        return false;
    }

    @Override
	public boolean isSystemTable(String catalogName, String schemaName, String tableName) {
        return false;
    }

    @Override
	public boolean isLiquibaseTable(String tableName) {
        return false;
    }

    @Override
	public boolean shouldQuoteValue(String value) {
        return false;
    }

    @Override
	public boolean supportsTablespaces() {
        return false;
    }

    @Override
	public String getViewDefinition(String schemaName, String name) throws DatabaseException {
        return null;
    }

    public String getDatabaseProductName(DatabaseConnection conn) throws DatabaseException {
        return null;
    }

    @Override
	public boolean isSystemView(String catalogName, String schemaName, String name) {
        return false;
    }

    @Override
	public String getDateLiteral(Date date) {
        return null;
    }

    @Override
	public String getTimeLiteral(Time time) {
        return null;
    }

    @Override
	public String getDateTimeLiteral(Timestamp timeStamp) {
        return null;
    }

    @Override
	public String getDateLiteral(java.util.Date defaultDateValue) {
        return null;
    }

    @Override
	public String escapeTableName(String schemaName, String tableName) {
        return null;
    }

    @Override
	public String escapeIndexName(String schemaName, String indexName) {
        return null;
    }

    @Override
	public String escapeDatabaseObject(String objectName) {
        return null;
    }

    @Override
	public String escapeColumnName(String schemaName, String tableName, String columnName) {
        return null;
    }

    @Override
	public String escapeColumnNameList(String columnNames) {
        return null;
    }

    @Override
	public String convertRequestedSchemaToSchema(String requestedSchema) throws DatabaseException {
        return null;
    }

    @Override
	public String convertRequestedSchemaToCatalog(String requestedSchema) throws DatabaseException {
        return null;
    }

    @Override
	public boolean supportsSchemas() {
        return false;
    }

    @Override
	public String generatePrimaryKeyName(String tableName) {
        return null;
    }

    @Override
	public String escapeSequenceName(String schemaName, String sequenceName) {
        return null;
    }

    @Override
	public String escapeViewName(String schemaName, String viewName) {
        return null;
    }

    @Override
	public ChangeSet.RunStatus getRunStatus(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return null;
    }

    @Override
	public RanChangeSet getRanChangeSet(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return null;
    }

    @Override
	public void markChangeSetExecStatus(ChangeSet changeSet, ChangeSet.ExecType execType) throws DatabaseException {

    }

    @Override
	public List<RanChangeSet> getRanChangeSetList() throws DatabaseException {
        return null;
    }

    @Override
	public java.util.Date getRanDate(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return null;
    }

    @Override
	public void removeRanStatus(ChangeSet changeSet) throws DatabaseException {

    }

    @Override
	public void commit() throws DatabaseException {

    }

    @Override
	public void rollback() throws DatabaseException {

    }

    @Override
	public String escapeStringForDatabase(String string) {
        return null;
    }

    @Override
	public void close() throws DatabaseException {

    }

    @Override
	public boolean supportsRestrictForeignKeys() {
        return false;
    }

    @Override
	public String escapeConstraintName(String constraintName) {
        return constraintName;
    }

    @Override
	public boolean isLocalDatabase() throws DatabaseException {
    	return false;
    }

    @Override
	public void executeStatements(Change change, DatabaseChangeLog changeLog, List<SqlVisitor> sqlVisitors) throws LiquibaseException, UnsupportedChangeException {

    }

    @Override
	public void execute(SqlStatement[] statements, List<SqlVisitor> sqlVisitors) throws LiquibaseException {

    }

    @Override
	public void saveStatements(Change change, List<SqlVisitor> sqlVisitors, Writer writer) throws IOException, UnsupportedChangeException, StatementNotSupportedOnDatabaseException, LiquibaseException {

    }

    @Override
	public void executeRollbackStatements(Change change, List<SqlVisitor> sqlVisitors) throws LiquibaseException, UnsupportedChangeException, RollbackImpossibleException {

    }

    @Override
	public void saveRollbackStatement(Change change, List<SqlVisitor> sqlVisitors, Writer writer) throws IOException, UnsupportedChangeException, RollbackImpossibleException, StatementNotSupportedOnDatabaseException, LiquibaseException {

    }

	@Override
	public String getLiquibaseSchemaName(){
		return null;
	}

    @Override
	public int getNextChangeSetSequenceValue() throws LiquibaseException {
        return 1;
    }

    @Override
	public java.util.Date parseDate(String dateAsString) throws DateParseException {
        return new java.util.Date();
    }

    @Override
	public boolean isReservedWord(String string) {
        return false;
    }

    @Override
	public boolean supportsDropTableCascadeConstraints() {
        return false;
    }

	@Override
	public String getAutoIncrementClause(BigInteger arg0, BigInteger arg1) {
		return null;
	}
}