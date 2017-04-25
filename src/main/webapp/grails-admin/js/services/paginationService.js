app.service('paginationService', ['templateService'], function (templateService) {
    "use strict";

    var max_left_right = 3;

    function build (currentPage, totalPages) {
        var leftPoints = currentPage - max_left_right - 1
        var rightPoints = currentPage + max_left_right + 1;
        var result = [];
        totalPages++; //Pages count from 1

        if (totalPages > 1) {
            for (var i = 1; i <= totalPages; i++) {
                if (i > leftPoints && i < rightPoints) {
                    result.push({'text': i, 'page': i, 'active': i === currentPage});
                } else if (i === leftPoints) {
                    result.push({'text': 1, 'page': 1});

                    if (1 !== leftPoints) {
                        result.push({'text': '...', 'disabled': true});
                    }
                } else if(i === rightPoints) {
                    if (rightPoints !== totalPages) {
                        result.push({'text': '...', 'disabled': true});
                    }

                    result.push({'text': totalPages, 'page': totalPages});
                }
            }
        }

        return templateService.get('grails-admin-pagination', result);
    }

    return {
        build: build
    };
});
