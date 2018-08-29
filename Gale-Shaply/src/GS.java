// Usage:
// java GS [inputfile] [outputfile]
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class GS
/**
 * A class to handle input and output for processing stable matching problems.
 */
{
    int n; // number of proposers and respondents

    int ProposerPrefs[][]; // preference list of proposers (n*n)
    int RespondentPrefs[][]; // preference list of respondents (n*n)

    ArrayList<MatchedPair> MatchedPairsList; // your output should fill this arraylist which is empty at start

    public class MatchedPair
            /**
             * A class storing an individual pair of one proposer and one respondent.
             */
    {
        int proposer; // proposer's number
        int respondent; // respondent's number

        public MatchedPair(int Proposer, int Respondent)
        {
            proposer=Proposer;
            respondent=Respondent;
        }

        public MatchedPair()
        {
        }
    }


    void input(String input_name)
    /**
     * Method to handle the processing of the input file saved at @input_name.
     * The first line of this file contains the number of proposers/respondents n.
     * The following n lines give ordered lists of preferences for proposers 1-n,
     * followed by another n lines of the ordered lists of preferences for
     * respondents 1-n. This code should not be modified for this assignment.
     */
    {
        File file = new File(input_name);
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new FileReader(file));

            // Initialize n
            String text = reader.readLine();
            String [] parts = text.split(" ");
            n=Integer.parseInt(parts[0]);

            ProposerPrefs=new int[n][n];
            RespondentPrefs=new int[n][n];

            // Initialize proposer preferences
            for (int i=0;i<n;i++)
            {
                text=reader.readLine();
                String [] mList=text.split(" ");
                for (int j=0;j<n;j++)
                {
                    ProposerPrefs[i][j]=Integer.parseInt(mList[j]);
                }
            }

            // Initialize respondent preferences
            for (int i=0;i<n;i++)
            {
                text=reader.readLine();
                String [] wList=text.split(" ");
                for(int j=0;j<n;j++)
                {
                    RespondentPrefs[i][j]=Integer.parseInt(wList[j]);
                }
            }

            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // writing the output
    void output(String output_name)
    /**
     * Writes output to the file @output_name with n lines.
     * Each line contains the index of one proposer followed by
     * the respondent to whom they are matched, separated by a space.
     */
    {
        try
        {
            PrintWriter writer = new PrintWriter(output_name, "UTF-8");

            for(int i=0;i<MatchedPairsList.size();i++)
            {
                writer.println(MatchedPairsList.get(i).proposer+" "+MatchedPairsList.get(i).respondent);
            }

            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public GS(String []Args)
    {
        // Load in the problem information and preferences
        input(Args[0]);

        MatchedPairsList=new ArrayList<MatchedPair>(); // you should put the final stable matching in this array list

        /* NOTE
         * if you want to declare that proposer x and respondent y will get matched in the matching, you can
         * write a code similar to what follows:
         * MatchedPair pair=new MatchedPair(x,y);
         * MatchedPairsList.add(pair);
         */

        //YOUR CODE STARTS HERE
        // Initialize the list of proposers that each respondent matched
        int[] MatchProposers = new int[n];

        // Initialize the unmatched proposer list
        LinkedList<Integer> unmatchedProposer = new LinkedList<Integer>();

        for (int i = 0; i < n; i++) {
            unmatchedProposer.add(i);
        }

        // Initialize the next respondent that the proposer wanna propose
        int[] next = new int[n];

        for (int i = 0; i < n; i++) {
            next[i] = 0;
        }

        // Initialize the respondent matching status
        boolean[] RespondentMatch = new boolean[n];

        for (int i = 0; i < n; i++) {
            RespondentMatch[i] = false;
        }

        // Rank of a proposer in the respondent's preference list
        int[][] rank = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rank[RespondentPrefs[i][j]][i] = j;
            }
        }

        // G-S algorithm
        while (!unmatchedProposer.isEmpty()) {

            int proposer = unmatchedProposer.getFirst();
            int respondent = ProposerPrefs[proposer][next[proposer]];
            next[proposer] += 1;

            // Match if the respondent is unmatched
            if (RespondentMatch[respondent] == false) {
                MatchProposers[respondent] = proposer;
                RespondentMatch[respondent] = true;
                unmatchedProposer.removeFirst();
            }

            // Rematch if the rank of this match is higher than the already matched ones
            else if (rank[proposer][respondent] < rank[MatchProposers[respondent]][respondent]) {
                RespondentMatch[respondent] = true;
                unmatchedProposer.removeFirst();
                unmatchedProposer.add(MatchProposers[respondent]);
                MatchProposers[respondent] = proposer;
            }
        }

        for (int i = 0; i < n; i++) {
            MatchedPair pair = new MatchedPair(MatchProposers[i], i);
            MatchedPairsList.add(pair);
        }

        //YOUR CODE ENDS HERE

        // Output the stable matching in MatchedPairsList
        output(Args[1]);
    }

    public static void main(String [] Args) // Strings in Args are the name of the input file followed by the name of the output file
    {
        new GS(Args);
    }
}


