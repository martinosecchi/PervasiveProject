/**
 * Function to populate table
 */
function populateBindiv(parentDiv, data) {
	if(data.length == 0) {
		var ele = {name:"test", lat:"22", lng:"33"};
		
		var inner = createBin(ele);		
		parentDiv.appendChild( inner );
	}
  	for (var i = 0; i < data.length; i++) {
		var inner = createBin(data[i]);		
		//add to table
		parentDiv.appendChild( inner );
  	}
};

function createBin(data) {
	var inner = document.createElement('div');
	inner.className = 'stylish bin';
	inner.addEventListener('click', function() { location.href = '/r/ctx?bin='+data.name; }, false);
	var txt = "<p><b>"+data.name+"</b></p><br><p>"+data.name+"</p><br><p>"+data.lat+"</p>";
		
	var nm = document.createElement('p');
	nm.appendChild( document.createTextNode( data.name ) );
	var latlng = document.createElement('p');
	latlng.appendChild( document.createTextNode( data.lat + ", " + data.lng ) );
		
	//Cells
	inner.appendChild( nm );
	inner.appendChild( latlng );
	return inner;
};