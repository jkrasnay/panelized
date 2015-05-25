var $ = require('./JQuery'),
    Pikaday = require('pikaday');

function init(element, options) {

  var opts = $.extend({
    field: $(element)[0]
  }, options);

  console.log('calling Pikaday', opts);

  new Pikaday(opts);

}

module.exports = {
  init: init
};

