import java.util.ArrayList;
import java.util.Collections;

public class NormalFormConverter 
{
    //Incomplete
    static ArrayList<Relation> toSecondNF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        ArrayList<Relation> DecomposedRelation = new ArrayList<Relation>();
        ArrayList<FunctionalDependency> dummyFD = new ArrayList<FunctionalDependency>();
        ArrayList<String> oldAttr = r.Attributes;
        for(FunctionalDependency f : FD)
        {
            dummyFD.add(f);
        }

        
        for(int i=0;i<FD.size();i++)
        {
            
            FunctionalDependency fd = FD.get(i);
            boolean partialDependency = false;
            Collections.sort(fd.A);
            for(ArrayList<String> ck : r.CandidateKeyList)
            {
                if(ck.containsAll(fd.A) && ck.size()!=fd.A.size())
                {
                    partialDependency = true;
                    break;
                }
            }

            if(partialDependency)
            {
                
                ArrayList<String> dependents = new ArrayList<String>();
                for(String f : fd.B){
                    dependents.add(f);
                }
                
                for(int j=i+1;j<FD.size();j++)
                {
            
                    if(fd.A.equals(FD.get(j).A) || dependents.containsAll(FD.get(j).A))
                    {
                        for(String f : FD.get(j).B){
                            if(!dependents.contains(f)){
                                dependents.add(f);
                            }
                        }
                        FD.remove(j);
                        j--;
                    }
                }

                Relation dr = new Relation();
                ArrayList<String> newTableAttr = new ArrayList<String>();
                for(String f : fd.A){
                    newTableAttr.add(f);
                }
                for(String f : dependents)
                {
                    if(!newTableAttr.contains(f)){
                        newTableAttr.add(f);
                    }
                    if(oldAttr.contains(f)){
                        oldAttr.remove(f);
                    }
                }
                dr.Attributes = newTableAttr;
                dr.FD = TableUtil.findFunctionalDependencies(dr, dummyFD);
                dr.CandidateKeyList = TableUtil.findCandidateKeys(dr, dr.FD);
                dr.SuperKeyList = TableUtil.returnSuperKeys(dr, dr.FD);
                DecomposedRelation.add(dr);
            }
        }
        Relation oldRel = new Relation();
        oldRel.Attributes = oldAttr;
        oldRel.FD = TableUtil.findFunctionalDependencies(oldRel, dummyFD);
        oldRel.CandidateKeyList = TableUtil.findCandidateKeys(oldRel, oldRel.FD);
        oldRel.SuperKeyList = TableUtil.returnSuperKeys(oldRel, oldRel.FD);
        DecomposedRelation.add(oldRel);
        for(ArrayList<String> ck : r.CandidateKeyList)
        {
            // System.out.println("Testing "+ck);
            Relation dr = new Relation();
            dr.Attributes = ck;
            dr.FD = TableUtil.findFunctionalDependencies(dr, dummyFD);
            dr.CandidateKeyList = TableUtil.findCandidateKeys(dr, dr.FD);
            dr.SuperKeyList = TableUtil.returnSuperKeys(dr, dr.FD);
            DecomposedRelation.add(dr);
        }
        
        //TO REMOVE REDUNDANT TABLES
        for(int i=0;i<DecomposedRelation.size();i++){
            Relation dr1 = DecomposedRelation.get(i);
            Collections.sort(dr1.Attributes);
            for(int j=i+1;j<DecomposedRelation.size();j++){
                Relation dr2 = DecomposedRelation.get(j);
                Collections.sort(dr2.Attributes);
                if(dr1.Attributes.containsAll(dr2.Attributes) && dr1.Attributes.size()!=dr2.Attributes.size()){
                    DecomposedRelation.remove(j);
                    j--;
                }
            }
        }

