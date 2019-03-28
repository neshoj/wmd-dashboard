{
    let Site = window.Site,
        token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    //Add animation to numerals
    $('.counter').counterUp({ delay: 100, time: 1500 });

}