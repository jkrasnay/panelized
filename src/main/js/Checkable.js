
var $ = require('./JQuery');

/**
 * Function that enables or disables child inputs of a "checkable" based on
 * whether the checkable input (a radio button or checkbox) is checked.
 * `this` must refer to the actual input element representing the checkable.
 * Called from a click handler installed globally, and also when a modal popup
 * is shown (see effRegModal.js).
 */
function enableChildren() {

  console.log('Checkable.enableChildren');

  var $children = $(this).parents('.pnl-Checkable').find('input,select,textarea').not(this);

  if (this.checked) {

    // TODO only re-enable if we don't have the 'disabled' class
    $children.not('.disabled').removeAttr('disabled');
    $children.first().focus();

    if ($(this).attr('type') === 'radio') {
      //
      // If this is a radio button, disable the children of other radio
      // buttons in the same group, as they are of course now not selected
      //
      var name = $(this).attr('name');
      $('input[name="' + name + '"]').not(this).each(function () {
        $(this).parents('.pnl-Checkable').find('input,select,textarea').not(this).attr('disabled', 'disabled');
      });
    }

    //$(this).parent().find('.show-when-parent-checked').css("display", "");

  } else {

    $children.attr('disabled', 'disabled');

    //$(this).parent().find('.show-when-parent-checked').css("display", "none");

  }

}

/**
 * Install doc-level click handler on checkables to run enableChildren
 */
function init() {
  $(document).on('click', '.pnl-Checkable-input', enableChildren);
}

module.exports = {
  init: init,
  enableChildren: enableChildren
};

