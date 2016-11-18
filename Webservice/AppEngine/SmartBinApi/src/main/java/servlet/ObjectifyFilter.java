package servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;

import servlet.entities.*;

public class ObjectifyFilter implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public void contextInitialized(ServletContextEvent arg0) {
		ObjectifyService.register(SmartbinEntity.class);
		ObjectifyService.register(ContextEntity.class);
	}

}
