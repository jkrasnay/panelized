
var $ = require('./JQuery'),
    Wicket = require('./Wicket');

/**
 * Function that calls back to another function after a given delay.
 * If called again before the delay fires, the timer is reset.
 *
 * See http://stackoverflow.com/questions/1909441/jquery-keyup-delay
 */
var delay = (function () {
  var timer = 0;
  return function (callback, ms) {
    clearTimeout(timer);
    timer = setTimeout(callback, ms);
  };
})();

function doCallback(url, searchString) {
  Wicket.Ajax.get({ u: url + "&searchString=" + encodeURIComponent(searchString) });
}

/**
 * Initializes the search field.
 *
 * @param {object|string} el Element or identifier for the input field to be initialized as a search field.
 *
 * @param {string} url Callback URL. When doing a search, this URL is called with the parameter `searchString`
 * set to the users search string.
 *
 * @param {object} options Optional object with further options.
 *
 * @param {number} options.searchDelay Number of milliseconds after the last key press after which the callback
 * URL is automatically called. By default, the search is not done until the user presses the Enter key
 * or clicks the search icon.
 */
function init(el, url, options) {

  var opts = $.extend({}, options),
      $input = $(el);

  $input.on('keyup', function (e) {

    e.preventDefault();
    e.stopPropagation();

    var searchString = $(this).val();

    if (e.which == 13) {
      doCallback(url, searchString);
    } else {
      if (opts.searchDelay) {
        delay(function () { doCallback(url, searchString); }, options.searchDelay);
      }
    }

  });

  $input.parents('.pnl-SearchField').find('.pnl-SearchField-clear').on('click', function (e) {
    $input.val('');
    $input.focus();
    doCallback(url, '');
  });
}

module.exports = {
  init: init
};

