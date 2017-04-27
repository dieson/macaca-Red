/**
 * Serializer module for Rangy.
 * Serializes Ranges and Selections. An example use would be to store a user's selection on a particular page in a
 * cookie or local storage and restore it on the user's next visit to the same page.
 *
 * Part of Rangy, a cross-browser JavaScript range and selection library
 * https://github.com/timdown/rangy
 *
 * Depends on Rangy core.
 *
 * Copyright 2015, Tim Down
 * Licensed under the MIT license.
 * Version: 1.3.1-dev
 * Build date: 20 May 2015
 */
(function (factory, root) {
    if (typeof define == "function" && define.amd) {
        // AMD. Register as an anonymous module with a dependency on Rangy.
        define(["./rangy-core"], factory);
    } else if (typeof module != "undefined" && typeof exports == "object") {
        // Node/CommonJS style
        module.exports = factory(require("rangy"));
    } else {
        // No AMD or CommonJS support so we use the rangy property of root (probably the global variable)
        factory(root.rangy);
    }
})(function (rangy) {
    rangy.createModule("Serializer", ["WrappedSelection"], function (api, module) {
        var UNDEF = "undefined";
        var util = api.util;

        // encodeURIComponent and decodeURIComponent are required for cookie handling
        if (typeof encodeURIComponent == UNDEF || typeof decodeURIComponent == UNDEF) {
            module.fail("encodeURIComponent and/or decodeURIComponent method is missing");
        }

        // Checksum for checking whether range can be serialized
        var crc32 = (function () {
            function utf8encode(str) {
                var utf8CharCodes = [];

                for (var i = 0, len = str.length, c; i < len; ++i) {
                    c = str.charCodeAt(i);
                    if (c < 128) {
                        utf8CharCodes.push(c);
                    } else if (c < 2048) {
                        utf8CharCodes.push((c >> 6) | 192, (c & 63) | 128);
                    } else {
                        utf8CharCodes.push((c >> 12) | 224, ((c >> 6) & 63) | 128, (c & 63) | 128);
                    }
                }
                return utf8CharCodes;
            }

            var cachedCrcTable = null;

            function buildCRCTable() {
                var table = [];
                for (var i = 0, j, crc; i < 256; ++i) {
                    crc = i;
                    j = 8;
                    while (j--) {
                        if ((crc & 1) == 1) {
                            crc = (crc >>> 1) ^ 0xEDB88320;
                        } else {
                            crc >>>= 1;
                        }
                    }
                    table[i] = crc >>> 0;
                }
                return table;
            }

            function getCrcTable() {
                if (!cachedCrcTable) {
                    cachedCrcTable = buildCRCTable();
                }
                return cachedCrcTable;
            }

            return function (str) {
                var utf8CharCodes = utf8encode(str), crc = -1, crcTable = getCrcTable();
                for (var i = 0, len = utf8CharCodes.length, y; i < len; ++i) {
                    y = (crc ^ utf8CharCodes[i]) & 0xFF;
                    crc = (crc >>> 8) ^ crcTable[y];
                }
                return (crc ^ -1) >>> 0;
            };
        })();

        var dom = api.dom;

        function escapeTextForHtml(str) {
            return str.replace(/</g, "&lt;").replace(/>/g, "&gt;");
        }

        function nodeToInfoString(node, infoParts) {
            infoParts = infoParts || [];
            var nodeType = node.nodeType, children = node.childNodes, childCount = children.length;
            var nodeInfo = [nodeType, node.nodeName, childCount].join(":");
            var start = "", end = "";
            switch (nodeType) {
                case 3: // Text node
                    start = escapeTextForHtml(node.nodeValue);
                    break;
                case 8: // Comment
                    start = "<!--" + escapeTextForHtml(node.nodeValue) + "-->";
                    break;
                default:
                    start = "<" + nodeInfo + ">";
                    end = "</>";
                    break;
            }
            if (start) {
                infoParts.push(start);
            }
            for (var i = 0; i < childCount; ++i) {
                nodeToInfoString(children[i], infoParts);
            }
            if (end) {
                infoParts.push(end);
            }
            return infoParts;
        }

        // Creates a string representation of the specified element's contents that is similar to innerHTML but omits all
        // attributes and comments and includes child node counts. This is done instead of using innerHTML to work around
        // IE <= 8's policy of including element properties in attributes, which ruins things by changing an element's
        // innerHTML whenever the user changes an input within the element.
        function getElementChecksum(el) {
            var info = nodeToInfoString(el).join("");
            return crc32(info).toString(16);
        }

        // Author: Leo
        function validateQualifiedElement(node) {
            return node.nodeType == 1 
            	&& node.className != "search_highlight" 
            	&& node.className != "highlight" 
            	&& node.className != "noteIcon" 
            	&& node.className != "tagIcon" 
            	&& node.className != "circle" 
            	&& node.className != "tagiconellipse";
        }

        // Author: Leo
        function serializePosition(node, offset, rootNode) {
            var xpath = '', n = node;
            rootNode = rootNode || dom.getDocument(node).documentElement;
            while (n && $(n).attr("class") != "main") {
                if (validateQualifiedElement(n)) {
                    var index = findNodeIndexInParent(n);
                    xpath = '/' + n.nodeName + '[' + index + ']' + xpath;
                }
                n = n.parentNode;
            }
            return xpath + ":" + getXPathOffset(node, offset);
        }

        // Author: Leo
        function getXPathOffset(orgContainerNode, orgOffset) {
            var node = orgContainerNode;
            var offset = orgOffset;
            var orgNodeQualified = true;
            while (!validateQualifiedElement(node)) {
                node = node.parentNode;
                orgNodeQualified = false;
            }
            if (!orgNodeQualified) {
                offset = calculateOffset(node, orgContainerNode, orgOffset);
            }
            return offset;
        }

        // Author: Leo
        function calculateOffset(newContainerNode, orgContainerNode, orgOffset) {
            var doc = api.DomRange.getRangeDocument(this);
            var preSelectionRange = api.createRange(doc);
            containerNode = newContainerNode || dom.getBody(doc);
            preSelectionRange.selectNodeContents(containerNode);
            preSelectionRange.setEnd(orgContainerNode, orgOffset);
            return preSelectionRange.toString().length;
        }

        // Author: Leo
        function findNodeIndexInParent(node) {
            var elementName = node.nodeName;
            var i = 1;
            while ((node = node.previousSibling) != null) {
                if (node.nodeName == elementName) {
                    i++;
                }
            }
            return i;
        }

        //Author: Leo
		function deserializePosition(serialized, rootNode, doc) {
            if (!rootNode) {
                rootNode = (doc || document).documentElement;
            }
            try {
                var parts = serialized.split(":");
                var node = serialized == "" || serialized == undefined ? null : rootNode;
                var nodeIndices = parts[0] ? parts[0].split("/") : [], nodeLevel = nodeIndices.length, i = 0;
                while (i < nodeLevel) {
                    if (nodeIndices[i] != "" && nodeIndices[i] != undefined && nodeIndices[i] != null && node != null) {
                        var tagName = nodeIndices[i].split('[')[0];
                        var nodeIndex = nodeIndices[i].split('[')[1].replace("]", "") - 1;
                        var nodeoffset = 0, j = 0;
                        while (j < node.childNodes.length) {
                            if (node.childNodes[j].nodeName.toUpperCase() == tagName.toUpperCase()) {
                                if (nodeoffset == nodeIndex) {
                                    node = node.childNodes[j];
                                    nodeoffset++;
                                    break;
                                }
                                nodeoffset++;
                            }
                            j++;
                        }

                        if (nodeoffset <= nodeIndex) {
                            node = null;
                            break;
                        }
                    }
                    i++;
                }
            }
            catch (e) {
                node = null;
            }
            return new dom.DomPosition(node, parseInt(parts[1], 10));
        }

        // Author: Leo
        function serializeRange(range, omitChecksum, rootNode) {
            rootNode = rootNode || api.DomRange.getRangeDocument(range).documentElement;
            if (!dom.isOrIsAncestorOf(rootNode, range.commonAncestorContainer)) {
                throw module.createError("serializeRange(): range " + range.inspect() +
                    " is not wholly contained within specified root node " + dom.inspectNode(rootNode));
            }
            var serialized = serializePosition(range.startContainer, range.startOffset, rootNode) + "," +
                serializePosition(range.endContainer, range.endOffset, rootNode);
            return serialized;
        }

        var deserializeRegex = /^([^,]+),([^,\{]+)(\{([^}]+)\})?$/;

        // Author: Leo
        function deserializeRange(serialized, rootNode, doc) {
            if (rootNode) {
                doc = doc || dom.getDocument(rootNode);
            } else {
                doc = doc || document;
                rootNode = doc.documentElement;
            }
            var range = api.createRange(doc);
            var result = serialized.split(",");
            try {
                var start = deserializePosition(result[0], rootNode, doc), end = deserializePosition(result[1], rootNode, doc);
                if (start.node != null && end.node != null && start.node != undefined && end.node != "undefined") {
                    range.moveToBookmarkEx({
                        start: {
                            node: start.node,
                            offset: start.offset
                        },
                        end: {
                            node: end.node,
                            offset: end.offset
                        }
                    });
                }
                else {
                    range = null;
                }

            }
            catch (e) {
                range = null;
            }
            return range;
        }

        // Author: Leo
        function deserializeRangeByJson(serialized, startMainDiv, endtMainDiv, doc) {
            doc = doc || window.document;
            var startPath = serialized.StartXpath + ":" + serialized.StartXpathOffset;
            var endPath = serialized.EndXpath + ":" + serialized.EndXpathOffset;
            var start = deserializePosition(startPath, startMainDiv, doc), end = deserializePosition(endPath, endtMainDiv, doc);
            var range = api.createRange(doc);
            try {
                if (start.node != null && end.node != null && start.node != "undefined" && end.node != "undefined") {
                    range.moveToBookmarkEx({
                        start: {
                            node: start.node,
                            offset: start.offset
                        },
                        end: {
                            node: end.node,
                            offset: end.offset
                        }
                    });
                }
                else {
                    range = null;
                }
            }
            catch (e) {
                range = null;
            }
            //range.setStartAndEnd(start.node.firstChild, start.offset, end.node.lastChild, end.offset);
            return range;
        }

        function canDeserializeRange(serialized, rootNode, doc) {
            if (!rootNode) {
                rootNode = (doc || document).documentElement;
            }
            var result = deserializeRegex.exec(serialized);
            var checksum = result[3];
            return !checksum || checksum === getElementChecksum(rootNode);
        }

        function serializeSelection(selection, omitChecksum, rootNode) {
            selection = api.getSelection(selection);
            var ranges = selection.getAllRanges(), serializedRanges = [];
            for (var i = 0, len = ranges.length; i < len; ++i) {
                serializedRanges[i] = serializeRange(ranges[i], omitChecksum, rootNode);
            }
            return serializedRanges.join("|");
        }

        function deserializeSelection(serialized, rootNode, win) {
            if (rootNode) {
                win = win || dom.getWindow(rootNode);
            } else {
                win = win || window;
                rootNode = win.document.documentElement;
            }
            var serializedRanges = serialized.split("|");
            var sel = api.getSelection(win);
            for (var i = 0, len = serializedRanges.length; i < len; ++i) {
                var range = deserializeRange(serializedRanges[i], rootNode, win.document);
                if (range != null && range != undefined) {
                    sel.addRange(range);
                }
            }
            return sel.getAllRanges();
        }


        //Author:Leo
        function deserializeSelectionByJson(serializedRanges, rootNode, win) {
            if (rootNode) {
                win = win || dom.getWindow(rootNode);
            } else {
                win = win || window;
                rootNode = win.document.documentElement;
            }
            var ranges = [];
            for (var i = 0, len = serializedRanges.length; i < len; ++i) {
                var startMainDiv = $("div.main[data-docid='" + serializedRanges[i].StartDocId + "']")[0];
                var endtMainDiv = $("div.main[data-docid='" + serializedRanges[i].EndDocId + "']")[0];

                ranges[i] = deserializeRangeByJson(serializedRanges[i], startMainDiv, endtMainDiv, win.document); 
            }
            return ranges;
        }

        function canDeserializeSelection(serialized, rootNode, win) {
            var doc;
            if (rootNode) {
                doc = win ? win.document : dom.getDocument(rootNode);
            } else {
                win = win || window;
                rootNode = win.document.documentElement;
            }
            var serializedRanges = serialized.split("|");

            for (var i = 0, len = serializedRanges.length; i < len; ++i) {
                if (!canDeserializeRange(serializedRanges[i], rootNode, doc)) {
                    return false;
                }
            }

            return true;
        }

        var cookieName = "rangySerializedSelection";

        function getSerializedSelectionFromCookie(cookie) {
            var parts = cookie.split(/[;,]/);
            for (var i = 0, len = parts.length, nameVal, val; i < len; ++i) {
                nameVal = parts[i].split("=");
                if (nameVal[0].replace(/^\s+/, "") == cookieName) {
                    val = nameVal[1];
                    if (val) {
                        return decodeURIComponent(val.replace(/\s+$/, ""));
                    }
                }
            }
            return null;
        }

        function restoreSelectionFromCookie(win) {
            win = win || window;
            var serialized = getSerializedSelectionFromCookie(win.document.cookie);
            if (serialized) {
                deserializeSelection(serialized, win.doc);
            }
        }

        function saveSelectionCookie(win, props) {
            win = win || window;
            props = (typeof props == "object") ? props : {};
            var expires = props.expires ? ";expires=" + props.expires.toUTCString() : "";
            var path = props.path ? ";path=" + props.path : "";
            var domain = props.domain ? ";domain=" + props.domain : "";
            var secure = props.secure ? ";secure" : "";
            var serialized = serializeSelection(api.getSelection(win));
            win.document.cookie = encodeURIComponent(cookieName) + "=" + encodeURIComponent(serialized) + expires + path + domain + secure;
        }

        util.extend(api, {
            serializePosition: serializePosition,
            deserializePosition: deserializePosition,
            serializeRange: serializeRange,
            deserializeRange: deserializeRange,
            deserializeRangeByJson: deserializeRangeByJson,
            canDeserializeRange: canDeserializeRange,
            serializeSelection: serializeSelection,
            deserializeSelection: deserializeSelection,
            deserializeSelectionByJson: deserializeSelectionByJson,
            canDeserializeSelection: canDeserializeSelection,
            restoreSelectionFromCookie: restoreSelectionFromCookie,
            saveSelectionCookie: saveSelectionCookie,
            getElementChecksum: getElementChecksum,
            nodeToInfoString: nodeToInfoString
        });

        util.crc32 = crc32;
    });

    return rangy;
}, this);
