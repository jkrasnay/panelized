
var DropDownMenu = require("./DropDownMenu");

function init() {
  DropDownMenu.init();
  console.log('Panelized initialized');
}

module.exports = {
  init: init,
  DataTable: require('./DataTable'),
  Modal: require('./Modal')
};

