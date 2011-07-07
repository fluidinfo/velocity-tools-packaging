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

import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import javax.servlet.http.HttpSession;
import org.apache.struts.tiles.AttributeDefinition;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.ComponentDefinition;
import org.apache.struts.tiles.Controller;
import org.apache.struts.tiles.DefinitionAttribute;
import org.apache.struts.tiles.DefinitionNameAttribute;
import org.apache.struts.tiles.DefinitionsFactoryException;
import org.apache.struts.tiles.DirectStringAttribute;
import org.apache.struts.tiles.TilesUtil;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;
import org.apache.velocity.tools.view.ImportSupport;
import org.apache.velocity.tools.view.ViewContext;

/**
 * <p>The TilesTool is used to interact with the Struts-Tiles framework that is part
 *  of Struts 1.</p>
 * <p><pre>
 * Template example(s):
 *  &lt;!-- insert a tile --&gt;
 *  $tiles.myTileDefinition
 *
 *  &lt;!-- get named attribute value from the current tiles-context --&gt;
 *  $tiles.getAttribute("myTileAttribute")
 *
 *  &lt;!-- import all attributes of the current tiles-context into the velocity-context. --&gt;
 *  $tiles.importAttributes()
 *
 * Toolbox configuration:
 * &lt;tools&gt;
 *   &lt;toolbox scope="request"&gt;
 *     &lt;tool class="org.apache.velocity.tools.struts.TilesTool"/&gt;
 *   &lt;/toolbox&gt;
 * &lt;/tools&gt;
 * </pre></p>
 *
 * <p>This tool may only be used in the request scope.</p>
 *
 * @author <a href="mailto:marinoj@centrum.is">Marino A. Jonsson</a>
 * @since VelocityTools 1.1
 * @version $Revision: 601976 $ $Date: 2007-12-06 19:50:51 -0800 (Thu, 06 Dec 2007) $
 */
@DefaultKey("tiles")
@ValidScope(Scope.REQUEST)
public class TilesTool extends ImportSupport
{
    static final String PAGE_SCOPE = "page";
    static final String REQUEST_SCOPE = "request";
    static final String SESSION_SCOPE = "session";
    static final String APPLICATION_SCOPE = "application";

    protected Context velocityContext;

    /**
     * A stack to hold ComponentContexts while nested tile-definitions
     * are rendered.
     */
    protected Stack contextStack;
    
    /**
     * Indicates if there is a MethodExceptionEventHandler present
     */
    protected boolean catchExceptions = true;

    /******************************* Constructors ****************************/

    @Deprecated
    public void init(Object obj)
    {
        if (obj instanceof ViewContext)
        {
            ViewContext ctx = (ViewContext)obj;
            setVelocityContext(ctx.getVelocityContext());
            setRequest(ctx.getRequest());
            setResponse(ctx.getResponse());
            setServletContext(ctx.getServletContext());
            setLog(ctx.getVelocityEngine().getLog());
        }
    }

    /**
     * Initializes this tool.
     *
     * @param context the current {@link Context}
     * @throws IllegalArgumentException if the param is not a {@link Context}
     */
    public void setVelocityContext(Context context)
    {
        if (context == null)
        {
            throw new NullPointerException("velocity context should not be null");
        }
        this.velocityContext = context;
    }

    public void setCatchExceptions(boolean catchExceptions)
    {
        this.catchExceptions = catchExceptions;
    }

    /***************************** View Helpers ******************************/

    /**
     * A generic tiles insert function.
     *
     * <p>This is functionally equivalent to
     * <code>&lt;tiles:insert attribute="foo" /&gt;</code>.</p>
     *
     * @param obj Can be any of the following:
     *        AttributeDefinition,
     *        tile-definition name,
     *        tile-attribute name,
     *        regular uri.
     *        (checked in that order)
     * @return the rendered template or value as a String
     * @throws Exception on failure
     */
    public String get(Object obj) throws Exception {
        try
        {
            Object value = getCurrentContext().getAttribute(obj.toString());
            if (value != null)
            {
                return processObjectValue(value);
            }
            return processAsDefinitionOrURL(obj.toString());
        }
        catch (Exception e)
        {
            LOG.error("TilesTool : Exeption while rendering Tile " + obj, e);

            /* delegate exception handling to an EventHandler if present. */
            if (!this.catchExceptions) {
            	throw e;
            }
            
            return null;
        }
    }

