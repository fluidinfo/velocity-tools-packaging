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
<%@ taglib uri="/WEB-INF/sslext.tld" prefix="sslext"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html>
<head>
</head>

<body>
<font size="+4"><center><%=request.getRequestURI()%></center></font>
<br>
We are on the form page.  View the page source to see the difference in the action attribute values between the two forms.
<br>
<br>
<sslext:form action="/secureSubmit_jsp" >
This posts to a secure action.
<br>
   <html:text property="propA" value="" size="8" maxlength="8" />
<br>
   <html:text property="propB" value="" size="8" maxlength="8" />
<br>
<html:submit/>
</sslext:form>
<sslext:form action="/nonsecureSubmit_jsp" >
This posts to a non-secure action.
<br>
   <html:text property="propA" value="" size="8" maxlength="8" />
<br>
   <html:text property="propB" value="" size="8" maxlength="8" />
<br>
<html:submit/>
</sslext:form>
</body>
</html>
