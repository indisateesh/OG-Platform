/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.timeseries.db;

import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.DEACTIVATE_META_DATA;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.DELETE_DATA_POINT;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.DELETE_TIME_SERIES_BY_ID;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.FIND_DATA_POINT_BY_DATE_AND_ID;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.GET_ACTIVE_META_DATA_BY_OID;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.GET_ACTIVE_META_DATA_BY_PARAMETERS;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.GET_TIME_SERIES_BY_ID;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.GET_TIME_SERIES_KEY;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.GET_TIME_SERIES_KEY_BY_ID;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_DATA_FIELD;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_DATA_PROVIDER;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_DATA_SOURCE;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_DOMAIN;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_IDENTIFIER;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_OBSERVATION_TIME;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_QUOTED_OBJECT;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_TIME_SERIES;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_TIME_SERIES_DELTA_D;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_TIME_SERIES_DELTA_I;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_TIME_SERIES_DELTA_U;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INSERT_TIME_SERIES_KEY;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.INVALID_KEY;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.LOAD_ALL_DATA_FIELDS;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.LOAD_ALL_DATA_PROVIDER;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.LOAD_ALL_DATA_SOURCES;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.LOAD_ALL_IDENTIFIERS;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.LOAD_ALL_OBSERVATION_TIMES;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.LOAD_ALL_SCHEME;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.LOAD_TIME_SERIES_DELTA;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.LOAD_TIME_SERIES_WITH_DATES;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.MILLIS_IN_DAY;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.SELECT_DATA_FIELD_ID;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.SELECT_DATA_PROVIDER_ID;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.SELECT_DATA_SOURCE_ID;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.SELECT_DOMAIN_ID;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.SELECT_OBSERVATION_TIME_ID;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.SELECT_QUOTED_OBJECT_FROM_IDENTIFIERS;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.SELECT_QUOTED_OBJECT_ID;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.SQL_MAP_KEYS;
import static com.opengamma.timeseries.db.TimeSeriesDaoConstants.UPDATE_TIME_SERIES;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.sql.DataSource;
import javax.time.Instant;
import javax.time.calendar.LocalDate;
import javax.time.calendar.format.CalendricalParseException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.opengamma.DataNotFoundException;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.financial.security.db.EnumWithDescriptionBean;
import com.opengamma.id.Identifier;
import com.opengamma.id.IdentifierBundle;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.timeseries.DataFieldBean;
import com.opengamma.timeseries.DataPointDocument;
import com.opengamma.timeseries.DataProviderBean;
import com.opengamma.timeseries.DataSourceBean;
import com.opengamma.timeseries.ObservationTimeBean;
import com.opengamma.timeseries.SchemeBean;
import com.opengamma.timeseries.TimeSeriesDocument;
import com.opengamma.timeseries.TimeSeriesMaster;
import com.opengamma.timeseries.TimeSeriesMetaData;
import com.opengamma.timeseries.TimeSeriesRequest;
import com.opengamma.timeseries.TimeSeriesSearchHistoricRequest;
import com.opengamma.timeseries.TimeSeriesSearchHistoricResult;
import com.opengamma.timeseries.TimeSeriesSearchResult;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.time.DateUtil;
import com.opengamma.util.timeseries.DoubleTimeSeries;
import com.opengamma.util.timeseries.date.DateDoubleTimeSeries;
import com.opengamma.util.timeseries.localdate.ArrayLocalDateDoubleTimeSeries;
import com.opengamma.util.timeseries.localdate.LocalDateDoubleTimeSeries;
import com.opengamma.util.timeseries.sqldate.ArraySQLDateDoubleTimeSeries;
import com.opengamma.util.timeseries.sqldate.MapSQLDateDoubleTimeSeries;
import com.opengamma.util.tuple.ObjectsPair;
import com.opengamma.util.tuple.Pair;

/**
 * 
 * 
 */
public abstract class RowStoreTimeSeriesMaster implements TimeSeriesMaster {
  
  /**
   * The scheme used for UniqueIdentifier objects.
   */
  public static final String IDENTIFIER_SCHEME_DEFAULT = "Tss";

  private static final Logger s_logger = LoggerFactory.getLogger(RowStoreTimeSeriesMaster.class);
  
  private String _identifierScheme = IDENTIFIER_SCHEME_DEFAULT;
  private DataSourceTransactionManager _transactionManager;
  private SimpleJdbcTemplate _simpleJdbcTemplate;
  private Map<String, String> _namedSQLMap;

