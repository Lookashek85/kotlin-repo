import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ArraysExercise {
    public static void main(String[] args) {
        int [] nums = {-1, -2, -3, 2};
        int [] expected = {-1, -3};
        List<Integer> actual = processArray(nums);
        boolean areInputsEqual = expected.length == actual.size() &&
                IntStream.range(0, expected.length)
                        .allMatch(i -> {
                            Integer num = actual.get(i);
                            return num != null && expected[i] == num;
                        });
        System.out.println(areInputsEqual);
    }

    public static List<Integer> processArray(int[] input) {
        final int remainingNumMarker = 1;
        final int removedNumMarker = -1;
        int inputLength = input.length;
        int[] buffer = new int[inputLength];
        int size = 0;

        BIT tree = new BIT(inputLength);

        for (int num : input) {
            if (num < 0) {
                buffer[size] = num;
                tree.update(size + 1, remainingNumMarker);
                size++;
            } else if (num > 0 && num <= tree.query(size)) {
                int idx = tree.findIndexByPrefix(num);
                tree.update(idx, removedNumMarker);
            }
        }

        List<Integer> result = new ArrayList<>(tree.query(size));
        for (int i = 1 ; i <= size; i++) {
            if (tree.query(i) - tree.query(i-1)  == 1) {
                result.add(buffer[i-1]);
            }
        }
        return result;
    }

    static class BIT {
        private final int[] tree;

        BIT(int n) {
            // BIT tree must use 1 based index for bits manipulation to work as expected, zero based would require adjusting indices every time
            tree = new int[n + 1];
        }

        void update(int i, int delta) {
            while (i < tree.length) {
                tree[i] += delta;
                i += i & -i;
            }
        }

        int query(int i) {
            int sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= i & -i;
            }
            return sum;
        }

        int findIndexByPrefix(int prefix) {
            int idx = 0;
            int bitMask = Integer.highestOneBit(tree.length - 1);
            while (bitMask != 0) {
                int next = idx + bitMask;
                if (next < tree.length && tree[next] < prefix) {
                    prefix -= tree[next];
                    idx = next;
                }
                bitMask >>= 1;
            }
            return idx + 1;
        }
    }
}
