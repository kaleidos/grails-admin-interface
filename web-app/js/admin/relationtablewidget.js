$( ".relationtablewidget" ).on( "click", ".js-relationtablewidget-delete", function(event) {
    event.preventDefault();
    var button = $(this);
    var r = confirm( "Do you wish to delete the relation?" );
    if (r == true) {
        button.closest( ".relationtablewidget" ).find("input[type='hidden'][value="+button.data('value')+"]").remove();
        button.closest( "tr" ).remove()

    }
});


$( ".js-relationtablewidget-add").click(function(event) {
    var target = $(this).data("target");


    $.ajax({
        method: "GET",
        url: $(this).data('url'),
        dataType: "JSON",
        contentType: 'application/json; charset=utf-8',
    })
    .done(function (result) {

        var html = "<table class=\"table table-bordered\">";
        html += createHeader(result[0]);
        $.each( result, function( key, value ) {
            html += createLine(value);
        });
        html += "</table>"



        $(target).find(".modal-body").html(html);
    })
    .fail(function (result) {
        $(target).find(".modal-body").html("ERROR");
    });

});

$( ".js-relationtablewidget-add-action").click(function(event) {
    var button = $(this);
    var url = button.data('url');
    var val = $(this).closest('.modal-content').find('.relationtablewidget-radio:checked').val();
    var txt = $(this).closest('.modal-content').find('.relationtablewidget-radio:checked').data('txt');

    var detailUrl = $(".relationtablewidget").find("table").data('detailurl');
    var propertyName = $(".relationtablewidget").find("table").data('property-name');
    detailUrl = detailUrl.replace("0", val);

    var newLine = "<tr><td><a href=\""+detailUrl+"\">"+txt+"</a></td>";
    if ($(".relationtablewidget").find("table").data('optional')) {
        newLine += "<td><a class=\"btn btn-default btn-sm js-relationtablewidget-delete\" data-value=\""+val+"\" href=\"#\"><span class=\"glyphicon glyphicon-trash\"></span> Delete</a></td>"
    }
    newLine += "</tr>"

    $(".relationtablewidget").find("tbody").append(newLine)

    $(".relationtablewidget").find("input[type='hidden']:last").after("<input type=\"hidden\" name=\""+propertyName+"\"  value=\""+val+"\" />")


});



function createHeader(json) {
    var line = "<tr><th>&nbsp;</th>";

    $.each( json, function( key, value ) {
        if (key.lastIndexOf("_", 0) !== 0) {
            line += "<th>"+escape(key)+"</th>";
        }
    });
    line += "</tr>";
    return line;
}

function createLine(json) {
    var line = "<tr><td><input class=\"relationtablewidget-radio\" type=\"radio\" name=\"elementId\" value=\""+escape(json['id'])+"\" data-txt=\""+escape(json['__text__'])+"\"></input></td>";
    $.each( json, function( key, value ) {
        if (key.lastIndexOf("_", 0) !== 0) {
            line += "<td>"+escape(value)+"</td>";
        }
    });
    line += "</tr>";
    return line;
}
