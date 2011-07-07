package org.apache.velocity.tools.view;

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

import java.util.Map;

/**
 * Common interface for toolbox manager implementations.
 *
 * @author Nathan Bubna
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @author <a href="mailto:sidler@teamup.com">Gabe Sidler</a>
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id: ToolboxManager.java 651470 2008-04-25 00:47:52Z nbubna $
 */
@Deprecated
public interface ToolboxManager
{

    /**
     * Adds a tool to be managed
     */
    void addTool(ToolInfo info);

    /**
     * Adds a data object for the context.
     *
     * @param info An object that implements ToolInfo
     */
    void addData(ToolInfo info);

    /**
     * Retrieves a map of the tools and data being managed. Tools
     * that have an init(Object) method will be (re)initialized
     * using the specified initData.
     *
     * @param initData data used to initialize tools
     * @return the created ToolboxContext
     * @since VelocityTools 1.2
     */
    Map getToolbox(Object initData);

}
