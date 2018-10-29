import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class Framework
{
    int n; // number of candidates
    int k; // number of recruiters

    // provided data structures (already filled in)
    ArrayList<ArrayList<Integer>> neighbors;
    int[] recruiterCapacities;
    int[] preliminaryAssignment;

    // provided data structures (you need to fill these in)
    boolean existsValidAssignment;
    int[] validAssignment;
    int[] bottleneckRecruiters;

    // reading the input
    void input(String input_name)
    {
        File file = new File(input_name);
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new FileReader(file));

            String text = reader.readLine();
            String[] parts = text.split(" ");

            n = Integer.parseInt(parts[0]);
            k = Integer.parseInt(parts[1]);
            neighbors = new ArrayList<ArrayList<Integer>>(n+k);
            recruiterCapacities = new int[n+k];
            preliminaryAssignment = new int[n];

            for (int j = 0; j < n+k; j++) {
                neighbors.add(new ArrayList<Integer>());
            }
            for (int i = 0; i < n; i++) {
                text = reader.readLine();
                parts = text.split(" ");
                int numRecruiters = Integer.parseInt(parts[0]);
                for (int j = 0; j < numRecruiters; j++) {
                    int recruiter = Integer.parseInt(parts[j+1]);
                    neighbors.get(i).add(recruiter);
                    neighbors.get(recruiter).add(i);
                }
            }

            text = reader.readLine();
            parts = text.split(" ");
            for (int j = 0; j < k; j++) {
                recruiterCapacities[n+j] = Integer.parseInt(parts[j]);
            }

            text = reader.readLine();
            parts = text.split(" ");
            for (int i = 0; i < n-1; i++) {
                preliminaryAssignment[i] = Integer.parseInt(parts[i]);
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
    {
        try
        {
            PrintWriter writer = new PrintWriter(output_name,
                    "UTF-8");

            if (existsValidAssignment) {
                writer.println("Yes");
                for (int i = 0; i < n-1; i++) {
                    writer.print(validAssignment[i] + " ");
                }
                writer.println(validAssignment[n-1]);
            } else {
                writer.println("No");
                for (int j = 0; j < n+k-1; j++) {
                    writer.print(bottleneckRecruiters[j] + " ");
                }
                writer.println(bottleneckRecruiters[n+k-1]);
            }

            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Framework(String []Args)
    {
        input(Args[0]);

        // Fill these in as instructed in the problem statement.
        existsValidAssignment = false;
        validAssignment = new int[n];
        bottleneckRecruiters = new int[n+k];

        //YOUR CODE STARTS HERE

        // n candidates + k recruiter + 1 source + 1 sink
        ArrayList<ArrayList<Integer>> rGraph = new ArrayList<>();

        for (int i = 0; i < n+k+2; i++) {
            rGraph.add(new ArrayList<>());
        }

        // Source node: n+k; Sink node: n+k+1
        int source = n+k;
        int sink = n+k+1;

        for (int i = 0; i < n; i++) {
            rGraph.get(source).add(i);
            for (int j : neighbors.get(i).toArray(new Integer[neighbors.get(i).size()])) {
                rGraph.get(i).add(j);
            }
        }

        for (int i = n; i < n+k; i++) {
            for (int j = 0; j < recruiterCapacities[i]; j++) {
                rGraph.get(i).add(sink);
            }
        }

        for (int i = 0; i < n-1; i++) {
            rGraph.get(i).add(source);
            rGraph.get(source).remove(Integer.valueOf(i));
            rGraph.get(i).remove(Integer.valueOf(preliminaryAssignment[i]));
            rGraph.get(preliminaryAssignment[i]).add(i);
            rGraph.get(preliminaryAssignment[i]).remove(Integer.valueOf(sink));
        }
        
        // BFS
        int parent[] = new int[n+k+2];
        boolean visited[] = new boolean[n+k+2];
        for (int i = 0; i < n+k+2; i++) {
            visited[i] = false;
        }
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        parent[source] = -1;
        while (queue.size() != 0) {
            int i = queue.poll();
            if (i >= n && i <= n+k-1) {
                if ((!rGraph.get(i).contains(sink)) && (!rGraph.get(sink).contains(i))){
                    bottleneckRecruiters[i] = 1;
                }
            }

            for (int j = 0; j < n+k+2; j++) {
                if(!visited[j] && rGraph.get(i).contains(j)) {
                    visited[j] = true;
                    queue.add(j);
                    parent[j] = i;
                }
            }
        }
        validAssignment = preliminaryAssignment;
        
        int node = sink;
        while (parent[node] != -1) {
            rGraph.get(node).add(parent[node]);
            if (rGraph.get(parent[node]).contains(node)) {
                rGraph.get(parent[node]).remove(Integer.valueOf(node));
            }
            if (node >= n && node <= n+k-1) {
                if (validAssignment[parent[node]] != node) {
                    validAssignment[parent[node]] = node;
                }
            }
            node = parent[node];
        }
        existsValidAssignment = visited[sink];
        if (!existsValidAssignment) {
            for (int i=0; i<n; i++) {
                bottleneckRecruiters[i] = 0;
            }
        }
        else {
            bottleneckRecruiters = null;
        }

        //YOUR CODE ENDS HERE

        output(Args[1]);
    }

    // Strings in Args are the name of the input file followed by
    // the name of the output file
    public static void main(String [] Args)
    {
        new Framework(Args);
    }
}