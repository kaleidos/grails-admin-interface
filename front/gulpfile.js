var gulp = require('gulp'),
    jade = require('gulp-jade'),
    connect = require('gulp-connect'),
    watch = require('gulp-watch');

gulp.task('connect', function() {
  connect.server({
      root: 'dist',
      port: 9000
  });
});

gulp.task('default', ['connect'], function () {
    gulp.src('src/**/*.jade')
        .pipe(watch(function(files) {
            return files.pipe(jade())
                .pipe(gulp.dest('./dist/'))
        }));
});
