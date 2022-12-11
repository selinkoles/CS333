import java.util.*;

public class project2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useDelimiter("\n");
        String[] outletsInput, bulbsInput;
        boolean flag;

        System.out.print("Enter the number of electric outlets: ");
        int num = sc.nextInt();

        Integer[] outlets = new Integer[num];
        Integer[] bulbs = new Integer[num];

        System.out.println("Now give the list of " + num + " 2-digit hex codes that represent the order from top to bottom.");

        do {
            flag = false;
            System.out.print("Enter the codes of " + num + " electric outlets: ");
            String inputOutlets = sc.next();
            outletsInput = inputOutlets.split(" ");
            for (int i = 0; i < outletsInput.length; i++) {
                outlets[i] = Integer.parseInt(outletsInput[i], 16);
                if (outlets[i] > 255) {
                    flag = true;
                }
            }
        } while ((outletsInput.length != num) || flag);

        do {
            flag = false;
            System.out.print("Enter the codes of " + num + " electric bulbs: ");
            String inputBulbs = sc.next();
            bulbsInput = inputBulbs.split(" ");
            for (int i = 0; i < bulbsInput.length; i++) {
                bulbs[i] = Integer.parseInt(bulbsInput[i], 16);
                if (bulbs[i] > 255) {
                    flag = true;
                }
            }
        } while ((bulbsInput.length != num) || flag);

        Integer[] outletsToBulbs = new Integer[num];

        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                if (Objects.equals(outlets[i], bulbs[j])) {
                    outletsToBulbs[i] = j;
                }
            }
        }

        Integer[] seqList = new Integer[outletsToBulbs.length];
        Arrays.fill(seqList, 1);

        Integer[] hashIndices = new Integer[outletsToBulbs.length];

        int lastIndex = 0;
        int maxSubSeqLen = 1;

        for (int i = 0; i < seqList.length; i++) {
            hashIndices[i] = i;
            try {
                ArrayList<Integer> subprograms = new ArrayList<>();
                for (int k = 0; k < i; k++) {
                    if (outletsToBulbs[k] < outletsToBulbs[i]) {
                        subprograms.add(seqList[k]);
                        if (1+seqList[k] > seqList[i]) {
                            hashIndices[i] = k;
                        }
                    }
                }
                seqList[i] = 1 + Collections.max(subprograms);
                if (seqList[i] > maxSubSeqLen) {
                    maxSubSeqLen = seqList[i];
                    lastIndex = i;
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        ArrayList<String> result = new ArrayList<>();
        result.add(outletsInput[lastIndex]);
        while (hashIndices[lastIndex] != lastIndex) {
            lastIndex = hashIndices[lastIndex];
            result.add(outletsInput[lastIndex]);
        }

        System.out.println(result.size());
        for (int i = result.size() - 1; i >= 0; i--) {
            System.out.print(result.get(i) + " ");
        }

        sc.close();
    }
}
