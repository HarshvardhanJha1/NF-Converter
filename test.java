import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class test {
    public static void main(String args[])
    {
        // Relation r1 = new Relation();
        // r1.Attributes = new ArrayList<String>();
        // r1.Attributes.add("A");r1.Attributes.add("B");r1.Attributes.add("C");r1.Attributes.add("D");
        // Relation r2 = new Relation();
        // r2.Attributes = new ArrayList<String>();
        // r2.Attributes.add("A");r2.Attributes.add("C");r2.Attributes.add("D");
        Scanner s1 = new Scanner(System.in);
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
        ArrayList<FunctionalDependency> minFD = MinimalCover.findMinimalCover(FD);
        //System.out.println(relFD.size());
        for(FunctionalDependency fd : minFD)
        {
            System.out.println(fd.A+">"+fd.B);
        }
    }
}