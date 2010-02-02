/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.util.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.hibernate.dialect.Dialect;

import com.opengamma.OpenGammaRuntimeException;

/**
 * Command-line interface to create or clear databases.
 *
 * @author pietari
 */
public class DBTool {
  
  private DBDialect _dialect;
  private final String _dbServerHost;
  private final String _user;
  private final String _password;
  
  public DBTool(String dbServerHost,
      String user,
      String password) {
    
    _dbServerHost = dbServerHost;
    _user = user;
    _password = password;
    
    Map<String, DBDialect> url2Dialect = new HashMap<String, DBDialect>();
    url2Dialect.put("jdbc:postgresql", PostgresDialect.getInstance());  // add new supported DB types to this Map
    url2Dialect.put("jdbc:derby", DerbyDialect.getInstance());  // add new supported DB types to this Map
    
    String dbUrlLowercase = dbServerHost.toLowerCase();
    for (Map.Entry<String, DBDialect> entry : url2Dialect.entrySet()) {
      if (dbUrlLowercase.indexOf(entry.getKey()) != -1) {
        _dialect = entry.getValue();        
        break;
      }
    }
    
    if (_dialect == null) {
      throw new OpenGammaRuntimeException("Database " + dbServerHost + " not supported. The database URL must contain one of: " + url2Dialect.entrySet());
    }
    
    _dialect.initialise(dbServerHost, user, password);
  }
  
  
  
  public String getUser() {
    return _user;
  }

  public String getPassword() {
    return _password;
  }



  public void createTestSchema() {
    createSchema(getTestCatalog(), getTestSchema());
  }
  
  public void dropTestSchema() {
    dropSchema(getTestCatalog(), getTestSchema());
  }
  
  public void clearTestTables() {
    clearTables(getTestCatalog(), getTestSchema());
  }


 
  public void createSchema(String catalog, String schema) {
    _dialect.createSchema(catalog, schema);
  }
  
  public void dropSchema(String catalog, String schema) {
    _dialect.dropSchema(catalog, schema);
  }
  
  public void clearTables(String catalog, String schema) {
    _dialect.clearTables(catalog, schema);    
  }
  
  
  
  public static String getTestCatalogStatic() {
    return "test_" + System.getProperty("user.name");
  }
  
  public String getTestCatalog() {
    return getTestCatalogStatic();    
  }
  
  public String getTestSchema() {
    return null; // use default    
  }
  
  public String getTestDatabaseURL() {
    return _dbServerHost + "/" + getTestCatalog();         
  }
  
  public Dialect getHibernateDialect() {
    return _dialect.getHibernateDialect();
  }
  
  public Class<?> getJDBCDriverClass() {
    return _dialect.getJDBCDriverClass();
  }
  
  
  
  public void createTestTables() {
    createTables(getTestCatalog());
  }
  
  public void createTables(String catalog) {
    File file = new File("db/" + _dialect.getDatabaseName() + "/create-db.sql");
    String sql;
    try {
      sql = FileUtils.readFileToString(file);
    } catch (IOException e) {
      throw new OpenGammaRuntimeException("Cannot read file " + file.getAbsolutePath(), e);      
    }
    executeSql(catalog, sql);
  }
  
  public void executeSql(String catalog, String sql) {
    _dialect.executeSql(catalog, sql);    
  }
 
  
    
  public static void usage() {
    System.out.println();
    System.out.println("Usage:");    
    System.out.println();
    System.out.println("java com.opengamma.util.test.DBTool [args]");
    System.out.println("where args are any of the following:");
    System.out.println("--server={url} DB server URL (no database at the end) - for example, jdbc:postgresql://localhost:1234");
    System.out.println("--user={user} User name to the DB");
    System.out.println("--password={pw} Password to the DB");
    System.out.println("--database={dbname} Name of database on the DB server - for example, OpenGammaTests");
    System.out.println("--schema={schemaname} Name of schema within database. Optional. If not specified, the default schema for the database is used.");
    System.out.println("--create Creates the given database/schema");
    System.out.println("--drop Drops all tables and sequences within the given database/schema");
    System.out.println("--clear Clears all tables within the given database/schema");
    System.out.println("--createtestdb={dbtype} Drops schema in database test_<user.name> and recreates it.");
    System.out.println("  {dbtype} should be one of derby, postgres, all.");
    System.out.println("  Connection parameters are read from test.properties so you do not need");
    System.out.println("  to specify --server, --user, or --password.");
  }
  
  public static void main(String[] args) {
    
    String dbUrl = null;
    String user = null;
    String password = null;
    String catalog = null;
    String schema = null;
    String testDbType = null;
    
    boolean create = false;
    boolean drop = false;
    boolean clear = false;
    boolean createTestDb = false;
    
    for (String arg : args) {
      if (arg.startsWith("--server=")) {
        dbUrl = arg.substring("--server=".length());        
      }
      else if (arg.startsWith("--user=")) {
        user = arg.substring("--user=".length());
      }
      else if (arg.startsWith("--password=")) {
        password = arg.substring("--password=".length());
      }
      else if (arg.startsWith("--database=")) {
        catalog = arg.substring("--database=".length());
      }
      else if (arg.startsWith("--schema=")) {
        schema = arg.substring("--schema=".length());
      }
      else if (arg.equals("--create")) {
        create = true;
      }
      else if (arg.equals("--drop")) {
        drop = true;
      }
      else if (arg.equals("--clear")) {
        clear = true;
      }
      else if (arg.startsWith("--createtestdb=")) {
        createTestDb = true;
        testDbType = arg.substring("--createtestdb=".length());
      }
      else {
        System.out.println("Unrecognized option: " + arg);
        usage();
        System.exit(-1);
      }
    }
    
    if (!createTestDb) {
      if (dbUrl == null) {
        System.out.println("No DB server specified.");
        usage();
        System.exit(-1);
      }
      
      if (catalog == null) {
        System.out.println("No database on the DB server specified.");
        usage();
        System.exit(-1);
      }
    }
    
    if (!create && !drop && !clear && !createTestDb) {
      System.out.println("Nothing to do.");
      usage();
      System.exit(-1);
    }
    
    if (clear) {
      System.out.println("Clearing tables...");
      DBTool dbtool = new DBTool(dbUrl, user, password);
      dbtool.clearTables(catalog, schema);
    }
    
    if (drop) {
      System.out.println("Dropping schema...");
      DBTool dbtool = new DBTool(dbUrl, user, password);
      dbtool.dropSchema(catalog, schema);
    }

    if (create) {
      System.out.println("Creating schema...");
      DBTool dbtool = new DBTool(dbUrl, user, password);
      dbtool.createSchema(catalog, schema);      
    }
    
    if (createTestDb) {
      for (String dbType : TestProperties.getDatabaseTypes(testDbType)) {
        System.out.println("Creating " + dbType + " test database...");
        
        dbUrl = TestProperties.getDbHost(dbType);
        user = TestProperties.getDbUsername(dbType);
        password = TestProperties.getDbPassword(dbType);
        
        DBTool dbtool = new DBTool(dbUrl, user, password);
        dbtool.dropTestSchema(); // make sure it's empty if it already existed
        dbtool.createTestSchema();
        dbtool.createTestTables();
      }
    }
    
    System.out.println("All tasks succeeded.");
    System.exit(0);
  }

}
