package servlet.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SmartbinEntity {
	
<<<<<<< Updated upstream
	@Id public String key;
=======
	@Id
	public String name;
>>>>>>> Stashed changes
	public long lat, lng;

	public SmartbinEntity() {
	}
	
<<<<<<< Updated upstream
	public SmartbinEntity(long lat, long lng, long concentration, long level) {
		this();
		key = Key.create();
		this.concentration = concentration;
		this.level = level;
		this.lat = lat;
		this.lng = lng;
=======
	public SmartbinEntity(String name, long lat, long lng, long concentration, long level) {
		this.lat = lat;
		this.lng = lng;
		this.name = name;
>>>>>>> Stashed changes
	}
		
}
