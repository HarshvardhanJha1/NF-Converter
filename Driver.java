import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class Driver
{
    public static void main(String args[])
    {
        Scanner s1 = new Scanner(System.in);
        System.out.println("Enter the attributes present in the table in the format A,B,C,D,E where each of the letters represents an attribute");
        ArrayList<String> TableAttributes = new ArrayList<String>();
        String attributes = s1.nextLine();
        String[] attributeArray = attributes.split(",");
        for(String attr : attributeArray)
        {
            TableAttributes.add(attr);
        }
        System.out.println("The attributes of the table are : "+TableAttributes);
        String userPrompt = "Y"; 
        ArrayList<FunctionalDependency> FD = new ArrayList<FunctionalDependency>();
        while(userPrompt.compareTo("Y")==0)
        {
            
            System.out.println("Enter the functional dependencies in the format A,B,C>D,E");
            String functionalDependencyString = s1.nextLine();
            String[] arr = functionalDependencyString.split(">");
            String fdLHS[] = arr[0].split(",");
            String fdRHS[] = arr[1].split(",");

            ArrayList<String> A1 = new ArrayList<String>(); 
            for(int i=0;i<fdLHS.length;i++)
            {
                A1.add(fdLHS[i]);
            }

            ArrayList<String> B1 = new ArrayList<String>();
            for(int i=0;i<fdRHS.length;i++)
            {
                B1.add(fdRHS[i]);
            }

            FunctionalDependency fd = new FunctionalDependency(A1, B1);
            FD.add(fd);
            System.out.println("Type Y to continue");
            userPrompt = s1.nextLine();
          
        }
        s1.close();
        System.out.println("The given functional dependencies are :");
        for(FunctionalDependency fd : FD)
        {
            System.out.println(fd.A+">"+fd.B);
        }
        System.out.println("The Candidate Keys are :");
        ArrayList<ArrayList<String>> Keys = new ArrayList<ArrayList<String>>();
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
        ArrayList<String> PrimaryKey = new ArrayList<String>();
        PrimaryKey = CandidateKeys.get(0);

        for(ArrayList<String> ck : CandidateKeys)
        {
            System.out.println(ck);
        }

        ArrayList<FunctionalDependency> minifiedFD = MinimalCover.findMinimalCover(FD);
        Relation r1 = new Relation();
        r1.PrimaryKeys = PrimaryKey;
        r1.Attributes = TableAttributes;
        r1.SuperKeyList = TableUtil.returnSuperKeys(r1, minifiedFD);
        r1.FD = minifiedFD;
        r1.CandidateKeyList = TableUtil.findCandidateKeys(r1, minifiedFD);
        

        for(ArrayList<String> ck : r1.CandidateKeyList)
        {
            System.out.println("Candidate Key "+ ck);
        }
        // // ArrayList<ArrayList<String>> key = TableUtil.findPrimaryKeys(r1, FD);
        // // System.out.println("Table Util :"+key);
        // //Finding Minimal Cover
        
        
        
        
        // ArrayList<Relation> DecomposedRelations = NormalFormConverter.toSecondNF(r1, FD);
        // ArrayList<FunctionalDependency> minifiedFD2 = MinimalCover.findMinimalCover(FD);
        // for(int i=0;i<DecomposedRelations.size();i++)
        // {
        //     System.out.println("Decomposed Relation :" + DecomposedRelations.get(i).Attributes);
        //     ArrayList<String> pk = TableUtil.findPrimaryKeys(DecomposedRelations.get(i), minifiedFD2).get(0);
        //     System.out.println("Primary Key : "+pk);
        //     System.out.println();
        // }  
        System.out.println("2NF : "+NFChecker.is2NF(r1, minifiedFD));
        System.out.println("3NF :"+NFChecker.is3NF(r1, minifiedFD));
        System.out.println("BCNF :"+NFChecker.isBCNF(r1, minifiedFD));
        
        System.out.println("------------------");
        ArrayList<Relation> DecomposedTables = NormalFormConverter.toThirdNF(r1, r1.FD);
        for(Relation r : DecomposedTables)
        {
            System.out.println("Decomposed Relation :"+r.Attributes);
            System.out.println("Candidate Key : "+r.CandidateKeyList);
            System.out.println("2NF : "+NFChecker.is2NF(r, r.FD));
            System.out.println("3NF :"+NFChecker.is3NF(r, r.FD));
            System.out.println("BCNF :"+NFChecker.isBCNF(r, r.FD));
            System.out.println(" ");
        }
    }
    
    
}