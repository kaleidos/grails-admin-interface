$( ".js-relationtablewidget-delete" ).click(function(event) {
    event.preventDefault();
    var button = $(this);
    var r = confirm( "Do you wish to delete the relation?" );
    if (r == true) {
        $.ajax({
            method: button.data('method'),
            url: button.data('url'),
            dataType: "JSON",
            contentType: 'application/json; charset=utf-8',
        })
        .done(function (result) {
            button.closest( "tr" ).remove()
        })
        .fail(function (result) {
            var errors = result.responseJSON.errors;

            alert("Error")
        });
    }
});
