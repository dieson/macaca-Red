/*execute javascript which required to be excuted after document is ready*/
$(function(){
	if($(".hiddendiv").length > 0 && $(".hiddendiv").children("span").length > 0){
		$(".hiddendiv").children("span").remove();
	}

 	var readyScript = $("#document_ready_script_container").text() ;
	if(readyScript != "#DOCUMENT_READY_SCRIPT#"){

		eval(readyScript);
	}

	var scrollToId = $("#data_container").data("scrolltoid");
	var scrollToData = $("#data_container").data("scolltodata");
	var scrollToAnchor =$("#data_container").data("scolltoanchor");

	if(typeof(scrollToId) != "undefined" && scrollToId !=""){
		var scrollToEle = document.getElementById(scrollToId);//the id contains special chars like ".", "=", so don't using jquery selector here
		if(scrollToEle){
			var tmpDisplay = scrollToEle.style.display;
			scrollToEle.style.display = "inline";
			$(window).scrollTo(scrollToEle);	
			scrollToEle.style.display = tmpDisplay;
		} 
	}else if(typeof(scrollToData) != "undefined" && scrollToData !=""){
 		red.infinitescroll.scrollToData(scrollToData);
	}else if(typeof(scrollToAnchor) != "undefined" && scrollToAnchor !=""){
 		red.infinitescroll.scrollToAnchor(scrollToAnchor);
 	}

	$(window).scroll(function(){

		if(red.infinitescroll){//only in content page, infinitescroll object would be loaded
			red.infinitescroll.scroll();
		}
	});

	red.search.highlightSearchKeyword();

	//scroll to the first highlighted searchkeyword for search, hiltor.js use em tag to highlight words
	var firstEm = $("em:first");
	if(firstEm.length > 0){
			$(window).scrollTo(firstEm);
	}
});