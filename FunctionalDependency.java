import java.util.ArrayList;
import java.util.Objects;

public class FunctionalDependency
{
    ArrayList<String> A;
    ArrayList<String> B;
    ArrayList<String> concatenated;
    FunctionalDependency(ArrayList<String> A1, ArrayList<String> B1)
    {
        this.A = A1;
        this.B = B1;
    }
    public boolean equals(FunctionalDependency fd)
    {
        
        if(this.A.containsAll(fd.A) && fd.A.containsAll(this.A) && this.B.containsAll(fd.B) && fd.B.containsAll(this.B))
        {
            return true;
        }
        return false;
    }
    public int hashCode() {
        ArrayList<String> concatenated = new ArrayList<String>();
        for(String f : this.A)
        {
            concatenated.add(f);
        }
        concatenated.add(">");
        for(String f : this.B)
        {
            concatenated.add(f);
        }
        return Objects.hash(concatenated);
    }

}