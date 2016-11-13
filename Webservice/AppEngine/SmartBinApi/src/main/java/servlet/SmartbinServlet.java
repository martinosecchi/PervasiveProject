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
	final static long M_range = (long)0.00005;
	final static HashMap<String, Long> mappings = new HashMap<String, Long>();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {



		List<SmartbinEntity> bins = null;
		try {
			String _key = req.getParameter("key"), p1 = req.getParameter("lat"), p2 = req.getParameter("lng");
			if( isValidArg(_key) ) {
				bins = new ArrayList<SmartbinEntity>();
				SmartbinEntity sbe = null;
				long id = lookupId(_key);
				sbe = ofy().load().type(SmartbinEntity.class).id(id).now();
				if(sbe == null) throw new Exception("no such element");
				bins.add( sbe );
			}
			else if( isValidArg(p1) && isValidArg(p2) ) {
				//checks if input params are actual values
				Long  	lat = Long.parseLong( p1 ),
						lng = Long.parseLong( p2 );    		
				if(lat != null && lng != null) {
					List<SmartbinEntity> all = ofy().load().type(SmartbinEntity.class).list();
					//filters the cached values if lat/lng has values
					bins = new ArrayList<SmartbinEntity>();
					for (SmartbinEntity sb : all) {
						if( filterRangeLocation(lat, lng, sb) ) bins.add(sb);
					}
				}
			} else {
					bins = ofy().load().type(SmartbinEntity.class).list();
			}
			doGetResponse(resp, bins);
		} catch (Exception e) {
			doGetResponse(resp, "Exception "+e);
		}
		
	}

	//Objectify only allows to filter on 1 property at a time so we filter in the ws requests
	private boolean filterRangeLocation(long lat, long lng, SmartbinEntity be) {
		return 	(be.lat <= (lat + M_range)) 	&&
				(be.lat >= (lat - M_range)) 	&&
				(be.lng <= (lng + M_range)) 	&&
				(be.lng >= (lng - M_range));
	}

	private boolean isValidArg(String arg) {
		return arg != null && arg != "";
	}
	
	// Process the http POST of the form
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Gson gson = new Gson();
		long id = -1;
		String rawJson = new BufferedReader(
				new InputStreamReader( req.getInputStream() )
				).readLine();

		SmartbinEntity sbe = gson.fromJson(rawJson, SmartbinEntity.class);
		SmartbinEntity res = null;

		//lookup tables by id
		if( sbe.key != null ) {
			id = lookupId(sbe.key);
			res = ofy().load().type(SmartbinEntity.class).id(id).now();
		}
		com.googlecode.objectify.Key<SmartbinEntity> k;
		//new key or no key
		if ( res == null ) {
			k = ofy().save().entity(sbe).now();
		}
		//existing entity - merge
		else {
			res = mergeBinEntity(res, sbe);
			k = ofy().save().entity( res ).now();
		}
		updateMap(res.key, res.id);
		gson.toJson( (SmartbinEntity)ofy().load().key(k).now(), resp.getWriter());
	}
	
	private SmartbinEntity mergeBinEntity(SmartbinEntity fromDS, SmartbinEntity fromReq) {		
		fromDS.key = fromReq.key != null && fromReq.key != fromDS.key ? fromReq.key : fromDS.key;
		fromDS.lat = fromReq.lat != fromDS.lat ? fromReq.lat : fromDS.lat;
		fromDS.lng = fromReq.lng != fromDS.lng ? fromReq.lng : fromDS.lng;
		fromDS.concentration = fromReq.concentration != fromDS.concentration ? fromReq.concentration : fromDS.concentration;
		fromDS.level = fromReq.level != fromReq.level ? fromReq.level : fromDS.level;
		
		return fromDS;
	}
	
	private void doGetResponse(HttpServletResponse resp, Object data) throws IOException {
		Gson gson = new Gson();
		resp.setContentType("application/json");
		gson.toJson(data, resp.getWriter());
	}

	private void updateMap(String key, long id) {
		this.mappings.put(key, id);
	}
	
	private long lookupId(String key) {
		return this.mappings.get(key) == null ? -1 : this.mappings.get(key);
	}
	
}
