window.red = {};

/**
 * Serveral sequential "window.location.href=''" can not be captured by webview delegate
 * Using iframe to fix this issue
 * https://stackoverflow.com/questions/2934789/triggering-shouldstartloadwithrequest-with-multiple-window-location-href-calls
 */
red.sendRequest = function(url){
	iFrame = document.createElement("IFRAME");
	iFrame.setAttribute("src", url);
	document.body.appendChild(iFrame); 
	iFrame.parentNode.removeChild(iFrame);
	iFrame = null;
}

red.getSelectedTextRect = function(){
	if(window.getSelection() && window.getSelection().getRangeAt(0)){
		return JSON.stringify(
			{
			X:window.getSelection().getRangeAt(0).getBoundingClientRect().left,
			Y:window.getSelection().getRangeAt(0).getBoundingClientRect().top,
			Width:window.getSelection().getRangeAt(0).getBoundingClientRect().width,
			Height:window.getSelection().getRangeAt(0).getBoundingClientRect().height
			}
		);
	}else{
		return JSON.stringify({X:0, Y:0, Width:0, Height:0});
	}

};

red.getSelectedHtmlSource = function(){
	if (window.getSelection) {
		var range=window.getSelection().getRangeAt(0);
		var container = document.createElement('div');
		container.appendChild(range.cloneContents());
		return container.innerHTML;
	}
}

red.getSelectedText = function(){
	return window.getSelection().toString();
}

red.setFontSize = function(size){
	if(typeof size == "number"){
		document.getElementsByTagName("body")[0].style.fontSize= size + "px";
	}
}

red.zoom = function(zoomRate){
	if(typeof zoomRate == "number"){
		document.body.style.zoom = zoomRate;
	}
}

Array.prototype.unique = function()
{
	var n = {},r=[];
	for(var i = 0; i < this.length; i++)
	{
		if (!n[this[i]])
		{
			n[this[i]] = true;
			r.push(this[i]);
		}
	}
	return r;
}

String.prototype.trim=function() {  
    return this.replace(/(^\s*)|(\s*$)/g,'');  
};  


