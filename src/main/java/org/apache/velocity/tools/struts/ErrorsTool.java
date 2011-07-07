package org.apache.velocity.tools.struts;

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

import org.apache.struts.action.ActionMessages;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;

/**
 * <p>
 * This tool deals with Struts error messages. Errors may stem from the validation
 * of a submitted form or from the processing of a request. If there are errors,
 * they are made available to the view to render. A few important aspects about errors
 * are:</p>
 * <ul>
 *     <li>Error message strings are looked up in the message resources. Support
 *         for internationalized messages is provided.</li>
 *     <li>Error messages can have up to five replacement parameters.</li>
 *     <li>Errors have an attribute <code>property</code> that describes the category of
 *         error. This allows the view designer to place error messages precisely where an
 *         error occurred. For example, errors that apply to the entire page can be rendered
 *         at the top of the page, errors that apply to a specific input field can be rendered
 *         next to this input field. Several methods of this tool provide a parameter
 *         <code>property</code> that allows to select a specific category of errors to operate
 *         on. Without the <code>property</code> parameter, methods operate on all error messages.</li>
 * </ul>
 * 
 * <p>See the Struts User's Guide, section
 * <a href="http://struts.apache.org/1.3.8/userGuide/building_view.html">Building View Components</a>
 * for more information on this topic.</p>
 * 
 * <p><pre>
 * Template example(s):
 *   #if( $errors.exist() )
 *     &lt;div class="errors"&gt;
 *     #foreach( $e in $errors.all )
 *       $e &lt;br&gt;
 *     #end
 *     &lt;/div&gt;
 *   #end
 *
 * Toolbox configuration:
 * &lt;tools&gt;
 *   &lt;toolbox scope="request"&gt;
 *     &lt;tool class="org.apache.velocity.tools.struts.ErrorsTool"/&gt;
 *   &lt;/toolbox&gt;
 * &lt;/tools&gt;
 * </pre></p>
 *
 * <p>This tool should only be used in the request scope.</p>
 * <p>Since VelocityTools 1.1, ErrorsTool extends ActionMessagesTool.</p>
 *
 * @author <a href="mailto:sidler@teamup.com">Gabe Sidler</a>
 * @author Nathan Bubna
 * @since VelocityTools 1.0
 * @version $Id: ErrorsTool.java 601976 2007-12-07 03:50:51Z nbubna $
 */
@DefaultKey("errors")
@ValidScope(Scope.REQUEST)
public class ErrorsTool extends ActionMessagesTool
{

    protected ActionMessages getActionMessages()
    {
        if (this.actionMsgs == null)
        {
            this.actionMsgs = StrutsUtils.getErrors(this.request);
        }
        return this.actionMsgs;
    }


    /**
     * <p>Renders the queued error messages as a list. This method expects
     * the message keys <code>errors.header</code> and <code>errors.footer</code>
     * in the message resources. The value of the former is rendered before
     * the list of error messages and the value of the latter is rendered
     * after the error messages.</p>
     *
     * @return The formatted error messages. If no error messages are queued,
     * an empty string is returned.
     */
    public String getMsgs()
    {
        return getMsgs(null, null);
    }


    /**
     * <p>Renders the queued error messages of a particual category as a list.
     * This method expects the message keys <code>errors.header</code> and
     * <code>errors.footer</code> in the message resources. The value of the
     * former is rendered before the list of error messages and the value of
     * the latter is rendered after the error messages.</p>
     *
     * @param property the category of errors to render
     *
     * @return The formatted error messages. If no error messages are queued,
     * an empty string is returned.
     */
    public String getMsgs(String property)
    {
        return getMsgs(property, null);
    }


    /**
     * <p>Renders the queued error messages of a particual category as a list.
     * This method expects the message keys <code>errors.header</code> and
     * <code>errors.footer</code> in the message resources. The value of the
     * former is rendered before the list of error messages and the value of
     * the latter is rendered after the error messages.</p>
     *
     * @param property the category of errors to render
     * @param bundle the message resource bundle to use
     *
     * @return The formatted error messages. If no error messages are queued,
     * an empty string is returned.
     * @since VelocityTools 1.1
     */
    public String getMsgs(String property, String bundle)
    {
        return StrutsUtils.errorMarkup(property, bundle, request,
                                       request.getSession(false), application);
    }

}
