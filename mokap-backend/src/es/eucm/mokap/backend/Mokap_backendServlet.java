package es.eucm.mokap.backend;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class Mokap_backendServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
