<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>



<sevenislands:layout pageName="board">
    
    
    <div id="top-section">
        <img id="profile-avatar" src="/resources/images/profile-photo.png">
        <h2><c:out value="${game.name}"/></h2>
    </div>
    <p> Your will lose your turn in <span id="countdowntimer"> 10 </span> Seconds</p>
    <div>
        <div class="col-md-4">
            <div class="playersList">
                <h3 class="text-center">Player List:</h3>
                <c:forEach items ="${game.players}" var="p">
                    <div class="row text-center">
                        <c:out value = "${p.user.username}"/><br>
                    </div>
                </c:forEach> 
            </div>
        </div>
        <div class="col-md-8 playersList">
            <sevenislands:board board="${board}"/>
            <!-- <c:forEach items="${board.cells}" var="cell">
            	<game:cell size="100" cell="${cell}"/>
            </c:forEach>  -->
        </div>
    </div>
</sevenislands:layout>