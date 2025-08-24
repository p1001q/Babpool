//합칠 때 사용해야되는데 아직 미사용중임


let currentStoreId = null;
let recommendList = [];  // recommendList는 JSP에서 세팅

let selectedTypes = [1, 2, 3, 4, 5];

function openHelp() {
    document.getElementById("helpOverlay").style.display = "block";
    document.getElementById("mascotModal").style.display = "block";
    document.getElementById("mascotCharacter").style.display = "block";
    document.getElementById("recommendPlace").style.display = "none";
    document.getElementById("recommendMenu").style.display = "none";
    document.getElementById("ByeMessage").style.display = "none";
}

function recommendNearby() {
    document.getElementById("recommendPlace").style.display = "block";
    document.getElementById("ByeMessage").style.display = "none";

    fetch("getRandomStore")
    .then(response => response.json())
    .then(data => {
        document.getElementById("storeName").innerText = data.storeName;
        const avg = parseFloat(data.ratingAvg);
        let stars = "⭐⭐⭐⭐⭐";
        if (avg <= 1) stars = "☆ ☆ ☆ ☆ ☆";
        else if (avg <= 1.5) stars = "⭐ ☆ ☆ ☆ ☆";
        else if (avg <= 2.5) stars = "⭐⭐ ☆ ☆ ☆";
        else if (avg <= 3.5) stars = "⭐⭐⭐ ☆ ☆";
        else if (avg <= 4.5) stars = "⭐⭐⭐⭐ ☆";
        document.getElementById("ratingStars").innerText = stars;
        document.getElementById("ratingAvg").innerText = `(${avg} / ${data.reviewCount}개)`;
        document.getElementById("likeCount").innerText = data.likeCount;

        if (data.reviews.length === 0) {
            document.getElementById("reviewList").innerHTML = `
                <div class="review-section-title">풀잎이들의 리뷰</div>
                <div class="review-box"><span>아직 등록된 리뷰가 없어요 ㅠ</span></div>`;
        } else {
            const reviewHtml = data.reviews.map(r => `<div class="review-box"><span>${r}</span></div>`).join("");
            document.getElementById("reviewList").innerHTML = `<div class="review-section-title">풀잎이들의 리뷰</div>${reviewHtml}`;
        }

        const tagHtml = data.tags.map(t => `<div><span>#${t}</span></div>`).join("");
        document.getElementById("tagList").innerHTML = tagHtml;

        document.querySelector(".recommend-image img").src = data.imagePath;
        currentStoreId = data.storeId;
    });

    document.getElementById("mascotModal").style.display = "none";
    document.getElementById("recommendPlace").style.display = "block";
}

function goStorePlace() {
    document.getElementById("recommendPlace").style.display = "none";
    document.getElementById("ByeMessage").style.display = "block";
    document.getElementById("messageText").innerText = "내가 도움이 됐다니 기쁘다! 밥 맛있게 먹어!";

    setTimeout(function() {
        window.location.href = `placeDetail.jsp?storeId=${currentStoreId}`;
    }, 1500);
}

function recommendMenu() {
    randomMenu();
    document.getElementById("mascotModal").style.display = "none";
    document.getElementById("recommendMenu").style.display = "block";

    document.querySelectorAll(".typeBtn").forEach(btn => {
        const type = parseInt(btn.dataset.type);
        btn.classList.toggle("active", selectedTypes.includes(type));
    });
}

function randomMenu() {
    const filtered = recommendList.filter(menu => selectedTypes.includes(menu.type));
    const randomIndex = Math.floor(Math.random() * filtered.length);
    const selected = filtered[randomIndex];
    document.getElementById("menuName").innerText = selected.name;
}

function goStoreMenu() {
    document.getElementById("recommendMenu").style.display = "none";
    document.getElementById("ByeMessage").style.display = "block";
    document.getElementById("messageText").innerText = "내가 도움이 됐다니 기쁘다! 밥 맛있게 먹어!";

    setTimeout(function() {
        document.getElementById("helpOverlay").style.display = "none";
        document.getElementById("ByeMessage").style.display = "none";
        document.getElementById("mascotCharacter").style.display = "none";
    }, 1500);
}

function closeHelp() {
    document.getElementById("mascotModal").style.display = "none";
    document.getElementById("recommendPlace").style.display = "none";
    document.getElementById("recommendMenu").style.display = "none";
    document.getElementById("ByeMessage").style.display = "block";
    document.getElementById("messageText").innerText = "다시 도움이 필요하면 날 눌러줘!";
    setTimeout(function() {
        document.getElementById("helpOverlay").style.display = "none";
        document.getElementById("ByeMessage").style.display = "none";
        document.getElementById("mascotCharacter").style.display = "none";
    }, 1500);
}

document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll(".typeBtn").forEach(btn => {
        btn.addEventListener("click", () => {
            const type = parseInt(btn.dataset.type);
            if (selectedTypes.includes(type)) {
                selectedTypes = selectedTypes.filter(t => t !== type);
                btn.classList.remove("active");
            } else {
                selectedTypes.push(type);
                btn.classList.add("active");
            }
        });
    });
});
