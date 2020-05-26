import pl.jrj.dsm.IDSManagerRemote;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michał Śliwa
 */
@WebServlet(name = "servlet136933")
public class Solver extends HttpServlet {

    private static final long serialVersionUID = -260607599701613167L;

    @EJB
    private IBlockRemote block;

    @EJB(mappedName = "java:global/ejb-project/DSManager")
    private IDSManagerRemote dsManager;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String tableName = request.getParameter("t");
        List<float[]> points = collectDataFromDB(tableName);
        float result = block.calculateLateralSurface(points);
        response.getWriter().print(String.format("%.5f", result));
    }

    private List<float[]> collectDataFromDB(String tableName) {

        List<float[]> points = new ArrayList<>();
        try {

            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup(dsManager.getDS());
            Connection conn = dataSource.getConnection();

            Statement s = conn.createStatement();
            ResultSet r = s.executeQuery("SELECT x,y,z from " + tableName);
            while (r.next()) {
                float x = r.getFloat("x");
                float y = r.getFloat("y");
                float z = r.getFloat("z");

                points.add(new float[]{x, y, z});
            }

            conn.close();

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }

        return points;
    }


}
