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
        
        System.out.println(" ");
        ArrayList<FunctionalDependency> minifiedFD = MinimalCover.findMinimalCover(FD);
        Relation r1 = new Relation();
        r1.Attributes = TableAttributes;
        r1.SuperKeyList = TableUtil.returnSuperKeys(r1, minifiedFD);
        r1.FD = minifiedFD;
        r1.CandidateKeyList = TableUtil.findCandidateKeys(r1, minifiedFD);
        // System.out.println("Minimal Cover of FD's are :");
        // for(FunctionalDependency fd : minifiedFD)
        // {
        //     System.out.println(fd.A+">"+fd.B);
        // }
        System.out.println("The Candidate Keys are :");
        for(ArrayList<String> ck : r1.CandidateKeyList)
        {
            System.out.println("Candidate Key "+ ck);
        }
        double normalForm = NFChecker.getNF(r1, r1.FD);
        
        if(normalForm==3.5){System.out.println("Normal Form satisfied : BCNF");}
        else if(normalForm==3.0){System.out.println("Normal Form satisfied : 3NF");}
        else if(normalForm==2.0){System.out.println("Normal Form satisfied : 2NF");}
        else{System.out.println("Normal Form satisfied : 1NF");}
        System.out.println("-------------------");
        //System.out.println("Find");
        if(normalForm==3.5){
            System.out.println("No higher conversion possible");
        }
        else
        {
            ArrayList<Relation> DecomposedTables;
            if(normalForm==3)
            {
                DecomposedTables = NormalFormConverter.toBCNF(r1, FD);
            }
            else if(normalForm==2)
            {
                DecomposedTables = NormalFormConverter.toThirdNF(r1, r1.FD);
            }
            else {
                DecomposedTables = NormalFormConverter.toSecondNF(r1, r1.FD);
            }
            for(Relation r : DecomposedTables)
            {
                System.out.println("Decomposed Relation :"+r.Attributes);
                System.out.println(" ");
                System.out.println("Candidate Key(s): ");
                for(ArrayList<String> ck : r.CandidateKeyList){
                    System.out.println(ck);
                }
                System.out.println("Functional Dependencies :");
                for(FunctionalDependency fde : r.FD){
                    System.out.println(fde.A+">"+fde.B);
                }
                System.out.println(" ");
                double nf = NFChecker.getNF(r, r.FD);
                if(nf==3.5){System.out.println("Normal Form satisfied : BCNF");}
                else if(nf==3.0){System.out.println("Normal Form satisfied : 3NF");}
                else if(nf==2.0){System.out.println("Normal Form satisfied : 2NF");}
                else{System.out.println("Normal Form satisfied : 1NF");}
                System.out.println("--------------------------");
            }
        }
        
    }
    
    
}