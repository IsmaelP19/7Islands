<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="SevenIslands" tagdir="/WEB-INF/tags" %>


<SevenIslands:layout pageName="Lobby">
    <jsp:body>

        <div class="row text-center"><h2>Lobby</h2></div>
        <br>
        <br>

        <form:form modelAttribute="game" class="form-horizontal" action="#">
            <div class="form-group has-feedback">
                <div class="row">
                    <div class="col-sm-4">
                        <div class="row text-center">
                            <p>Game details</p>
                            <br>
                            <br>
                            <p>Name: <c:out value="${game.name}"/></p>
                            <p>Room code: <c:out value="${game.code}"/></p>
                        </div>
                    </div> 
                    <div class="col-sm-4">
                        <div class="row text-center">
                            <p>Friends</p>
                            <br>
                            <br>
                            <c:forEach items="${player.players_friends}" var="friend">
                                <c:out value="${friend.user.username}"></c:out><br>
                            </c:forEach>
                        </div>

                    </div>
                    <div class="col-sm-4">
                        <div class="row text-center">Party members</div>
                        <br>
                        <br>
                        <c:forEach items ="${game.players}" var="p">
                            <div class="row text-center">
                                <c:out value = "${p.user.username}"/><br>
                            </div>
                        </c:forEach> 
                    </div>
                    
                </div>
            </div>
        </form:form>
    </jsp:body>

</SevenIslands:layout>