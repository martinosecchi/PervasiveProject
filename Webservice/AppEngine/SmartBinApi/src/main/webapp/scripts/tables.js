/**
 * Function to populate table
 */
function populateBindiv(parentDiv, data) {
  	for (var i = 0; i < data.length; i++) {
		var inner = document.createElement('div');
		inner.className = 'stylish bin';
		
		//Cells
		inner.appendChild(document.createTextNode(data[i].name ) );
		inner.appendChild(document.createTextNode(data[i].lat ) );
		inner.appendChild(document.createTextNode(data[i].lng ) );
		
		//add to table
		parentDiv.appendChild( inner );
  	}
};