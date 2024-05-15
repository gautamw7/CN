import java.util.Arrays;

public class HammingCode{
    public static void main(String[] args){
        String data = "110011";

        String hammingCode = getHammingCode(data);
        System.out.println("Hamming Code of " + data  + " is: " + hammingCode );

        boolean flag = checkHammingCode(hammingCode);
        System.out.println("Hamming Code is : " + flag);
    }

    private static boolean checkHammingCode(String data) {
        int parityAmt = AmtofParity(data);

        int[] dataArray = new int[data.length()];
        for(int index = 0; index < dataArray.length ; index++) {
            dataArray[index] = Character.getNumericValue(data.charAt(index));
        }

        int[][] bitMatrix = new int[dataArray.length][parityAmt];
        bitMatrix = bitMatrixMaker(bitMatrix);


        for(int index = 0, i = 0; index < dataArray.length ; index++) {
            if(powerOfTwo(index + 1)){
                int correctValue = checkParityValue(bitMatrix, (int) (Math.log(index + 1)/Math.log(2)) );
                System.out.println("Correct Value " + correctValue + " DataArray[" + dataArray[index] + "] ");
                if(correctValue != dataArray[index]){
                    return false;
                }
            }
        }
        return true;
    }

    private static String getHammingCode(String data) {
        int parityAmt = AmtofParity(data);

        int[] dataArray = new int[data.length() + parityAmt];

        int[][] bitMatrix = new int[dataArray.length][parityAmt + 1];
        bitMatrix = bitMatrixMaker(bitMatrix);

        for(int index = 0, i = 0; index < dataArray.length ; index++) {
            if(powerOfTwo(index + 1)){
                dataArray[index] = checkParityValue(bitMatrix, (int) (Math.log(index+1)/Math.log(2)) );
            }else{
                dataArray[index] = Character.getNumericValue(data.charAt(i++));
            }
        }
        data = "";
        for(int num : dataArray){
            data += num;
        }

        return data;
    }

    private static int checkParityValue(int[][] bitMatrix, int index) {
        int counter = 0;
        int y = bitMatrix[0].length;
        for (int[] matrix : bitMatrix) {
            if (matrix[y - index - 1] == 1) {
                counter++;
            }
        }
        if(counter % 2 == 0){
            return 0;
        }else{
            return 1;
        }
    }

    private static boolean powerOfTwo(int n) {
        return ((n & (n-1)) == 0);
    }

    private static int[][] bitMatrixMaker(int[][] bitMatrix) {
        int row = 1;
        for(int x = 0; x < bitMatrix.length ; x++){
            for(int y = 0; y < bitMatrix[x].length ; y++) {
                if (y == 0) {
                    bitMatrix[x][0] = row++;
                } else {
                    bitMatrix[x][y] = (bitMatrix[x][0] >> (4 - y)) & 1 ;
                }
            };
        }

        return bitMatrix;
    }

    private static int AmtofParity(String data) {
        int cnt = 0;
        while (true){
            if(Math.pow(2, cnt) > data.length()){
                return cnt + 1;
            }else{
                cnt++;
            }
        }
    }
}