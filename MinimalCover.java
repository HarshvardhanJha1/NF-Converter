import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
        ArrayList<FunctionalDependency> minifiedFD1 = new ArrayList<FunctionalDependency>();
        for(int i=0;i<minifiedFD2.size();i++)
        {
            //clear minifiedFD1
            minifiedFD1.clear();
            for(int j=0;j<minifiedFD2.size();j++){
                if(i!=j){
                    minifiedFD1.add(minifiedFD2.get(j));
                }
            }
            
            ArrayList<String> B1 = new ArrayList<String>();
            ArrayList<String> A = new ArrayList<String>();
            for(String f : minifiedFD2.get(i).A){
                A.add(f);
            }
            ArrayList<String> B = new ArrayList<String>();
            for(String f : minifiedFD2.get(i).B){
                B.add(f);
            }
            for(String f: B){
                B1.add(f);
            }
            for(int j=0;j<A.size();j++)
            {
                String a = A.get(j);
                A.remove(j);
                FunctionalDependency lastfd = new FunctionalDependency(A,B1);
                minifiedFD1.add(lastfd);
                boolean equivalence1 = FindClosure.checkEquivalence(minifiedFD1, minifiedFD2);
                boolean equivalence2 = FindClosure.checkEquivalence(minifiedFD2,minifiedFD1);
                if(!(equivalence1 && equivalence2)){ A.add(a);}
                else{j--;}
                Collections.sort(A);
            } 
        }
    
        //Removal of remaining redundant dependencies
        ArrayList<FunctionalDependency> dummyFD = new ArrayList<FunctionalDependency>();
        ArrayList<FunctionalDependency> minifiedFD = new ArrayList<FunctionalDependency>();
        
        for(FunctionalDependency f : minifiedFD1){
            minifiedFD.add(f);
        }
        for(int j=0;j<minifiedFD1.size();j++)
        {
            dummyFD.clear();
            for(int k=0;k<minifiedFD1.size();k++){
                if(k!=j){
                    dummyFD.add(minifiedFD1.get(k));
                }
            }
            boolean equivalence1 = FindClosure.checkEquivalence(dummyFD, minifiedFD1);
            boolean equivalence2 = FindClosure.checkEquivalence(minifiedFD1, dummyFD);
            if(equivalence1&&equivalence2){
                
                FunctionalDependency removeFD = new FunctionalDependency(minifiedFD1.get(j).A,minifiedFD1.get(j).B);
                minifiedFD1.remove(j); j--;
                System.out.println("Need to remove : "+ removeFD.A+">"+removeFD.B);
                for(int l=0;l<minifiedFD.size();l++){
                    if(removeFD.equals(minifiedFD.get(l))){
                        minifiedFD.remove(l);
                        break;
                    }
                }
            }
        }

        return minifiedFD;

        // ArrayList<FunctionalDependency> minifiedFD = new ArrayList<FunctionalDependency>();
        // return minifiedFD;
    }
}