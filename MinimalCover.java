import java.util.ArrayList;
import java.util.Collections;


public class MinimalCover 
{
    static ArrayList<FunctionalDependency> findMinimalCover(ArrayList<FunctionalDependency> FD)
    {
        
        ArrayList<FunctionalDependency> minifiedFD2 = new ArrayList<FunctionalDependency>();
        //Removing compound dependents on RHS
        for(int i=0;i<FD.size();i++)
        {
            FunctionalDependency fd = FD.get(i);
            for(int j=0;j<fd.B.size();j++)
            {
                ArrayList<String> B1 = new ArrayList<String>();
                B1.add(fd.B.get(j));
                FunctionalDependency newFd = new FunctionalDependency(fd.A, B1);
                minifiedFD2.add(newFd);
            }
        }
        
        //Attempting to remove compound elements on LHS
        
        ArrayList<FunctionalDependency> minifiedFD1 = minifiedFD2;
        for(int i=0;i<minifiedFD1.size();i++)
        {
            if(minifiedFD1.get(i).A.size()==1)
            {
                continue;
            }
            else
            {
                for(int j=0;j<minifiedFD1.get(i).A.size();j++)
                {
                    String l = minifiedFD1.get(i).A.get(j);
                    if(minifiedFD1.get(i).A.size()==1){
                        break;
                    }
                    String temp = l;
                    ArrayList<String> remaining = new ArrayList<String>();
                    //ArrayList<String> removed = new ArrayList<String>();
                    for(String f : minifiedFD1.get(i).A){
                        if(!f.equals(l)){
                            remaining.add(f);
                        }
                    }
                    //System.out.println("Remaining elements "+remaining);
                    ArrayList<String> removed = new ArrayList<String>();
                    removed.add(temp);
                    ArrayList<String> tempClosure = FindClosure.findClosure(removed, minifiedFD1);
                    //System.out.println("Closure of "+removed+"is "+tempClosure);
                    ArrayList<String> intersection = new ArrayList<String>();
                    for(int g=0;g<tempClosure.size();g++)
                    {
                        if(remaining.contains(tempClosure.get(g))){
                            intersection.add(tempClosure.get(g));
                        }
                    }
                    if(intersection.size()!=0){
                        ArrayList<String> A1 = minifiedFD1.get(i).A;
                        for(int g=0;g<intersection.size();g++){
                            if(A1.contains(intersection.get(g))){
                                A1.remove(intersection.get(g));
                            }
                        }
                        minifiedFD1.get(i).A = A1;
                    }
                    
                }
            }
             
        }
    
        //Removal of remaining redundant dependencies
        for(int i=0;i<minifiedFD1.size();i++)
        {
            ArrayList<FunctionalDependency> tempFD = new ArrayList<FunctionalDependency>();
            for(int j=0;j<minifiedFD1.size();j++){
                if(i!=j){tempFD.add(minifiedFD1.get(j));}
            }
            
            FunctionalDependency currFD = new FunctionalDependency(minifiedFD1.get(i).A, minifiedFD1.get(i).B);
            //System.out.println("Current FD is :"+currFD.A+">"+currFD.B);
            ArrayList<String> Closure = FindClosure.findClosure(currFD.A, tempFD);
            //System.out.println("Closure if "+currFD.A+"is"+Closure);
            if(Closure.containsAll(currFD.B)){
                minifiedFD1.remove(i);i--;
            }

        }

        for(int i=0;i<minifiedFD1.size();i++)
        {
            for(int j=0;j<minifiedFD1.size();j++)
            {
                if(minifiedFD1.get(i).A.equals(minifiedFD1.get(j).A) && i!=j && minifiedFD1.get(i).B.equals(minifiedFD1.get(j).B))
                {
                    minifiedFD1.remove(j);j--;
                }
            }
        }

        return minifiedFD1;

        // ArrayList<FunctionalDependency> minifiedFD = new ArrayList<FunctionalDependency>();
        // return minifiedFD;
    }
}