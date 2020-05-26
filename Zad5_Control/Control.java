import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Michał Śliwa
 */
@WebServlet(name = "Control")
public class Control extends HttpServlet {
    private static final long serialVersionUID = 8323012551755541446L;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print(String.format("%.3f", 0.5435f));
    }
}
