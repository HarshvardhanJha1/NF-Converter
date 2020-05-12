import java.util.ArrayList;
import java.util.Collections;

public class NFChecker 
{
    static boolean is2NF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        for(ArrayList<String> ck: r.CandidateKeyList)
        {
            for(int i=0;i<FD.size();i++)
            {
                if(ck.containsAll(FD.get(i).A)&& FD.get(i).A.size()!=ck.size())
                    return false;
            }
        }
        return true;
    }

    static boolean is3NF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        if(!(is2NF(r, FD))){return false;}
        else
        {
            boolean flag=false;
            int count=0;
            for(int i=0;i<FD.size();i++)
            {
                flag=false;
                Collections.sort(FD.get(i).A);
                for(ArrayList<String> sk : r.SuperKeyList){
                    Collections.sort(sk);
                    if(FD.get(i).A.equals(sk))
                    {
                        flag=true;
                        break;
                    }
                
                }
                for(ArrayList<String> var : r.CandidateKeyList){
                    Collections.sort(var);
                    if(var.containsAll(FD.get(i).B))
                    {
                        flag=true;
                        break;
                    }
                }
                if(flag)
                {
                    count++;
                }
            }
            if(count==FD.size()){return true;}
        }
        return false;
    }
    static boolean isBCNF(Relation r, ArrayList<FunctionalDependency> FD)
    {

        if(!(is2NF(r, FD) && is3NF(r, FD))){return false;}
        else
        {
            int count=0;
            for(int i=0;i<FD.size();i++)
            {
                Collections.sort(FD.get(i).A);
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