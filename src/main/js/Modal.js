/*
 * Modal Popups
 *
 * This module implements modal popups.
 *
 * - the overlay must cover the whole scrollable area, not just the viewport
 *   with fixed positioning, because mobile browsers do not support fixed
 *   positioning. Otherwise, the user could just scroll away from the overlay.
 *
 * - the popup is centered on the page, if possible. If not, it is positioned
 *   close to the top and/or left side and the user is allowed to scroll around
 *   the page to see the rest of it.
 *
 * - clicking the overlay should *not* close the popup, as this makes it
 *   difficult to switch back and forth between a popup and, say, an email
 *   message from which you're copying data
 *
 * - modal displays must not require external CSS rules
 *
 * - this plugin will not change the size of the popup. It will only manage its
 *   visibility and position.
 *
 * - must handle bleed-through of select elements on IE6
 *
 * - must handle zoomed pages in iPad and other browsers
 *
 * We specifically avoid requiring overflow:auto on popups that are larger than
 * the viewport, since this requires a non-intuitive two-finger scroll on iPad
 *
 * Usage:
 *
 * var modal = require('./Modal');
 *
 * modal.show('#myPopup');
 * modal.hide();
 *
 * If you're hiding one modal and immediately showing another one, you can
 * keep the overlay to avoid flicker...
 *
 * modal.show('#myPopup');
 * modal.hide(true);
 * modal.show('#myOtherPopup');
 *
 */

var $ = require('./JQuery');

var overlayClass = 'modal-overlay',
    overlaySelector = '.' + overlayClass,
    isIE6 = /MSIE 6.0/.test(navigator.userAgent),
    modalStack = [],
    $overlay;

/**
 * Hides the topmost modal element.
 *
 * @param keepOverlay boolean If true, the overlay is not hidden. Use this when
 * switching modals to avoid flicker.
 */
function hide (keepOverlay) {

  if (modalStack.length === 0) {
    return;
  }

  var $modal = modalStack.pop();

  $modal.trigger('popup.hide');

  if (typeof tinyMCE != 'undefined') {
    $modal.find('textarea.richTextArea').each(function () {
      var id = $(this).attr('id');
      if (tinyMCE.get(id)) {
        // See http://www.tinymce.com/forum/viewtopic.php?pid=15440#p15440
        tinyMCE.execCommand('mceFocus', false, id);
        tinyMCE.execCommand('mceRemoveControl', false, id);
      }
    });
  }

  $modal
  .appendTo($modal.data('parent')) // Put the modal back to it's original place in the DOM, in case the original parent gets refreshed via ajax
  .hide();

  if (modalStack.length > 0) {

    // Move the overlay just below the next modal
    // We used to move the modal to the end of the body, but this caused a problem
    // in which TinyMCE controls on the modal lost their content.
    $overlay.insertBefore(modalStack[modalStack.length - 1]);

  } else if (!keepOverlay) {

    $(overlaySelector).hide();
    $overlay = 0;

    if (isIE6) {
      $('select').css('visibility', '');
    }
  }

}

function onResize() {

  var winW, winH;

  if ($overlay) {

    if (window.innerWidth) {
      // This works on modern browsers, and most importantly,
      // respects the zoom level on iPad et. al.
      winW = window.innerWidth;
      winH = window.innerHeight;
    } else {
      // Fallback for IE 6,7,8
      winW = $(window).width();
      winH = $(window).height();
    }

    for (var i = 0; i < modalStack.length; i++) {

      var $modal = modalStack[i];

      $modal.css({
        top: $(window).scrollTop() + Math.max((winH - $modal.height()) / 2 + i * 20, 20) + 'px',
        left: $(window).scrollLeft() + Math.max((winW - $modal.width()) / 2 + i * 20, 20) + 'px'
      });

    }

    $overlay.css({ height: $(document).height() + "px", width: $(document).width() + "px" });

  }
}

/**
 * Shows the given element as a modal.
 * @param e string|object DOM element or element ID to be shown as a modal popup.
 */
var show = function (e) {

  var $modal = $(e);

  if (modalStack.length > 0 && modalStack[modalStack.length - 1][0] == $modal[0]) {
    /* Modal is already on the stack. This can happen when
     * the user clicks the link to shown a modal a second time
     * before the first click takes effect
     */
    return;
  }

  if (!$(overlaySelector).size()) {
    $('body').append($('<div></div>').addClass(overlayClass).css({
      backgroundColor: '#000',
      opacity: 0.5,
      position: 'absolute',
      top: 0,
      left: 0,
      'z-index': 2 // Match Bootstrap active buttons
    }));
  }

  $overlay = $(overlaySelector).show().appendTo('body'); // Re-append, so it covers existing modals on the stack

  modalStack.push($modal);

  $modal.css('position', 'absolute')
  .data('parent', $modal.parent()) // Save the parent so we can put the popup back in place upon hide
  .appendTo('body')
  .show();

  onResize();

  // For some reason focusing the element now doesn't work in some cases
  // (e.g. clicking the favourite list link on the attendee list page)
  // Maybe Wicket is cancelling the event or something.

  setTimeout(function () { $modal.find('[autofocus]').first().focus(); }, 10);

  if (isIE6) {
    $('select').css('visibility', 'hidden');
  }

  // $modal.find('.checkable > input').each(EffReg.checkableChildEnablementHandler);

};

/* Old jQuery plugin
  var methods = {

    show: function () {
      show(this.first());
    },

    hide: function (keepOverlay) {
      hide(keepOverlay);
    },

    fixNestedForm: function () {

      // To support nested forms, Wicket transforms the <form> to a <div>.
      // When we move the modal to the bottom of the form, this remains
      // in place. This works *unless* the nested popup has a file upload
      // component, in which case the form loses the enctype="multipart/form-data"
      // and fails on the server side. This method fixes that up. It
      // should be called on the form component itself, not on the popup,
      // since we can't reliably determine the form <div> from the popup
      // itself.

      return this.each(function () {
        if (this.tagName.toLowerCase() == "div") {
          var id = $(this).attr("id");
          var $form = $("<form></form>").attr("method", "post").attr("enctype", "multipart/form-data").attr("id", id);
          $(this).removeAttr("id");
          $(this).wrap($form);
        }
      });

    },

    isVisible: function () {
      return $(this).first().css('display') != 'none';
    }

  };

  $.fn.effRegModal = function (method) {
    if (methods[method]) {
      return methods[method].apply(this, Array.prototype.slice.call( arguments, 1 ));
    } else {
      $.error('Method ' +  method + ' does not exist on jQuery.effRegModal');
    }
  };
*/

//
// Hook window events
//

$(window).resize(function () {
  setTimeout(function () {
    onResize();
  }, 50);
});

  // TODO: this is a problem b/c it hides the form without disabling it
  //       If an outer form is submitted, we could get e.g. validation errors
  //       from the hidden form. We should check the top modal if it has
  //       a "close callback URL" or some such and notify that.
  //
  //    $(window).keyup(function (e) {
  //        if (e.keyCode == 27) {
  //            hide(false);
  //        }
  //    });

module.exports = {
  show: show,
  hide: hide,
};