    /**
     * Fetches a named attribute-value from the current tiles-context.
     *
     * <p>This is functionally equivalent to
     * <code>&lt;tiles:getAsString name="foo" /&gt;</code>.</p>
     *
     * @param name the name of the tiles-attribute to fetch
     * @return attribute value for the named attribute
     */
    public Object getAttribute(String name)
    {
        Object value = getCurrentContext().getAttribute(name);
        if (value == null)
        {
            LOG.warn("TilesTool : Tile attribute '" + name + "' wasn't found in context.");
        }
        return value;
    }

    /**
     * Imports the named attribute-value from the current tiles-context into the
     * current Velocity context.
     *
     * <p>This is functionally equivalent to
     * <code>&lt;tiles:importAttribute name="foo" /&gt;</code>
     *
     * @param name the name of the tiles-attribute to import
     */
    public void importAttribute(String name)
    {
        this.importAttribute(name, PAGE_SCOPE);
    }

    /**
     * Imports the named attribute-value from the current tiles-context into the
     * named context ("page", "request", "session", or "application").
     *
     * <p>This is functionally equivalent to
     * <code>&lt;tiles:importAttribute name="foo" scope="scopeValue" /&gt;</code>
     *
     * @param name the name of the tiles-attribute to import
     * @param scope the named context scope to put the attribute into.
     */
    public void importAttribute(String name, String scope)
    {
        Object value = getCurrentContext().getAttribute(name);
        if (value == null)
        {
            LOG.warn("TilesTool : Tile attribute '" + name + "' wasn't found in context.");
        }

        if (scope.equals(PAGE_SCOPE))
        {
            velocityContext.put(name, value);
        }
        else if (scope.equals(REQUEST_SCOPE))
        {
            request.setAttribute(name, value);
        }
        else if (scope.equals(SESSION_SCOPE))
        {
            request.getSession().setAttribute(name, value);
        }
        else if (scope.equals(APPLICATION_SCOPE))
        {
            application.setAttribute(name, value);
        }
    }

    /**
     * Imports all attributes in the current tiles-context into the
     * current velocity-context.
     *
     * <p>This is functionally equivalent to
     * <code>&lt;tiles:importAttribute /&gt;</code>.</p>
     */
    public void importAttributes()
    {
        this.importAttributes(PAGE_SCOPE);
    }

    /**
     * Imports all attributes in the current tiles-context into the named
     * context ("page", "request", "session", or "application").
     *
     * <p>This is functionally equivalent to
     * <code>&lt;tiles:importAttribute scope="scopeValue" /&gt;</code>.</p>
     *
     * @param scope the named context scope to put the attributes into.
     */
    public void importAttributes(String scope)
    {
        ComponentContext context = getCurrentContext();
        Iterator names = context.getAttributeNames();

        if (scope.equals(PAGE_SCOPE))
        {
            while (names.hasNext())
            {
                String name = (String)names.next();
                velocityContext.put(name, context.getAttribute(name));
            }
        }
        else if (scope.equals(REQUEST_SCOPE))
        {
            while (names.hasNext())
            {
                String name = (String)names.next();
                request.setAttribute(name, context.getAttribute(name));
            }
        }
        else if (scope.equals(SESSION_SCOPE))
        {
            HttpSession session = request.getSession();
            while (names.hasNext())
            {
                String name = (String)names.next();
                session.setAttribute(name, context.getAttribute(name));
            }
        }
        else if (scope.equals(APPLICATION_SCOPE))
        {
            while (names.hasNext())
            {
                String name = (String)names.next();
                application.setAttribute(name, context.getAttribute(name));
            }
        }
    }


    /************************** Protected Methods ****************************/

    /**
     * Process an object retrieved as a bean or attribute.
     *
     * @param value - Object can be a typed attribute, a String, or anything
     *        else. If typed attribute, use associated type. Otherwise, apply
     *        toString() on object, and use returned string as a name.
     * @throws Exception - Throws by underlying nested call to
     *         processDefinitionName()
     * @return the fully processed value as String
     */
    protected String processObjectValue(Object value) throws Exception
    {
        /* First, check if value is one of the Typed Attribute */
        if (value instanceof AttributeDefinition)
        {
            /* We have a type => return appropriate IncludeType */
            return processTypedAttribute((AttributeDefinition)value);
        }
        else if (value instanceof ComponentDefinition)
        {
            return processDefinition((ComponentDefinition)value);
        }
        /* Value must denote a valid String */
        return processAsDefinitionOrURL(value.toString());
    }

