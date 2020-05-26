import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Collections;

@Stateless
public class Cost implements ICostRemote {

    @Override
    public double calculateMinimalCutCost(ArrayList<Double> x, ArrayList<Double> y) {

        x.sort(Collections.reverseOrder());
        y.sort(Collections.reverseOrder());

        int j = 0, i = 0;
        int cutX = 1, cutY = 1;
        double cost = 0;

        while(i< x.size() && j< y.size()){

            if(x.get(i)*cutY >= y.get(j)*cutX){
                cutX++;
                cost += x.get(i)*cutY;
                i++;
            }
            else{
                cutY++;
                cost += y.get(j)*cutX;
                j++;
            }
        }

        for(;i<x.size();i++){
            cost += x.get(i)*cutY;
        }

        for(;j<y.size();j++){
            cost += y.get(j)*cutX;
        }

        return cost;
    }
}
