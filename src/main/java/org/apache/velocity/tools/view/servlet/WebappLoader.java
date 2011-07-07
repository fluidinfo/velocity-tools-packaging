package org.apache.velocity.tools.view.servlet;

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

import org.apache.velocity.tools.view.WebappResourceLoader;
import org.apache.commons.collections.ExtendedProperties;

/**
 * <p>This is basically an empty subclass of {@link WebappResourceLoader} that exists
 *    merely for backwards compatibility with VelocityTools 1.x. Please
 *    use {@link WebappResourceLoader} directly, as this may be removed
 *    in VelocityTools 2.1.
 * </p>
 * @deprecated Use {@link WebappResourceLoader} instead.
 * @version $Id: WebappLoader.java 511959 2007-02-26 19:24:39Z nbubna $  
 */
@Deprecated
public class WebappLoader extends WebappResourceLoader
{
    public void init(ExtendedProperties configuration)
    {
        log.warn("WebappLoader is deprecated. Use "+
                 WebappResourceLoader.class.getName()+" instead.");
        super.init(configuration);
    }
}
