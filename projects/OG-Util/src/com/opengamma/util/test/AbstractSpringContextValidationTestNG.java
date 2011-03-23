/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */

package com.opengamma.util.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import com.opengamma.util.PlatformConfigUtils;

/**
 * Extend from this to verify that a Spring configuration is valid. This is to spot
 * changes made to the code that prevent the beans from being instantiated properly. 
 */
public abstract class AbstractSpringContextValidationTestNG {

  private ThreadLocal<GenericApplicationContext> _springContext = new ThreadLocal<GenericApplicationContext>();

  @DataProvider(name = "runModes")
  public static Object[][] data_runMode() {
    return new Object[][] {
      {"shareddev"},
      {"standalone"},
    };
  }

  protected GenericApplicationContext getSpringContext() {
    return _springContext.get();
  }

  protected void setSpringContext(final GenericApplicationContext springContext) {
    _springContext.set(springContext);
  }

  //-------------------------------------------------------------------------
  /**
   * This should be called by the subclass to initialise the test.
   */
  protected void loadClassPathResource(final String opengammaPlatformRunmode, final String name) {
    PlatformConfigUtils.configureSystemProperties(opengammaPlatformRunmode);
    
    GenericApplicationContext springContext = new GenericApplicationContext();
    XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(springContext);
    xmlReader.loadBeanDefinitions(new ClassPathResource(name));
    springContext.refresh();
    setSpringContext(springContext);
  }

  @AfterMethod
  public void runAfter() {
    getSpringContext().close();
  }

  //-------------------------------------------------------------------------
  /**
   * This tests that something was loaded.
   */
  protected void assertContextLoaded() {
    final String[] beans = getSpringContext().getBeanDefinitionNames();
    assertNotNull(beans);
    if (beans.length == 0) {
      fail("No beans created");
    }
    System.out.println("Beans created");
    for (String bean : beans) {
      System.out.println("\t" + bean);
    }
  }

  /**
   * This tests that a specific bean was loaded.
   */
  @SuppressWarnings("unchecked")
  protected <T> T assertBeanExists(final Class<T> clazz, final String name) {
    final Object bean = getSpringContext().getBean(name);
    assertNotNull(bean);
    assertTrue(clazz.isAssignableFrom(bean.getClass()));
    return (T) bean;
  }

//  protected void loadClassPathResource(final String name) {
//    XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(getSpringContext());
//    xmlReader.loadBeanDefinitions(new ClassPathResource(name));
//  }
//
//  protected void loadFileSystemResource(final String path) {
//    XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(getSpringContext());
//    xmlReader.loadBeanDefinitions(new FileSystemResource(path));
//  }
//
//  protected void loadXMLResource(final String xml) {
//    XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(getSpringContext());
//    xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
//    xmlReader.loadBeanDefinitions(new InputSource(new StringReader(xml)));
//  }
//
//  protected void loadUrlResource(final String url) {
//    try {
//      XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(getSpringContext());
//      xmlReader.loadBeanDefinitions(new UrlResource(url));
//    } catch (MalformedURLException ex) {
//      throw new OpenGammaRuntimeException("Malformed URL - " + url, ex);
//    }
//  }
//
//  protected void assertSomethingHappened() {
//    final String[] beans = getSpringContext().getBeanDefinitionNames();
//    assertNotNull(beans);
//    if (beans.length == 0) {
//      fail("No beans created");
//    }
//    System.out.println("Beans created");
//    for (String bean : beans) {
//      System.out.println("\t" + bean);
//    }
//  }
//
//  @SuppressWarnings("unchecked")
//  protected <T> T assertBeanExists (final Class<T> clazz, final String name) {
//    final Object bean = getSpringContext ().getBean(name);
//    assertNotNull (bean);
//    assertTrue(clazz.isAssignableFrom(bean.getClass()));
//    return (T)bean;
//  }

}
