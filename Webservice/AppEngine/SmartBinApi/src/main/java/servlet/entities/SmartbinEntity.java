package servlet.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SmartbinEntity {
	
	@Id public String key;
	public long lat, lng;
	public long concentration, level;

	public SmartbinEntity() {
	}
	
	public SmartbinEntity(long lat, long lng, long concentration, long level) {
		this();
		key = Key.create();
		this.concentration = concentration;
		this.level = level;
		this.lat = lat;
		this.lng = lng;
	}
		
}
