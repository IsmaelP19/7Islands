<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>


<script type="text/javascript">

    // TIMER

    let diffSeconds = '${tempo}';
    let diffMinutes = 0;


    window.addEventListener("load", ()=>{
        
        const timerSecs = document.getElementById("turn-secs");
        const timerMins = document.getElementById("turn-mins");

        setInterval(() => {
            updateTimer();
            decrementTime();
        }, 1000);

        function updateTimer() {
            timerSecs.innerHTML = diffSeconds;
            timerMins.innerHTML = diffMinutes;
        }

        function decrementTime() {

            diffSeconds --;

            if(diffSeconds<0){
                reloadPage();
            }
        }

        function reloadPage() {

            location.reload();
        }
    });

</script>