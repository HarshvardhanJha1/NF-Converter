import java.util.ArrayList;

public class Relation 
{
    ArrayList<String> PrimaryKeys;
    ArrayList<String> Attributes;  
    ArrayList<FunctionalDependency> FD;
    ArrayList<ArrayList<String>> CandidateKeyList;
    ArrayList<ArrayList<String>> SuperKeyList;  
}