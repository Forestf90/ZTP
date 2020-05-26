import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Michal Sliwa
 */
public class AppClient {

    @EJB
    private static ICostRemote costRemote;

    @EJB
    private static IPlateRemote plateRemote;

    /**
     * Główna funkcja programu
     * pobiera dane wejsciowe, inicjalizuje moduły EJB
     * oraz wypisuje wynik algorytmu
     *
     * @param args dane wejsciowe - datasource oraz table
     */
    public static void main(String[] args) {

        try {
            String dataSource = args[0];
            String tableName = args[1];
            Context context = new InitialContext();

            plateRemote = (IPlateRemote) context
                    .lookup("java:global/136933/Plate!IPlateRemote");

            costRemote = (ICostRemote) context
                    .lookup("java:global/136933/Cost!ICostRemote");

            DataSource ds = (DataSource) context.lookup(dataSource);
            loadData(ds, tableName);

            double result = costRemote.calculateMinimalCutCost(plateRemote.getXList(), plateRemote.getYList());

            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Funkcja wczytuje dane z bazy danych i zapisuje w komponecie Plate
     *
     * @param ds    Zrodlo bazy danych
     * @param table nazwa tabeli
     */
    private static void loadData(DataSource ds, String table) {

        try (Connection con = ds.getConnection()) {
            String sqlSelect = "SELECT x,y FROM " + table + ";";
            Statement s = con.createStatement();
            ResultSet r = s.executeQuery(sqlSelect);
            while (r.next()) {
                double x = r.getFloat("x");
                double y = r.getFloat("y");

                if (x > 0) plateRemote.addX(x);
                if (y > 0) plateRemote.addY(y);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
