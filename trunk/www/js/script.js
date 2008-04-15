function setCookie (cookieName, cookieValue, forever) {

	var date = new Date("June 30, 2020");
	var cookieDate = date.toGMTString();
	var theCookie = cookieName + "=" + cookieValue;

	if (forever) theCookie += ";expires=" + cookieDate;
	
	theCookie += ";path=/ ";
	document.cookie = theCookie;
}


function deleteCookie (cookieName) {

	var date = new Date("June 30, 1998");
	var cookieDate = date.toGMTString();
	var theCookie = cookieName + "=" + 'deleted';

	theCookie += ";expires=" + cookieDate + ";path=/ ";
	document.cookie = theCookie;
}





function trim(s)
{
	while ((s.substring(0,1) == ' ') || (s.substring(0,1) == '\n') || (s.substring(0,1) == '\r'))
	{ s = s.substring(1,s.length); }
	while ((s.substring(s.length-1,s.length) == ' ') || (s.substring(s.length-1,s.length) == '\n') || (s.substring(s.length-1,s.length) == '\r'))
	{ s = s.substring(0,s.length-1); }
	return s;
}





function updateColumns() {
	var className1 = 'table_blue';
	var className2 = 'table_yellow';
	
	var nodes = $('column_1').childNodes;	
	for( var x = 0; x < nodes.length ; x++ ) {
		nodes[x].className = (x%2 != 0) ? className1 : className2;
	}
	
	var nodes = $('column_2').childNodes;	
	for( var x = 0; x < nodes.length ; x++ ) {
		nodes[x].className = (x%2 == 0) ? className1 : className2;
	}
	
}


