package servlet;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.googlecode.objectify.Result;

import servlet.entities.SmartbinEntity;

public class SmartbinServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Gson gson = new Gson();
		resp.setContentType("application/json");
		List<SmartbinEntity> all = ofy().load().type(SmartbinEntity.class).list();
		gson.toJson(all, resp.getWriter());
	}

	// Process the http POST of the form
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Gson gson = new Gson();
		com.googlecode.objectify.Key<SmartbinEntity> k;
		String rawJson = new BufferedReader(
				new InputStreamReader( req.getInputStream() )
				).readLine();

		SmartbinEntity sbe = gson.fromJson(rawJson, SmartbinEntity.class);
		SmartbinEntity res = null;
		if( sbe.name != null ) res = ofy().load().type(SmartbinEntity.class).id(sbe.name).now();

		//new key or no key
		if ( res == null ) {
			k = ofy().save().entity(sbe).now();
		}
		else {
			//existing entity - merge
			res.lat = sbe.lat;
			res.lng = sbe.lng;
			k = ofy().save().entity(res).now();
		}
		gson.toJson( (SmartbinEntity)ofy().load().key(k).now(), resp.getWriter());
	}

}
