
/*
 * Copyright (C) 2011 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.dellroad.stuff.servlet;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.IntrospectorCleanupListener;

/**
 * Clears references that can cause container memory leaks on Web container class unloading.
 *
 * @see <a href="http://opensource.atlassian.com/confluence/spring/pages/viewpage.action?pageId=2669">Spring Wiki Discussion of
 *      ClassLoader Memory Leaks</a>
 * @since 1.0.49
 */
public class ContextCleanupListener extends IntrospectorCleanupListener {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Unregisters any JDBC drivers registered under the current class loader after
     * first invoking the superclass version of this method.
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            super.contextDestroyed(event);
            for (Enumeration<Driver> e = DriverManager.getDrivers(); e.hasMoreElements(); ) {
                Driver driver = e.nextElement();
                if (driver.getClass().getClassLoader() == getClass().getClassLoader()) {
                    DriverManager.deregisterDriver(driver);
                }
            }
        } catch (Throwable e) {
            log.error("exception cleaning up servlet context", e);
        }
    }
}