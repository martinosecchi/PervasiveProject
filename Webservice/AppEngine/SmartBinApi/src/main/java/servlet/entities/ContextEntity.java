package servlet.entities;

import java.util.Date;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ContextEntity {
	
	@Id
	public long id;
	public String bin;
	@Index
	public java.util.Date date;
	public long concentration, level;

	public ContextEntity() {
		date = new Date();
	}
	
	public ContextEntity(String parent, long concentration, long level) {
		this();
		this.bin = parent;
		this.concentration = concentration;
		this.level = level;
	}
		
}
