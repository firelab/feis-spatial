/**
 * Busy Status
 */

if (!window["busystatus"]) {
	var busystatus = {};
	
}

busystatus.onStatusChange = function onStatusChange(data) {
	var status = data.status;
	
	if (status === "begin") {
		document.body.style.cursor = 'wait';
	} else {
		document.body.style.cursor = 'auto';
	}
	
};

jsf.ajax.addOnEvent(busystatus.onStatusChange);