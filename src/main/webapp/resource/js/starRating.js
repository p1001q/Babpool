document.addEventListener("DOMContentLoaded", function () {
    const starBar = document.getElementById("starBar");
    const ratingValue = document.getElementById("ratingValue");
    const ratingInput = document.getElementById("ratingInput");

    function updateRating(e) {
        const rect = starBar.getBoundingClientRect();
        const percent = Math.min(Math.max((e.clientX - rect.left) / rect.width, 0), 1);
        const score = Math.round(percent * 50) / 10;
        ratingValue.textContent = score.toFixed(1);
        ratingInput.value = score.toFixed(1);
        starBar.style.background = `linear-gradient(to right, gold ${percent * 100}%, #ccc ${percent * 100}%)`;
    }

    let isDragging = false;

    starBar.addEventListener("mousedown", function (e) {
        isDragging = true;
        updateRating(e);
    });

    window.addEventListener("mousemove", function (e) {
        if (isDragging) {
            updateRating(e);
        }
    });

    window.addEventListener("mouseup", function () {
        isDragging = false;
    });
});
