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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
<head>
</head>

<body>
<font size="+4"><center><%=request.getRequestURI()%></center></font>
<br>
<jsp:include page="top.html" flush="true"/>
<br>
We are on the true page. The secure (SSL) action "true" forwards to this page.
<br>
<br>

Try the <sslext:link page="/false_jsp.do" >false</sslext:link> page.
<br>
Try the <sslext:link page="/any_jsp.do" >any</sslext:link> page.
<br>
<br>
Go to <sslext:link page="/formAction_jsp.do">form</sslext:link> test page.
</body>
</html>
