
var $ = require('./JQuery');
var Wicket = require('./Wicket');

/**
 * Initializes a data table.
 *
 * @param e {string|object} Element or jQuery selector representing the data table.
 * @param reorderable {boolean} True if the rows of the data table can be re-ordered (required jQuery UI)
 * @param remoteSearchUrl {string} URL of the data table's behaviour.
 */
function init(e, reorderable, remoteSearchUrl) {

    // See http://stackoverflow.com/questions/1909441/jquery-keyup-delay
    var delay = (function () {
        var timer = 0;
        return function (callback, ms) {
            clearTimeout(timer);
            timer = setTimeout(callback, ms);
        };
    })();

    var localSearch = function () {

        var term = $(this).val().toLowerCase(),
            $rows = $(this).parents('div.bdr').find('tbody tr');

        $rows.hide();
        $rows.filter(function () {
            return $(this).find('td.f').text().toLowerCase().indexOf(term) === 0;
        }).show();
    };

    var searchFunction = localSearch;

    var moveStartIndex;

    if (remoteSearchUrl) {
        searchFunction = function () {
            var searchTerm = $(this).val().toLowerCase();
            delay(function () {
              Wicket.Ajax.get({ u: remoteSearchUrl + "&filter=" + encodeURIComponent(searchTerm) });
            }, 500);
        };
    }

    $(e).find('div.searchField input').bind('keyup', searchFunction).focus().caretToEnd();

    var clearQuickFilter = function () {
        Wicket.Ajax.get({ u: remoteSearchUrl + "&filter=" });
        $(this).siblings('input').val('').focus();
    };

    $(e).find('div.searchField a').bind('click', clearQuickFilter);

    if (reorderable) {
        $(e).find('tbody').sortable({

            handle: '.drag-handle',

            helper: function (event, ui) {
                ui.children().each(function() {
                    $(this).width($(this).width());
                });
                return ui.clone().addClass("dt-drag").css({ opacity: 0.9 });
            },

            start: function (event, ui) {
                moveStartIndex = $(ui.item).parent().children().index(ui.item.get(0));
            },

            update: function (event, ui) {
                var moveEndIndex = $(ui.item).parent().children().index(ui.item.get(0));
                var url = remoteSearchUrl + "&move=" + moveStartIndex + "," + moveEndIndex;
                Wicket.Ajax.get({ u: url });
            }
        })
        .disableSelection();
    }

    //
    // Set up click handling on checkboxes and links in the tools column
    //

    /**
     * Returns a JQuery object consisting of all rows (TR elements) that
     * have their checkboxes checked.
     */
    var checkedRows = function () {
        return $(e).find('tr').has('td.dt-tools input:checkbox:checked');
    };

    /**
     * Returns a JQuery object consisting of all A elements that are on
     * checked rows and are enabled for the given action index.
     */
    var activeActions = function (actionIndex) {
        return checkedRows().find('a[data-action=' + actionIndex + ']').not('.disabled');
    };

    /**
     * Updates the CSS class of multi-actions (those shown in the cog menu
     * in the table header) to reflect selected rows.
     */
    var updateMultiActionEnablement = function () {

        $(e).find('th.dt-tools a').not('.btn').each(function () {
            var $this = $(this);
            var actionIndex = $this.data('action');
            if (activeActions(actionIndex).size() > 0) {
                $this.removeClass('disabled');
            } else {
                $this.addClass('disabled');
            }
        });


        //
        // Disable page-level menu items if there are any checked checkboxes
        //
        var checked = ($(e).find('td.dt-tools input:checked').size() > 0);

        $('.pg-title .dropdown-menu a').each(function () {
            var $this = $(this);
            if (checked) { // TODO only enable/disable if there's a matching multi-action on the data table

                if (!$this.data('onclick')) {
                    $this.data('onclick', $this.attr('onclick'));
                }

                $this.addClass('disabled')
                .attr('onclick', 'return false');

            } else {

                //
                // NOTE: force-disabled set by DropDownMenuPanel if the action itself is disabled
                //

                $this.not('.force-disabled').removeClass('disabled');

                if ($this.data('onclick')) {
                    $this.attr('onclick', $this.data('onclick'));
                }
            }
        });

    };

    updateMultiActionEnablement();

    $(e).on('click', 'th.dt-tools input:checkbox', function (e) {
        $(this).parents('table').find('td.dt-tools input:checkbox').prop('checked', $(this).prop('checked'));
        updateMultiActionEnablement();
    });

    $(e).on('click', 'th.dt-tools a[data-action]', function (e) {

        var $this = $(this);

        if (!$(this).hasClass('disabled')) {

            var actionIndex = $this.data('action');
            var checkedCount = checkedRows().size();

            var rowIds = activeActions(actionIndex).parents('tr').map(function() {
                return $(this).data('rowId');
            }).get().join(',');

            var url = remoteSearchUrl + '&action=' + actionIndex + '&rowId=' + rowIds + "&checkedCount=" + checkedRows().size();

            checkedRows().has('a[data-action=' + actionIndex + '].disabled')
            .filter(function () { return $(this).data('row-name'); })
            .slice(0, 10).each(function() {
                url += '&badRow=' + encodeURIComponent($(this).data('rowName'));
            });

            Wicket.Ajax.get({ u: url });
        }

        e.preventDefault();

    });

    $(e).on('click', 'td.dt-tools input:checkbox', function (e) {
        updateMultiActionEnablement();
    });

    $(e).on('click', 'td.dt-tools a[data-action]', function (e) {
        if (!$(this).hasClass('disabled')) {
            var url = remoteSearchUrl + '&action=' + $(this).data('action') + '&rowId=' + $(this).parents('tr').data('rowId');
            Wicket.Ajax.get({ u: url });
        }
        e.preventDefault();
    });

    $(e).on('click', 'td.dt-comment a', function (e) {
        var url = remoteSearchUrl + '&comment=1&rowId=' + $(this).parents('tr').data('rowId');
        Wicket.Ajax.get({ u: url });
        e.preventDefault();
    });

}


