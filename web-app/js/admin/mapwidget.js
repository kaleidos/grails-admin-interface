app.view('mapwidget', ['$el'], function ($el) {
    "use strict";

    $el.on( "click", ".js-map-widget-refresh", function(event) {
        var value = $(this).closest(".map-widget").find(".js-map-widget-text").val();
        var html = "<iframe width='425' height='350' frameborder='0' scrolling='no' marginheight='0' marginwidth='0'";
        html += "src='https://maps.google.com/maps?f=q&amp;q=" + value + "&amp;output=embed'></iframe>";
        $(this).closest(".map-widget").find(".map-container").html(html);
    });
});
