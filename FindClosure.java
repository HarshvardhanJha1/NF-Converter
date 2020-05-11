import java.util.ArrayList;
import java.util.Collections;

public class FindClosure 
{
    static ArrayList<String> findClosure(ArrayList<String> attributeList ,ArrayList<FunctionalDependency> FD)
    {
        ArrayList<String> Closure = new ArrayList<String>();
        for(String f : attributeList)
        {
            Closure.add(f);
        }
        while(true)
           {
               ArrayList<String> OldClosure = new ArrayList<String>();
               for(String f : Closure)
               {
                   OldClosure.add(f);
               } 
               for(int i=0;i<FD.size();i++)
               {
                   FunctionalDependency fd = FD.get(i);
                   if(Closure.containsAll(fd.A))
                   {
                       for(int j=0;j<fd.B.size();j++)
                       {
                           if(!Closure.contains(fd.B.get(j))){
                               Closure.add(fd.B.get(j));
                           }
                       }
                       Collections.sort(Closure);
                   }
               }
               Collections.sort(Closure);
               Collections.sort(OldClosure);
               if(Closure.equals(OldClosure))
               {
                   break;
               }
           }

        return Closure;
    }

    

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