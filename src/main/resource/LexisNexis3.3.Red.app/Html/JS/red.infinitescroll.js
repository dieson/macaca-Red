red.infinitescroll = {
	isLoading:false,//indicate whether loading content or not, true: content(next page or previous page) is loading, otherwise means no content is loading
	changePBOPageNumWhenScroll:function (){
		var scrollToPageNum = 0;

		if($(".pagebreak").length > 0){
			$(".pagebreak").each(function(){
				if(($(this).offset().top + $(this).height()) <= $(window).scrollTop() ){
		 			scrollToPageNum = $(this).data("count");
				}else{
				   	return false;
				}
			});
			if(scrollToPageNum == 0){
				scrollToPageNum = parseInt($(".pagebreak").first().data("count")) -1;
			}
		}else{
			scrollToPageNum = parseInt($("#data_container").data("pbopagenum"));
		}


		if(scrollToPageNum != $("#data_container").data("pbopagenum")){
			$("#data_container").data("pbopagenum", scrollToPageNum);
			red.sendRequest("looseleaf://setCurrentPBOPageNum?pageNum=" + scrollToPageNum);
		}
	},
	scrollToPBOPageHeader:function(pageNum){
		/*
		var targetPageBreak = $(".pagebreak").filter(function(){
			return parseInt($(this).data("count")) == pageNum;
		});
		var targetEle;
		if(targetPageBreak.length > 0){
			targetEle = targetPageBreak.children(".pagenum").children("span");
		}
		*/
		var targetEle = $("span[data-count='"+pageNum+"'] .pagenum >span");
		if(targetEle && targetEle.length > 0){
			if($(document).height() <= targetEle.offset().top + document.body.clientHeight){
				this.loadingNextPage();
				setTimeout(function(){$(window).scrollTo(targetEle.offset().top + 1);}, 1000);
			}else{
				$(window).scrollTo(targetEle.offset().top + 1);
			}
		}
	},
	removeInvisiblePage:function (){
		//remove invisible page_container to improve performance
		if($(".page_container").length >= 3){
			var toBeRemoved = new Array(), i=0;
			$(".page_container").each(function(){
				if(($(this).offset().top + $(this).height()) < $(window).scrollTop() ){
					toBeRemoved[i++] = $(this);
				}
			});
			if(toBeRemoved.length > 0){
				$("#beginLoadingTitle").data("tocid", $(toBeRemoved[i-1]).data("tocid"));
				$("#beginLoadingTitle").text($(toBeRemoved[i-1]).data("toctitle"));
			}

			for(x in toBeRemoved){
				$(toBeRemoved[x]).remove();
			}


			toBeRemoved = new Array();
			i = 0;
			var bottomOffset = $(window).scrollTop() + $(window).height();
			$(".page_container").each(function(){
				if( $(this).offset().top  > bottomOffset){
					toBeRemoved[i++] = $(this);
				}
			});
			if(toBeRemoved.length > 0){
				$("#endLoadingTitle").data("tocid", $(toBeRemoved[0]).data("tocid"));
				$("#endLoadingTitle").text($(toBeRemoved[0]).data("toctitle"));

			}
			for(x in toBeRemoved){
				$(toBeRemoved[x]).remove();
			}
		}
	},
	showEndLoading:function (){
		this.isLoading = true;
		$("#endloading").slideToggle(50, function(){
			$(window).scrollTo("max");//scroll to end
		});
	},
	loadingNextPage:function  (){
		setTimeout(function(){
			$("#content").append("<div id='page_container_" + $("#endLoadingTitle").data("tocid")  +"' data-tocid='" + $("#endLoadingTitle").data("tocid") + "' data-toctitle='"+ $("#endLoadingTitle").text() +"' class='page_container next_page'></div>");
			$("#endloading").slideToggle();
			red.sendRequest("looseleaf://loadpage?page=next&tocid=" + $("#endLoadingTitle").data("tocid"));
			}, 
			200);
	},
	showBeginLoading:function (){
		this.isLoading = true;
		$("#beginloading").slideToggle(50, function(){
			$(window).scrollTo(1);
		});
	},
	loadingPreviousPage:function  (){
		setTimeout(function(){
			$("#beginloading").slideToggle(0);
			$("#content").prepend("<div id='page_container_" + $("#beginLoadingTitle").data("tocid") + "' data-tocid='" + $("#beginLoadingTitle").data("tocid") + "' data-toctitle='" + $("#beginLoadingTitle").text().replace("'","\'") + "' class='page_container'></div>");
			$(window).scrollTo(1);
			red.sendRequest("looseleaf://loadpage?page=previous&tocid="+ $("#beginLoadingTitle").data("tocid"));
			}, 
			500);
	},
	setLoadingTOCTitle:function (tocid, title, position){
		if(position == "next"){
			$("#endLoadingTitle").text(title);
			$("#endLoadingTitle").data("tocid", tocid)
		}else if(position == "previous"){
			$("#beginLoadingTitle").text(title);
			$("#beginLoadingTitle").data("tocid", tocid)
		}
	},
	appendPageContent:function (content, position){
		content = content.replace(/##/g, "\"");
		if(position == "next"){
			$("div.page_container:last").html(content);
		}else{//previous
			$("div.page_container:first").html(content);
			$(window).scrollTo($("div.page_container:first").height() - 29);
		}
		this.isLoading = false;
		setTimeout(function(){
			if($(".hiddendiv").length > 0 && $(".hiddendiv").children("span").length > 0){
				$(".hiddendiv").children("span").remove();
			}
		}, 1000);
	},
	scroll:function(){
		var curPageDiv = $(".page_container:first");
		$(".page_container").each(function(){
			if($(window).scrollTop() > $(this).offset().top){
				curPageDiv = $(this);
			}
		});
		if(curPageDiv.data("tocid") != $("#data_container").data("highlightedtocid")){
				red.sendRequest("looseleaf://highlighttoc?tocid="+ curPageDiv.data("tocid"));
				$("#data_container").data("highlightedtocid", curPageDiv.data("tocid"));
		}
		if(this.isLoading == false){
			if($(window).scrollTop() < 1 && $("#beginLoadingTitle").data("tocid") != "-1"){//scroll to previous page, "-1" means no more pages to which scroll up or down
				this.showBeginLoading();
				this.loadingPreviousPage();
				this.removeInvisiblePage ();
			} else if( ( $(window).scrollTop() + document.body.clientHeight + 10) >= $(document).height()  && $("#endLoadingTitle").data("tocid") != "-1" ){//scroll to next page	
				this.showEndLoading();
				this.loadingNextPage();
				this.removeInvisiblePage ();
			}
		}

		if($("#data_container").data("ispbo") == true || $("#data_container").data("ispbo") == "true"){
			this.changePBOPageNumWhenScroll();
		}
	},
	scrollToData:function(scrollToData){
 		if(typeof(scrollToData) != "undefined" && scrollToData !=""){
			var targetEle = $("span[data-refpt_id='"+scrollToData+"']");
			var AnchorDiv = $("div[id='"+scrollToData+"']");
			var AnchorDivDisplay = AnchorDiv.css("display");
 			if(targetEle.length > 0){
 				targetEle.css("display","block");
				$(window).scrollTo(targetEle);
				targetEle.css("display",AnchorDivDisplay);
 			}else{
  				if(AnchorDiv.length > 0){ //data-value
 					AnchorDiv.css("display","block");
					$(window).scrollTo(AnchorDiv);
					AnchorDiv.css("display", AnchorDivDisplay);
 				}
 			}
 		}
 	},
 	scrollToAnchor:function(scrollToAnchor){
 		if(typeof(scrollToAnchor) != "undefined" && scrollToAnchor !=""){
 			var targetEle = $("a[id='"+scrollToAnchor+"']");
   			if(targetEle.length > 0){
  			$(window).scrollTo(targetEle);	
      	  }
 		}
  	}
 	
 }

 
