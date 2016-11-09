/**
 * Function to populate table
 */
function populateBintable(table, data) {
	var tr = [];
  	for (var i = 0; i < data.length; i++) {
		//row
		tr[i] = document.createElement('tr');
		
		//Cells
		var key = document.createElement('td');
		key.appendChild(document.createTextNode(data[i].key ) );
		var lat = document.createElement('td');
		lat.appendChild(document.createTextNode(data[i].lat ) );
		var lng = document.createElement('td');
		lng.appendChild(document.createTextNode(data[i].lng ) );
		var conc = document.createElement('td');
		conc.appendChild(document.createTextNode(data[i].concentration ) );
		var level = document.createElement('td');
		level.appendChild(document.createTextNode(data[i].level ) );
		
		//populate row
		tr[i].appendChild(key);
		tr[i].appendChild(lat);
		tr[i].appendChild(lng);
		tr[i].appendChild(conc);
		tr[i].appendChild(level);

		//add to table
		table.appendChild( tr[i] );
  	}
};