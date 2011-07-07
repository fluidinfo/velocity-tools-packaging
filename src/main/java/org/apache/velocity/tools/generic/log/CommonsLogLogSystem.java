package org.apache.velocity.tools.generic.log;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;

/**
 * Redirects Velocity's LogSystem messages to commons-logging.
 *
 * @author Nathan Bubna
 * @since VelocityTools 1.1
 * @version $Id: CommonsLogLogSystem.java 534721 2007-05-03 05:55:15Z nbubna $
 */

/**
 * Redirects Velocity's LogSystem messages to commons-logging.
 *
 * <p>Please use CommonsLogLogChute in Velocity Engine 1.6 as this will be
 *    removed in VelocityTools releases subsequent to Velocity 1.6's release,
 *    if not earlier.
 * </p>
 *
 * <p>To use, first set up commons-logging, then tell Velocity to use
 * this class for logging by adding the following to your velocity.properties:
 *
 * <code>
 * runtime.log.logsystem.class = org.apache.velocity.tools.generic.log.CommonsLogLogSystem
 * </code>
 * </p>
 *
 * <p>You may also set this property to specify what log/name Velocity's
 * messages should be logged to (example below is default).
 * <code>
 * runtime.log.logsystem.commons.logging.name = org.apache.velocity
 * </code>
 * </p>
 *
 * @author Nathan Bubna
 * @deprecated Use CommonsLogLogChute in Velocity 1.6 instead.
 * @since VelocityTools 1.1
 * @version $Id: CommonsLogLogSystem.java 534721 2007-05-03 05:55:15Z nbubna $
 */
@Deprecated
public class CommonsLogLogSystem implements LogSystem
{

    /** Property key for specifying the name for the log instance */
    public static final String LOGSYSTEM_COMMONS_LOG_NAME =
        "runtime.log.logsystem.commons.logging.name";

    /** Default name for the commons-logging instance */
    public static final String DEFAULT_LOG_NAME = "org.apache.velocity";


    /** the commons-logging Log instance */
    protected Log log;


    /********** LogSystem methods *************/

    public void init(RuntimeServices rs) throws Exception
    {
        String name =
            (String)rs.getProperty(LOGSYSTEM_COMMONS_LOG_NAME);

        if (name == null)
        {
            name = DEFAULT_LOG_NAME;
        }
        log = LogFactory.getLog(name);
        logVelocityMessage(LogSystem.DEBUG_ID,
                           "CommonsLogLogSystem name is '" + name + "'");
    }

    /**
     * Send a log message from Velocity.
     */
    public void logVelocityMessage(int level, String message)
    {
        switch (level)
        {
            case LogSystem.WARN_ID:
                log.warn(message);
                break;
            case LogSystem.INFO_ID:
                log.info(message);
                break;
            //NOTE: this is a hack to offer minor support for the
            //      new trace level in Velocity 1.5
            case -1:
                log.trace(message);
                break;
            case LogSystem.ERROR_ID:
                log.error(message);
                break;
            case LogSystem.DEBUG_ID:
            default:
                log.debug(message);
                break;
        }
    }

}
