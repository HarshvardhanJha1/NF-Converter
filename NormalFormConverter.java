import java.util.ArrayList;
import java.util.Collections;

public class NormalFormConverter 
{
    //Incomplete
    static ArrayList<Relation> toSecondNF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        ArrayList<FunctionalDependency> minimumFD = MinimalCover.findMinimalCover(FD);
        //ArrayList<FunctionalDependency> minifiedFD = MinimalCover.findMinimalCover(FD);
        //Since it is minimumFD, everything on RHS will be atomic
        ArrayList<Relation> DecomposedRelations = new ArrayList<Relation>();
        while(minimumFD.size()!=0)
        {
            System.out.println("Relation 1 : ");
            ArrayList<String> lhs = minimumFD.get(0).A;
            System.out.println("LHS is : "+lhs);
            ArrayList<String> dependents = minimumFD.get(0).B;
            System.out.println("RHS initially is :"+dependents);
            Collections.sort(lhs);
            Collections.sort(dependents);
            ArrayList<String> primaryKeys = r.PrimaryKeys;
            Collections.sort(primaryKeys);
            if(primaryKeys.containsAll(lhs))
            {
                for(int i=0;i<minimumFD.size();i++)
                {
                    Collections.sort(minimumFD.get(i).A);
                    if(lhs.equals(minimumFD.get(i).A) || dependents.containsAll(minimumFD.get(i).A)){
                        ArrayList<String> rhs = minimumFD.get(i).B;
                        for(String f : rhs)
                        {
                            if(!dependents.contains(f))
                            {
                                dependents.add(f);
                            }
                        }
                    }
                }
                for(int i=0;i<minimumFD.size();i++)
                {
                    
                    if(lhs.equals(minimumFD.get(i).A) || dependents.containsAll(minimumFD.get(i).A)){
                        minimumFD.remove(i);
                        i--;
                    }

                }
                System.out.println("LHS is : "+ lhs);
                System.out.println("rhs is : "+dependents);
                Relation r1 = new Relation();
                ArrayList<String> newAttributes = new ArrayList<String>();
                for(String f : lhs)
                {

                    newAttributes.add(f);
                }
                for(String f : dependents)
                {
                    newAttributes.add(f);
                }
                System.out.println("The new attr are : "+ newAttributes);
                r1.Attributes = newAttributes;
                DecomposedRelations.add(r1);
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
        ArrayList<Relation> TempRelations = new ArrayList<Relation>();
        ArrayList<Relation> DecomposedRelations = new ArrayList<Relation>();
        // TempRelations.add(r);
        // while(!TempRelations.isEmpty())
        // {
        //     if(TempRelations.get(0) is in BCNF)
        //     {
        //         DecomposedRelations.add(TempRelations.get(0));
        //         TempRelations.remove(0);
        //     }
        //     else
        //     {
        //         //Run Algo to decompose into two relations r1, and r2
        //         Relation r1 = something;
        //         Relatin r2 = something;
        //         TempRelations.remove(0);
        //         TempRelations.add(r1);
        //         TempRelations.add(r2);
        //     }
        // }
        return DecomposedRelations;
    }
}