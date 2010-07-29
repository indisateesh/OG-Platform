/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.position.master.db;

/**
 * Configuration object defining the workers in use for {@code DbPositionMaster}.
 * <p>
 * This class is designed for injection of alternative implementations.
 */
public class DbPositionMasterWorkers {

  /** Worker. */
  private DbPositionMasterWorker _searchPortfolioTreesWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _getPortfolioTreeWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _addPortfolioTreeWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _updatePortfolioTreeWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _removePortfolioTreeWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _searchHistoricPortfolioTreesWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _correctPortfolioTreeWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _searchPositionsWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _getPositionWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _addPositionWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _updatePositionWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _removePositionWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _searchHistoricPositionsWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _correctPositionWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _getFullPortfolioWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _getFullPortfolioNodeWorker = new GetPositionDbPositionMasterWorker();
  /** Worker. */
  private DbPositionMasterWorker _getFullPositionWorker = new GetPositionDbPositionMasterWorker();

  /**
   * Creates an instance.
   */
  public DbPositionMasterWorkers() {
  }

  /**
   * Initializes the instance.
   * @param master  the position master, non-null
   */
  protected void init(final DbPositionMaster master) {
    _searchPortfolioTreesWorker.init(master);
    _getPortfolioTreeWorker.init(master);
    _addPortfolioTreeWorker.init(master);
    _updatePortfolioTreeWorker.init(master);
    _removePortfolioTreeWorker.init(master);
    _searchHistoricPortfolioTreesWorker.init(master);
    _correctPortfolioTreeWorker.init(master);
    _searchPositionsWorker.init(master);
    _getPositionWorker.init(master);
    _addPositionWorker.init(master);
    _updatePositionWorker.init(master);
    _removePositionWorker.init(master);
    _searchHistoricPositionsWorker.init(master);
    _correctPositionWorker.init(master);
    _getFullPortfolioWorker.init(master);
    _getFullPortfolioNodeWorker.init(master);
    _getFullPositionWorker.init(master);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the searchPortfolioTreesWorker field.
   * @return the searchPortfolioTreesWorker
   */
  public DbPositionMasterWorker getSearchPortfolioTreesWorker() {
    return _searchPortfolioTreesWorker;
  }

  /**
   * Sets the searchPortfolioTreesWorker field.
   * @param searchPortfolioTreesWorker  the searchPortfolioTreesWorker
   */
  public void setSearchPortfolioTreesWorker(DbPositionMasterWorker searchPortfolioTreesWorker) {
    _searchPortfolioTreesWorker = searchPortfolioTreesWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the getPortfolioTreeWorker field.
   * @return the getPortfolioTreeWorker
   */
  public DbPositionMasterWorker getGetPortfolioTreeWorker() {
    return _getPortfolioTreeWorker;
  }

  /**
   * Sets the getPortfolioTreeWorker field.
   * @param getPortfolioTreeWorker  the getPortfolioTreeWorker
   */
  public void setGetPortfolioTreeWorker(DbPositionMasterWorker getPortfolioTreeWorker) {
    _getPortfolioTreeWorker = getPortfolioTreeWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the addPortfolioTreeWorker field.
   * @return the addPortfolioTreeWorker
   */
  public DbPositionMasterWorker getAddPortfolioTreeWorker() {
    return _addPortfolioTreeWorker;
  }

  /**
   * Sets the addPortfolioTreeWorker field.
   * @param addPortfolioTreeWorker  the addPortfolioTreeWorker
   */
  public void setAddPortfolioTreeWorker(DbPositionMasterWorker addPortfolioTreeWorker) {
    _addPortfolioTreeWorker = addPortfolioTreeWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the updatePortfolioTreeWorker field.
   * @return the updatePortfolioTreeWorker
   */
  public DbPositionMasterWorker getUpdatePortfolioTreeWorker() {
    return _updatePortfolioTreeWorker;
  }

  /**
   * Sets the updatePortfolioTreeWorker field.
   * @param updatePortfolioTreeWorker  the updatePortfolioTreeWorker
   */
  public void setUpdatePortfolioTreeWorker(DbPositionMasterWorker updatePortfolioTreeWorker) {
    _updatePortfolioTreeWorker = updatePortfolioTreeWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the removePortfolioTreeWorker field.
   * @return the removePortfolioTreeWorker
   */
  public DbPositionMasterWorker getRemovePortfolioTreeWorker() {
    return _removePortfolioTreeWorker;
  }

  /**
   * Sets the removePortfolioTreeWorker field.
   * @param removePortfolioTreeWorker  the removePortfolioTreeWorker
   */
  public void setRemovePortfolioTreeWorker(DbPositionMasterWorker removePortfolioTreeWorker) {
    _removePortfolioTreeWorker = removePortfolioTreeWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the searchHistoricPortfolioTreesWorker field.
   * @return the searchHistoricPortfolioTreesWorker
   */
  public DbPositionMasterWorker getSearchHistoricPortfolioTreesWorker() {
    return _searchHistoricPortfolioTreesWorker;
  }

  /**
   * Sets the searchHistoricPortfolioTreesWorker field.
   * @param searchHistoricPortfolioTreesWorker  the searchHistoricPortfolioTreesWorker
   */
  public void setSearchHistoricPortfolioTreesWorker(DbPositionMasterWorker searchHistoricPortfolioTreesWorker) {
    _searchHistoricPortfolioTreesWorker = searchHistoricPortfolioTreesWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the correctPortfolioTreeWorker field.
   * @return the correctPortfolioTreeWorker
   */
  public DbPositionMasterWorker getCorrectPortfolioTreeWorker() {
    return _correctPortfolioTreeWorker;
  }

  /**
   * Sets the correctPortfolioTreeWorker field.
   * @param correctPortfolioTreeWorker  the correctPortfolioTreeWorker
   */
  public void setCorrectPortfolioTreeWorker(DbPositionMasterWorker correctPortfolioTreeWorker) {
    _correctPortfolioTreeWorker = correctPortfolioTreeWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the searchPositionsWorker field.
   * @return the searchPositionsWorker
   */
  public DbPositionMasterWorker getSearchPositionsWorker() {
    return _searchPositionsWorker;
  }

  /**
   * Sets the searchPositionsWorker field.
   * @param searchPositionsWorker  the searchPositionsWorker
   */
  public void setSearchPositionsWorker(DbPositionMasterWorker searchPositionsWorker) {
    _searchPositionsWorker = searchPositionsWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the getPositionWorker field.
   * @return the getPositionWorker
   */
  public DbPositionMasterWorker getGetPositionWorker() {
    return _getPositionWorker;
  }

  /**
   * Sets the getPositionWorker field.
   * @param getPositionWorker  the getPositionWorker
   */
  public void setGetPositionWorker(DbPositionMasterWorker getPositionWorker) {
    _getPositionWorker = getPositionWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the addPositionWorker field.
   * @return the addPositionWorker
   */
  public DbPositionMasterWorker getAddPositionWorker() {
    return _addPositionWorker;
  }

  /**
   * Sets the addPositionWorker field.
   * @param addPositionWorker  the addPositionWorker
   */
  public void setAddPositionWorker(DbPositionMasterWorker addPositionWorker) {
    _addPositionWorker = addPositionWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the updatePositionWorker field.
   * @return the updatePositionWorker
   */
  public DbPositionMasterWorker getUpdatePositionWorker() {
    return _updatePositionWorker;
  }

  /**
   * Sets the updatePositionWorker field.
   * @param updatePositionWorker  the updatePositionWorker
   */
  public void setUpdatePositionWorker(DbPositionMasterWorker updatePositionWorker) {
    _updatePositionWorker = updatePositionWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the removePositionWorker field.
   * @return the removePositionWorker
   */
  public DbPositionMasterWorker getRemovePositionWorker() {
    return _removePositionWorker;
  }

  /**
   * Sets the removePositionWorker field.
   * @param removePositionWorker  the removePositionWorker
   */
  public void setRemovePositionWorker(DbPositionMasterWorker removePositionWorker) {
    _removePositionWorker = removePositionWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the searchHistoricPositionsWorker field.
   * @return the searchHistoricPositionsWorker
   */
  public DbPositionMasterWorker getSearchHistoricPositionsWorker() {
    return _searchHistoricPositionsWorker;
  }

  /**
   * Sets the searchHistoricPositionsWorker field.
   * @param searchHistoricPositionsWorker  the searchHistoricPositionsWorker
   */
  public void setSearchHistoricPositionsWorker(DbPositionMasterWorker searchHistoricPositionsWorker) {
    _searchHistoricPositionsWorker = searchHistoricPositionsWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the correctPositionWorker field.
   * @return the correctPositionWorker
   */
  public DbPositionMasterWorker getCorrectPositionWorker() {
    return _correctPositionWorker;
  }

  /**
   * Sets the correctPositionWorker field.
   * @param correctPositionWorker  the correctPositionWorker
   */
  public void setCorrectPositionWorker(DbPositionMasterWorker correctPositionWorker) {
    _correctPositionWorker = correctPositionWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the getFullPortfolioWorker field.
   * @return the getFullPortfolioWorker
   */
  public DbPositionMasterWorker getGetFullPortfolioWorker() {
    return _getFullPortfolioWorker;
  }

  /**
   * Sets the getFullPortfolioWorker field.
   * @param getFullPortfolioWorker  the getFullPortfolioWorker
   */
  public void setGetFullPortfolioWorker(DbPositionMasterWorker getFullPortfolioWorker) {
    _getFullPortfolioWorker = getFullPortfolioWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the getFullPortfolioNodeWorker field.
   * @return the getFullPortfolioNodeWorker
   */
  public DbPositionMasterWorker getGetFullPortfolioNodeWorker() {
    return _getFullPortfolioNodeWorker;
  }

  /**
   * Sets the getFullPortfolioNodeWorker field.
   * @param getFullPortfolioNodeWorker  the getFullPortfolioNodeWorker
   */
  public void setGetFullPortfolioNodeWorker(DbPositionMasterWorker getFullPortfolioNodeWorker) {
    _getFullPortfolioNodeWorker = getFullPortfolioNodeWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the getFullPositionWorker field.
   * @return the getFullPositionWorker
   */
  public DbPositionMasterWorker getGetFullPositionWorker() {
    return _getFullPositionWorker;
  }

  /**
   * Sets the getFullPositionWorker field.
   * @param getFullPositionWorker  the getFullPositionWorker
   */
  public void setGetFullPositionWorker(DbPositionMasterWorker getFullPositionWorker) {
    _getFullPositionWorker = getFullPositionWorker;
  }

  //-------------------------------------------------------------------------
  /**
   * Returns a string summary of this position master.
   * @return the string summary, not null
   */
  @Override
  public String toString() {
    return getClass().getSimpleName() + "@" + System.identityHashCode(this);
  }

}
