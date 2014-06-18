var gulp = require('gulp'),
sass = require('gulp-ruby-sass');

var path = {
    'scss': 'front/src/scss/**/*.scss',
    'mainStyle': 'web-app/grails-admin/css/',
    'libsDir': 'web-app/grails-admin/libs/'
};

gulp.task('libs', function () {
    gulp.src(['front/dist/lib/bootstrap/dist/js/bootstrap.js'])
        .pipe(gulp.dest(path.libsDir + 'bootstrap/js/'));
    gulp.src(['front/dist/lib/bootstrap/dist/fonts/*'])
        .pipe(gulp.dest(path.libsDir + 'bootstrap/fonts/'));
    gulp.src(['front/dist/lib/bootstrap/dist/css/bootstrap.css',
              'front/dist/lib/bootstrap/dist/css/bootstrap-theme.css'])
        .pipe(gulp.dest(path.libsDir + 'bootstrap/css/'));
    gulp.src(['front/dist/lib/bootstrap-datepicker/css/*'])
        .pipe(gulp.dest(path.libsDir + 'bootstrap-datepicker/css/'));
    gulp.src(['front/dist/lib/bootstrap-datepicker/js/bootstrap-datepicker.js'])
        .pipe(gulp.dest(path.libsDir + 'bootstrap-datepicker/js/'));
    gulp.src(['front/dist/lib/bootstrap-datepicker/js/locales/*'])
        .pipe(gulp.dest(path.libsDir + 'bootstrap-datepicker/js/locales'));
    gulp.src(['front/dist/lib/select2/select2_locale*.js',
              'front/dist/lib/select2/select2.js',
              'front/dist/lib/select2/select2-bootstrap.css',
              'front/dist/lib/select2/select2-spinner.gif',
              'front/dist/lib/select2/select2.css',
              'front/dist/lib/select2/select2.png',
              'front/dist/lib/select2/select2x2.png',
              'front/dist/lib/select2/select2.jquery.json'
              ])
        .pipe(gulp.dest(path.libsDir + 'select2/'));
    gulp.src(['front/dist/lib/lodash/dist/lodash.js',
              'front/dist/lib/handlebars/handlebars.js',
              'front/dist/lib/jQuery.serializeObject/jquery.serializeObject.js',
              'front/dist/lib/jquery/dist/jquery.js',
              'front/dist/lib/injectorJS/src/injector.js',
              'front/dist/lib/parsleyjs/dist/parsley.js',
              'front/dist/lib/parsleyjs/dist/parsley.remote.js'])
        .pipe(gulp.dest(path.libsDir));
});

gulp.task('scss', function () {
    gulp.src(path.scss)
        .pipe(sass())
        .pipe(gulp.dest(path.mainStyle));
});

gulp.task('default', ['scss'], function () {
    gulp.watch(path.scss, ['scss']);
});