  public RowStoreTimeSeriesMaster(DataSourceTransactionManager transactionManager, Map<String, String> namedSQLMap) {
    ArgumentChecker.notNull(transactionManager, "transactionManager");
    _transactionManager = transactionManager;
    DataSource dataSource = _transactionManager.getDataSource();
    _simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);   
    checkNamedSQLMap(namedSQLMap);
    _namedSQLMap = Collections.unmodifiableMap(namedSQLMap);
  }
  
  protected abstract boolean isTriggerSupported();
  
  @Override
  public List<Identifier> getAllIdentifiers() {
    List<Identifier> result = _simpleJdbcTemplate.query(_namedSQLMap.get(LOAD_ALL_IDENTIFIERS), new ParameterizedRowMapper<Identifier>() {
      @Override
      public Identifier mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Identifier.of(rs.getString("scheme"), rs.getString("identifier_value"));
      }
    }, new Object[]{});
    return result;
  }

  private UniqueIdentifier addTimeSeries(IdentifierBundle identifiers,
      String dataSource, String dataProvider, String field,
      String observationTime, final LocalDateDoubleTimeSeries timeSeries) {

    DoubleTimeSeries<Date> sqlDateDoubleTimeSeries = timeSeries.toSQLDateDoubleTimeSeries();
    s_logger.debug("adding timeseries for {} with dataSource={}, dataProvider={}, dataField={}, observationTime={} startdate={} endate={}", 
        new Object[]{identifiers, dataSource, dataProvider, field, observationTime, timeSeries.getEarliestTime(), timeSeries.getLatestTime()});
    
    IdentifierBundleRowHandler bundleBean = findBundleBean(identifiers);
    //should return just one bundle id
    if (bundleBean.getIds().size() > 1) {
      s_logger.warn("{} has more than one bundle ids associated to identifiers {}", identifiers);
      throw new OpenGammaRuntimeException(identifiers + " has more than one bundle ids associated to them, can not treat as same instrument");
    }
    long bundleId = INVALID_KEY;
    long tsKey = INVALID_KEY;
    if (bundleBean.getIds().size() == 1) {
      //check there are no timeseries with same metadata
      bundleId = bundleBean.getIds().iterator().next();
      s_logger.debug("Looking up timeSeriesMetaData by quotedObj for identifiers={}, dataSource={}, dataProvider={}, dataField={}, observationTime={}, bundleId={}", 
          new Object[]{identifiers, dataSource, dataProvider, field, observationTime, bundleId});
      
      String sql = _namedSQLMap.get(GET_ACTIVE_META_DATA_BY_PARAMETERS);
      
      MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("bundleId", bundleId, Types.BIGINT)
        .addValue("dataSource", dataSource, Types.VARCHAR)
        .addValue("dataField", field, Types.VARCHAR)
        .addValue("dataProvider", dataProvider, Types.VARCHAR)
        .addValue("observationTime", observationTime, Types.VARCHAR);
      
      List<TimeSeriesMetaData> tsMetaDataList = _simpleJdbcTemplate.query(sql, new TimeSeriesMetaDataRowMapper(), parameters);
      if (!tsMetaDataList.isEmpty()) {
        throw new IllegalArgumentException("cannot add duplicate TimeSeries for identifiers " + identifiers);
      }
    } else {
      Identifier identifier = identifiers.getIdentifiers().iterator().next();
      String bundleName = identifier.getScheme().getName() + "_" + identifier.getValue();
      bundleId = getOrCreateIdentifierBundle(bundleName, bundleName, identifiers);
    }
    tsKey = getOrCreateTimeSeriesKey(bundleId, dataSource, dataProvider, field, observationTime);
    insertDataPoints(sqlDateDoubleTimeSeries, tsKey);
    return UniqueIdentifier.of(_identifierScheme, String.valueOf(tsKey));
  }

  private void insertDataPoints(DoubleTimeSeries<Date> sqlDateDoubleTimeSeries, long tsKey) {
    String insertSQL = _namedSQLMap.get(INSERT_TIME_SERIES);
    String insertDelta = _namedSQLMap.get(INSERT_TIME_SERIES_DELTA_I);
    
    Date now = new Date(System.currentTimeMillis());
    
    SqlParameterSource[] batchArgs = new MapSqlParameterSource[sqlDateDoubleTimeSeries.size()];
    int index = 0;
    
    for (Entry<Date, Double> dataPoint : sqlDateDoubleTimeSeries) {
      Date date = dataPoint.getKey();
      Double value = dataPoint.getValue();
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("timeSeriesID", tsKey, Types.BIGINT);
      parameterSource.addValue("date", date, Types.DATE);
      parameterSource.addValue("value", value, Types.DOUBLE);
      parameterSource.addValue("timeStamp", now, Types.TIMESTAMP);
      batchArgs[index++] = parameterSource;
    }
    
    if (!isTriggerSupported()) {
      _simpleJdbcTemplate.batchUpdate(insertDelta, batchArgs);
    } 
    _simpleJdbcTemplate.batchUpdate(insertSQL, batchArgs);
  }

  private long getOrCreateTimeSeriesKey(long quotedObjId, String dataSource, String dataProvider, String field, String observationTime) {
    long timeSeriesKeyID = getTimeSeriesKey(quotedObjId, dataSource, dataProvider, field, observationTime);
    if (timeSeriesKeyID == INVALID_KEY) {
      timeSeriesKeyID = createTimeSeriesKey(quotedObjId, dataSource, dataProvider, field, observationTime);
    }
    return timeSeriesKeyID;
  }

  private long createDataProvider(String dataProvider, String description) {
    String sql = _namedSQLMap.get(INSERT_DATA_PROVIDER);
    insertNamedDimension(sql, dataProvider, description);
    return getDataProviderId(dataProvider);
  }

  private void insertNamedDimension(String sql, String name, String description) {
    s_logger.debug("running sql={} with values({}, {})", new Object[]{sql, name, description});
    SqlParameterSource parameters = new MapSqlParameterSource()
      .addValue("name", name, Types.VARCHAR)
      .addValue("description", description, Types.VARCHAR);
    _simpleJdbcTemplate.update(sql, parameters);
  }

  private long createDataSource(String dataSource, String description) {
    String sql = _namedSQLMap.get(INSERT_DATA_SOURCE);
    insertNamedDimension(sql, dataSource, description);
    return getDataSourceId(dataSource);
  }

  private IdentifierBundleRowHandler findBundleBean(final IdentifierBundle identifiers) {
    IdentifierBundleRowHandler result = new IdentifierBundleRowHandler();
    String namedSql = _namedSQLMap.get(SELECT_QUOTED_OBJECT_FROM_IDENTIFIERS);
    StringBuilder bundleWhereCondition = new StringBuilder(" ");
    int orCounter = 1;
    Object[] parameters = new Object[identifiers.size() * 2];
    int paramIndex = 0;
    for (Identifier identifier : identifiers.getIdentifiers()) {
      bundleWhereCondition.append("(d.name = ? AND dsi.identifier_value = ?)");
      parameters[paramIndex++] = identifier.getScheme().getName();
      parameters[paramIndex++] = identifier.getValue();
      if (orCounter++ != identifiers.size()) {
        bundleWhereCondition.append(" OR ");
      }
    }
    bundleWhereCondition.append(" ");
    
    String findIdentifiersSql = StringUtils.replace(namedSql, ":identifierBundleClause", bundleWhereCondition.toString());
    
    JdbcOperations jdbcOperations = _simpleJdbcTemplate.getJdbcOperations();
    jdbcOperations.query(findIdentifiersSql, parameters, result);
    
    return result;
    
  }

  private long createObservationTime(String observationTime, String description) {
    String sql = _namedSQLMap.get(INSERT_OBSERVATION_TIME);
    insertNamedDimension(sql, observationTime, description);
    return getObservationTimeId(observationTime);
  }

  private long createBundle(String name, String description) {
    String sql = _namedSQLMap.get(INSERT_QUOTED_OBJECT);
    insertNamedDimension(sql, name, description);
    return getBundleId(name);
  }

  private long createDataField(String field, String description) {
    String sql = _namedSQLMap.get(INSERT_DATA_FIELD);
    insertNamedDimension(sql, field, description);
    return getDataFieldId(field);
  }

  private long getDataProviderId(String name) {
    s_logger.debug("looking up id for dataProvider={}", name);
    String sql = _namedSQLMap.get(SELECT_DATA_PROVIDER_ID);
    return getNamedDimensionId(sql, name);
  }

  private long getNamedDimensionId(final String sql, final String name) {
    s_logger.debug("looking up id from sql={} with name={}", sql, name);
    SqlParameterSource parameters = new MapSqlParameterSource().addValue("name", name);

    long result = INVALID_KEY;
    try {
      result = _simpleJdbcTemplate.queryForInt(sql, parameters);
    } catch (EmptyResultDataAccessException e) {
      s_logger.debug("Empty row return for name = {} from sql = {}", name, sql);
      result = INVALID_KEY;
    }
    s_logger.debug("id = {}", result);
    return result;
  }

  private long getDataSourceId(String name) {
    s_logger.debug("looking up id for dataSource={}", name);
    String sql = _namedSQLMap.get(SELECT_DATA_SOURCE_ID);
    return getNamedDimensionId(sql, name);
  }

  private long getDataFieldId(String name) {
    s_logger.debug("looking up id for dataField={}", name);
    String sql = _namedSQLMap.get(SELECT_DATA_FIELD_ID);
    return getNamedDimensionId(sql, name);
  }

  private long getObservationTimeId(String name) {
    s_logger.debug("looking up id for observationTime={}", name);
    String sql = _namedSQLMap.get(SELECT_OBSERVATION_TIME_ID);
    return getNamedDimensionId(sql, name);
  }

  private long getBundleId(String name) {
    s_logger.debug("looking up id for bundle={}", name);
    String sql = _namedSQLMap.get(SELECT_QUOTED_OBJECT_ID);
    return getNamedDimensionId(sql, name);
  }
  
  private long getSchemeId(String name) {
    s_logger.debug("looking up id for domain={}", name);
    String sql = _namedSQLMap.get(SELECT_DOMAIN_ID);
    return getNamedDimensionId(sql, name);
  }
  
  private long createScheme(String scheme, String description) {
    String sql = _namedSQLMap.get(INSERT_DOMAIN);
    insertNamedDimension(sql, scheme, description);
    return getSchemeId(scheme);
  }
  
  private long createTimeSeriesKey(long bundleId, String dataSource,
      String dataProvider, String dataField, String observationTime) {
    
    s_logger.debug("creating timeSeriesKey with quotedObjId={}, dataSource={}, dataProvider={}, dataField={}, observationTime={}", 
        new Object[]{bundleId, dataSource, dataProvider, dataField, observationTime});
    
    DataSourceBean dataSourceBean = getOrCreateDataSource(dataSource, null);
    DataProviderBean dataProviderBean = getOrCreateDataProvider(dataProvider, null);
    DataFieldBean dataFieldBean = getOrCreateDataField(dataField, null);
    ObservationTimeBean observationTimeBean = getOrCreateObservationTime(observationTime, null);
    
    String sql = _namedSQLMap.get(INSERT_TIME_SERIES_KEY);
    
    SqlParameterSource parameterSource = new MapSqlParameterSource()
      .addValue("bundleId", bundleId)
      .addValue("dataSourceId", dataSourceBean.getId())
      .addValue("dataProviderId", dataProviderBean.getId())
      .addValue("dataFieldId", dataFieldBean.getId())
      .addValue("observationTimeId", observationTimeBean.getId());
    
    _simpleJdbcTemplate.update(sql, parameterSource);
    
    return getTimeSeriesKey(bundleId, dataSourceBean.getId(), dataProviderBean.getId(), dataFieldBean.getId(), observationTimeBean.getId());
  }
 
  private long getTimeSeriesKey(long quotedObjId, String dataSource, String dataProvider, String dataField, String observationTime) {
    long result = INVALID_KEY;
    s_logger.debug("looking up timeSeriesKey quotedObjId={}, dataSource={}, dataProvider={}, dataField={}, observationTime={}", 
        new Object[]{quotedObjId, dataSource, dataProvider, dataField, observationTime});
    String sql = _namedSQLMap.get(GET_TIME_SERIES_KEY);
    SqlParameterSource parameterSource = new MapSqlParameterSource()
      .addValue("bundleId", quotedObjId, Types.BIGINT)
      .addValue("dataSource", dataSource, Types.VARCHAR)
      .addValue("dataProvider", dataProvider, Types.VARCHAR)
      .addValue("dataField", dataField, Types.VARCHAR)
      .addValue("observationTime", observationTime, Types.VARCHAR)
      .addValue("quotedObject", null);
      
    try {
      result = _simpleJdbcTemplate.queryForInt(sql, parameterSource);
    } catch (EmptyResultDataAccessException e) {
      s_logger.debug("Empty row returned for timeSeriesKeyID");
      result = INVALID_KEY;
    }
    s_logger.debug("timeSeriesKeyID = {}", result);
    return result;
  }
  
  private long getTimeSeriesKey(long quotedObjId, long dataSourceId, long dataProviderId, long dataFieldId, long observationTimeId) {
    long result = INVALID_KEY;
    s_logger.debug("looking up timeSeriesKey quotedObjId={}, dataSourceId={}, dataProviderId={}, dataFieldId={}, observationTimeId={}", 
        new Object[]{quotedObjId, dataSourceId, dataProviderId, dataFieldId, observationTimeId});
    String sql = _namedSQLMap.get(GET_TIME_SERIES_KEY_BY_ID);
    SqlParameterSource parameterSource = new MapSqlParameterSource()
      .addValue("qoid", quotedObjId, Types.BIGINT)
      .addValue("dsid", dataSourceId, Types.BIGINT)
      .addValue("dpid", dataProviderId, Types.BIGINT)
      .addValue("dfid", dataFieldId, Types.BIGINT)
      .addValue("otid", observationTimeId, Types.BIGINT);
    try {
      result = _simpleJdbcTemplate.queryForInt(sql, parameterSource);
    } catch (EmptyResultDataAccessException e) {
      s_logger.debug("Empty row returned for timeSeriesKeyID");
      result = INVALID_KEY;
    }
    s_logger.debug("timeSeriesKeyID = {}", result);
    return result;
  }
  
  private void deleteDataPoints(long tsId) {
    
    s_logger.debug("deleting timeseries with id = {}", tsId);
    
    String deleteSql = _namedSQLMap.get(DELETE_TIME_SERIES_BY_ID);
    MapSqlParameterSource tsIDParameter = new MapSqlParameterSource().addValue("tsID", tsId, Types.BIGINT);
    
    if (!isTriggerSupported()) {
      String selectTSSQL = _namedSQLMap.get(GET_TIME_SERIES_BY_ID);
      List<Pair<Date, Double>> queryResult = _simpleJdbcTemplate.query(selectTSSQL, new ParameterizedRowMapper<Pair<Date, Double>>() {
  
        @Override
        public Pair<Date, Double> mapRow(ResultSet rs, int rowNum) throws SQLException {
          double tsValue = rs.getDouble("value");
          Date tsDate = rs.getDate("ts_date");
          return Pair.of(tsDate, tsValue);
        }
      }, tsIDParameter);
      
      String insertDelta = _namedSQLMap.get(INSERT_TIME_SERIES_DELTA_D);
      Date now = new Date(System.currentTimeMillis());
      SqlParameterSource[] batchArgs = new MapSqlParameterSource[queryResult.size()];
      int i = 0;
      for (Pair<Date, Double> pair : queryResult) {
        Date date = pair.getFirst();
        Double value = pair.getSecond();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("timeSeriesID", tsId, Types.BIGINT);
        parameterSource.addValue("date", date, Types.DATE);
        parameterSource.addValue("value", value, Types.DOUBLE);
        parameterSource.addValue("timeStamp", now, Types.TIMESTAMP);
        batchArgs[i++] = parameterSource;
      }
      
      _simpleJdbcTemplate.batchUpdate(insertDelta, batchArgs);
    }
      
    _simpleJdbcTemplate.update(deleteSql, tsIDParameter);
    
  }
    
  private DoubleTimeSeries<Date> loadTimeSeries(long timeSeriesKey, LocalDate start, LocalDate end) {
    MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("timeSeriesKey", timeSeriesKey, Types.INTEGER);
    parameters.addValue("startDate", start != null ? toSQLDate(start) : null, Types.DATE);
    parameters.addValue("endDate", end != null ? toSQLDate(end) : null, Types.DATE);
    
    final List<Date> dates = new LinkedList<Date>();
    final List<Double> values = new LinkedList<Double>();
    
    NamedParameterJdbcOperations parameterJdbcOperations = _simpleJdbcTemplate.getNamedParameterJdbcOperations();
    parameterJdbcOperations.query(_namedSQLMap.get(LOAD_TIME_SERIES_WITH_DATES), parameters, new RowCallbackHandler() {
      
      @Override
      public void processRow(ResultSet rs) throws SQLException {
        values.add(rs.getDouble("value"));
        dates.add(rs.getDate("ts_date"));
      }
    });
    
    return new ArraySQLDateDoubleTimeSeries(dates, values);
  }

  private void updateDataPoint(LocalDate date, Double value, long tsID) {
    String selectSQL = _namedSQLMap.get(FIND_DATA_POINT_BY_DATE_AND_ID);
    
    MapSqlParameterSource parameters = new MapSqlParameterSource()
      .addValue("tsID", tsID, Types.BIGINT)
      .addValue("date", toSQLDate(date), Types.DATE);
    
    Double oldValue = _simpleJdbcTemplate.queryForObject(selectSQL, Double.class, parameters);
    
    String updateSql = _namedSQLMap.get(UPDATE_TIME_SERIES);
    String insertDelta = _namedSQLMap.get(INSERT_TIME_SERIES_DELTA_U);
    
    Date now = new Date(System.currentTimeMillis());
    
    parameters.addValue("timeStamp", now, Types.TIMESTAMP);
    parameters.addValue("oldValue", oldValue, Types.DOUBLE);
    parameters.addValue("newValue", value, Types.DOUBLE);
    
    if (!isTriggerSupported()) {
      _simpleJdbcTemplate.update(insertDelta, parameters);
    }
    _simpleJdbcTemplate.update(updateSql, parameters);
  }
  
  private void removeDataPoint(long tsID, Date sqlDate) {
    String selectTSSQL = _namedSQLMap.get(FIND_DATA_POINT_BY_DATE_AND_ID);
    MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("tsID", tsID, Types.INTEGER);
    parameters.addValue("date", sqlDate, Types.DATE);
    
    Double oldValue = _simpleJdbcTemplate.queryForObject(selectTSSQL, Double.class, parameters);
    
    String deleteSql = _namedSQLMap.get(DELETE_DATA_POINT);
    String insertDelta = _namedSQLMap.get(INSERT_TIME_SERIES_DELTA_D);
    
    Date now = new Date(System.currentTimeMillis());
    
    MapSqlParameterSource deltaParameters = new MapSqlParameterSource();
    deltaParameters.addValue("timeSeriesID", tsID, Types.INTEGER);
    deltaParameters.addValue("date", sqlDate, Types.DATE);
    deltaParameters.addValue("value", oldValue, Types.DOUBLE);
    deltaParameters.addValue("timeStamp", now, Types.TIMESTAMP);
    
    if (!isTriggerSupported()) {
      _simpleJdbcTemplate.update(insertDelta, deltaParameters);
    }
    _simpleJdbcTemplate.update(deleteSql, parameters);
  }
  
