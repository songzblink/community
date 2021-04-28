function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            console.log(response);
            if (response.code == 200) {
                $("#comment_section").hide();
            } else {
                alert(response.message);
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=8f3c188a9891dd464883&redirect_uri=http://localhost:8888/callback&scope=user&state=1");
                        window.localStorage.setItem("closeable", "true");
                    }
                } else {

                }
            }
        },
        dataType: "json",
        contentType: "application/json"
    });
}