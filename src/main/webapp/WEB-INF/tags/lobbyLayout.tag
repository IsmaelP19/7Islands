<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="pageName" required="true" %>
<%@ attribute name="customScript" required="false" fragment="true"%>
<%@ attribute name="title" required="false"%>
<%@ attribute name="subtitle" required="false"%>

<!doctype html>
<html>
<sevenislands:htmlHeader/>

<body>
<sevenislands:bodyHeader menuName="${pageName}"/>


<div id="body-section">
    <div id="top-section">
        <h2 id="title"><c:out value="${title}"></c:out></h2>
    </div>

    <c:if test="${not empty message}" >
        <div class="alert alert-${not empty messageType ? messageType : 'info'}" role="alert">
            <c:out value="${message}"></c:out>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button> 
        </div>
    </c:if>

    <div id="mid-section" style="background-color: transparent !important;">
        <div id="background">
            <div id="sun">
                <div id="sun-shadow"></div>
            </div>
            <div class="waves">
                    <div class="wave circulo a" ></div>
                    <div class="wave circulo b" ></div>
                    <div class="wave circulo c" ></div>
            </div>
        </div>
        <jsp:doBody/>
    </div>

    <div id="bottom-section"></div>

</div>

<sevenislands:footer/>
<jsp:invoke fragment="customScript" />

</body>

</html>