//  protected Pair<UniqueIdentifier, LocalDateDoubleTimeSeries> getHistoricalTimeSeries(IdentifierBundle identifiers, String dataSource,
//      String dataProvider, String field) {
//    return getHistoricalTimeSeries(identifiers, dataSource, dataProvider, field, null, null);
//  }
//
//  protected Pair<UniqueIdentifier, LocalDateDoubleTimeSeries> getHistoricalTimeSeries(IdentifierBundle identifiers, String dataSource,
//      String dataProvider, String field, LocalDate start, LocalDate end) {
//    validateMetaData(identifiers, dataSource, dataProvider, field);
//    
//    TimeSeriesRequest request = new TimeSeriesRequest();
//    request.setIdentifiers(identifiers);
//    request.setDataSource(dataSource);
//    request.setDataProvider(dataProvider);
//    request.setDataField(field);
//    request.setStart(start);
//    request.setEnd(end);
//    request.setLoadTimeSeries(true);
//    
//    LocalDateDoubleTimeSeries timeseries = new ArrayLocalDateDoubleTimeSeries();
//    TimeSeriesSearchResult searchResult = searchTimeSeries(request);
//    List<TimeSeriesDocument> documents = searchResult.getDocuments();
//    UniqueIdentifier uid = null;
//    if (!documents.isEmpty()) {
//      if (documents.size() > 1) {
//        Object[] param = new Object[]{identifiers, dataSource, dataProvider, field, start, end};
//        s_logger.warn("multiple timeseries return for identifiers={}, dataSource={}, dataProvider={}, dataField={}, start={} end={}", param);
//      }
//      TimeSeriesDocument timeSeriesDocument = documents.get(0);
//      timeseries = timeSeriesDocument.getTimeSeries();
//      uid = timeSeriesDocument.getUniqueIdentifier();
//    }
//    return new ObjectsPair<UniqueIdentifier, LocalDateDoubleTimeSeries>(uid, timeseries);
//  }

  /**
   * @param identifiers
   * @param dataSource
   * @param dataProvider
   * @param field
   */
  private void validateMetaData(IdentifierBundle identifiers, String dataSource, String dataProvider, String field) {
    ArgumentChecker.notNull(identifiers, "identifiers");
    ArgumentChecker.notNull(dataSource, "dataSource");
    ArgumentChecker.notNull(dataProvider, "dataProvider");
    ArgumentChecker.notNull(field, "field");
  }
  
  private LocalDateDoubleTimeSeries getTimeSeriesSnapshot(Instant timeStamp, long tsID) {
    String selectDeltaSql = _namedSQLMap.get(LOAD_TIME_SERIES_DELTA);
    
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    parameterSource.addValue("time", new Date(timeStamp.toEpochMillisLong()), Types.TIMESTAMP);
    parameterSource.addValue("tsID", tsID, Types.BIGINT);

    final List<Date> deltaDates = new ArrayList<Date>();
    final List<Double> deltaValues = new ArrayList<Double>();
    final List<String> deltaOperations = new ArrayList<String>();
    
    NamedParameterJdbcOperations jdbcOperations = _simpleJdbcTemplate.getNamedParameterJdbcOperations();
    
    jdbcOperations.query(selectDeltaSql, parameterSource, new RowCallbackHandler() {
      @Override
      public void processRow(ResultSet rs) throws SQLException {
        deltaDates.add(rs.getDate("ts_date"));
        deltaValues.add(rs.getDouble("old_value"));
        deltaOperations.add(rs.getString("operation"));
      }
    });
    
    DoubleTimeSeries<Date> timeSeries = loadTimeSeries(tsID, null, null);
    
    MapSQLDateDoubleTimeSeries tsMap = new MapSQLDateDoubleTimeSeries(timeSeries);
    
    //reapply deltas
    for (int i = 0; i < deltaDates.size(); i++) {
      Date date = deltaDates.get(i);
      Double oldValue = deltaValues.get(i);
      String operation = deltaOperations.get(i);
      if (operation.toUpperCase().equals("I")) {
        tsMap.removeDataPoint(date);
      }
      if (operation.toUpperCase().equals("D") || operation.toUpperCase().equals("U")) {
        tsMap.putDataPoint(date, oldValue);
      }
    }
    
    return tsMap.toLocalDateDoubleTimeSeries();
  }
  
  private Date toSQLDate(LocalDate localDate) {
    return new Date(localDate.toEpochDays() * MILLIS_IN_DAY);
  }
    
  /**
   * @param namedSQLMap the map containing sql queries
   */
  protected void checkNamedSQLMap(Map<String, String> namedSQLMap) {
    ArgumentChecker.notNull(namedSQLMap, "namedSQLMap");
    for (String queryName : SQL_MAP_KEYS) {
      checkSQLQuery(queryName, namedSQLMap);
    }
  }

  private void checkSQLQuery(String key, Map<String, String> namedSQLMap) {
    if (StringUtils.isBlank(namedSQLMap.get(key))) {
      s_logger.warn(key + " query is missing from injected SQLMap when creating " + getClass());
      throw new IllegalArgumentException(key + " query is missing from injected SQLMap when creating " + getClass());
    }
  }

  protected LocalDateDoubleTimeSeries getHistoricalTimeSeries(UniqueIdentifier uid, LocalDate start, LocalDate end) {
    ArgumentChecker.notNull(uid, "UniqueIdentifier");
    ArgumentChecker.isTrue(uid.getScheme().equals(_identifierScheme), "Uid not for TimeSeriesStorage");
    ArgumentChecker.isTrue(uid.getValue() != null, "Uid value cannot be null");
    int timeSeriesKey = Integer.parseInt(uid.getValue());
    DoubleTimeSeries<Date> timeSeries = loadTimeSeries(timeSeriesKey, start, end);
    return timeSeries.toLocalDateDoubleTimeSeries();
  }

  protected LocalDateDoubleTimeSeries getHistoricalTimeSeries(UniqueIdentifier uid) {
    return getHistoricalTimeSeries(uid, null, null);
  }

  @Override
  public TimeSeriesDocument addTimeSeries(TimeSeriesDocument document) {
    validateTimeSeriesDocument(document);
    
    IdentifierBundle identifier = document.getIdentifiers();
    String dataSource = document.getDataSource();
    String dataProvider = document.getDataProvider();
    String field = document.getDataField();
    String observationTime = document.getObservationTime();
    LocalDateDoubleTimeSeries timeSeries = document.getTimeSeries();
    
    UniqueIdentifier uid = addTimeSeries(identifier, dataSource, dataProvider, field, observationTime, timeSeries);
    document.setUniqueIdentifier(uid);
    return document;
  }
 
  @Override
  public void appendTimeSeries(TimeSeriesDocument document) {
    validateTimeSeriesDocument(document);
    Long tsId = validateAndGetTimeSeriesId(document.getUniqueIdentifier());
    insertDataPoints(document.getTimeSeries().toSQLDateDoubleTimeSeries(), tsId);
  }

  private void validateTimeSeriesDocument(TimeSeriesDocument document) {
    ArgumentChecker.notNull(document, "timeseries document");
//    ArgumentChecker.isTrue(document.getTimeSeries() != null && !document.getTimeSeries().isEmpty(), "cannot add null/empty timeseries");
    ArgumentChecker.notNull(document.getTimeSeries(), "Timeseries");
    ArgumentChecker.isTrue(document.getIdentifiers() != null && !document.getIdentifiers().getIdentifiers().isEmpty(), "cannot add timeseries with empty identifiers");
    ArgumentChecker.isTrue(!StringUtils.isBlank(document.getDataSource()), "cannot add timeseries with blank dataSource");
    ArgumentChecker.isTrue(!StringUtils.isBlank(document.getDataProvider()), "cannot add timeseries with blank dataProvider");
    ArgumentChecker.isTrue(!StringUtils.isBlank(document.getDataProvider()), "cannot add timeseries with blank dataProvider");
    ArgumentChecker.isTrue(!StringUtils.isBlank(document.getDataField()), "cannot add timeseries with blank field");
    ArgumentChecker.isTrue(!StringUtils.isBlank(document.getObservationTime()), "cannot add timeseries with blank observationTime");
    ArgumentChecker.isTrue(!StringUtils.isBlank(document.getDataProvider()), "cannot add timeseries with blank dataProvider");
  }
  
  private TimeSeriesMetaData getTimeSeriesMetaData(long tsId) {
    
    TimeSeriesMetaData result = new TimeSeriesMetaData();
    
    final Set<Identifier> identifiers = new HashSet<Identifier>();
    final Set<String> dataSourceSet = new HashSet<String>();
    final Set<String> dataProviderSet = new HashSet<String>();
    final Set<String> dataFieldSet = new HashSet<String>();
    final Set<String> observationTimeSet = new HashSet<String>();
    final Set<Long> tsKeySet = new HashSet<Long>();
    
    String sql = _namedSQLMap.get(GET_ACTIVE_META_DATA_BY_OID);
    MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("oid", tsId, Types.BIGINT);
    NamedParameterJdbcOperations parameterJdbcOperations = _simpleJdbcTemplate.getNamedParameterJdbcOperations();
    parameterJdbcOperations.query(sql, parameters, new RowCallbackHandler() {
      @Override
      public void processRow(ResultSet rs) throws SQLException {
        String scheme = rs.getString("scheme");
        String value = rs.getString("value");
        Identifier identifier = Identifier.of(scheme, value);
        identifiers.add(identifier);
        dataSourceSet.add(rs.getString("dataSource"));
        dataProviderSet.add(rs.getString("dataProvider"));
        dataFieldSet.add(rs.getString("dataField"));
        observationTimeSet.add(rs.getString("observationTime"));
        tsKeySet.add(rs.getLong("tsKey"));
      }
    });
    
    if (tsKeySet.isEmpty()) {
      s_logger.debug("TimeSeries not found id: {}", tsId);
      throw new DataNotFoundException("TimeSeries not found id: " + tsId);
    }
    
    IdentifierBundle identifierBundle = new IdentifierBundle(identifiers);
    
    result.setIdentifiers(identifierBundle);
    assert (dataFieldSet.size() == 1);
    result.setDataField(dataFieldSet.iterator().next());
    assert (dataProviderSet.size() == 1);
    result.setDataProvider(dataProviderSet.iterator().next());
    assert (dataSourceSet.size() == 1);
    result.setDataSource(dataSourceSet.iterator().next());
    assert (observationTimeSet.size() == 1);
    result.setObservationTime(observationTimeSet.iterator().next());
    assert (tsKeySet.size() == 1);
    
    return result;
    
  }

  @Override
  public TimeSeriesDocument getTimeSeries(UniqueIdentifier uid) {
    Long tsId = validateAndGetTimeSeriesId(uid);
    
    TimeSeriesDocument result = new TimeSeriesDocument();
    result.setUniqueIdentifier(uid);
    
    TimeSeriesMetaData metaData = getTimeSeriesMetaData(tsId);
    
    result.setIdentifiers(metaData.getIdentifiers());
    result.setDataField(metaData.getDataField());
    result.setDataProvider(metaData.getDataProvider());
    result.setDataSource(metaData.getDataSource());
    result.setObservationTime(metaData.getObservationTime());
    DoubleTimeSeries<Date> timeSeries = loadTimeSeries(tsId, null, null);
    result.setTimeSeries(timeSeries.toLocalDateDoubleTimeSeries());
    
    return result;
  }
  
  private ObjectsPair<Long, LocalDate> validateAndGetDataPointId(UniqueIdentifier uid) {
    ArgumentChecker.notNull(uid, "DataPoint UID");
    ArgumentChecker.isTrue(uid.getScheme().equals(_identifierScheme), "UID not TSS");
    ArgumentChecker.isTrue(uid.getValue() != null, "Uid value cannot be null");
    String[] tokens = StringUtils.split(uid.getValue(), '-');
    if (tokens.length != 2) {
      throw new IllegalArgumentException("UID not expected format<12345-yyyymmdd> " + uid);
    }
    String id = tokens[0];
    String date = tokens[1];
    LocalDate localDate = null;
    Long tsId = Long.MIN_VALUE;
    if (id != null && date != null) {
      try {
        localDate = DateUtil.toLocalDate(date);
      } catch (CalendricalParseException ex) {
        throw new IllegalArgumentException("UID not expected format<12345-yyyymmdd> " + uid);
      }
      try {
        tsId = Long.parseLong(id);
      } catch (NumberFormatException ex) {
        throw new IllegalArgumentException("UID not expected format<12345-yyyymmdd> " + uid);
      }
    } else {
      throw new IllegalArgumentException("UID not expected format<12345-yyyymmdd> " + uid);
    }
    return ObjectsPair.of(tsId, localDate);
  }

  /**
   * @param uid
   * @return
   */
  private Long validateAndGetTimeSeriesId(UniqueIdentifier uid) {
    ArgumentChecker.notNull(uid, "TimeSeries UID");
    ArgumentChecker.isTrue(uid.getScheme().equals(_identifierScheme), "UID not TSS");
    ArgumentChecker.isTrue(uid.getValue() != null, "Uid value cannot be null");
    
    Long tsId = Long.MIN_VALUE;
    
    try {
      tsId = Long.parseLong(uid.getValue());
    } catch (NumberFormatException ex) {
      s_logger.warn("Invalid UID {}", uid);
      throw new IllegalArgumentException("Invalid UID " + uid);
    }
    return tsId;
  }

  @Override
  public List<DataFieldBean> getDataFields() {
    List<DataFieldBean> result = new ArrayList<DataFieldBean>();
    for (EnumWithDescriptionBean bean : loadEnumWithDescription(_namedSQLMap.get(LOAD_ALL_DATA_FIELDS))) {
      DataFieldBean dataField = new DataFieldBean(bean.getName(), bean.getDescription());
      dataField.setId(bean.getId());
      result.add(dataField);
    }
    return result;
  }

  @Override
  public List<DataProviderBean> getDataProviders() {
    List<DataProviderBean> result = new ArrayList<DataProviderBean>();
    for (EnumWithDescriptionBean bean : loadEnumWithDescription(_namedSQLMap.get(LOAD_ALL_DATA_PROVIDER))) {
      DataProviderBean dataProviderBean = new DataProviderBean(bean.getName(), bean.getDescription());
      dataProviderBean.setId(bean.getId());
      result.add(dataProviderBean);
    }
    return result;
  }

  @Override
  public List<DataSourceBean> getDataSources() {
    List<DataSourceBean> result = new ArrayList<DataSourceBean>();
    for (EnumWithDescriptionBean bean : loadEnumWithDescription(_namedSQLMap.get(LOAD_ALL_DATA_SOURCES))) {
      DataSourceBean dataSourceBean = new DataSourceBean(bean.getName(), bean.getDescription());
      dataSourceBean.setId(bean.getId());
      result.add(dataSourceBean);
    }
    return result;
  }

  @Override
  public List<ObservationTimeBean> getObservationTimes() {
    List<ObservationTimeBean> result = new ArrayList<ObservationTimeBean>();
    for (EnumWithDescriptionBean bean : loadEnumWithDescription(_namedSQLMap.get(LOAD_ALL_OBSERVATION_TIMES))) {
      ObservationTimeBean obBean = new ObservationTimeBean(bean.getName(), bean.getDescription());
      obBean.setId(bean.getId());
      result.add(obBean);
    }
    return result;
  }
  
  

  @Override
  public List<SchemeBean> getSchemes() {
    List<SchemeBean> result = new ArrayList<SchemeBean>();
    for (EnumWithDescriptionBean bean : loadEnumWithDescription(_namedSQLMap.get(LOAD_ALL_SCHEME))) {
      SchemeBean schemeBean = new SchemeBean(bean.getName(), bean.getDescription());
      schemeBean.setId(bean.getId());
      result.add(schemeBean);
    }
    return result;
  }

  @Override
  public DataFieldBean getOrCreateDataField(String field, String description) {
    long id = getDataFieldId(field);
    if (id == INVALID_KEY) {
      id = createDataField(field, description);
    }
    DataFieldBean result = new DataFieldBean(field, description);
    result.setId(id);
    return result;
  }

  @Override
  public DataProviderBean getOrCreateDataProvider(String dataProvider, String description) {
    long id = getDataProviderId(dataProvider);
    if (id == INVALID_KEY) {
      id = createDataProvider(dataProvider, description);
    }
    DataProviderBean result = new DataProviderBean(dataProvider, description);
    result.setId(id);
    return result;
  }

  @Override
  public DataSourceBean getOrCreateDataSource(String dataSource, String description) {
    long id = getDataSourceId(dataSource);
    if (id == INVALID_KEY) {
      id = createDataSource(dataSource, description);
    }
    DataSourceBean result = new DataSourceBean(dataSource, description);
    result.setId(id);
    return result;
  }

  @Override
  public SchemeBean getOrCreateScheme(String scheme, String description) {
    long id = getSchemeId(scheme);
    if (id == INVALID_KEY) {
      id = createScheme(scheme, description);
    }
    SchemeBean result = new SchemeBean(scheme, description);
    result.setId(id);
    return result;
  }

  @Override
  public ObservationTimeBean getOrCreateObservationTime(String observationTime, String description) {
    long id = getObservationTimeId(observationTime);
    if (id == INVALID_KEY) {
      id = createObservationTime(observationTime, null);
    }
    ObservationTimeBean result = new ObservationTimeBean(observationTime, description);
    result.setId(id);
    return result;
  }

  @Override
  public void removeTimeSeries(UniqueIdentifier uid) {
    Long tsId = validateAndGetTimeSeriesId(uid);
    SqlParameterSource parameters = new MapSqlParameterSource()
      .addValue("tsKey", tsId, Types.BIGINT);
    _simpleJdbcTemplate.update(_namedSQLMap.get(DEACTIVATE_META_DATA), parameters);
    deleteDataPoints(tsId);
  }

  @Override
  public TimeSeriesSearchResult searchTimeSeries(TimeSeriesRequest request) {
    ArgumentChecker.notNull(request, "timeseries request");
    
    TimeSeriesSearchResult result = new TimeSeriesSearchResult();
    UniqueIdentifier uid = request.getTimeSeriesId();
    if (uid != null) {
      long tsId = validateAndGetTimeSeriesId(uid);
      TimeSeriesMetaData tsMetaData = getTimeSeriesMetaData(tsId);
      s_logger.debug("tsMetaData={}", tsMetaData);
      TimeSeriesDocument document = new TimeSeriesDocument();
      document.setDataField(tsMetaData.getDataField());
      document.setDataProvider(tsMetaData.getDataProvider());
      document.setDataSource(tsMetaData.getDataSource());
      document.setIdentifiers(tsMetaData.getIdentifiers());
      document.setObservationTime(tsMetaData.getObservationTime());
      document.setUniqueIdentifier(uid);
      if (request.isLoadTimeSeries()) {
        LocalDateDoubleTimeSeries timeSeries = loadTimeSeries(tsId, request.getStart(), request.getEnd()).toLocalDateDoubleTimeSeries();
        document.setTimeSeries(timeSeries);
      }
      result.getDocuments().add(document);
    } else {
      ArgumentChecker.isTrue(request.getIdentifiers() != null && !request.getIdentifiers().getIdentifiers().isEmpty(), "cannot search timeseries with empty identifiers");
      IdentifierBundleRowHandler bundleBean = findBundleBean(request.getIdentifiers());
      if (!bundleBean.getIds().isEmpty()) {
        if (bundleBean.getIds().size() > 1) {
          s_logger.warn("IdentifierBundle {} not associated to the same instrument", request.getIdentifiers().toString());
        } else {
          long bundleId = bundleBean.getIds().iterator().next();
          IdentifierBundle identifiers = new IdentifierBundle(bundleBean.getIdentifiers());
          String dataSource = request.getDataSource();
          String dataProvider = request.getDataProvider();
          String dataField = request.getDataField();
          String observationTime = request.getObservationTime();
          
          s_logger.debug("Looking up timeSeriesMetaData by quotedObj for identifiers={}, dataSource={}, dataProvider={}, dataField={}, observationTime={}, bundleId={}", 
              new Object[]{identifiers, dataSource, dataProvider, dataField, observationTime, bundleId});
          
          String sql = _namedSQLMap.get(GET_ACTIVE_META_DATA_BY_PARAMETERS);
          
          MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("bundleId", bundleId, Types.BIGINT)
            .addValue("dataSource", dataSource, Types.VARCHAR)
            .addValue("dataField", dataField, Types.VARCHAR)
            .addValue("dataProvider", dataProvider, Types.VARCHAR)
            .addValue("observationTime", observationTime, Types.VARCHAR);
          
          List<TimeSeriesMetaData> tsMetaDataList = _simpleJdbcTemplate.query(sql, new TimeSeriesMetaDataRowMapper(), parameters);
          for (TimeSeriesMetaData tsMetaData : tsMetaDataList) {
            TimeSeriesDocument document = new TimeSeriesDocument();
            long timeSeriesKey = tsMetaData.getTimeSeriesId();
            document.setDataField(tsMetaData.getDataField());
            document.setDataProvider(tsMetaData.getDataProvider());
            document.setDataSource(tsMetaData.getDataSource());
            document.setIdentifiers(identifiers);
            document.setObservationTime(tsMetaData.getObservationTime());
            document.setUniqueIdentifier(UniqueIdentifier.of(IDENTIFIER_SCHEME_DEFAULT, String.valueOf(tsMetaData.getTimeSeriesId())));
            if (request.isLoadTimeSeries()) {
              LocalDateDoubleTimeSeries timeSeries = loadTimeSeries(timeSeriesKey, request.getStart(), request.getEnd()).toLocalDateDoubleTimeSeries();
              document.setTimeSeries(timeSeries);
            }
            result.getDocuments().add(document);
          }
        }
      }
    }
    return result;
  }

  @Override
  public TimeSeriesSearchHistoricResult searchHistoric(TimeSeriesSearchHistoricRequest request) {
    ArgumentChecker.notNull(request, "TimeSeriesSearchHistoricRequest");
    ArgumentChecker.notNull(request.getTimeStamp(), "Timestamp");
    UniqueIdentifier uid = request.getTimeSeriesId();
    TimeSeriesSearchHistoricResult searchResult = new TimeSeriesSearchHistoricResult();
    if (uid == null) {
      validateSearchHistoricRequest(request);
      String dataProvider = request.getDataProvider();
      String dataSource = request.getDataSource();
      String field = request.getDataField();
      IdentifierBundle identifiers = request.getIdentifiers();
      uid = resolveIdentifier(identifiers, dataSource, dataProvider, field);
      if (uid == null) {
        return searchResult;
      }
    }
    Instant timeStamp = request.getTimeStamp();
    long tsId = validateAndGetTimeSeriesId(uid);
    LocalDateDoubleTimeSeries seriesSnapshot = getTimeSeriesSnapshot(timeStamp, tsId);
    TimeSeriesDocument document = new TimeSeriesDocument();
    document.setDataField(request.getDataField());
    document.setDataProvider(request.getDataProvider());
    document.setDataSource(request.getDataSource());
    document.setIdentifiers(request.getIdentifiers());
    document.setObservationTime(request.getObservationTime());
    document.setUniqueIdentifier(uid);
    document.setTimeSeries(seriesSnapshot);
    searchResult.getDocuments().add(document);
    return searchResult;
  }

  private void validateSearchHistoricRequest(TimeSeriesSearchHistoricRequest request) {
    ArgumentChecker.isTrue(request.getIdentifiers() != null && !request.getIdentifiers().getIdentifiers().isEmpty(), "cannot search with null/empty identifiers");
    ArgumentChecker.isTrue(!StringUtils.isBlank(request.getDataSource()), "cannot search with blank dataSource");
    ArgumentChecker.isTrue(!StringUtils.isBlank(request.getDataProvider()), "cannot search with blank dataProvider");
    ArgumentChecker.isTrue(!StringUtils.isBlank(request.getDataProvider()), "cannot search with blank dataProvider");
    ArgumentChecker.isTrue(!StringUtils.isBlank(request.getDataField()), "cannot search with blank field");
    ArgumentChecker.isTrue(!StringUtils.isBlank(request.getDataProvider()), "cannot add timeseries with blank dataProvider");
  }

  @Override
  public TimeSeriesDocument updateTimeSeries(TimeSeriesDocument document) {
    validateTimeSeriesDocument(document);
    Long tsId = validateAndGetTimeSeriesId(document.getUniqueIdentifier());
    
    TimeSeriesMetaData metaData = getTimeSeriesMetaData(tsId);
    if (!metaData.getDataField().equals(document.getDataField())) {
      throw new OpenGammaRuntimeException("Cannot modify datafield for id: " + tsId);
    }
    if (!metaData.getDataProvider().equals(document.getDataProvider())) {
      throw new OpenGammaRuntimeException("Cannot modify dataProvider for id: " + tsId);
    }
    if (!metaData.getDataSource().equals(document.getDataSource())) {
      throw new OpenGammaRuntimeException("Cannot modify dataSource for id: " + tsId);
    }
    if (!metaData.getObservationTime().equals(document.getObservationTime())) {
      throw new OpenGammaRuntimeException("Cannot modify ObservationTime for id: " + tsId);
    }
    if (!metaData.getIdentifiers().equals(document.getIdentifiers())) {
      throw new OpenGammaRuntimeException("Cannot modify Identifiers for id: " + tsId);
    }
    
    deleteDataPoints(tsId);
    insertDataPoints(document.getTimeSeries().toSQLDateDoubleTimeSeries(), tsId);
    return document;
  }
  
  @Override
  public DataPointDocument updateDataPoint(DataPointDocument document) {
    ArgumentChecker.notNull(document, "dataPoint document");
    ArgumentChecker.notNull(document.getDate(), "data point date");
    ArgumentChecker.notNull(document.getValue(), "data point value");
    Long tsId = validateAndGetTimeSeriesId(document.getTimeSeriesId());
    updateDataPoint(document.getDate(), document.getValue(), tsId);
    return document;
  }
  
  @Override
  public DataPointDocument addDataPoint(DataPointDocument document) {
    ArgumentChecker.notNull(document, "dataPoint document");
    ArgumentChecker.notNull(document.getDate(), "data point date");
    ArgumentChecker.notNull(document.getValue(), "data point value");
    Long tsId = validateAndGetTimeSeriesId(document.getTimeSeriesId());
    
    String insertSQL = _namedSQLMap.get(INSERT_TIME_SERIES);
    String insertDelta = _namedSQLMap.get(INSERT_TIME_SERIES_DELTA_I);
    
    Date now = new Date(System.currentTimeMillis());
    
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    parameterSource.addValue("timeSeriesID", tsId, Types.BIGINT);
    parameterSource.addValue("date", toSQLDate(document.getDate()), Types.DATE);
    parameterSource.addValue("value", document.getValue(), Types.DOUBLE);
    parameterSource.addValue("timeStamp", now, Types.TIMESTAMP);
    
    if (!isTriggerSupported()) {
      _simpleJdbcTemplate.update(insertDelta, parameterSource);
    } 
    _simpleJdbcTemplate.update(insertSQL, parameterSource);
    String uid = new StringBuilder(String.valueOf(tsId)).append("-").append(DateUtil.printYYYYMMDD(document.getDate())).toString();
    document.setDataPointId(UniqueIdentifier.of(_identifierScheme, uid));
    return document;
  }
  
  @Override
  public void removeDataPoint(UniqueIdentifier uid) {
    ObjectsPair<Long, LocalDate> tsIdDatePair = validateAndGetDataPointId(uid);
    Date date = toSQLDate(tsIdDatePair.getSecond());
    Long tsId = tsIdDatePair.getFirst();
    removeDataPoint(tsId, date);
  }

  @Override
  public DataPointDocument getDataPoint(UniqueIdentifier uid) {
    ObjectsPair<Long, LocalDate> tsIdDatePair = validateAndGetDataPointId(uid);
    
    Date date = toSQLDate(tsIdDatePair.getSecond());
    Long tsId = tsIdDatePair.getFirst();
    
    NamedParameterJdbcOperations jdbcOperations = _simpleJdbcTemplate.getNamedParameterJdbcOperations();
    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    paramSource.addValue("tsID", tsId, Types.BIGINT);
    paramSource.addValue("date", date, Types.DATE);
    
    final DataPointDocument result = new DataPointDocument();
    result.setDate(tsIdDatePair.getSecond());
    result.setTimeSeriesId(UniqueIdentifier.of(_identifierScheme, String.valueOf(tsId)));
    result.setDataPointId(uid);
    jdbcOperations.query(_namedSQLMap.get(FIND_DATA_POINT_BY_DATE_AND_ID), paramSource, new RowCallbackHandler() {
      @Override
      public void processRow(ResultSet rs) throws SQLException {
        result.setValue(rs.getDouble("value"));
      }
    });
       
    return result;
  }

  @Override
  public UniqueIdentifier resolveIdentifier(IdentifierBundle identifiers, String dataSource, String dataProvider, String field) {
    validateMetaData(identifiers, dataSource, dataProvider, field);
    TimeSeriesRequest request = new TimeSeriesRequest();
    request.setIdentifiers(identifiers);
    request.setDataField(field);
    request.setDataProvider(dataProvider);
    request.setDataSource(dataSource);
    request.setLoadTimeSeries(false);
    
    UniqueIdentifier result = null;
    TimeSeriesSearchResult searchResult = searchTimeSeries(request);
    List<TimeSeriesDocument> documents = searchResult.getDocuments();
    if (!documents.isEmpty()) {
      result = documents.get(0).getUniqueIdentifier();
    }
    return result;
  }

  private long getOrCreateIdentifierBundle(String bundleName, String description, IdentifierBundle identifiers) {
    s_logger.debug("creating/updating identifiers {} with quotedObj={}", identifiers, bundleName);
    long bundleId = getOrCreateIdentifierBundle(bundleName, description);
    Set<Identifier> resolvedIdentifiers = new HashSet<Identifier>(identifiers.getIdentifiers());
    SqlParameterSource[] batchArgs = new MapSqlParameterSource[resolvedIdentifiers.size()];
    int index = 0;
    for (Identifier identifier : resolvedIdentifiers) {
      String scheme = identifier.getScheme().getName();
      EnumWithDescriptionBean schemeBean = getOrCreateScheme(scheme, null);
      Map<String, Object> valueMap = new HashMap<String, Object>();
      valueMap.put("bundleId", bundleId);
      valueMap.put("schemeId", schemeBean.getId());
      valueMap.put("identifier_value", identifier.getValue());
      batchArgs[index++] = new MapSqlParameterSource(valueMap);
    }
    _simpleJdbcTemplate.batchUpdate(_namedSQLMap.get(INSERT_IDENTIFIER), batchArgs);
    return bundleId;
  }

  private long getOrCreateIdentifierBundle(String quotedObj, String desc) {
    long result = getBundleId(quotedObj);
    if (result == INVALID_KEY) {
      result = createBundle(quotedObj, desc);
    }
    return result;
  }
  
  private List<EnumWithDescriptionBean> loadEnumWithDescription(String sql) {
    List<EnumWithDescriptionBean> result = new ArrayList<EnumWithDescriptionBean>();
    SqlParameterSource parameterSource = null;
    List<Map<String, Object>> sqlResult = _simpleJdbcTemplate.queryForList(sql, parameterSource);
    for (Map<String, Object> element : sqlResult) {
      Long id = (Long) element.get("id");
      String name = (String) element.get("name");
      String desc = (String) element.get("description");
      EnumWithDescriptionBean bean = new EnumWithDescriptionBean();
      bean.setId(id);
      bean.setName(name);
      bean.setDescription(desc);
      result.add(bean);
    }
    return result;
  }
  
  private static class IdentifierBundleRowHandler  implements RowCallbackHandler {
    private Set<Long> _ids = new HashSet<Long>();
    private Set<Identifier> _identifiers = new HashSet<Identifier>();
    
    public IdentifierBundleRowHandler() {
    }

    public Set<Long> getIds() {
      return _ids;
    }

    public Set<Identifier> getIdentifiers() {
      return _identifiers;
    }

    @Override
    public void processRow(ResultSet rs) throws SQLException {
      _ids.add(rs.getLong("id"));
      _identifiers.add(Identifier.of(rs.getString("scheme"), rs.getString("identifier_value")));
    }
  }
  
}
