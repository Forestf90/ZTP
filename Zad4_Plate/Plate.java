import javax.ejb.Stateless;
import java.util.ArrayList;

@Stateless
public class Plate implements  IPlateRemote{

    private ArrayList<Double> x;
    private ArrayList<Double> y;


    public Plate(){
        x = new ArrayList<>();
        y = new ArrayList<>();
    }
    @Override
    public void addX(double x) {
        this.x.add(x);
    }

    @Override
    public void addY(double y) {
        this.y.add(y);
    }

    @Override
    public ArrayList<Double> getXList() {
        return this.x;
    }

    @Override
    public ArrayList<Double> getYList() {
        return this.y;
    }
}
