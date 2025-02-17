<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    // BUTTONS

    window.addEventListener("load", ()=> {

    let travelBtn = document.getElementById("travel-btn");
    let skipBtn = document.getElementById("skip-btn");
    let diceBtn = document.getElementById("dice-btn");
    
    travelBtn.addEventListener("click", (e) => {
        let form = document.querySelector("form");
        form.submit();
    });

    skipBtn.addEventListener("click", (e) => {
        location.href = "/boards/${game.id}/changeTurn";
    });

    diceBtn.addEventListener("click", (e) => {
        location.href = "/boards/${game.id}/rollDie";
    });
    
    });
        
</script>
