var gulp = require('gulp'),
jade = require('gulp-jade'),
sass = require('gulp-ruby-sass');
connect = require('gulp-connect');

gulp.task('connect', function() {
  connect.server({
      root: 'dist',
      port: 9000
  });
});

var path = {
    'jade': ['src/**/*.jade', '!src/includes/*.jade'],
    'html': 'dist/',
    'scss': 'src/scss/**/*.scss',
    'mainStyle': 'dist/css/'
};

gulp.task('scss', function () {
    gulp.src(path.scss)
        .pipe(sass())
        .pipe(gulp.dest(path.mainStyle));
});

gulp.task('jade', function () {
    gulp.src(path.jade)
        .pipe(jade().on('error', function(err) {
            console.log(err);
        }))
        .pipe(gulp.dest(path.html));
});

gulp.task('default', ['connect'], function () {
    gulp.watch(path.jade, ['jade']);
    gulp.watch(path.scss, ['scss']);
});
