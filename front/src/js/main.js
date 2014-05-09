$('.select2-choice').select2();

$('#confirm').on('show.bs.modal', function (e) {
    var url = $(e.relatedTarget).data('url');

    if (url) {
        $(this).find('form').attr('action', url);
    }
});

(function (msg) {
    if (msg.length) {
        setTimeout(function () {
            msg.fadeOut();
        }, 5000);
    }
}($("#msg")));

$('input[type=date]').datepicker();

$('.form-action').on('click', function () {
    var form = $(this).closest('form');
    var btnUrl = $(this).data('url');

    form.submit();

    form.off('grailsadmin:validated');
    form.on('grailsadmin:validated', function (event, result) {
        if (btnUrl) {
            if (result && result.id) {
                window.location.href = btnUrl+ '?id=' + result.id;
            } else {
                window.location.href = btnUrl;
            }
        } else {
            window.location.reload();
        }
    });
});

function searchFieldInstance(form, fieldName) {
    for(var i = 0; i < form.fields.length; i++) {
        if (form.fields[i].$element.attr('name') === fieldName) {
            return form.fields[i];
        }
    }
}

$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

//override parsley remote
window.ParsleyExtend = $.extend(window.ParsleyExtend, {
    onSubmitValidate: function (event) {
        var that = this;
        var form = that.$element;
        var grailsadminRemote = form.attr('grailsadmin-remote');

        // This is a Parsley generated submit event, do not validate, do not prevent, simply exit and keep normal behavior
        if (true === event.parsley)
            return;

        // Clone the event object
        this.submitEvent = $.extend(true, {}, event);

        // Prevent form submit and immediately stop its event propagation
        if (event instanceof $.Event) {
            event.stopImmediatePropagation();
            event.preventDefault();
        }

        return this._asyncValidateForm(undefined, event)
            .then(function () {
                var deferred = $.Deferred();

                if (grailsadminRemote && grailsadminRemote === 'enabled') {
                    that.reset();

                    //ajax submit
                    $.ajax({
                        method: form.data('method'),
                        url: form.attr('action'),
                        dataType: "JSON",
                        contentType: 'application/json; charset=utf-8',
                        data: JSON.stringify(form.serializeObject())
                    })
                        .done(function (result) {
                            deferred.resolve(result);
                        })
                        .fail(function (result) {
                            var errors = result.responseJSON.errors;

                            for(var i = 0; i < errors.length; i++) {
                                window.ParsleyUI.addError(searchFieldInstance(that, errors[i].field), errors[i].field, errors[i].message);
                            }

                            deferred.fail();
                        });
                } else {
                    deferred.resolve();
                }

                return deferred.promise();
            })
            .done(function (result) {
                if (grailsadminRemote) {
                    form.trigger('grailsadmin:validated', result);
                } else {
                    // If user do not have prevented the event, re-submit form
                    if (!that.submitEvent.isDefaultPrevented())
                        that.$element.trigger($.extend($.Event('submit'), { parsley: true }));
                }
            });
    }
});

$('.validate-form').parsley({
    errorClass: "has-error",
    classHandler: function(el) {
        return el.$element.closest(".form-group");
    },
    errorsWrapper: "<span class='help-block'></span>",
    errorTemplate: "<span></span>"
});
