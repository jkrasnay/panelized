var $ = require('./JQuery'),
    Pikaday = require('pikaday');

function init(element, options) {

  var opts = $.extend({
    field: $(element)[0]
  }, options);

  $(element).data('pikaday', new Pikaday(opts));

}

module.exports = {
  init: init
};

