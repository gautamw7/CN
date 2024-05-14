import java.util.Arrays;

public class HammingCode {

    public static void main(String[] args){
        String data = "110011";

        String HammingCode = HammingCode(data);

        System.out.println("Hamming Code of " + data + " is: " + HammingCode);

        boolean result = checkHammingCode(HammingCode);
        System.out.println("Code :" + HammingCode + "is : " + result);
    }

    private static boolean checkHammingCode(String hammingCode) {
        int spots = AmtofSpots(hammingCode) - 1;

        int[][] bitMatrix = new int[hammingCode.length()][spots + 1];
        bitMatrix = BitMatrixMaker(bitMatrix);

        int[] dataArray = new int[hammingCode.length()];
        for(int index = 0; index < dataArray.length; index++){
            dataArray[index] = Character.getNumericValue(hammingCode.charAt(index));
        }

        for(int index = 0; index < dataArray.length; index++){
            if(isPowerOfTwo(index + 1)){
                boolean flag = checkEvenParity(index, bitMatrix);
                if(flag == true && dataArray[index] != 0) return false;
                else if(flag == false && dataArray[index] != 1) return false;
            }
        }

        return true;
    }

    public static String HammingCode(String data){
        int spots = AmtofSpots(data);
        int[] dataArray = new int[spots + data.length()  ];

        int i = 0;

        int[][] bitMatrix = new int[dataArray.length][spots + 1];
        bitMatrix = BitMatrixMaker(bitMatrix);

        for(int index = 0; index < dataArray.length; index++){
            if(isPowerOfTwo(index + 1)){
                boolean flag = checkEvenParity(index, bitMatrix);
                if(flag)   dataArray[index] = 0;
                else dataArray[index] = 1;

            }else{
                dataArray[index] =  Character.getNumericValue(data.charAt(i));
                i++;
            }
        }

        String result = "";
        for(int num : dataArray){
            result += num;
        }
        return result;

    }
    private static boolean checkEvenParity(int index, int[][] bitMatrix) {
        boolean flag = false;
        index += 1;
        int Rvalue = (int) (Math.log(index) / Math.log(2));
        index = 4 - Rvalue;
        int counter = 0;
        for(int x = 0; x < bitMatrix.length ; x++){
                if(bitMatrix[x][index] == 1) counter++;
        }
        return (counter % 2 == 0);
    }

    private static int[][] BitMatrixMaker(int[][] bitMatrix) {
        int row = 1;
        for(int x = 0; x < bitMatrix.length ; x++){
            for(int y = 0; y < bitMatrix[x].length; y++){
                if(y == 0){
                    bitMatrix[x][y] = row++;
                }
                else{
                    bitMatrix[x][y] = (bitMatrix[x][0] >> (4 - y)) & 1;
                }
            }
        }

        return bitMatrix;
    }

    private static boolean isPowerOfTwo(int n) {
        return (n > 0) && ((n & (n - 1)) == 0);
    }

    static int AmtofSpots(String data){
        int i = 0;
        int value = (int) Math.pow(2,i);
        while(value < data.length()){
            value = (int) Math.pow(2,i++);
        }
        return i;
    }

}
