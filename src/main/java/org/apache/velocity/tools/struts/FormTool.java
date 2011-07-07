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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.velocity.tools.view.ViewContext;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;

/**
 * <p>View tool to work with HTML forms in Struts.</p>
 * 
 * <p>Struts has support to parse incoming HTTP requests and populate a Java bean
 * with the submitted request parameters. The same Java bean is used to populate
 * forms with initial values. Additionally, a hook allows the application developer
 * to include automatic form validation code.</p>
 * 
 * <p>FormTool provides miscellaneous methods to work with forms and form bean in
 * the context of Struts applications.</p>
 * 
 * <p><pre>
 * Template example(s):
 *  &lt;input type="hidden" name="$form.tokenName" value="$form.token"&gt;
 *  &lt;input type="submit" name="$form.cancelName" value="Cancel"&gt;
 *
 * Toolbox configuration:
 * &lt;tools&gt;
 *   &lt;toolbox scope="request"&gt;
 *     &lt;tool class="org.apache.velocity.tools.struts.FormTool"/&gt;
 *   &lt;/toolbox&gt;
 * &lt;/tools&gt;
 * </pre></p>
 *
 * <p>This tool may only be used in the request scope.</p>
 *
 * @author <a href="mailto:sidler@teamup.com">Gabe Sidler</a>
 * @since VelocityTools 1.0
 * @version $Id: FormTool.java 601976 2007-12-07 03:50:51Z nbubna $
 */
@DefaultKey("form")
@ValidScope(Scope.REQUEST)
public class FormTool
{

    // --------------------------------------------- Properties ---------------


    /**
     * A reference to the HtttpServletRequest.
     */
    protected HttpServletRequest request;


    /**
     * A reference to the HtttpSession.
     */
    protected HttpSession session;



    // --------------------------------------------- Constructors -------------

    @Deprecated
    public void init(Object obj)
    {
        if (obj instanceof ViewContext)
        {
            setRequest(((ViewContext)obj).getRequest());
        }
    }

    /**
     * Initializes this tool.
     *
     * @param request the current {@link HttpServletRequest}
     * @throws IllegalArgumentException if the param is not a ViewContext
     */
    public void setRequest(HttpServletRequest request)
    {
        if (request == null)
        {
            throw new NullPointerException("request should not be null");
        }
        this.request = request;
        this.session = request.getSession(false);
    }



    // --------------------------------------------- View Helpers -------------

    /**
     * <p>Returns the form bean associated with this action mapping.</p>
     *
     * <p>This is a convenience method. The form bean is automatically
     * available in the Velocity context under the name defined in the
     * Struts configuration.</p>
     *
     * <p>If the form bean is used repeatedly, it is recommended to create a
     * local variable referencing the bean rather than calling getBean()
     * multiple times.</p>
     *
     * <pre>
     * Example:
     * #set ($defaults = $form.bean)
     * &lt;input type="text" name="username" value="$defaults.username"&gt;
     * </pre>
     *
     * @return the {@link ActionForm} associated with this request or
     * <code>null</code> if there is no form bean associated with this mapping
     */
    public ActionForm getBean()
    {
        return StrutsUtils.getActionForm(request, session);
    }

    /**
     * <p>Returns the form bean name associated with this action mapping.</p>
     *
     * @return the name of the ActionForm associated with this request or
     * <code>null</code> if there is no form bean associated with this mapping
     */
    public String getName()
    {
        return StrutsUtils.getActionFormName(request, session);
    }



    /**
     * <p>Returns the query parameter name under which a cancel button press
     * must be reported if form validation is to be skipped.</p>
     *
     * <p>This is the value of
     * <code>org.apache.struts.taglib.html.Constants.CANCEL_PROPERTY</code></p>
     */
    public String getCancelName()
    {
        return org.apache.struts.taglib.html.Constants.CANCEL_PROPERTY;
    }


    /**
     * Returns the transaction control token for this session or
     * <code>null</code> if no token exists.
     */
    public String getToken()
    {
        return StrutsUtils.getToken(session);
    }


    /**
     * <p>Returns the query parameter name under which a transaction token
     * must be reported. This is the value of
     * <code>org.apache.struts.taglib.html.Constants.TOKEN_KEY</code></p>
     */
    public String getTokenName()
    {
        return org.apache.struts.taglib.html.Constants.TOKEN_KEY;
    }

}
