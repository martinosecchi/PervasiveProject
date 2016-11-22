package servlet.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class SmartbinEntity {
	@Id
	public String name;
	public Long lat, lng;
	
	public float calibration, concentration, level;

	public SmartbinEntity() {
	}
	
	public SmartbinEntity(String name, long lat, long lng) {
		this.lat = lat;
		this.lng = lng;
		this.name = name;
	}
	public SmartbinEntity(String name, long lat, long lng, float calibration) {
		this(name, lat, lng);
		this.calibration = calibration;
	}
		
}
