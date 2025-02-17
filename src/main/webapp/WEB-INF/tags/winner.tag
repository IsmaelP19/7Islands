<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="position" required="true"%>
<%@ attribute name="player" required="false" rtexprvalue="true" type="org.springframework.samples.SevenIslands.statistic.PlayerWithStatistics" %>

<div class="winner ${'winner-'.concat(position)}">

    <h1 id="position">
        ${position} ${position == 1 ? "st" : ""}${position == 2 ? "nd" : ""}${position == 3 ? "rd" : ""}
    </h1>

    <a href="${player == null ? '/players/profile/' + player.id : '#'}" htmlEscape="true" />
        <img src="${player.profilePhoto == null || player == null ? player.profilePhoto : '/resources/images/profile-photo.png'}" alt="Profile photo">
        <p>${player == null ? "" : player.username}</p>
    </a>

</div>
