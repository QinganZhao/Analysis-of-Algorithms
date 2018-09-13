import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;


public class Framework {
    int n; //number of cells. Cells are labeled from 1 to n
    int rewards[]; // the reward in each cell
    int total_reward[]; // total_reward[i] equals to the maximum score when Alice is starting using the subproblem of shifts [1, i] only
    boolean picked[]; //picked[i] means whether Alice has picked i or not

    //reading the input
    void input(String input_name){
        File file = new File(input_name);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));

            String text = reader.readLine();
            n = Integer.parseInt(text);

            rewards = new int[n + 1];
            total_reward = new int[n + 1];
            picked = new boolean[n + 1];

            text = reader.readLine();
            String parts[] = text.split(" ");

            for (int i = 1; i <= n; i++)
                rewards[i] = Integer.parseInt(parts[i - 1]);

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //writing the output
    void output(String output_name)
    {
        try{
            PrintWriter writer = new PrintWriter(output_name, "UTF-8");

            writer.println(total_reward[n]);
            for (int i = 1; i <= n; i++)
                if (picked[i]) writer.print("1 ");
                else writer.print("0 ");
            writer.println();

            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Framework(String []Args){
        input(Args[0]);

        //YOUR CODE GOES HERE

        if (n == 0) {}

        else if (n == 1) {
            total_reward[1] = rewards[1];
            picked[1] = true;
        }

        else {
            total_reward[0] = 0;
            total_reward[1] = rewards[1];

            for (int i = 2; i <= n; i++) {

                if ((rewards[i] + total_reward[i - 2]) > total_reward[i - 1]) {
                    total_reward[i] = rewards[i] + total_reward[i - 2];
                }
                else {
                    total_reward[i] = total_reward[i - 1];
                }
            }

            int i = n;
            while (i > 1) {
                if ((rewards[i] + total_reward[i - 2]) < total_reward[i - 1]) {
                    picked[i] = false;
                    picked[i - 1] = true;
                    i -= 1;
                } else {
                    picked[i] = true;
                    i -= 2;
                }
            }

            if (picked[2] == false) {
                picked[1] = true;
            }
        }

        //END OF YOUR CODE

        output(Args[1]);
    }

    public static void main(String [] Args) //Strings in Args are the name of the input file followed by the name of the output file
    {
        new Framework(Args);
    }
}

