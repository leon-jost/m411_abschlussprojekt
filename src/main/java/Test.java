import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    private static List<int[]> combine(int[][] matrix) {
        int[] sizeArray = new int[matrix.length];
        int[] counterArray = new int[matrix.length];
        int total = 1;

        // find out total combinations
        for (int i = 0; i < matrix.length; ++i) {
            sizeArray[i] = matrix[i].length;
            total *= matrix[i].length;
        }

        List<int[]> list = new ArrayList<>(total);
        StringBuilder sb;
        for (int count = total; count > 0; --count) {
            sb = new StringBuilder();
            for (int i = 0; i < matrix.length; ++i) {
                sb.append(matrix[i][counterArray[i]]);
            }
            int[] tmpi = new int[sb.toString().length()];
            for (int tmp = 0; tmp < sb.toString().length(); tmp++) {
                tmpi[tmp] = Integer.parseInt("" + sb.toString().toCharArray()[tmp]);
            }
            list.add(tmpi);
            for (int incIndex = matrix.length - 1; incIndex >= 0; --incIndex) {
                if (counterArray[incIndex] + 1 < sizeArray[incIndex]) {
                    ++counterArray[incIndex];
                    break;
                }
                counterArray[incIndex] = 0;
            }
        }
        return list;
    }

    public static void main(String[] args) {
        int[][] matrix = {{1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}};
        int i = 0;
        for (int[] c : (combine(matrix))) {
            System.out.println(Arrays.toString(c));
            i++;
        }
        System.out.println(i);
    }
}
