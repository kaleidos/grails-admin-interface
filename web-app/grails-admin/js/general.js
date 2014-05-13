app.load.onLoad(function () {
    "use strict";

    var msg = $("#msg");

    if (msg.length) {
        setTimeout(function () {
            msg.fadeOut();
        }, 5000);
    }
});
