app.view('relationpopuponewidget', ['$el'], function ($el) {
    "use strict";

    $el.find(".js-relationpopuponewidget-list").on('click', function(){
        event.preventDefault();
        var val = window.prompt("JSON","1,TEST");
        var splitval = val.split(',');
        app.addOneElementCallBack(splitval[0], splitval[1]);
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
            app.addOneElementCallBack(result["id"], result["name"]);
        })
        .fail(function(result){
            alert(">> FAIL " + result);
        });
    });

    $el.find(".js-relationpopuponewidget-delete").on('click', function(){
        event.preventDefault();
    });

    app.addOneElementCallBack = function(objectId, objectText) {
        console.log($el)
        $el.find(".js-one-rel-value").val(objectId);
        $el.find(".js-one-rel-text").text(objectText);
    };
});
