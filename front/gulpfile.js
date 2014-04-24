var gulp = require('gulp');
var jade = require('gulp-jade');
var watch = require('gulp-watch')

gulp.task('default', function () {
    gulp.src('src/**/*.jade')
        .pipe(watch(function(files) {
            return files.pipe(jade())
                .pipe(gulp.dest('./dist/'))
        }));
});
