import java.util.ArrayList;

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
            for(int i=0;i<FD.size();i++)
            {
                if(!r.CandidateKeyList.containsAll(FD.get(i).A)){
                    return false;
                }
            }
        }
        return true;
    }    
}