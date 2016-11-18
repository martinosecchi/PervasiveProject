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


import servlet.entities.ContextEntity;

public class ContextServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Gson gson = new Gson();
		resp.setContentType("application/json");

		try {
			String bin_name = req.getParameter("bin");
			//String from_date = req.getParameter("date");
			List<ContextEntity> ctxts = null;
			if(bin_name != null) {
				ctxts = ofy().load().type(ContextEntity.class).filter("bin =", bin_name).order("bin").order("-date").list();
			} else {
				ctxts = ofy().load().type(ContextEntity.class).order("-date").list();
			}
			if(ctxts == null) throw new Exception("Contexts are null");
			gson.toJson(ctxts, resp.getWriter());
		} catch (Exception e) {
			gson.toJson("Something went wrong... "+e, resp.getWriter());
		}

	}

	// Process the http POST of the form
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Gson gson = new Gson();

		String rawJson = new BufferedReader(
				new InputStreamReader( req.getInputStream() )
				).readLine();

		ContextEntity ce = gson.fromJson(rawJson, ContextEntity.class);
		ContextEntity res = new ContextEntity(ce.bin, ce.concentration, ce.level);


		com.googlecode.objectify.Key<ContextEntity> k = ofy().save().entity(ce).now();
		gson.toJson( (ContextEntity)ofy().load().key(k).now() , resp.getWriter());
	}

}
