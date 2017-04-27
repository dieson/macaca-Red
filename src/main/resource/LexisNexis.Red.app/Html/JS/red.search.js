/*
 * Using hitor.js from http://www.the-art-of-web.com/javascript/search-highlight to do search keyword highlight
 * 
 */

red.search = {
	highlightSearchKeyword:function (keywords){
		if(keywords === undefined){
			keywords = $("#data_container").data("highlightedkeyword");
		}
		var searchKeywordHilitor = new Hilitor("content");
		searchKeywordHilitor.apply(keywords);
	},
	scrollToHeading:function(tocId, headingType, headingNum){
		var headingSelector = ".page_container[data-tocid='"+tocId+"'] "+headingType+":eq("+headingNum+")";
		if(tocId && headingType && headingNum && $(headingSelector).length > 0){
			var targetHighlightEle;
			var headTop = $(headingSelector).offset().top;
			$(".search_highlight").each(function(){
				if($(this).offset().top >= headTop){
					targetHighlightEle = this;
					return false;
				}
			});
			if( targetHighlightEle){
				$(window).scrollTo(targetHighlightEle, 300);
			}else{
				$(window).scrollTo($(headingSelector), 300);
			}	
		}
		
	}
}