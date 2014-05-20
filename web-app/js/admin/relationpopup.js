
var relationPopupCreateBody = function (json, excludeValues) {
    var html = "<table class=\"table table-bordered\">";
    html += relationPopupCreateHeader(json[0]);
    $.each( json, function( key, value ) {
        if (excludeValues.indexOf(value['id'].toString()) == -1) {
            html += relationPopupCreateLine(value);
        }
    });
    html += "</table>";
    return html;
};


var relationPopupCreateHeader = function (json) {
    var line = "<tr><th>&nbsp;</th>";

    $.each( json, function( key, value ) {
        if (key.lastIndexOf("_", 0) !== 0) {
            line += "<th>" + htmlEncode(key) + "</th>";
        }
    });
    line += "</tr>";
    return line;
};


var relationPopupCreateLine = function(json) {
    var line = "<tr><td><input class=\"relationpopup-radio\" type=\"radio\" name=\"elementId\" value=\"" + htmlEncode(json['id']) + "\" data-txt=\"" + htmlEncode(json['__text__']) + "\"></input></td>";
    $.each( json, function( key, value ) {
        if (key.lastIndexOf("_", 0) !== 0) {
            line += "<td>" + htmlEncode(value) + "</td>";
        }
    });
    line += "</tr>";
    return line;
};


var relationPopupOpenConfirmDialog = function(title, body, successFunction){
    var dialogId = "confirm-dialog-" + new Date().getTime();

    var dialog = "<div id='" + dialogId + "' class='modal fade in' aria-hidden='false' aria-labelledby='confirmLabel' role='dialog' tabindex='-1'>"
    dialog += "<div class='modal-dialog'><div class='modal-content'>";
    dialog += "<div class='modal-header'>";
    dialog += "<buton class='close' aria-hidden='true' data-dismiss='modal' type='button'>x</buton>";
    dialog += "<h4 class='modal-title' id='confirmLabel'>" + title + "</h4>";
    dialog += "</div>";
    dialog += "<div class='modal-body'>";
    dialog +=  body;
    dialog += "</div>";
    dialog += "<div class='modal-footer'>";
    dialog += "<button class='btn btn-default' data-dismiss='modal' type='button'>Cancel</button>";
    dialog += "<button class='btn btn-primary js-ok' data-dismiss='modal' type='button'>OK</button>";
    dialog += "</div>";
    dialog += "</div>";
    dialog += "</div>";
    dialog += "</div>";


    $("body").append(dialog);
    $("#" + dialogId).on('hidden.bs.modal', function(){
        $("#" + dialogId).remove();
    });

    $("#" + dialogId).find(".js-ok").on('click', function(){
        var selectedItem = $('.relationpopup-radio:checked');
        if (selectedItem !== undefined) {
            var objectId = selectedItem.val();
            var objectText = selectedItem.data('txt');
            successFunction(objectId, objectText);
        }
    });

    $("#" + dialogId).modal({"backdrop" : "true", "keyboard":"true"});
};


var htmlEncode = function(value){
    if (value) {
        return jQuery('<div />').text(value).html();
    } else {
        return '';
    }
}
