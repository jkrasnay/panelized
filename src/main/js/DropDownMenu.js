/*
 * Panelized DropDownMenu
 */

var $ = require('./JQuery');

/**
 * Adds a click handler to the document.
 */
function init() {

  $('body').on('click', '.pnl-DropDownMenu-toggle', function (e) {
    $(this).parents('.pnl-DropDownMenu').toggleClass('pnl-DropDownMenu--open');
    e.preventDefault();
    e.stopPropagation();
  });

  $('body').on('click', function (e) {
    $('.pnl-DropDownMenu--open').removeClass('pnl-DropDownMenu--open');
  });

}

module.exports = {
  init: init
};

