import java.util.ArrayList;
import java.util.Collections;

public class NFChecker 
{
    static boolean is2NF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        return true;
    }

    static boolean is3NF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        return true;
    }
    static boolean isBCNF(Relation r, ArrayList<FunctionalDependency> FD)
    {

        if(!(is2NF(r, FD) && is3NF(r, FD))){return false;}
        else
        {
            int count=0;
            for(int i=0;i<FD.size();i++)
            {
                for(ArrayList<String> sk : r.SuperKeyList){
                    Collections.sort(sk);
                    if(FD.get(i).A.equals(sk))
                    {
                        count++;
                        break;
                    }
                }
                
            }
            if(count==FD.size()){return true;}
        }
        return false;
    }    
}