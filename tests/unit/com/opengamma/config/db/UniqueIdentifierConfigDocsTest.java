/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.config.db;

import java.util.Random;

import org.junit.After;
import org.junit.Before;

import com.opengamma.config.ConfigurationDocumentRepo;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.util.MongoDBConnectionSettings;
import com.opengamma.util.test.MongoDBTestUtils;

import static org.junit.Assert.assertEquals;

/**
 * Test UniqueIdentifier as a configuration document
 *
 */
public class UniqueIdentifierConfigDocsTest extends MongoConfigDocumentRepoTestcase<UniqueIdentifier> {

  private Random _random = new Random();

  /**
   * @param entityType
   */
  public UniqueIdentifierConfigDocsTest() {
    super(UniqueIdentifier.class);
  }

  private MongoDBConnectionSettings _mongoSettings;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    super.setUp();
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
    super.tearDown();
  }

  @Override
  public ConfigurationDocumentRepo<UniqueIdentifier> createMongoConfigRepo() {
    //use className as collection so dont set collectionName
    MongoDBConnectionSettings settings = MongoDBTestUtils.makeTestSettings(null, false);
    _mongoSettings = settings;
    return new MongoDBConfigurationRepo<UniqueIdentifier>(UniqueIdentifier.class, settings, true);
  }

  @Override
  public UniqueIdentifier makeTestConfigDoc(int version) {
    return UniqueIdentifier.of("TestScheme", "TestID", String.valueOf(version));
  }

  public MongoDBConnectionSettings getMongoDBConnectionSettings() {
    return _mongoSettings;
  }

  @Override
  protected UniqueIdentifier makeRandomConfigDoc() {
    return UniqueIdentifier.of("SCHEME" + _random.nextInt(), "ID" + _random.nextInt(), String.valueOf(_random
        .nextInt(100)));
  }

  @Override
  protected void assertConfigDocumentValue(UniqueIdentifier expected, UniqueIdentifier actual) {
    assertEquals(expected.getValue(), actual.getValue());
  }

}
