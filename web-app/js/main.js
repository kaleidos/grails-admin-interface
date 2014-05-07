$('.select2-choice').select2();

$('#confirm').on('show.bs.modal', function (e) {
    var id = $(e.relatedTarget).data('id');

    if (id) {
        $(this).find('[name="id"]').val(id);
    }
});

(function (msg) {
    if (msg.length) {
        setTimeout(function () {
            msg.fadeOut();
        }, 5000);
    }
}($("#msg")));

$('.main-form').parsley({
    errorClass: "has-error",
    classHandler: function(el) {
        return el.$element.closest(".form-group");
    },
    errorsWrapper: "<span class='help-block'></span>",
    errorTemplate: "<span></span>"
});
