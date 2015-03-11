/*
 * Panelized DropDownMenu
 */

var $ = require('./JQuery');

function closeMenus() {
  $('.pnl-DropDownMenu--open').removeClass('pnl-DropDownMenu--open');
}

/**
 * Adds a click handler to the document.
 */
function init() {

  $(document).on('click', function (e) {

    var $target = $(e.target);

    if ($target.hasClass('pnl-DropDownMenu-toggle') || $target.parents('.pnl-DropDownMenu-toggle').size()) {

      var $menu = $target.parents('.pnl-DropDownMenu');

      $('.pnl-DropDownMenu--open').not($menu).removeClass('pnl-DropDownMenu--open');

      $menu.toggleClass('pnl-DropDownMenu--open');

      e.preventDefault();

    } else {
      $('.pnl-DropDownMenu--open').removeClass('pnl-DropDownMenu--open');
    }

  });

}

module.exports = {
  init: init
};

