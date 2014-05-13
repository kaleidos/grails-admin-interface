var gulp = require('gulp'),
jade = require('gulp-jade'),
sass = require('gulp-ruby-sass');

var path = {
    'scss': 'front/src/scss/**/*.scss',
    'mainStyle': 'web-app/css/'
};

gulp.task('scss', function () {
    gulp.src(path.scss)
        .pipe(sass())
        .pipe(gulp.dest(path.mainStyle));
});

gulp.task('default', ['scss'], function () {
    gulp.watch(path.scss, ['scss']);
});
