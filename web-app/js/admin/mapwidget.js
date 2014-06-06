app.view('mapwidget', ['$el'], function ($el) {
    "use strict";

    $el.on( "click", ".js-map-widget-refresh", function(event) {
        var value = $el.find(".js-map-widget-text").val();
        var mapContainer = $el.find(".map-container");

        var mapIframe = $("<iframe>")
            .attr({
                "width": 425,
                "height": 350,
                "frameborder": 0,
                "scrolling": 0,
                "marginheight": 0,
                "marginwidth": 0,
                "src": "https://maps.google.com/maps?f=q&q=" + value + "&output=embed"
            });

        mapContainer.html(mapIframe)
    });
});
