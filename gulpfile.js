var gulp = require('gulp'),
jade = require('gulp-jade'),
sass = require('gulp-ruby-sass');

var path = {
    'scss': 'front/src/scss/**/*.scss',
    'mainStyle': 'web-app/css/',
    'js': 'front/src/js/**/*.js',
    'mainJs': 'web-app/js/',
};

gulp.task('scss', function () {
    gulp.src(path.scss)
        .pipe(sass())
        .pipe(gulp.dest(path.mainStyle));
});

gulp.task('js', function () {
    gulp.src(path.js)
        .pipe(gulp.dest(path.mainJs));
});

gulp.task('default', ['scss', 'js'], function () {
    gulp.watch(path.scss, ['scss']);
    gulp.watch(path.js, ['js']);
});
