Sortable.create("column_1", {
	constraint: false,
	dropOnEmpty:true,
	hoverclass: 'hovering',
	handle: 'handle_move',
	ghosting: false,
	containment:['column_1','column_2'],
	onChange: function() { },
	onUpdate: function(){ updateColumns(); savePlacement(); },
	tag:'div'
});


Sortable.create("column_2", {
	constraint: false,
	dropOnEmpty:true,
	hoverclass: 'hovering',
	handle: 'handle_move',
	ghosting: false,
	containment:['column_1','column_2'],
	onChange: function() {  },
	onUpdate: function(){ updateColumns(); savePlacement(); },
	tag:'div'
});

function minAll() {
	var nodes = document.getElementsByClassName('section_content');
	for( var i = 0; i < nodes.length; i++) {
		Element.hide(nodes[i]);
	}
}

function maxAll() {
	var nodes = document.getElementsByClassName('section_content');
	for( var i = 0; i < nodes.length; i++) {
		Element.show(nodes[i]);
	}
}

function savePlacement() {
	new Ajax.Request('action.php?' + Sortable.serialize("column_1") );
	new Ajax.Request('action.php?' + Sortable.serialize("column_2") );
}

function resetPlacement() {

	if (confirm("Confirm Reset Placement")) {
		deleteCookie('column_1'); 
		deleteCookie('column_2'); 
		window.location.reload();
	}
}