    /**
     * Process typed attribute according to its type.
     *
     * @param value Typed attribute to process.
     * @return the fully processed attribute value as String.
     * @throws Exception - Throws by underlying nested call to processDefinitionName()
     */
    protected String processTypedAttribute(AttributeDefinition value)
        throws Exception
    {
        if (value instanceof DirectStringAttribute)
        {
            return (String)value.getValue();
        }
        else if (value instanceof DefinitionAttribute)
        {
            return processDefinition((ComponentDefinition)value.getValue());
        }
        else if (value instanceof DefinitionNameAttribute)
        {
            return processAsDefinitionOrURL((String)value.getValue());
        }
        /* else if( value instanceof PathAttribute ) */
        return doInsert((String)value.getValue(), null, null);
    }

    /**
     * Try to process name as a definition, or as an URL if not found.
     *
     * @param name Name to process.
     * @return the fully processed definition or URL
     * @throws Exception
     */
    protected String processAsDefinitionOrURL(String name) throws Exception
    {
        try
        {
            ComponentDefinition definition =
                    TilesUtil.getDefinition(name, this.request, this.application);
            if (definition != null)
            {
                return processDefinition(definition);
            }
        }
        catch (DefinitionsFactoryException ex)
        {
        /* silently failed, because we can choose to not define a factory. */
        }
        /* no definition found, try as url */
        return processUrl(name);
    }

    /**
     * End of Process for definition.
     *
     * @param definition Definition to process.
     * @return the fully processed definition.
     * @throws Exception from InstantiationException Can't create requested controller
     */
    protected String processDefinition(ComponentDefinition definition) throws
            Exception
    {
        Controller controller = null;
        try
        {
            controller = definition.getOrCreateController();

            String role = definition.getRole();
            String page = definition.getTemplate();

            return doInsert(definition.getAttributes(),
                            page,
                            role,
                            controller);
        }
        catch (InstantiationException ex)
        {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Processes an url
     *
     * @param url the URI to process.
     * @return the rendered template as String.
     * @throws Exception
     */
    protected String processUrl(String url) throws Exception
    {
        return doInsert(url, null, null);
    }

    /**
     * Use this if there is no nested tile.
     *
     * @param page the page to process.
     * @param role possible user-role
     * @param controller possible tiles-controller
     * @return the rendered template as String.
     * @throws Exception
     */
    protected String doInsert(String page, String role, Controller controller) throws
            Exception
    {
        if (role != null && !this.request.isUserInRole(role))
        {
            return null;
        }

        ComponentContext subCompContext = new ComponentContext();
        return doInsert(subCompContext, page, role, controller);
    }

    /**
     * Use this if there is a nested tile.
     *
     * @param attributes attributes for the sub-context
     * @param page the page to process.
     * @param role possible user-role
     * @param controller possible tiles-controller
     * @return the rendered template as String.
     * @throws Exception
     */
    protected String doInsert(Map attributes,
                              String page,
                              String role,
                              Controller controller) throws Exception
    {
        if (role != null && !this.request.isUserInRole(role))
        {
            return null;
        }

        ComponentContext subCompContext = new ComponentContext(attributes);
        return doInsert(subCompContext, page, role, controller);
    }

    /**
     * An extension of the other two doInsert functions
     *
     * @param subCompContext the sub-context to set in scope when the
     *        template is rendered.
     * @param page the page to process.
     * @param role possible user-role
     * @param controller possible tiles-controller
     * @return the rendered template as String.
     * @throws Exception
     */
    protected String doInsert(ComponentContext subCompContext,
                              String page,
                              String role,
                              Controller controller) throws Exception
    {
        pushTilesContext();
        try
        {
            ComponentContext.setContext(subCompContext, this.request);

            /* Call controller if any */
            if (controller != null)
            {
                controller.execute(subCompContext,
                                   this.request,
                                   this.response,
                                   this.application);
            }
            /* pass things off to ImportSupport */
            return this.acquireString(page);
        }
        finally
        {
            popTilesContext();
        }
    }

    /**
     * Retrieve the current tiles component context.
     * This is pretty much just a convenience method.
     */
    protected ComponentContext getCurrentContext()
    {
        return ComponentContext.getContext(this.request);
    }

    /**
     * <p>pushes the current tiles context onto the context-stack.
     * preserving the context is necessary so that a sub-context can be
     * put into request scope and lower level tiles can be rendered</p>
     */
    protected void pushTilesContext()
    {
        if (this.contextStack == null)
        {
            this.contextStack = new Stack();
        }
        contextStack.push(getCurrentContext());
    }

    /**
     * Pops the tiles sub-context off the context-stack after the lower level
     * tiles have been rendered.
     */
    protected void popTilesContext()
    {
        ComponentContext context = (ComponentContext)this.contextStack.pop();
        ComponentContext.setContext(context, this.request);
    }

}
