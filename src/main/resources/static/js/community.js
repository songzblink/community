/**
 * 提交回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                // 回复成功后刷新页面
                window.location.reload();
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


/**
 * 展开二级评论
 */
function collapseComments(e) {
    // 获取 th:data-id="${comment.id}"
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    var dom = document.querySelector("#comment-" + id);
    // 关闭二级评论
    if (dom.className.includes("in")) {
        comments.removeClass("in");
    } else { // 展开二级评论
        var subCommentContainer = $("#comment-" + id);
        // 如果加载过了就不加载了
        if (!subCommentContainer.hasClass("loaded")) {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        html: comment.user.name
                    })).append($("<div/>", {
                        html: comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        html: moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                })
            });
            // 标记加载过了
            subCommentContainer.addClass("loaded");
        }
        comments.addClass("in");
    }

}