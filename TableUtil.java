import java.util.ArrayList;
import java.util.Collections;

public class TableUtil 
{
    static ArrayList<ArrayList<String>> findCandidateKeys(Relation r1, ArrayList<FunctionalDependency> FD)
    {
     
       ArrayList<ArrayList<String>> Keys = new ArrayList<ArrayList<String>>();
       ArrayList<String> TableAttributes = r1.Attributes;
       int attrCount = TableAttributes.size();
       for(int l=0;l<(1<<attrCount);l++)
       {    
           ArrayList<String> K = new ArrayList<String>();
           for(int m=0;m<attrCount;m++){
               if((l & (1<<m))!=0)
               {
                   K.add(TableAttributes.get(m));
               }
           }
           Collections.sort(K);
           //Finding closure.
           ArrayList<String> KClosure = new ArrayList<String>();
           for(String a:K)
           {
               KClosure.add(a);
           }
   
           while(true)
           {
               ArrayList<String> OldKClosure = new ArrayList<String>();
               for(String f : KClosure)
               {
                   OldKClosure.add(f);
               } 
               for(int i=0;i<FD.size();i++)
               {
                   FunctionalDependency fd = FD.get(i);
                   if(KClosure.containsAll(fd.A))
                   {
                       for(int j=0;j<fd.B.size();j++)
                       {
                           if(!KClosure.contains(fd.B.get(j))){
                               KClosure.add(fd.B.get(j));
                           }
                       }
                       Collections.sort(KClosure);
                   }
               }
               Collections.sort(KClosure);
               Collections.sort(OldKClosure);
               if(KClosure.equals(OldKClosure))
               {
                   break;
               }
           }
           if(KClosure.containsAll(TableAttributes))
           {
               Keys.add(K);
           }
       }
       ArrayList<ArrayList<String>> CandidateKeys = new ArrayList<ArrayList<String>>();
       for(int i=0;i<Keys.size();i++)
       {
           boolean candidateKey = true;
           for(int j=0;j<Keys.size();j++)
           {
               if(i!=j){
                   if(Keys.get(i).containsAll(Keys.get(j)))
                   {
                       candidateKey = false;
                       break;
                   }
               }
               
           }
           if(candidateKey)
            {
                Collections.sort(Keys.get(i));
                if(!CandidateKeys.contains(Keys.get(i)))
                {
                    CandidateKeys.add(Keys.get(i));
                }
            }
       }
       return CandidateKeys;
    }    

    static ArrayList<ArrayList<String>> returnSuperKeys(Relation r1, ArrayList<FunctionalDependency> FD)
    {
        ArrayList<ArrayList<String>> superKeys = new ArrayList<ArrayList<String>>();

       ArrayList<String> TableAttributes = r1.Attributes;
       int attrCount = TableAttributes.size();
       for(int l=0;l<(1<<attrCount);l++)
       {    
           ArrayList<String> K = new ArrayList<String>();
           for(int m=0;m<attrCount;m++){
               if((l & (1<<m))!=0)
               {
                   K.add(TableAttributes.get(m));
               }
           }
           Collections.sort(K);
           //Finding closure.
           ArrayList<String> KClosure = new ArrayList<String>();
           for(String a:K)
           {
               KClosure.add(a);
           }
   
           while(true)
           {
               ArrayList<String> OldKClosure = new ArrayList<String>();
               for(String f : KClosure)
               {
                   OldKClosure.add(f);
               } 
               for(int i=0;i<FD.size();i++)
               {
                   FunctionalDependency fd = FD.get(i);
                   if(KClosure.containsAll(fd.A))
                   {
                       for(int j=0;j<fd.B.size();j++)
                       {
                           if(!KClosure.contains(fd.B.get(j))){
                               KClosure.add(fd.B.get(j));
                           }
                       }
                       Collections.sort(KClosure);
                   }
               }
               Collections.sort(KClosure);
               Collections.sort(OldKClosure);
               if(KClosure.equals(OldKClosure))
               {
                   break;
               }
           }
           if(KClosure.containsAll(TableAttributes))
           {
               superKeys.add(K);
           }
       }
       return superKeys;
    }

    static ArrayList<FunctionalDependency>  findFunctionalDependencies(Relation oldRel , Relation r, ArrayList<FunctionalDependency> FD)
    {
        ArrayList<FunctionalDependency> relevantFD = new ArrayList<FunctionalDependency>();
        ArrayList<String> newTableAttr = r.Attributes;
        //System.out.println(r.Attributes);
        for(int i=1;i<(1<<newTableAttr.size())-1;i++)
        {
            ArrayList<String> subsetAttr = new ArrayList<String>();
            for(int j=0;j<newTableAttr.size();j++)
            {
                if((i&(1<<j))!=0)
                {
                    //System.out.println("Entered");
                    subsetAttr.add(newTableAttr.get(j));
                }
            }
            //System.out.println("subset gen "+subsetAttr);
            ArrayList<String> RHS = FindClosure.findClosure(subsetAttr, FD);
            for(String f : RHS)
            {
                ArrayList<String> right = new ArrayList<String>();
                right.add(f);
                if(!subsetAttr.containsAll(right) && r.Attributes.containsAll(right))
                {
                    FunctionalDependency fd = new FunctionalDependency(subsetAttr,right);
                    relevantFD.add(fd);
                }
            }
        }

        ArrayList<FunctionalDependency> minRelevantFD = MinimalCover.findMinimalCover(relevantFD);
        return minRelevantFD;
    }
}