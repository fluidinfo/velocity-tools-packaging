<%--
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
--%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
    <head>
    	<title><bean:message key="title"/> </title>
    	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    </head>

    <body>

        <jsp:useBean id="address" scope="session" class="examples.app1.AddressBean" />

        <p><b><bean:message key="title"/> (JSP <bean:message key="version"/>)</b></p>

        <p><bean:message key="intro"/></p>

        <form name="form1" action="address1.do">
        <p><bean:message key="language"/>
        <select name="locale" size="1" onchange="form1.submit(); return false;">
            <option value="" SELECTED></option>
            <option value="English">English</option>
            <option value="Deutsch">Deutsch</option>
        </select>
        </p>
        </form>

        <p><bean:message key="header"/></p>
        <table width="300" border="1" cellspacing="" cellpadding="5">
        	<tr>
        		<td><bean:message key="firstname"/></td>
        		<td>&nbsp;<bean:write name="address" property="firstname"/></td>
        	</tr>
        	<tr>
        		<td><bean:message key="lastname"/></td>
        		<td>&nbsp;<bean:write name="address" property="lastname"/></td>
        	</tr>
        	<tr>
        		<td><bean:message key="street"/></td>
        		<td>&nbsp;<bean:write name="address" property="street"/></td>
        	</tr>
        	<tr>
        		<td><bean:message key="zip"/></td>
        		<td>&nbsp;<bean:write name="address" property="zip"/></td>
        	</tr>
        	<tr>
        		<td><bean:message key="city"/></td>
        		<td>&nbsp;<bean:write name="address" property="city"/></td>
        	</tr>
        	<tr>
        		<td><bean:message key="country"/></td>
        		<td>&nbsp;<bean:write name="address" property="country"/></td>
        	</tr>
            <tr>
                <td><bean:message key="languages"/></td>
                <td>
                       <logic:present name="address" property="languages">
                           <logic:iterate id="language" name="address" property="languages">
                               &nbsp; <bean:message name="language"/><br>
                           </logic:iterate>
                       </logic:present>
                       &nbsp;
                </td>
            </tr>
        </table>

        <br>
        <a href="address1.do?action=edit"><bean:message key="edit"/></a> (JSP)<br><br>
        <a href="address2.do?action=edit"><bean:message key="edit"/></a> (Velocity)

        <br><br>
        <html:link forward="showAddressSrcJsp">Template</html:link>

    </body>
</html>
