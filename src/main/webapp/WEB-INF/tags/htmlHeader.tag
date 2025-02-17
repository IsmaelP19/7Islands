<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--
SevenIslandsCss :: a Spring Framework demonstration
--%>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=1">
    <%-- The above 4 meta tags *must* come first in the head; any other head content must come *after* these tags --%>

    <link rel="shortcut icon" type="image/png" sizes="32x32" href="/resources/images/favicon-32x32.png">
    <link rel="shortcut icon" type="image/png" sizes="16x16" href="/resources/images/favicon-16x16.png">

    <title>Seven Islands</title>

    <%-- CSS generated from LESS --%>

    <spring:url value="/resources/css/sevenislands.css" var="sevenislandsCss"/>
    <link href="${sevenislandsCss}" rel="stylesheet"/>



    <%-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries --%>
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- Only datepicker is used -->
    <spring:url value="/webjars/jquery-ui/1.11.4/jquery-ui.min.css" var="jQueryUiCss"/>
    <link href="${jQueryUiCss}" rel="stylesheet"/>
    <spring:url value="/webjars/jquery-ui/1.11.4/jquery-ui.theme.min.css" var="jQueryUiThemeCss"/>
    <link href="${jQueryUiThemeCss}" rel="stylesheet"/>
</head>
