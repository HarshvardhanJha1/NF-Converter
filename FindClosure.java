import java.util.ArrayList;
import java.util.Collections;

public class FindClosure 
{
    

    static boolean checkEquivalence(ArrayList<FunctionalDependency> FD1, ArrayList<FunctionalDependency> FD2)
    {
        for(int i=0;i<FD1.size();i++)
        {
            FunctionalDependency fd1 = FD1.get(i);
            ArrayList<String> A = fd1.A;
            ArrayList<String> B = fd1.B;
            ArrayList<String> Closure = new ArrayList<String>();
            for(String f:A)
            {
                Closure.add(f);
            }
            Collections.sort(A);
            Collections.sort(B);
        
            while(true){
                ArrayList<String> OldClosure = new ArrayList<String>();
                for(String f : Closure){
                    OldClosure.add(f);
                }
                for(int j=0;j<FD2.size();j++)
                {
                    Collections.sort(FD2.get(j).A);
                    if(Closure.containsAll(FD2.get(j).A))
                    {
                        for(String f : FD2.get(j).B){
                            if(!Closure.contains(f)){
                                Closure.add(f);
                                Collections.sort(Closure);
                            }
                        }
                    }
                }
                Collections.sort(Closure);
                Collections.sort(OldClosure);
                if(OldClosure.equals(Closure)){
                    break;
                }
            }   
            if(!Closure.containsAll(B)){
                return false;
            }

        }
        return true;
    }    
}