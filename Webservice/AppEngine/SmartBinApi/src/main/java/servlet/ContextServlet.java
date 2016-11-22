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

import data.ObjectifyWrapper.BIN;
import data.ObjectifyWrapper.CTX;
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
				ctxts = CTX.getByBin(bin_name);
			} else throw new Exception("No bin_name was provided");
			if(ctxts == null) throw new Exception("Contexts are null for bin: "+bin_name);
			gson.toJson(ctxts, resp.getWriter());
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No contexts where found, "+e);
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
		ContextEntity res = CTX.saveContext(ce);
		gson.toJson( res , resp.getWriter());
	}

}
