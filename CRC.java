class CRC{
    public static void main(String[] args){
        String data = "110011";
        String divisor = "1001";

        String crcData = getCRC(data, divisor);
        System.out.println("The CRC Code of Data: " + data + " Divisior: " + divisor + " -> CRCCode :" + crcData);

        boolean flag = checkCRC(crcData, divisor);
        System.out.println("CRCCode of Data: " + data + " is: " + flag);
    }

    private static boolean checkCRC(String data, String divisor) {
        StringBuilder remainder = new StringBuilder();
        while(data.length() >= divisor.length()){
            remainder = new StringBuilder("");
            for(int index = 0; index < divisor.length() ; index++){
                remainder.append(XORValue(data.charAt(index), divisor.charAt(index)));
            }

            if(remainder.indexOf("1") != -1){
                remainder = new StringBuilder(remainder.substring(remainder.indexOf("1")));
            }else{
                remainder = new StringBuilder("");
            }
            if(divisor.length() > data.length()){
                break;
            }
            data = remainder + data.substring(divisor.length()) ;
        }

        return (remainder.isEmpty());
    }

    private static String getCRC(String data, String divisor) {
        String originalData = data;
        for(int i = 0; i < divisor.length() - 1 ; i++){
            data += "0";
        }

        StringBuilder remainder = new StringBuilder();
        while(data.length() >= divisor.length()){
            remainder = new StringBuilder("");
            for(int index = 0; index < divisor.length() ; index++){
                remainder.append(XORValue(data.charAt(index), divisor.charAt(index)));
            }

            if(remainder.indexOf("1") != -1){
                remainder = new StringBuilder(remainder.substring(remainder.indexOf("1")));
            }else{
                remainder = new StringBuilder("");
            }
            if(divisor.length() > data.length()){
                break;
            }
            data = remainder + data.substring(divisor.length()) ;
        }
        originalData = originalData + remainder;

        return originalData;
    }

    private static int XORValue(char c, char c1) {
        return (c == c1) ? 0 : 1;
    }
}