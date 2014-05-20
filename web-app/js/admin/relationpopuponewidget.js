app.view('relationpopuponewidget', ['$el'], function ($el) {
    "use strict";

    $el.find(".js-relationpopuponewidget-list").on('click', function(){
        console.log($(this).data('url'));

        $.ajax({
            method: "GET",
            url:$(this).data('url'),
            dataType: "JSON",
            contentType: 'application/json; charset=utf-8',
        })
        .done(function (result) {
            var html = relationPopupCreateBody(result, []);
            relationPopupOpenConfirmDialog("Select", html, $el.addOneElementCallBack);
        })
        .fail(function (result) {
            $el.find(".modal-body").html("ERROR");
        });

    });

    $el.find(".js-relationpopuponewidget-new").on('click', function(){
        event.preventDefault();
        var val = window.prompt("JSON del elemento","{}");
        $.ajax({
            method: $el.data('method'),
            url: $el.attr('action'),
            dataType: "JSON",
            contentType: 'application/json; charset=utf-8',
            data: val
        })
        .done(function(result){
            $el.addOneElementCallBack(result["id"], result["name"]);
        })
        .fail(function(result){
            alert(">> FAIL " + result);
        });
    });

    $el.find(".js-relationpopuponewidget-delete").on('click', function(){
        event.preventDefault();
        $el.removeElementCallBack();
        $(".js-relationpopuponewidget-delete").hide();
    });

    $el.addOneElementCallBack = function(objectId, objectText) {
        console.log($el)
        $el.find(".js-one-rel-value").val(objectId);
        $el.find(".js-one-rel-text").text(objectText);
        $(".js-relationpopuponewidget-delete").show();
    };

    $el.removeElementCallBack = function() {
        console.log($el)
        $el.find(".js-one-rel-value").val(null);
        $el.find(".js-one-rel-text").text("<< empty >>");
    };
});