        //to remove duplicates
        for(int i=0;i<DecomposedRelation.size();i++)
        {
            Relation dr1 = DecomposedRelation.get(i);
            Collections.sort(dr1.Attributes);
            for(int j=i+1;j<DecomposedRelation.size();j++){
                Relation dr2 = DecomposedRelation.get(j);
                Collections.sort(dr2.Attributes);
                if(dr1.Attributes.equals(dr2.Attributes)){
                    DecomposedRelation.remove(j);
                    j--;
                }
            }
        }
        return DecomposedRelation;
    }

    static ArrayList<Relation> toThirdNF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        ArrayList<String> leftoverAttributes = r.Attributes;
        ArrayList<FunctionalDependency> minimumFD = MinimalCover.findMinimalCover(FD);
        ArrayList<FunctionalDependency> dummyFD = new ArrayList<FunctionalDependency>();
        for(FunctionalDependency fde : minimumFD){
            dummyFD.add(fde);
        }
        ArrayList<Relation> DecomposedRelations = new ArrayList<Relation>();
        
        for(int i=0;i<minimumFD.size();i++)
        {
            Relation dr = new Relation();
            ArrayList<String> X = minimumFD.get(i).A;
            //System.out.println("Making table with :"+X);
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
            for(String f : TableAttributes){
                leftoverAttributes.remove(f);
            }
            dr.Attributes = TableAttributes;
            dr.PrimaryKeys = X;
            dr.FD = TableUtil.findFunctionalDependencies(dr, dummyFD);
            dr.CandidateKeyList = TableUtil.findCandidateKeys(dr, dr.FD);
            dr.SuperKeyList = TableUtil.returnSuperKeys(dr, dr.FD);
            DecomposedRelations.add(dr);
            // for(int j=0;j<minimumFD.size();j++){
            //     if(minimumFD.get(j).A.equals(X)){
            //         minimumFD.remove(j);
            //         j--;
            //     }
            // }
        }
        if(leftoverAttributes.size()!=0)
        {
            Relation leftoverRel = new Relation();
            leftoverRel.Attributes = leftoverAttributes;
            leftoverRel.FD = TableUtil.findFunctionalDependencies(leftoverRel, dummyFD);
            leftoverRel.SuperKeyList = TableUtil.returnSuperKeys(leftoverRel, leftoverRel.FD);
            leftoverRel.CandidateKeyList = TableUtil.findCandidateKeys(leftoverRel, leftoverRel.FD);
            DecomposedRelations.add(leftoverRel);
        }
        


        for(ArrayList<String> ck : r.CandidateKeyList)
        {
            //System.out.println("Testing "+ck);
            Relation dr = new Relation();
            dr.Attributes = ck;
            dr.FD = TableUtil.findFunctionalDependencies(dr, dummyFD);
            dr.CandidateKeyList = TableUtil.findCandidateKeys(dr, dr.FD);
            dr.SuperKeyList = TableUtil.returnSuperKeys(dr, dr.FD);
            DecomposedRelations.add(dr);
        }

        //TO REMOVE REDUNDANT TABLES
        for(int i=0;i<DecomposedRelations.size();i++){
            Relation dr1 = DecomposedRelations.get(i);
            Collections.sort(dr1.Attributes);
            for(int j=i+1;j<DecomposedRelations.size();j++){
                
                Relation dr2 = DecomposedRelations.get(j);
                Collections.sort(dr2.Attributes);
                if(dr1.Attributes.containsAll(dr2.Attributes) && dr1.Attributes.size()!=dr2.Attributes.size()){
                    DecomposedRelations.remove(j);
                    j--;
                }
                
            }
        }

        //to remove duplicates
        for(int i=0;i<DecomposedRelations.size();i++)
        {
            Relation dr1 = DecomposedRelations.get(i);
            Collections.sort(dr1.Attributes);
            for(int j=i+1;j<DecomposedRelations.size();j++){
                
                Relation dr2 = DecomposedRelations.get(j);
                Collections.sort(dr2.Attributes);
                if(dr1.Attributes.equals(dr2.Attributes)){
                    DecomposedRelations.remove(j);
                    j--;
                }
                
            }
        }
        return DecomposedRelations;


    }
    //Incomplete
    static ArrayList<Relation> toBCNF(Relation r, ArrayList<FunctionalDependency> FD)
    {
        
        
        // ArrayList<Relation> TempRelations = new ArrayList<Relation>();
        // ArrayList<Relation> DecomposedRelations = new ArrayList<Relation>();
        // TempRelations.add(r);

        // while(TempRelations.size()!=0)
        // {
        //     //System.out.println("Entered");
        //      Relation t = TempRelations.get(0);
        //      ArrayList<FunctionalDependency> tFD = t.FD;
        //      if(NFChecker.isBCNF(t, t.FD))
        //      {
        //         DecomposedRelations.add(TempRelations.get(0));
        //         TempRelations.remove(0);
        //      }
        //      else
        //      {
        //         ArrayList<String> oldTableAttributes = t.Attributes;
        //         Relation t1 = new Relation();
        //         Relation t2 = new Relation();
        //         for(int i=0;i<tFD.size();i++)
        //         {
        //             FunctionalDependency fd = tFD.get(i);
        //             boolean createTable = true;
        //             Collections.sort(fd.A);
        //             for(ArrayList<String> sk : t.SuperKeyList){
        //                 Collections.sort(sk);
        //                 if(sk.equals(fd.A)){
        //                     createTable=false;
        //                     break;
        //                 }
        //             }
        //             if(createTable){
        //                 ArrayList<String> rhs = fd.B;
        //                 ArrayList<String> lhs = fd.A;
        //                 ArrayList<String> newTableAttributes1 = new ArrayList<String>();
        //                 for(String f : lhs){
        //                     if(!newTableAttributes1.contains(f)){newTableAttributes1.add(f);}
        //                 }
        //                 for(String f : rhs){
        //                     if(!newTableAttributes1.contains(f)){
        //                         newTableAttributes1.add(f);
        //                         oldTableAttributes.remove(f);
        //                     }
        //                 }
        //                 t1.Attributes = newTableAttributes1;
        //                 t1.FD = TableUtil.findFunctionalDependencies(t1, tFD);
        //                 t1.CandidateKeyList = TableUtil.findCandidateKeys(t1, t1.FD);
        //                 t1.SuperKeyList = TableUtil.returnSuperKeys(t1, t1.FD);
        //                 t2.Attributes = oldTableAttributes;
        //                 t2.FD = TableUtil.findFunctionalDependencies(t2,tFD);
        //                 t2.CandidateKeyList = TableUtil.findCandidateKeys(t2, t2.FD);
        //                 t2.SuperKeyList = TableUtil.returnSuperKeys(t2, t2.FD);
        //                 TempRelations.add(t1);
        //                 TempRelations.add(t2);
        //                 TempRelations.remove(t);
        //             }
        //         }
                
        //      }
        //  }
        // ArrayList<Relation> CKRelations = new ArrayList<Relation>();
        // for(ArrayList<String> ck : r.CandidateKeyList)
        // {
        //     Relation r1 = new Relation();
        //     r1.Attributes = ck;
        //     r1.FD = TableUtil.findFunctionalDependencies(r1, FD);
        //     r1.CandidateKeyList = TableUtil.findCandidateKeys(r1, r1.FD);
        //     r1.SuperKeyList = TableUtil.returnSuperKeys(r1, r1.FD);
        //     CKRelations.add(r1);
        // }

        // for(Relation ckr : CKRelations)
        // {
        //     DecomposedRelations.add(ckr);
        // }
        
        // //TO REMOVE REDUNDANT TABLES
        // for(int i=0;i<DecomposedRelations.size();i++){
        //     Relation dr1 = DecomposedRelations.get(i);
        //     Collections.sort(dr1.Attributes);
        //     for(int j=i+1;j<DecomposedRelations.size();j++){
        //         Relation dr2 = DecomposedRelations.get(j);
        //         Collections.sort(dr2.Attributes);
        //         if(dr1.Attributes.containsAll(dr2.Attributes) && dr1.Attributes.size()!=dr2.Attributes.size()){
        //             DecomposedRelations.remove(j);
        //             j--;
        //         }
        //     }
        // }

        // //to remove duplicates
        // for(int i=0;i<DecomposedRelations.size();i++)
        // {
        //     Relation dr1 = DecomposedRelations.get(i);
        //     Collections.sort(dr1.Attributes);
        //     for(int j=i+1;j<DecomposedRelations.size();j++){
        //         Relation dr2 = DecomposedRelations.get(j);
        //         Collections.sort(dr2.Attributes);
        //         if(dr1.Attributes.equals(dr2.Attributes)){
        //             DecomposedRelations.remove(j);
        //             j--;
        //         }
        //     }
        // }
        ArrayList<String> oldTableAttr = new ArrayList<String>();
        for(String f : r.Attributes){
            oldTableAttr.add(f);
        }
        ArrayList<Relation> DecomposedRelations = new ArrayList<Relation>();
        ArrayList<FunctionalDependency> dummyFD = new ArrayList<FunctionalDependency>();
        for(FunctionalDependency fde : r.FD)
        {
            dummyFD.add(fde);
        }

        for(int i=0;i<dummyFD.size();i++)
        {
            FunctionalDependency fd = dummyFD.get(i);
            ArrayList<String> LHS = fd.A;
            ArrayList<String> RHS = fd.B;
            Collections.sort(LHS);
            boolean createflag=true;
            for(ArrayList<String> sk : r.SuperKeyList)
            {
                Collections.sort(sk);
                if(sk.equals(LHS))
                {
                    createflag=false;
                    break;
                }
            }
            if(createflag)
            {
                //System.out.println("First FD : "+fd.A+">"+fd.B);
                for(int j=i+1;j<dummyFD.size();j++)
                {
                    Collections.sort(dummyFD.get(j).A);
                    if(dummyFD.get(j).A.equals(LHS))
                    {
                        for(String f : dummyFD.get(j).B)
                        {
                            if(!RHS.contains(f)){
                                RHS.add(f);
                            }
                        }
                    }
                    dummyFD.remove(j); j--;
                    
                }
                
                ArrayList<String> attr = new ArrayList<String>();
                for(String f : LHS){
                    attr.add(f);
                }
                for(String f : RHS){
                    if(!attr.contains(f)){
                        attr.add(f);
                    }
                }
                
                Relation r1 = new Relation();
                r1.Attributes = attr;
                r1.FD = TableUtil.findFunctionalDependencies(r1, FD);
                r1.SuperKeyList = TableUtil.returnSuperKeys(r1, r1.FD);
                r1.CandidateKeyList = TableUtil.findCandidateKeys(r1, r1.FD);
                for(String f : RHS){
                    if(oldTableAttr.contains(f)){
                        oldTableAttr.remove(f);
                    }
                }
                
                DecomposedRelations.add(r1);
            }
                
        }


        Relation oldtable = new Relation();
        oldtable.Attributes = oldTableAttr;
        oldtable.FD = TableUtil.findFunctionalDependencies(oldtable, FD);
        oldtable.SuperKeyList = TableUtil.returnSuperKeys(oldtable, oldtable.FD);
        oldtable.CandidateKeyList = TableUtil.findCandidateKeys(oldtable, oldtable.FD);
        DecomposedRelations.add(oldtable);
        ArrayList<Relation> CKRelations = new ArrayList<Relation>();
        for(ArrayList<String> ck : r.CandidateKeyList)
        {
            Relation r1 = new Relation();
            r1.Attributes = ck;
            r1.FD = TableUtil.findFunctionalDependencies(r1, FD);
            r1.CandidateKeyList = TableUtil.findCandidateKeys(r1, r1.FD);
            r1.SuperKeyList = TableUtil.returnSuperKeys(r1, r1.FD);
            CKRelations.add(r1);
        }

        for(Relation ckr : CKRelations)
        {
            DecomposedRelations.add(ckr);
        }
        
        //TO REMOVE REDUNDANT TABLES
        for(int i=0;i<DecomposedRelations.size();i++){
            Relation dr1 = DecomposedRelations.get(i);
            Collections.sort(dr1.Attributes);
            for(int j=i+1;j<DecomposedRelations.size();j++){
                Relation dr2 = DecomposedRelations.get(j);
                Collections.sort(dr2.Attributes);
                if(dr1.Attributes.containsAll(dr2.Attributes) && dr1.Attributes.size()!=dr2.Attributes.size()){
                    DecomposedRelations.remove(j);
                    j--;
                }
            }
        }

        //to remove duplicates
        for(int i=0;i<DecomposedRelations.size();i++)
        {
            Relation dr1 = DecomposedRelations.get(i);
            Collections.sort(dr1.Attributes);
            for(int j=i+1;j<DecomposedRelations.size();j++){
                Relation dr2 = DecomposedRelations.get(j);
                Collections.sort(dr2.Attributes);
                if(dr1.Attributes.equals(dr2.Attributes)){
                    DecomposedRelations.remove(j);
                    j--;
                }
            }
        }

         //add Relations present in CKrelations, and remove those that are a subset of relations already present

        return DecomposedRelations;
    }
}