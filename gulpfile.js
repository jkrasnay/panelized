
// Thanks to http://blog.avisi.nl/2014/04/25/how-to-keep-a-fast-build-with-browserify-and-reactjs/
// for how to configure watchify

var browserify = require('browserify');
var gulp       = require('gulp');
var gutil      = require('gulp-util');
var less       = require('gulp-less');
var source     = require("vinyl-source-stream");
var transform  = require('vinyl-transform');
var watchify   = require('watchify');

var dest = 'target/classes/ca/krasnay/panelized';

gulp.task('default', [ 'css', 'js' ]);

gulp.task('watch', [ 'watch-css', 'watch-js' ]);

gulp.task('css', function () {
  return gulp.src('src/main/less/Panelized.less')
  .pipe(less({ paths: [ './node_modules' ] }))
  .pipe(gulp.dest(dest));
});

gulp.task('watch-css', [ 'css' ], function() {
  gulp.watch('src/main/less/*.less', ['css']);
});

gulp.task('js', function () {
  return scripts(false);
});

gulp.task('watch-js', [ 'js' ], function () {
  return scripts(true);
});

function handleError(task) {
  return function(err) {
    gutil.log(gutil.colors.red(err));
    //notify.onError(task + ' failed, check the logs..')(err);
  };
}

function scripts(watch) {

  var bundler, rebundle;

  bundler = browserify('./src/main/js/Panelized.js', {
    standalone: 'Panelized',
    //debug: !production,
    cache: {}, // required for watchify
    packageCache: {}, // required for watchify
    fullPaths: watch // required to be true only for watchify
  });

  if(watch) {
    bundler = watchify(bundler);
    bundler.on('time', function (time) { gutil.log('Generated js in', gutil.colors.magenta(time, 'ms')); });
  }

  //bundler.transform(reactify);

  rebundle = function() {
    var stream = bundler.bundle();
    stream.on('error', handleError('Browserify'));
    stream = stream.pipe(source('./Panelized.js')); // weirdly, this is the name of the *target* file
    return stream.pipe(gulp.dest(dest /*'./target/classes/ca/krasnay/panelized'*/));
  };

  bundler.on('update', rebundle);

  return rebundle();

}

