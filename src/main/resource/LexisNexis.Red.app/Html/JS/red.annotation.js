$(document).ready(function(){
	rangy.init();
	window.highlighter = rangy.createHighlighter();
	highlighter.addClassApplier(rangy.createClassApplier("highlight", {
    	ignoreWhiteSpace: true,
    	tagNames: ["mark"],
    	elementTagName: "mark"
    }));


    rangy.rangePrototype.insertNodeAtBegin = function(node) {
    	var range = this.cloneRange();
	    range.collapse(true);
	    range.insertNode(node);
	    range.detach();
	    this.setEndAfter(node);
	};

    //Annotation object
    red.annotation = {
		addIconForAnnotation:function(jsonAnnotation, colorNames){
		    var ranges = rangy.deserializeSelectionByJson(jsonAnnotation);
		    if(ranges && ranges.length > 0){
		    	var firstMark  = this.findFirstHighlightMarkInRange(ranges[0]);
		    	this.addTagIconForRange(firstMark, jsonAnnotation[0].ID, colorNames);

		    	if(jsonAnnotation[0].IsNote){
			    	this.addNoteIconForRange(firstMark, jsonAnnotation[0].ID);
			    }else{
			    	$("#note"+jsonAnnotation[0].ID).remove();
			    }
		    }
		},
		addNoteIconForRange:function(node, id){
			if (node  && $("#note"+id).length == 0) {
				var noteSpanElement = document.createElement("span");
				noteSpanElement.setAttribute("id", "note"+id);
				noteSpanElement.setAttribute("class", "noteIcon");

				var noteIconElement = document.createElement("img");
				//noteIconElement.setAttribute("id", "note"+id);
				noteIconElement.setAttribute("class", "noteIcon");
				noteIconElement.setAttribute("src", "../Images/Annotation/Note@2x.png");

				noteSpanElement.appendChild(noteIconElement);
				
				
				$(node).prepend(noteSpanElement);
				$(noteSpanElement).css("z-index", "1000");
				var curLineNoteNum = -1;
				$("span.noteIcon").each(function(){
					if(this.offsetTop == noteSpanElement.offsetTop)
						curLineNoteNum++;
				});
				if(curLineNoteNum > 0){
					noteSpanElement.setAttribute("style", "padding:" + curLineNoteNum * 10 +"px;z-index:"+ (1000 - curLineNoteNum * 10));
				}
				if(curLineNoteNum > 3)
					$(noteSpanElement).css("display","none");
				
			    $(noteIconElement).click(function(){
					var top = $(this).offset().top - $(window).scrollTop();
					var left = $(this).offset().left;
					red.sendRequest("looseleaf://editAnnotation?id=" + id + "&top=" + top + "&left="+left);
					return false;
				});
		    }
		},
		addTagIconForRange:function(node, id, colorNames){
			var colorNameArr = JSON.parse(colorNames);
			if(node){
				var tagIconElement;
				if($("#tag"+id).length > 0){
					tagIconElement = $("#tag"+id).get(0);
					$("#tag"+id).html("");
				}else{
					tagIconElement = document.createElement("span");
					tagIconElement.setAttribute("class", "tagIcon");
					tagIconElement.setAttribute("id", "tag" + id);					
				}


				var circleIndex = 1;
				colorNameArr = colorNameArr.unique();
				for(var i = 0; i < colorNameArr.length; i++){
					var tagColorDiv = document.createElement("span");
					tagColorDiv.setAttribute("class", "circle circle_" + circleIndex++);
					tagColorDiv.setAttribute("style", "background-color:"+colorNameArr[i] +";")
					tagIconElement.appendChild(tagColorDiv);
					if(i >= 2)
						break;
					if(i < (colorNameArr.length - 1)){
						var whiteCircle = document.createElement("span");
						whiteCircle.setAttribute("class", "circle circle_" + circleIndex++ + " circle_white");
						tagIconElement.appendChild(whiteCircle);
					}
				}

				if(colorNameArr.length > 3){
					var ellipseEle = document.createElement("img");
					ellipseEle.setAttribute("class", "tagiconellipse");
					ellipseEle.setAttribute("id", "ellipse" + id);	
					ellipseEle.setAttribute("src", "./IMG/ellipse@3x.png");	
					tagIconElement.appendChild(ellipseEle);
				}

				if($("#tag"+id).length == 0){					
						$(node).prepend(tagIconElement);
						$(tagIconElement).css("z-index", "1000");
						var curLineTagIconNum = -1;
						$(".tagIcon").each(function(){
							if(this.offsetTop == tagIconElement.offsetTop)
								curLineTagIconNum++;
						});
						if(curLineTagIconNum > 0){
							tagIconElement.setAttribute("style", "padding:" + curLineTagIconNum * 10 + "px;z-index:"+ (1000 - curLineTagIconNum * 10));
						}
						if(curLineTagIconNum > 3)
							$(tagIconElement).css("display","none");
					
				}
				
				$("#tag"+id).children(".circle").click(function(){
					var top = $("#tag"+id).offset().top - $(window).scrollTop() + parseInt($("#tag"+id).css("padding"));
					var left = $("#tag"+id).offset().left + parseInt($("#tag"+id).css("padding"));
					red.sendRequest("looseleaf://editAnnotation?id=" + id + "&top=" + top + "&left="+left);
					return false;
				});
			}
		},
		getFirstRange:function () {
		    var sel = rangy.getSelection();
		    return sel.rangeCount ? sel.getRangeAt(0) : null;
		},
		getLastRange:function(){
			var sel = rangy.getSelection();
		    return sel.rangeCount ? sel.getRangeAt(sel.rangeCount - 1) : null;
		},
		getFirstSelectedNode:function(){
			var firstRange = this.getFirstRange();
			if(firstRange){
				var nodeArr = firstRange.getNodes();
				return nodeArr[0];
			}
			return null;
		},
		getLastSelectedNode:function(){
			var firstRange = this.getFirstRange();
			if(firstRange){
				var nodeArr = firstRange.getNodes();
				return nodeArr[nodeArr.length - 1];
			}
			return null;
		},
		highlightSelectedText:function () {
			highlighter.highlightSelection("highlight");
			var textInfo = this.getSelectTextInfo();
			this.removeSelection();		    
			return JSON.stringify(textInfo);
		},
		getSelectTextInfo:function(){
			var xpath = rangy.serializeSelection().split(",");

			var startEle = this.getFirstSelectedNode();
			var startMainDiv;
			if($(startEle).closest('.main').length > 0){
				startMainDiv = $(startEle).closest('.main')[0];
			}else{
				var startPageContainer = $(startEle).closest('.page_container')[0];
				startMainDiv = $(startPageContainer).children('.main')[0];
			}

		    var startTocId = $(startMainDiv.parentNode).data("tocid");
		    var startDocId = $(startMainDiv).data("docid");
		    var startFileName = $(startMainDiv).data("filename")
		    var startPosInfo = xpath[0].split(":");
		    var startXpath  = startPosInfo[0];
		    //var startOffset = rangy.getSelection().nativeSelection.baseOffset;
		    var startOffset = startPosInfo[1];
		    if(startXpath.indexOf("HTML") != -1){
		    	var firstSpanChildOfEndMain;
		    	$("span").each(function(){
		    		if($(this).closest('.main').length > 0 && $($(this).closest('.main')[0]).data("docid") == startDocId && $(this).closest('.hiddendiv').length == 0){
		    			firstSpanChildOfEndMain = this;
		    			return false;
		    		}
		    	});
		    	var startSpanPosInfo = rangy.serializePosition(firstSpanChildOfEndMain, 0);
		    	startPosInfo = startSpanPosInfo.split(":");
		    	startXpath= startPosInfo[0];
		   		startOffset = 0;
		    }

		    var endEle = this.getLastSelectedNode ();
		    var endMainDiv = $(endEle).closest('.main')[0];
		    var endTocId = $(endMainDiv.parentNode).data("tocid");
		    var endDocId = $(endMainDiv).data("docid");
		    var endFileName =  $(endMainDiv).data("filename");

		    var endPosInfo = xpath[1].split(":");
		    var endXpath = endPosInfo[0];
		    var endOffset = endPosInfo[1];
		    if(endXpath === "/DIV[3]"){
		    	var lastSpanChildOfEndMain;
		    	$("span").each(function(){
		    		if($(this).closest('.main').length > 0 && $($(this).closest('.main')[0]).data("docid") == endDocId){
		    			lastSpanChildOfEndMain = this;
		    		}
		    	});
		    	var endSpanPosInfo = rangy.serializePosition(lastSpanChildOfEndMain, $(lastSpanChildOfEndMain).text().length);
		    	endPosInfo = endSpanPosInfo.split(":");
		    	endXpath = endPosInfo[0];
		    	endOffset = endPosInfo[1];
		    }
		    //var endOffset = rangy.getSelection().nativeSelection.extentOffset;

		    var res = { 
		    	StartTOCID:startTocId, StartDocId:startDocId, StartFileName:startFileName, StartXpath:startXpath, StartXpathOffset:startOffset,
		    	EndTOCID:endTocId, EndDocId:endDocId, EndFileName:endFileName, EndXpath:endXpath, EndXpathOffset:endOffset,
		    	Text:rangy.getSelection().toString()
		    	};
		    return res;
		},


		getRGBAColorFromColor:function(color){
            var a = 0.7; 
            switch (color) 
            {
              case '#4c009a':
                   a = 0.6;
                   break;
              case '#363636':
                   a = 0.5;
                   break;
            }
            var r = parseInt(color.substring(1, 3), 16);
            var g = parseInt(color.substring(3, 5), 16);
            var b = parseInt(color.substring(5, 7), 16);
            tempColor = 'rgba(' + r + ',' + g + ',' + b + ',' + a + ')';
            return tempColor;
        },

		highlightAnnotation:function(jsonAnnotation){
		 
               for(var i = 0; i < jsonAnnotation.length; i++)
               {
                  var ranges = rangy.deserializeSelectionByJson([jsonAnnotation[i]]);
                  var legalRanges = [];
		          for (var j = 0; j < ranges.length; j++) 
		          {
				     if(ranges[j] != null && ranges[j] != undefined)
				        {
					      legalRanges.push(ranges[j]);
				        }
			      }

                  highlighter.highlightRanges("highlight", legalRanges); 
                  if(legalRanges != null && legalRanges.length > 0)
                  {
                  var object = this;
                    $("mark.highlight").each(function()
                      {
                        if($(this).attr("highlightcolor") == null || $(this).attr("highlightcolor") == "")
                          {
                            var colorNameArr = jsonAnnotation[i].TagColors;
                            if(colorNameArr.length > 0)
                            {
                            var tempColor = "";
                            var rgba = colorNameArr[0];
                            tempColor = object.getRGBAColorFromColor(rgba);
                             $(this).css("background-color",tempColor);
                             $(this).css("color","inherit");
                           }
                             $(this).attr("highlightcolor", "default");
                             $(this).css("color","inherit");
                             $(this).attr("id","highlight" + jsonAnnotation[i].ID);
                         
                       }
                   }); 
                }
            }
        },

		restoreAnnotationList:function(tocid, jsonAnnotation){
			this.wipeAnnotationFromDisplayedContent(tocid);

			this.highlightAnnotation(jsonAnnotation);
			var ranges = rangy.deserializeSelectionByJson(jsonAnnotation);
			var legalRanges = [];
			for (var i = 0; i < ranges.length; i++) {
				if(ranges[i] != null && ranges[i] != undefined){
					legalRanges.push(ranges[i]);
				}
			}            

            var orphanIdArr = [];
            for (var i = 0; i < jsonAnnotation.length; i++) {
            	if(ranges[i] == null || ranges[i] == undefined){
            		orphanIdArr.push(jsonAnnotation[i].ID);
            	}else{
            		var firstMark  = this.findFirstHighlightMarkInRange(ranges[i]);
            		if(jsonAnnotation[i].IsNote == true){//Add Note Icon
						this.addNoteIconForRange(firstMark, jsonAnnotation[i].ID);
	                }
					this.addTagIconForRange(firstMark, jsonAnnotation[i].ID, JSON.stringify(jsonAnnotation[i].TagColors));  
            	}     
            }

            if(orphanIdArr.length > 0){
            	red.sendRequest("looseleaf://setAsOrphan?id=" + orphanIdArr.join("|"));
            }
		},
		deserializeAnnotation: function(annoInfo){
			var jsonAnnotation = JSON.parse(annotationInfo);
			var xpathStr = jsonAnnotation.xpathInfo;
    		var docIdArray = jsonAnnotation.docInfo.split("|");
    		for (var i = 0; i < docIdArray.length; i++) {
        		var node = $("div.main[data-docid='" + docIdArray[i] + "']")[0];
       			if (node != null) {
            		var xpathHead = this.getXpathHead(node); // xpathHead是从body标签到main div的xpath
            		xpathStr = xpathStr.replace(new RegExp(docIdArray[i] + ":", "gm"), xpathHead);// 合并xpathHead和annotation里的xpath得到annotation在当前文档的完整的xpath
        		}
    		}
    		var ranges = rangy.deserializeSelection(xpathStr);
    		highlighter.highlightRanges("highlight", ranges);
		},
		getXpathHead:function (node) {
    		var xpathHead = "";
    		while (node != null && node.nodeName != "HTML") {
        		if (node.nodeType == 1) {
            		var index = this.findNodeIndexInParent(node);
            		xpathHead = '/' + node.nodeName + '[' + index + ']' + xpathHead;
        		}
        		node = node.parentNode;
    		}
    		return xpathHead;
		},
		findNodeIndexInParent:function(node) {
		    var elementName = node.nodeName;
		    var i = 1;
		    while ((node = node.previousSibling) != null) {
		        if (node.nodeName == elementName) {
		            i++;
		        }
		    }
		    return i;
		},
		scrollToAnnotation:function(anno){
			if(anno){
				var startMainDiv = $("div.main[data-docid='" + anno.StartDocId + "']")[0];
                var start = rangy.deserializePosition(anno.StartXpath + ":" + anno.StartXpathOffset, startMainDiv);
                if(start && start.node){
                	var mark = $(start.node).find("mark").filter(function(node){
                		return $(this).text().trim() != "";
                	});
                	if(mark && mark[0]){
                		$(window).scrollTo(mark[0], 200);
                	}else{
                		$(window).scrollTo(start.node, 200);
                	}
                }
			}
		},
		getHighlightElementInRange:function (n, rangeStr) {
		    while (n.nodeType != 1 || n.getAttribute("class") == "tagIcon" || n.getAttribute("class") == "noteIcon") {
		        if (n.parentNode == null) {//Insert Annotation: parentNode is null when merge mark element
		            return null;
		        }
		        n = n.parentNode;
		    }
		    if(n.getAttribute("class") == "highlight") {
		        return n;
		    }else {
		        $(n).children(".highlight").each(function () {
		            n = this;
		            if (rangeStr.indexOf(this.textContent) >= 0 || this.textContent.indexOf(rangeStr) >= 0) {
		                return false;
		            }
		        });
		    }
		    return n;
		},
		getRestoredAnnotationIds:function (){
			var annoIds = "";
			var idArr = [];
			$(".tagIcon").each(function(){
				idArr.push($(this).attr("id").replace("tag", ""));
			});
			return idArr.join('|');
		},
		getSelectedAnnoIds:function (annotationInfo) {
			
		    var range = this.getFirstRange();
		    if (range) {
		        var intersectIds = [];
		        if (annotationInfo != "" && annotationInfo != undefined && annotationInfo != null) {
		            var ranges = rangy.deserializeSelectionByJson(annotationInfo);
		            for (var i = 0; i < ranges.length; i++) {
		                if (range.intersectsOrTouchesRange(ranges[i])) {
		                    intersectIds.push(annotationInfo[i].ID);
		                }
		                else if (range.startOffset == 0 && range.startContainer.previousSibling == ranges[i].endContainer.parentNode) {
		                 	intersectIds.push(annotationInfo[i].ID);
		                }
		            }
		            //rangy.getSelection().removeAllRanges();
		            //rangy.getSelection().addRange(range);
		        }
		        return intersectIds.join('|');	
		    }
		  
		},
		findFirstHighlightMarkInRange:function(range){
			try{
				var markList = range.getNodes([Node.ELEMENT_NODE], function(node){
					return node.nodeName == "MARK";

				});
				var nodeInRange = range.getNodes([Node.ELEMENT_NODE], function(node){
					return node.nodeName == "MARK" && node.innerText.trim() != "" && range.toString().replace("\n", " ").indexOf(node.innerText.trim()) != -1;
				});
				
				if(nodeInRange && nodeInRange.length > 0)
					return nodeInRange[0];

			}catch(err){
				return this.getHighlightElementInRange(range.startContainer, range.toString());
			}

			return this.getHighlightElementInRange(range.startContainer, range.toString());
		},
		getAnnoNewTextInfo:function (jsonAnnotation) {
		    var range = this.getFirstRange();
		    this.removeSelection();

		    if (range && jsonAnnotation.length > 0) {
		        var ranges = rangy.deserializeSelectionByJson(jsonAnnotation);
		        
			    if (ranges && ranges.length > 0) {
			        var selectTextInfo = this.getSelectTextInfo();
			        var xpath = rangy.serializeRange(range).split(",");

			        var bookmark1 = range.getBookmark();
            		var bookmark2 = ranges[0].getBookmark();

			        var startCompareResult1 = this.ComparePositionByBookMark(range.startContainer, bookmark1.start, ranges[0].startContainer, bookmark2.start);
			        var startCompareResult2 = this.ComparePositionByBookMark(range.endContainer, bookmark1.end, ranges[0].endContainer, bookmark2.end);
			            
			        selectTextInfo.StartXpath = startCompareResult1 < 0 ? xpath[0].split(':')[0] : jsonAnnotation[0].StartXpath;
			        selectTextInfo.StartXpathOffset = startCompareResult1 < 0 ? xpath[0].split(':')[1] : jsonAnnotation[0].StartXpathOffset;
			        selectTextInfo.EndXpath =  startCompareResult2 > 0 ? xpath[1].split(':')[0] : jsonAnnotation[0].EndXpath;
			        selectTextInfo.EndXpathOffset =  startCompareResult2 > 0 ? xpath[1].split(':')[1]: jsonAnnotation[0].EndXpathOffset;

			        var annotation = jsonAnnotation[0];
            		annotation.StartXpath =  selectTextInfo.StartXpath;
            		annotation.StartXpathOffset = selectTextInfo.StartXpathOffset;
            		annotation.EndXpath = selectTextInfo.EndXpath;
            		annotation.EndXpathOffset = selectTextInfo.EndXpathOffset;

 
 			        if(startCompareResult1 < 0  || startCompareResult2 > 0){
			            var newRanges = rangy.deserializeSelectionByJson([annotation]);
			            if(newRanges && newRanges.length > 0){
 			            	selectTextInfo.Text = newRanges[0].toString();
 			            }
			            return JSON.stringify(selectTextInfo);
			        }
			    }
		    }
		},
		ComparePosition:function (node1, offset1, node2, offset2) {
			var tocId1 = $($(node1).closest('.page_container')[0]).data("tocid");
		    var tocId2 = $($(node2).closest('.page_container')[0]).data("tocid");
		    if (parseInt(tocId1, 10) == parseInt(tocId2, 10) && node1.nodeType == 3 && node2.nodeType == 3) {
		        var id1 = ($(node1).closest('span[id]')[0]).id;
		        var id2 = ($(node2).closest('span[id]')[0]).id;
		        if (parseInt(id1, 10) == parseInt(id2, 10)) {
		            return offset1 == offset2 ? 0 : (offset1 > offset2 ? 1 : -1);
		        }
		        else {
		            return id1 > id2 ? 1 : -1;
		        }
		    }else {
		        return 0;
		    }
		},
		ComparePositionByBookMark:function(node1, offset1, node2, offset2) {
		    try {
		        var tocId1 = $($(node1).closest('.page_container')[0]).data("tocid");
		    	var tocId2 = $($(node2).closest('.page_container')[0]).data("tocid");
		        if (parseInt(tocId1, 10) == parseInt(tocId2, 10)) {
		            return offset1 == offset2 ? 0 : (offset1 > offset2 ? 1 : -1);
		        }
		        else {
		            return 0;
		        }
		    }
		    catch (e) {
		        return 0;
		    }
		},

		getFilterHighLightAnnotationIDs:function(annoHighLights)
		{
		   var ids = [];
           var bool = 0;// 没有相同的mark.highlight
           for(var i = 0; i < annoHighLights.length; ++i)
           {
              if(bool == 0)
              {
                 ids.push(annoHighLights[i].id.replace("highlight", ""));
              }
              if(i < annoHighLights.length - 1)
	          {
	             if(annoHighLights[i].id == annoHighLights[i + 1].id)
                 {
                   bool = 1;
                 }
                 else
                 {   
                   bool = 0;
                 }
	          }
           }

           return ids;
		},

        getAllAnnotationIDsInWebView:function(){
           var annoHighLights = $("mark.highlight");

           var ids = this.getFilterHighLightAnnotationIDs(annoHighLights);

           return ids.join('|');
        },
		removeSelection:function(){
			//This is a workaround to remove text selection after note/highlight is created
			document.location.href = "removeSelection";
		},
		wipeAnnotationFromDisplayedContent:function(tocid){
			$("#page_container_" + tocid + " .tagIcon").each(function () {
		        $(this).remove();
		    });
		    $("#page_container_" + tocid + " .noteIcon").each(function () {
		        $(this).remove();
		    });
		    $("#page_container_" + tocid + " .highlight").each(function () {
		        $(this).replaceWith(this.innerHTML);
		    });
		    $("#page_container_" + tocid + " [highlightcolor]").each(function () {
		        $(this).replaceWith(this.innerHTML);
		    });
		    highlighter.removeAllHighlights();

		    rangy.getSelection().removeAllRanges();
		    //window.getSelection().removeAllRanges();

		},
		isSelectTextCrossTOC:function(){
			var startEle = this.getFirstSelectedNode();
			var endEle = this.getLastSelectedNode ();

			var startPageContainer = $(startEle).closest('.page_container')[0];
			var endPageContainer = $(endEle).closest('.page_container')[0];
			if($(startPageContainer).data("tocid") == $(endPageContainer).data("tocid")){
				return false;
			}else{
				return true;
			}
		},
		setPageContainerMarginBottomBeforePrint:function()
		{
		   $(".page_container").css("margin-bottom","0px");
		},
		setPageContainerMarginBottomAfterPrint:function()
		{
		   $(".page_container").css("margin-bottom","30px");
		},
		hideNoteBeforePrint:function(){
		    $(".noteIcon").css("display","none");
		},
		hideHighLightBeforePrint:function(){
		    $(".tagIcon").css("display","none");
			$(".highlight").css("background-color", "inherit");
		},
		hideAnnotationBeforePrint:function(){
			$(".noteIcon").css("display","none");
			$(".tagIcon").css("display","none");
			$(".highlight").css("background-color", "inherit");
			$(".page_container").css("margin-bottom","0px");

		},
		showAnnotationAfterPrint:function(Annotations){
			$(".noteIcon").css("display","inline");
			$(".tagIcon").css("display","inline");
			$(".page_container").css("margin-bottom","30px");
			var object = this;
			var allHighLight = $("mark.highlight");
			for(var i = 0;i < Annotations.length; ++i)
			{
			   var colors = Annotations[i].CategoryTags[0];
			   if(colors != null)
			   {  
			        for(var j = 0; j < allHighLight.length; ++j)
			        {
			           if(allHighLight[j].id.replace("highlight", "") == Annotations[i].AnnotationCode)
			           {
			               $(allHighLight[j]).css("background-color", object.getRGBAColorFromColor(colors.Color));
			           }
			        }  
			   }
			   else
			   {  
			       for(var j = 0; j < allHighLight.length; ++j)
			        {
			           if(allHighLight[j].id.replace("highlight", "") == Annotations[i].AnnotationCode)
			           {
			              $(allHighLight[j]).css("background-color", "#ffff00");
			           }
			        }  
			       
			   }
			}

		},
		getCurrentDisplayedTOCIds:function(){
			var tocIds = [];
			$(".page_container").each(function(){
				tocIds.push($(this).data("tocid"));

			});
			return tocIds.join("|");
		},



		GetAncestorsByNode:function(node, rootNode){
		    var aNodes = [], n = node.parentNode;
		    rootNode = rootNode || dom.getDocument(node).documentElement;
		    while (n && n != rootNode) {
		        aNodes.unshift(n);
		        n = n.parentNode;
		    }
		         return aNodes;
		},

		GetAllAncestorElements:function(selRange){
		     var bodyNode = $(selRange.startContainer).closest('body')[0];
		     var startAncestor = this.GetAncestorsByNode(selRange.startContainer, bodyNode);
		     var endAncestor = this.GetAncestorsByNode(selRange.endContainer, bodyNode);
		     var commonNodes = [];
		     for(var i = 0; i < startAncestor.length && i < endAncestor.length; ++i)
		     {
		       if(startAncestor[i] == endAncestor[i])
		       {
		         commonNodes.unshift(startAncestor[i].cloneNode(false));
		       }
		       else
		       {
		         break;
		       }
		     }
		     return commonNodes;
		},

		getFilterHighLightAnnotations:function(annoHighLights)
		{
		     var annoAfterFilter = [];
		     var bool = 0; // 没有相同的mark.highlight
		     for(var i = 0; i < annoHighLights.length; ++i)
		     {
		        if(bool == 0)
		        {
		           annoAfterFilter.push(annoHighLights[i]);
		        }
		        if(i < annoHighLights.length - 1)
			    {
			       if(annoHighLights[i].id == annoHighLights[i + 1].id)
		           {
		           bool = 1;
		           }
		           else
		           {   
		           bool = 0;
		           }
			    }
		     }
             return annoAfterFilter;
		},

		CloneAnnotionIconsTags:function(jnode,ids,annotations,boolArr)
		{
		     var annoHighLights = $(jnode).find("mark.highlight");
		     var annoAfterFilter = this.getFilterHighLightAnnotations(annoHighLights);

		     var tempNumberOfIcon = 1;
		     var noteAnnotations = [];
		     for(var i = 0; i < annoAfterFilter.length; ++i)
		     {
				    var current = annoAfterFilter[i];
				    var tagIcon = $("#tag" + ids[i]);
				    var noteIcon = $("#note" + ids[i])
				    if(tagIcon.length != 0)
				    {
				        if($(current).find("#tag" + ids[i]).length == 0)
				        {
				            current.insertBefore(tagIcon[0].cloneNode(true),current.firstChild);
				        }
				    }
				    if(noteIcon.length != 0)
				    {
				        var tempNoteNode = noteIcon[0].cloneNode(true);
				        var noteNumber = document.createElement("span"); 
		                noteNumber.className = "noteNumberForPrint"; 
		                noteNumber.innerHTML = tempNumberOfIcon++; 
		                $(noteNumber).css("font-size","12pt");
		                $(noteNumber).css("width","15px");
		                $(noteNumber).css("text-align","center");
		                $(noteNumber).css("display","inline-block");
		                var noteImg = tempNoteNode.firstChild;
		                $(noteImg).css("display","inline-block");

		                tempNoteNode.insertBefore(noteNumber,noteImg);

		                if($(current).find("#note" + ids[i]).length == 0)
		                {
		                   current.insertBefore(tempNoteNode,current.firstChild);
		                }
		                else
		                {
		                   var noteIconInCurrent = $(current).find("#note" + ids[i]);
		                   $(noteIconInCurrent[0]).replaceWith(tempNoteNode);   
		                }

				        for(var j = 0; j < annotations.length; ++j)
		                {
		                   if(annotations[j].AnnotationCode == ids[i])
		                   {
		                     noteAnnotations.push(annotations[j]);
		                   }
		                }
				    }
		     }
		     this.addNoteDetailDescription(jnode,noteAnnotations,boolArr);
		},

		addNoteNumberWhenPrintWholeDocumentWithShowNoteIcon:function(){
		        var annoHighLights = $("mark.highlight");
		        var annoAfterFilter = this.getFilterHighLightAnnotations(annoHighLights);
		        var tempNumberOfIcon = 1;
		        for(var i = 0; i < annoAfterFilter.length; ++i)
		        {   
		            var current = annoAfterFilter[i];
				    var noteIcons = $(current).find(".noteIcon");
				    if(noteIcons.length != 0)
				    {
				        var tempNoteNode = noteIcons[0];
				        var noteNumber = document.createElement("span"); 
		                noteNumber.className = "noteNumberForPrint"; 
		                noteNumber.innerHTML = tempNumberOfIcon++; 
		                $(noteNumber).css("font-size","12pt");
		                $(noteNumber).css("width","15px");
		                $(noteNumber).css("text-align","center");
		                $(noteNumber).css("display","inline-block");
		                var noteImg = tempNoteNode.firstChild;
		                $(noteImg).css("display","inline-block");

		                tempNoteNode.insertBefore(noteNumber,noteImg);
		               
				    }
		        }
		},

		addNoteDetailDescription:function(jnode,noteAnnotations,boolArr){
		     
		     var mainDiv = "";
		     if(boolArr[1])
		     {
		         mainDiv = $(jnode).find(".main");
		     }
		     else
		     {
		        mainDiv = $(".main");
		     }
		     var mainNode = mainDiv[mainDiv.length - 1];
		     var detailNoteForPrint = document.createElement("div");
		     detailNoteForPrint.className = "detailNoteForPrint"; 
		     mainNode.appendChild(detailNoteForPrint);
		     var selectedHtml = detailNoteForPrint.innerHTML;
		     var result = "";
		     if(noteAnnotations.length > 0 && boolArr[0])
		     {
		         result = "<p><hr></hr></p><p><b>Annotations</b></p><table class='NoteTableForPrint' width='100%' style='border-collapse:separate; border-spacing:5px;'>";
		         for(var i = 0; i <noteAnnotations.length; ++i)
		         {   
		          var tagPanel = document.getElementById('tag' + noteAnnotations[i].AnnotationCode);
		          result += "<tr class='noteTextRow'>"
		          result += "<td class='noteTextColIcon'><span class='fixedNotePanel'><span class='noteNumberForPrint'>" + (i + 1) + "</span><img class='annotationnoteicon_print' src='../Images/Annotation/Note@2x.png'></span></td>";
		          result += "<td class='noteTextColTag'>" + tagPanel.innerHTML + "</td>";
		          result += "<td class='noteTextColText'>" + noteAnnotations[i].NoteText + "</td>";
		          result += "</tr>"
		         }
		         result += "</table>"
		         detailNoteForPrint.innerHTML = result;
		     }

		     if(boolArr[0] && !boolArr[1]) // 打印整个文件,并且显示noteIcon;这时在文本中的noteIcon插入noteNumber,打印后删除noteNumber.
		     {
		        this.addNoteNumberWhenPrintWholeDocumentWithShowNoteIcon();
		     }
		},

		removeNoteDescriptionAfterPrint:function(){
		    var notesDescriptionDiv = $(".detailNoteForPrint")[0];
		    notesDescriptionDiv.parentNode.removeChild(notesDescriptionDiv);
		    var noteNumbersForPrint = $("span.noteNumberForPrint");
		    for(var i = 0; i < noteNumbersForPrint.length; ++i)
		    {
		       noteNumbersForPrint[i].parentNode.removeChild(noteNumbersForPrint[i]);
		    }
		},


	    fixTableNode:function(currentNode){
	        var tempcontainer = document.createElement('div');
		    tempcontainer.appendChild(currentNode);
	        var htmlString = tempcontainer.innerHTML;

	        if (htmlString.indexOf('<tr>') == 0)
		    {
	            var sel = window.getSelection();
			    var range = sel.getRangeAt(0);
	            var tds = $(range.startContainer).closest('td');
	            if (tds.length > 0) 
	            {
	               var index = $(tds[0]).index();
	               if (index > 0) 
	               {
	                  var pretdStr = "";
	                  for (var i = 0; i < index; i++) 
	                  {
	                      pretdStr += "<td></td>";
	                  }
	                  htmlString = htmlString.replace('<tr>', '<tr>' + pretdStr);
	               }
	            }
	        }


	        if (htmlString.indexOf('<thead>') == 0) 
	        {
	           var sel = window.getSelection();
			   var range = sel.getRangeAt(0);
	           var ths = $(range.startContainer).closest('th');
	           if (ths.length > 0) 
	           {
	               var index = $(ths[0]).index();
	               if (index > 0) 
	               {
	                   var pretdStr = "";
	                   for (var i = 0; i < index; i++) 
	                   {
	                        pretdStr += "<th></th>";
	                   }
	                   htmlString = htmlString.replace('<tr>', '<tr>' + pretdStr);
	               }
	           }
	         }


	        if (htmlString.indexOf('<td>') == 0 || htmlString.indexOf('<th>') == 0) 
	        {
                htmlString = '<tr>' + htmlString + '</tr>';
            }
            if (htmlString.indexOf('<thead>') == 0 || htmlString.indexOf('<tr>') == 0 || htmlString.indexOf('<tbody>') == 0) 
            {
               var sel = window.getSelection();
			   var range = sel.getRangeAt(0);
               var tables = $(range.startContainer).closest('table');
               $(tables[0]).css('width','100%');
               if (tables.length > 0) 
               {
                   var colGroups = $(tables[0]).children('colgroup');
                   if (colGroups.length > 0)
                   {
                       htmlString = colGroups[0].outerHTML + htmlString;
                   }
                   var copyTableNode = tables[0].cloneNode(false);
                   $(copyTableNode).html(htmlString);
                   htmlString = copyTableNode.outerHTML;
               }
             }

           var container = document.createElement("div");
           $(container).html(htmlString);

           return container;

	    },
	   



		getSelectedHtmlForPrint:function(ids,annotations,boolArr){
		   var sel = window.getSelection();
		   var range = sel.getRangeAt(0);
		   var ae = this.GetAllAncestorElements(range);
		   var container = document.createElement('div');
		   var current = range.cloneContents();
		   current = this.fixTableNode(current);
		   for(var i = 0; i < ae.length; ++i)
		   {
		     ae[i].appendChild(current);
		     current = ae[i];
		   }
		   container.appendChild(current);


		   var pageContainers = $(container).find(".page_container");
		   if(pageContainers.length > 1)
		   {
		      for(var i = 1; i < pageContainers.length; ++i)
		         {
		           var tempLastChild = pageContainers[0].lastChild
		           pageContainers[i].removeChild(pageContainers[i].firstChild);
		           var tempFirstChild = pageContainers[i].firstChild.cloneNode(true);
		           pageContainers[0].appendChild(tempFirstChild);
		           $(pageContainers[i]).remove();
		         }
		   }

		   this.CloneAnnotionIconsTags(current,ids,annotations,boolArr);

		   return container.innerHTML;

		},

}

});
