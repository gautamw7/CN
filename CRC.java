
class CRC{
    public static void main(String[] args){
        String data = "110011";
        String divisor = "1001";

        String CRCCode = CRCMaker(data, divisor);
        System.out.println("CRC Code of data : " + data + " is : " + CRCCode);

        boolean flag = CRCChecker(CRCCode, "1001");
        System.out.println("CRC Code is status : " + flag );
    }

    private static boolean CRCChecker(String data, String divisor) {
        StringBuilder remainder = new StringBuilder();

        while (data.length() >= divisor.length()) {
            remainder.setLength(0);
            for (int index = 0; index < divisor.length(); index++) {
                remainder.append(XORValue(data.charAt(index), divisor.charAt(index)));
            }

            if (remainder.indexOf("1") != -1) {
                remainder = new StringBuilder(remainder.substring(remainder.indexOf("1")));
            }

            if (data.length() > divisor.length()) {
                data = remainder + data.substring(divisor.length());
            } else {
                break;
            }
        }
        System.out.println("Remainder: " + remainder);
        return remainder.indexOf("1") == -1;
    }


    private static String CRCMaker(String data, String divisor) {
        String result = data;
        for(int i = 0; i < divisor.length() - 1; i++){
            data += '0';
        }

        StringBuilder remainder = new StringBuilder();


        while(data.length() >= divisor.length() ){
            int index = 0;
            remainder.setLength(0);
            for(int i = 0; i < divisor.length() ; i++, index++){
                remainder.append(XORValue(data.charAt(i),divisor.charAt(i)));
            }

            if(remainder.indexOf("1") != -1 ){
                remainder = new StringBuilder(remainder.substring(remainder.indexOf("1")));
            }else{
                remainder = new StringBuilder("");
            }
            if(index == data.length()){
                if(remainder.length() != divisor.length()){
                    int length = divisor.length() - remainder.length() - 1;
                    for(int i = 0; i < length ; i++){
                        remainder.insert(0,'0');
                    }
                    break;
                }
            }
            data = remainder + data.substring(index);
        }

        result = result + remainder;
        System.out.println("New CRC " + result);

        return result;
    }

    private static int XORValue(int a, int b){
        return (a == b) ? 0 : 1;
    }
}