/***********************************************
* Cool DHTML tooltip script II- © Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/

var offsetfromcursorX=12 //Customize x offset of tooltip
var offsetfromcursorY=10 //Customize y offset of tooltip

var offsetdivfrompointerX=10 //Customize x offset of tooltip DIV relative to pointer image
var offsetdivfrompointerY=14 //Customize y offset of tooltip DIV relative to pointer image. Tip: Set it to (height_of_pointer_image-1).

document.write('<div id="dhtmltooltip"></div>') //write out tooltip DIV
document.write('<img id="dhtmlpointer" src="images/arrow.gif">') //write out pointer image

var ie=document.all
var ns6=document.getElementById && !document.all
var enabletip=false
if (ie||ns6)
var tipobj=document.all? document.all["dhtmltooltip"] : document.getElementById? document.getElementById("dhtmltooltip") : ""

var pointerobj=document.all? document.all["dhtmlpointer"] : document.getElementById? document.getElementById("dhtmlpointer") : ""

function ietruebody(){
	return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

function ddrivetip(thetext, thewidth, thecolor){
	if (ns6||ie){
		if (typeof thewidth!="undefined") tipobj.style.width=thewidth+"px"
		if (typeof thecolor!="undefined" && thecolor!="") tipobj.style.backgroundColor=thecolor
		tipobj.innerHTML=thetext
		enabletip=true
		return false
	}
}

function positiontip(e){
	if (enabletip){
		var nondefaultpos=false
		var curX=(ns6)?e.pageX : event.clientX+ietruebody().scrollLeft;
		var curY=(ns6)?e.pageY : event.clientY+ietruebody().scrollTop;
		//Find out how close the mouse is to the corner of the window
		var winwidth=ie&&!window.opera? ietruebody().clientWidth : window.innerWidth-20
		var winheight=ie&&!window.opera? ietruebody().clientHeight : window.innerHeight-20

		var rightedge=ie&&!window.opera? winwidth-event.clientX-offsetfromcursorX : winwidth-e.clientX-offsetfromcursorX
		var bottomedge=ie&&!window.opera? winheight-event.clientY-offsetfromcursorY : winheight-e.clientY-offsetfromcursorY

		var leftedge=(offsetfromcursorX<0)? offsetfromcursorX*(-1) : -1000

		//if the horizontal distance isn't enough to accomodate the width of the context menu
		if (rightedge<tipobj.offsetWidth){
			//move the horizontal position of the menu to the left by it's width
			tipobj.style.left=curX-tipobj.offsetWidth+"px"
			nondefaultpos=true
		}
		else if (curX<leftedge)
		tipobj.style.left="5px"
		else{
			//position the horizontal position of the menu where the mouse is positioned
			tipobj.style.left=curX+offsetfromcursorX-offsetdivfrompointerX+"px"
			pointerobj.style.left=curX+offsetfromcursorX+"px"
		}

		//same concept with the vertical position
		if (bottomedge<tipobj.offsetHeight){
			tipobj.style.top=curY-tipobj.offsetHeight-offsetfromcursorY+"px"
			nondefaultpos=true
		}
		else{
			tipobj.style.top=curY+offsetfromcursorY+offsetdivfrompointerY+"px"
			pointerobj.style.top=curY+offsetfromcursorY+"px"
		}
		tipobj.style.visibility="visible"
		if (!nondefaultpos)
		pointerobj.style.visibility="visible"
		else
		pointerobj.style.visibility="hidden"
	}
}

function hideddrivetip(){
	if (ns6||ie){
		enabletip=false
		tipobj.style.visibility="hidden"
		pointerobj.style.visibility="hidden"
		tipobj.style.left="-1000px"
		tipobj.style.backgroundColor=''
		tipobj.style.width=''
	}
}

document.onmousemove=positiontip

function writeSource(div) {
	if (!document.getElementById) { return; }
	var o = document.getElementById(div);
	if (typeof(o) == "undefined" || o==null) { return; }
	var s = o.innerHTML;
	if (s==null || s.length==0) { 
		return;
		}
	else {
		var i;
		for(i=0;s.charAt(i)==" "||s.charAt(i)=="\n"||s.charAt(i)=="\r"||s.charAt(i)=="\t";i++) {}
		s = s.substring(i);
		for (i = s.length; i>0; i--) {
			if (s.charAt(i)=="<") {
				s = s.substring(0,i) + "&lt;" + s.substring(i+1) ;
				}
			}
		for (i = s.length; i>0; i--) {
			if (s.charAt(i)==">") {
				s = s.substring(0,i) + "&gt;" + s.substring(i+1) ;
				}
			}
		for (i = s.length; i>0; i--) {
			if (s.charAt(i)=="\t") {
				s = s.substring(0,i) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + s.substring(i+1) ;
				}
			}
		for (i = s.length; i>0; i--) {
			if (s.charAt(i)=="\n") {
				s = s.substring(0,i) + "<BR>" + s.substring(i+1) ;
				}
			}
		s = s + "<BR>";
		}
	document.write('<A STYLE="font-family:arial; font-size:x-small; text-decoration:none;" HREF="#" onClick="var d=document.getElementById(\'jssource'+div+'\').style; if(d.display==\'block\'){d.display=\'none\';this.innerText=\'+ Show Source\';}else{d.display=\'block\';this.innerText=\'- Hide Source\';} return false;">+ Show Source</A><BR>');
	document.write('<SPAN ID="jssource'+div+'" STYLE="display:none;background-color:#EEEEEE"><TT>'+s+'</TT></SPAN>');
	}
	
function setVisibility(layer_ref,state) {
	if (document.all) { //IS IE 4 or 5 (or 6 beta)
		eval( "document.all." + layer_ref + ".style.visibility = state");
	}
	if (document.layers) { //IS NETSCAPE 4 or below
		document.layers[layer_ref].visiblity = state;
	}
	if (document.getElementById && !document.all) {
		maxwell_smart = document.getElementById(layer_ref);
		maxwell_smart.style.visiblity = state;
	}
}
function visible(layer_ref)
{
	setVisibility(layer_ref,'visible');
}
function invisible(layer_ref) 
{
	setVisibility(layer_ref,'hidden');
}
function setDisplay(layer_ref,state) {
	if (document.all) { //IS IE 4 or 5 (or 6 beta)
		eval( "document.all." + layer_ref + ".style.display = state");
	}
	if (document.layers) { //IS NETSCAPE 4 or below
		document.layers[layer_ref].display = state;
	}
	if (document.getElementById && !document.all) {
		maxwell_smart = document.getElementById(layer_ref);
		maxwell_smart.style.display = state;
	}
}
function show(layer_ref)
{
	setDisplay(layer_ref,'');
}
function hide(layer_ref) 
{
	setDisplay(layer_ref,'none');
}
	