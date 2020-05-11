import java.util.ArrayList;
import java.util.Collections;

public class NormalFormConverter 
{
    //Incomplete
    static ArrayList<Relation> toSecondNF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        ArrayList<Relation> DecomposedRelations = new ArrayList<Relation>();
        ArrayList<String> primaryKey = r.PrimaryKeys;
        ArrayList<FunctionalDependency> minimumFD = MinimalCover.findMinimalCover(FD);
        ArrayList<FunctionalDependency> partialDependencies = new ArrayList<FunctionalDependency>();
        ArrayList<FunctionalDependency> fullDependencies = new ArrayList<FunctionalDependency>();
        for(FunctionalDependency fd : minimumFD)
        {
            fullDependencies.add(fd);
        }

        for(int i=0;i<fullDependencies.size();i++)
        {
            FunctionalDependency fd = fullDependencies.get(i);
            ArrayList<String> lhs = fd.A;
            ArrayList<String> rhs = fd.B;
            if(primaryKey.containsAll(lhs) && lhs.size()!=primaryKey.size() && !primaryKey.containsAll(rhs))
            {
                partialDependencies.add(fd);
                fullDependencies.remove(i);
                i--;
            }
            //compute closure of RHS and add those to partial dependencies, and remove from fulldependencies
            ArrayList<String> rhsClosure = FindClosure.findClosure(rhs, minimumFD);
            for(int j=0;j<fullDependencies.size();j++)
            {
                FunctionalDependency fd2 = fullDependencies.get(j);
                if(rhsClosure.containsAll(fd2.A))
                {
                    partialDependencies.add(fd2);
                    fullDependencies.remove(j);
                    j--;
                }
            }

        }
        ArrayList<String> xf = new ArrayList<String>();
        for(int i=0;i<fullDependencies.size();i++)
        {
            ArrayList<String> lhs = fullDependencies.get(i).A;
            ArrayList<String> rhs = fullDependencies.get(i).B;
            for(String f : lhs){
                if(!xf.contains(f)){
                    xf.add(f);
                }
            }
            for(String f : rhs){
                if(!xf.contains(f)){
                    xf.add(f);
                }
            }
        }

        for(FunctionalDependency pd : partialDependencies)
        {
            System.out.println("Partial Dependency :"+pd.A+">"+pd.B);
        }

        for(int i=0;i<partialDependencies.size();i++)
        {
            FunctionalDependency fd = partialDependencies.get(i);
            
            Collections.sort(fd.A);
            if(r.PrimaryKeys.containsAll(fd.A) && fd.A.size()!=r.PrimaryKeys.size())
            {
                Relation t = new Relation();
                ArrayList<String> attributes = new ArrayList<String>();
                attributes.add(fd.B.get(0));
                for(int j=i+1;j<partialDependencies.size();j++){
                    if(partialDependencies.get(j).A.equals(fd.A) || attributes.containsAll(partialDependencies.get(j).A)){
                        if(!attributes.contains(partialDependencies.get(j).B.get(0)))
                        {
                            attributes.add(partialDependencies.get(j).B.get(0));
                        }
                        partialDependencies.remove(j);
                        j--;
                    }
                }
                System.out.println("Closure of"+fd.A+" is "+ attributes);
                t.Attributes = attributes;
                t.PrimaryKeys = fd.A;
                Collections.sort(t.Attributes);
                for(String f : attributes)
                {
                    if(xf.contains(f)){
                        xf.remove(f);
                    }
                }
                for(String f : t.PrimaryKeys){
                    t.Attributes.add(f);
                }
                DecomposedRelations.add(t);
            }
        }
        Relation r2 = new Relation();
        r2.Attributes = xf;
        if(xf.size()!=0){
            DecomposedRelations.add(r2);
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

        boolean subsumed = false;
        for(int i=0;i<DecomposedRelations.size();i++)
        {
            if(DecomposedRelations.get(i).Attributes.containsAll(r.PrimaryKeys))
            {
                subsumed = true;
                break;
            }
        }
        if(subsumed==false)
        {
            Relation r1 = new Relation();
            r1.Attributes = r.PrimaryKeys;
            DecomposedRelations.add(r1);
        }

        return DecomposedRelations;
    }
    //Incomplete
    static ArrayList<Relation> toBCNF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        ArrayList<Relation> TempRelations = new ArrayList<Relation>();
        ArrayList<Relation> DecomposedRelations = new ArrayList<Relation>();
        TempRelations.add(r);
        while(!TempRelations.isEmpty())
        {
             Relation t = TempRelations.get(0);
             ArrayList<FunctionalDependency> tFD = t.FD;
             if(NFChecker.isBCNF(t, t.FD))
             {
                DecomposedRelations.add(TempRelations.get(0));
                TempRelations.remove(0);
             }
             else
             {

                Relation t1 = new Relation();
                Relation t2 = new Relation();
                for(int i=0;i<tFD.size();i++)
                {
                    FunctionalDependency fd = tFD.get(i);
                    Collections.sort(fd.A);
                    for(ArrayList<String> sk : t.SuperKeyList){
                        Collections.sort(sk);
                        if(sk.equals(fd.A)){
                            break;
                        }
                    }
                }
                
        //         //Run Algo to decompose into two relations r1, and r2
        //         Relation r1 = something;
        //         Relatin r2 = something;
        //         TempRelations.remove(0);
        //         TempRelations.add(r1);
        //         TempRelations.add(r2);
             }
         }
        return DecomposedRelations;
    }
}