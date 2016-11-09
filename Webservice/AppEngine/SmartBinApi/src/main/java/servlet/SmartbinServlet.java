package servlet;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
	final static long M_range = 0.00005L;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {



		List<BeaconEntity> bins = null;
		try {
			String _key = req.getParameter("key"), p1 = req.getParameter("lat"), p2 = req.getParameter("lng");
			if( isValidArg(_key) ) {
				bins = new ArrayList<SmartbinEntity>();
				bins.add( (BeaconEntity)ofy().load().key(_key).now() );
				doResponse(resp, bins);
				return;
			}
			if( isValidArg(p1) && isValidArg(p2) ) {
				//checks if input params are actual values
				Long  	lat = Long.parseLong( p1 ),
						lng = Long.parseLong( p2 );    		
				if(lat != null && lng != null) {
					ArrayList<SmartbinEntity> all = ofy().load().type(SmartbinEntity.class).list();
					//filters the cached values if lat/lng has values
					bins = new ArrayList<SmartbinEntity>();
					for (Smartbin sb : all) {
						if( filterRangeLocation(lat, lng, sb) ) bins.add(be);
					}
					doResponse(resp, bins);
					return;
				}
			}
		} catch (Exception e) {}
		
	}
	
	void doResponse(HttpServletResponse resp, List<BeaconEntity> bins) {
		Gson gson = new Gson();
		resp.setContentType("application/json");
		gson.toJson(bins, resp.getWriter());

	}

	//Objectify only allows to filter on 1 property at a time so we filter in the ws requests
	private boolean filterRangeLocation(long lat, long lng, BeaconEntity be) {
		return 	(be.lat <= (lat + M_range)) 	&&
				(be.lat >= (lat - M_range)) 	&&
				(be.lng <= (lng + M_range)) 	&&
				(be.lng >= (lng - M_range));
	}

	private boolean isValidArg(String arg) {
		return arg != null || arg != "";
	}
	
	// Process the http POST of the form
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Gson gson = new Gson();

		String rawJson = new BufferedReader(
				new InputStreamReader( req.getInputStream() )
				).readLine();

		SmartbinEntity sbe = gson.fromJson(rawJson, SmartbinEntity.class);
		SmartbinEntity res = null;

		//lookup tables by id
		if( sbe.key != null )
		res = ofy().load().type(SmartbinEntity.class).id(sbe.key).now();

		//new key or no key
		if ( res == null ) {
			com.googlecode.objectify.Key<BeaconEntity> k = ofy().save().entity(sbe).now();
			gson.toJson( (BeaconEntity)ofy().load().key(k).now() , resp.getWriter());
		}
		//existing entity - merge
		else {
			res.lat = sbe.lat;
			res.lng = sbe.lng;
			com.googlecode.objectify.Key<BeaconEntity> k = ofy().save().entity(res).now();
			gson.toJson( (BeaconEntity)ofy().load().key(k).now(), resp.getWriter());
		}
	}

}
