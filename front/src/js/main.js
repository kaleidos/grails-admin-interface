$('.select2-choice').select2();

$('#confirm').on('show.bs.modal', function (e) {
    var id = $(e.relatedTarget).data('id');

    $(this).find('[name="id"]').val(id);
});
