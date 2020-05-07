import java.util.ArrayList;
import java.util.Collections;

public class TableUtil 
{
    static ArrayList<ArrayList<String>> findPrimaryKeys(Relation r1, ArrayList<FunctionalDependency> FD)
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
           if(KClosure.size()==TableAttributes.size())
           {
               Keys.add(K);
           }
       }
       int minLength = 99999999;
        for(ArrayList<String> candidateKey : Keys)
        {
            if(candidateKey.size()<minLength){
                minLength = candidateKey.size();
            }
        }
        ArrayList<ArrayList<String>> CandidateKeys = new ArrayList<ArrayList<String>>();
        for(ArrayList<String> ck : Keys)
        {
            if(ck.size()==minLength)
            {
                CandidateKeys.add(ck);
            }
        }
       return CandidateKeys;
    }    
}