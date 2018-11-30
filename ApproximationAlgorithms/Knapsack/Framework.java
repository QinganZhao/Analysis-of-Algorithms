import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class Framework {
    int n;
    int values[];
    int weights[];
    int weight_limit;
    boolean picked[];
    void input(String input_name){
        File file = new File(input_name);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));

            String text = reader.readLine();
            String parts[];
            parts=text.split(" ");
            n=Integer.parseInt(parts[0]);
            weight_limit=Integer.parseInt(parts[1]);
            values=new int[n];
            weights=new int[n];
            picked=new boolean[n];
            for (int i=0;i<n;i++){
                text=reader.readLine();
                parts=text.split(" ");
                values[i]=Integer.parseInt(parts[0]);
                weights[i]=Integer.parseInt(parts[1]);
            }
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

            int total_value=0;
            for (int i=0;i<n;i++)
              if (picked[i])
                total_value += values[i];
            writer.println(total_value);
            for (int i=0;i<n;i++)
              if (picked[i])
                writer.println("1");
              else 
                writer.println("0");

            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Framework(String []Args){
        input(Args[0]);

        // YOUR CODE STARTS HERE

        // objR1: Greedy algorithm using v/w

        class objectR1 implements Comparable<objectR1> {

            private int value;
            private int weight;
            private int id;
            private double ratio;
            private boolean[] pick = new boolean[n];

            private objectR1(int value, int weight, int id) {
                this.value = value;
                this.weight = weight;
                this.id = id;
                this.ratio = ((double) value) / ((double) weight);
            }

            // Greedy algorithm implementation

            public Object[] greedy(objectR1[] objectList, int weightLimit, int id, int i) {

                int value = 0;
                int weight = weightLimit;
                int j = 0;
                int valuePrev = objectList[i].value;
                int weightPrev = objectList[i].weight;
                boolean ratios[] = new boolean[n];
                ratios[id] = true;
                objectList[i].weight = 0;
                objectList[i].value = 0;

                while (weight >= 0 && j < objectList.length) {

                    if (objectList[j].weight <= weight) {
                        value += objectList[j].value;
                        weight -= objectList[j].weight;
                        ratios[objectList[j].id] = true;
                        j += 1;
                    }

                    else {
                        objectList[i].value = valuePrev;
                        objectList[i].weight = weightPrev;
                        break;
                    }

                }

                return new Object[] {value, ratios};
            }

            // Find the optimal solution

            public int opt(objectR1[] objectList, int weightLimit) {
                int max = -1;
                for (int i = 0; i < objectList.length; i++) {
                    Object[] greedyResult = this.greedy(objectList, weightLimit - objectList[i].weight,
                            objectList[i].id, i);
                    if (objectList[i].value + (int) greedyResult[0] > max) {
                        this.pick = (boolean[]) greedyResult[1];
                        max = objectList[i].value +  (int) greedyResult[0];
                    }
                }
                return max;
            }

            // Comparing based on ratio (v/w)

            @Override
            public int compareTo(objectR1 object) {

                if (ratio < object.ratio) {
                    return 1;
                }
                else if (ratio == object.ratio) {
                    return 0;
                }
                else {
                    return -1;
                }

            }

        }


        // objR2: Greedy algorithm using v/w^2

        class objectR2 implements Comparable<objectR2> {

            private int value;
            private int weight;
            private int id;
            private double ratio;
            private boolean[] pick = new boolean[n];

            private objectR2(int value, int weight, int id) {
                this.value = value;
                this.weight = weight;
                this.id = id;
                this.ratio = ((double) value) / ((double) weight) / ((double) weight);
            }

            // Greedy algorithm implementation

            public Object[] greedy(objectR2[] objectList, int weightLimit, int id, int i) {

                int value = 0;
                int weight = weightLimit;
                int j = 0;
                int valuePrev = objectList[i].value;
                int weightPrev = objectList[i].weight;
                boolean ratios[] = new boolean[n];
                ratios[id] = true;
                objectList[i].weight = 0;
                objectList[i].value = 0;

                while (weight >= 0 && j < objectList.length) {

                    if (objectList[j].weight <= weight) {
                        value += objectList[j].value;
                        weight -= objectList[j].weight;
                        ratios[objectList[j].id] = true;
                        j += 1;
                    }

                    else {
                        objectList[i].value = valuePrev;
                        objectList[i].weight = weightPrev;
                        break;
                    }

                }

                return new Object[] {value, ratios};
            }

            // Find the optimal solution

            public int opt(objectR2[] objectList, int weightLimit) {
                int max = -1;
                for (int i = 0; i < objectList.length; i++) {
                    Object[] greedyResult = this.greedy(objectList, weightLimit - objectList[i].weight,
                            objectList[i].id, i);
                    if (objectList[i].value + (int) greedyResult[0] > max) {
                        this.pick = (boolean[]) greedyResult[1];
                        max = objectList[i].value +  (int) greedyResult[0];
                    }
                }
                return max;
            }

            // Comparing based on ratio (v/w^2)

            @Override
            public int compareTo(objectR2 object) {

                if (ratio < object.ratio) {
                    return 1;
                }
                else if (ratio == object.ratio) {
                    return 0;
                }
                else {
                    return -1;
                }

            }

        }


        // objV: Greedy algorithm using v

        class objectV implements Comparable<objectV> {

            private int value;
            private int weight;
            private int id;
            private boolean[] pick = new boolean[n];

            private objectV(int value, int weight, int id) {
                this.value = value;
                this.weight = weight;
                this.id = id;
            }

            // Greedy algorithm implementation

            public Object[] greedy(objectV[] objectList, int weightLimit, int id, int i) {

                int value = 0;
                int weight = weightLimit;
                int j = 0;
                int valuePrev = objectList[i].value;
                int weightPrev = objectList[i].weight;
                boolean values[] = new boolean[n];
                values[id] = true;
                objectList[i].weight = 0;
                objectList[i].value = 0;

                while (weight >= 0 && j < objectList.length) {

                    if (objectList[j].weight <= weight) {
                        value += objectList[j].value;
                        weight -= objectList[j].weight;
                        values[objectList[j].id] = true;
                        j += 1;
                    }

                    else {
                        objectList[i].value = valuePrev;
                        objectList[i].weight = weightPrev;
                        break;
                    }

                }
                return new Object[] {value, values};
            }


            // Find the optimal solution

            public int opt(objectV[] objectList, int weightLimit) {
                int max = -1;
                for (int i = 0; i < objectList.length; i++) {
                    Object[] greedyResult = this.greedy(objectList, weightLimit - objectList[i].weight,
                            objectList[i].id, i);
                    if (objectList[i].value + (int) greedyResult[0] > max) {
                        this.pick = (boolean[]) greedyResult[1];
                        max = objectList[i].value +  (int) greedyResult[0];
                    }
                }
                return max;
            }

            // Comparing based on value (v)

            @Override
            public int compareTo(objectV object) {

                if (value < object.value) {
                    return 1;
                }
                else if (value == object.value) {
                    return 0;
                }
                else {
                    return -1;
                }

            }

        }



        objectR1 r1 = new objectR1(0, 0, 0);
        objectR2 r2 = new objectR2(0, 0, 0);
        objectV v = new objectV(0, 0, 0);
        objectR1[] r1s = new objectR1[n];
        objectR2[] r2s = new objectR2[n];
        objectV[] vs = new objectV[n];

        for (int i = 0; i < n; i++) {
            r1s[i] = new objectR1(values[i], weights[i], i);
            r2s[i] = new objectR2(values[i], weights[i], i);
            vs[i] = new objectV(values[i], weights[i], i);
        }

        Arrays.sort(r1s);
        Arrays.sort(r2s);
        Arrays.sort(vs);

        if (r1.opt(r1s, weight_limit) >= r2.opt(r2s, weight_limit) &&
                r1.opt(r1s, weight_limit) >= v.opt(vs, weight_limit)) {
            picked = r1.pick;
        }

        else if (r2.opt(r2s, weight_limit) >= v.opt(vs, weight_limit) &&
                r2.opt(r2s, weight_limit) >= r1.opt(r1s, weight_limit)) {
            picked = r2.pick;
        }

        else {
            picked = v.pick;
        }

        // YOUR CODE ENDS HERE

        output(Args[1]);
    }

    public static void main(String [] Args) //Strings in Args are the name of the input file followed by the name of the output file
    {
        new Framework(Args);
    }
}
