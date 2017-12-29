
/*modify the real file path if needed*/
document.write('<link type="text/css" rel="stylesheet" href="../assets/msgbox/msg.box.css"/>');
document.write('<script src="../assets/msgbox/msg.box.js"><\/script>');
var width = 400;
var height= 200;


function MSGAlert(msg, handler) {
	MsgBox.alert(msg, width, height, 'MSG Tips', handler);
}

function MSGConfirm(msg, handler) {
	MsgBox.confirmInfo(msg, width, height, 'MSG Tips', handler);
}

function MSGError(msg, handler) {
	MsgBox.errorInfo(msg, width, height, 'MSG Tips', handler);
}

function MSGSucceed(msg, handler) {
	MsgBox.succeedInfo(msg, width, height, 'MSG Tips', handler);
}