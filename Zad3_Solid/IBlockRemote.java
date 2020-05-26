import javax.ejb.Remote;
import java.util.List;

@Remote
public interface IBlockRemote {
    float calculateLateralSurface(List<float[]> points);
}
