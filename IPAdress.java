import java.util.*;

public class IPAdress {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the IP Address: ");

        String ipAddress = scanner.nextLine();
        scanner.close();
        String[] octets = ipAddress.split("\\.");

        String ipClass = getIpClass(Integer.parseInt(octets[0]));

        String ipType = getIpType(ipClass);

        System.out.println("IP Address: " + ipAddress);
        System.out.println("Class: " + ipClass);
        System.out.println("Type: " + ipType);

    }

    private static String getIpType(String ipClass) {
        return switch (ipClass) {
            case "A" -> "Private and Public";
            case "B" -> "Private and Public";
            case "C" -> "Private and Public";
            case "D" -> "Multicast";
            case "E" -> "Reserved for military use";
            default -> "Unknown";
        };
    }

    private static String getIpClass(int ip) {
        if(ip >= 1 & ip <= 126){
            return "A";
        }else if(ip >= 128 & ip <= 191 ){
            return "B";
        }else if(ip >= 192 & ip <= 223 ){
            return "C";
        }else if(ip >= 224 & ip <= 239 ){
            return "D";
        }else if(ip >= 240 & ip <= 255 ){
            return "E";
        }else{
            return "F";
        }
    }
}