// From https://github.com/DrPheltRight/jquery-caret
//Set caret position easily in jQuery
//Written by and Copyright of Luke Morton, 2011
//Licensed under MIT
(function ($) {
 // Behind the scenes method deals with browser
 // idiosyncrasies and such
 $.caretTo = function (el, index) {
     if (el.createTextRange) {
         var range = el.createTextRange();
         range.move("character", index);
         range.select();
     } else if (el.selectionStart !== null) {
         el.focus();
         el.setSelectionRange(index, index);
     }
 };

 // Another behind the scenes that collects the
 // current caret position for an element

 // TODO: Get working with Opera
 $.caretPos = function (el) {
     if ("selection" in document) {
         var range = el.createTextRange();
         try {
             range.setEndPoint("EndToStart", document.selection.createRange());
         } catch (e) {
             // Catch IE failure here, return 0 like
             // other browsers
             return 0;
         }
         return range.text.length;
     } else if (el.selectionStart !== null) {
         return el.selectionStart;
     }
 };

 // The following methods are queued under fx for more
 // flexibility when combining with $.fn.delay() and
 // jQuery effects.

 // Set caret to a particular index
 $.fn.caret = function (index, offset) {
     if (typeof(index) === "undefined") {
         return $.caretPos(this.get(0));
     }

     return this.queue(function (next) {
         if (isNaN(index)) {
             var i = $(this).val().indexOf(index);

             if (offset === true) {
                 i += index.length;
             } else if (typeof(offset) !== "undefined") {
                 i += offset;
             }

             $.caretTo(this, i);
         } else {
             $.caretTo(this, index);
         }

         next();
     });
 };

 // Set caret to beginning of an element
 $.fn.caretToStart = function () {
     return this.caret(0);
 };

 // Set caret to the end of an element
 $.fn.caretToEnd = function () {
     return this.queue(function (next) {
         $.caretTo(this, $(this).val().length);
         next();
     });
 };
}(jQuery));



//
// From http://stackoverflow.com/questions/6745098/jquery-ui-sortable-doesnt-work-on-touch-devices-based-on-android-or-ios
//
// Support drag-and-drop on iPad
//
// Unfortunately this also breaks the cog menu, so we've commented it out
// until we can sort it out (or until JQueryUI is fixed to support touch)

/*
 * Content-Type:text/javascript
 *
 * A bridge between iPad and iPhone touch events and jquery draggable,
 * sortable etc. mouse interactions.
 * @author Oleg Slobodskoi
 *
 * modified by John Hardy to use with any touch device
 * fixed breakage caused by jquery.ui so that mouseHandled internal flag is reset
 * before each touchStart event
 *
 */
/*
(function( $ ) {

    $.support.touch = typeof Touch === 'object';

    if (!$.support.touch) {
        return;
    }

    var proto =  $.ui.mouse.prototype,
    _mouseInit = proto._mouseInit;

    $.extend( proto, {
        _mouseInit: function() {
            this.element
            .bind( "touchstart." + this.widgetName, $.proxy( this, "_touchStart" ) );
            _mouseInit.apply( this, arguments );
        },

        _touchStart: function( event ) {
            if ( event.originalEvent.targetTouches.length != 1 ) {
                return false;
            }

            this.element
            .bind( "touchmove." + this.widgetName, $.proxy( this, "_touchMove" ) )
            .bind( "touchend." + this.widgetName, $.proxy( this, "_touchEnd" ) );

            this._modifyEvent( event );

            $( document ).trigger($.Event("mouseup")); //reset mouseHandled flag in ui.mouse
            this._mouseDown( event );

            return false;
        },

        _touchMove: function( event ) {
            this._modifyEvent( event );
            this._mouseMove( event );
        },

        _touchEnd: function( event ) {
            this.element
            .unbind( "touchmove." + this.widgetName )
            .unbind( "touchend." + this.widgetName );
            this._mouseUp( event );
        },

        _modifyEvent: function( event ) {
            event.which = 1;
            var target = event.originalEvent.targetTouches[0];
            event.pageX = target.clientX;
            event.pageY = target.clientY;
        }

    });

})( jQuery );
*/

module.exports = {
  init: init
};


