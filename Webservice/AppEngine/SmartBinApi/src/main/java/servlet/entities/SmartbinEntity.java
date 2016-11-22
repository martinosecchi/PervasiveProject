package servlet.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class SmartbinEntity {
	@Id
	public String name;
	public long lat, lng;

	public SmartbinEntity() {
	}
	
	public SmartbinEntity(String name, long lat, long lng) {
		this.lat = lat;
		this.lng = lng;
		this.name = name;
	}
		
}
