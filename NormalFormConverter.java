import java.util.ArrayList;
import java.util.Collections;

public class NormalFormConverter 
{
    //Incomplete
    static ArrayList<Relation> toSecondNF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        ArrayList<FunctionalDependency> minimumFD = MinimalCover.findMinimalCover(FD);
        ArrayList<FunctionalDependency> minifiedFD = MinimalCover.findMinimalCover(FD);
        //Since it is minimumFD, everything on RHS will be atomic
        ArrayList<Relation> DecomposedRelations = new ArrayList<Relation>();
        while(minimumFD.size()!=0)
        {
            ArrayList<String> lhs = minimumFD.get(0).A;
            Collections.sort(lhs);
            if(r.PrimaryKeys.containsAll(lhs) && r.PrimaryKeys.size()!=lhs.size())
            {
                ArrayList<String> newTableAttributes = new ArrayList<String>();
                for(String f : lhs){
                    newTableAttributes.add(f);
                }
                for(int j=0;j<minimumFD.size();j++)
                {
                    Collections.sort(minimumFD.get(j).A);
                    if(minimumFD.get(j).A.equals(lhs) && !(r.PrimaryKeys.contains(minimumFD.get(j).B.get(0))) && !(newTableAttributes.contains(minimumFD.get(j).B.get(0))))
                    {
                        newTableAttributes.add(minimumFD.get(j).B.get(0));
                        r.Attributes.remove(minimumFD.get(j).B.get(0));
                    }
                }
                Relation newRelation = new Relation();
                newRelation.Attributes = newTableAttributes;
                DecomposedRelations.add(newRelation);
            }
            for(int j=0;j<minimumFD.size();j++)
            {
                Collections.sort(minimumFD.get(j).A);
                if(minimumFD.get(j).A.equals(lhs)){
                    minimumFD.remove(j);
                    j--;
                }
            }
        }
        // System.out.println("Random Table :"+r.Attributes);
        for(int i=0;i<minifiedFD.size();i++)
        {
            ArrayList<String> concatenatedFD = new ArrayList<String>();
            for(String f : minifiedFD.get(i).A)
            {
                concatenatedFD.add(f);
            }
            for(String f : minifiedFD.get(i).B)
            {
                concatenatedFD.add(f);
            }
            ArrayList<String> LHS = minifiedFD.get(i).A;
            Collections.sort(LHS);
            for(int j=0;j<minifiedFD.size();j++)
            {
                Collections.sort(minifiedFD.get(j).A);
                if(LHS.equals(minifiedFD.get(j).A))
                {
                    if(!(concatenatedFD.contains(minifiedFD.get(j).B.get(0)))){
                        concatenatedFD.add(minifiedFD.get(j).B.get(0));
                    }
                }
            }
            if(concatenatedFD.containsAll(r.Attributes)){
                DecomposedRelations.add(r);
                break;
            }
        }
        
        return DecomposedRelations;
    }

    static ArrayList<Relation> toThirdNF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        ArrayList<FunctionalDependency> minimumFD = MinimalCover.findMinimalCover(FD);
        ArrayList<Relation> DecomposedRelations = new ArrayList<Relation>();
        Relation br =  new Relation();
        br.Attributes = r.PrimaryKeys;
        br.PrimaryKeys = r.PrimaryKeys;
        //DecomposedRelations.add(br);
        for(int i=0;i<minimumFD.size();i++)
        {
            Relation dr = new Relation();
            ArrayList<String> X = minimumFD.get(i).A;
            Collections.sort(X);
            ArrayList<String> TableAttributes = new ArrayList<String>();
            for(String f : X){
                TableAttributes.add(f);
            }
            for(int j=0;j<minimumFD.size();j++){
                Collections.sort(minimumFD.get(j).A);
                if(minimumFD.get(j).A.equals(X)){
                    ArrayList<String> dependents = minimumFD.get(j).B;
                    for(String f : dependents){
                        if(!TableAttributes.contains(f))
                        {
                            TableAttributes.add(f);
                        }
                    }
                }
            }
            dr.Attributes = TableAttributes;
            dr.PrimaryKeys = X;
            DecomposedRelations.add(dr);
            for(int j=0;j<minimumFD.size();j++){
                if(minimumFD.get(j).A.equals(X)){
                    minimumFD.remove(j);
                    j--;
                }
            }
        }

        for(int i=0;i<DecomposedRelations.size();i++){
            Relation dr1 = DecomposedRelations.get(i);
            Collections.sort(dr1.Attributes);
            for(int j=0;j<DecomposedRelations.size();j++){
                if(i!=j)
                {
                    Relation dr2 = DecomposedRelations.get(j);
                    Collections.sort(dr2.Attributes);
                    if(dr1.Attributes.containsAll(dr2.Attributes)){
                        DecomposedRelations.remove(j);
                        j--;
                    }
                }
            }
        }
        return DecomposedRelations;
    }
    //Incomplete
    static ArrayList<Relation> toBCNF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        ArrayList<Relation> DecomposedRelations = new ArrayList<Relation>();
        
        return DecomposedRelations;
    }
}