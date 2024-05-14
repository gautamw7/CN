import java.util.Scanner;

public class SubnetCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the network address and the number of subnets
        System.out.print("Enter the network address (e.g., 192.168.1.0): ");
        String networkAddress = scanner.nextLine();
        System.out.print("Enter the number of subnets: ");
        int numberOfSubnets = scanner.nextInt();

        // Calculate the number of bits required to represent the number of subnets
        int bitsNeeded = (int) Math.ceil(Math.log(numberOfSubnets) / Math.log(2));
        System.out.println("Number of bits needed for subnetting: " + bitsNeeded);

        // Calculate the subnet mask
        String subnetMask = calculateSubnetMask(bitsNeeded);
        System.out.println("Subnet mask: " + subnetMask);



        // Display the subnet address ranges
        System.out.println("Subnet address ranges:");
        displaySubnetAddressRanges(networkAddress, subnetMask, numberOfSubnets);

        scanner.close();
    }

    // Method to calculate the subnet mask
    public static String calculateSubnetMask(int bitsNeeded) {
        StringBuilder subnetMask = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            if (i < bitsNeeded) {
                subnetMask.append("1");
            } else {
                subnetMask.append("0");
            }
        }
        return binaryToDecimal(subnetMask.toString());
    }

    // Method to convert binary string to decimal format
    public static String binaryToDecimal(String binaryString) {
        StringBuilder decimalString = new StringBuilder();
        for (int i = 0; i < 32; i += 8) {
            String octet = binaryString.substring(i, i + 8);
            int decimalValue = Integer.parseInt(octet, 2);
            decimalString.append(decimalValue);
            if (i < 24) {
                decimalString.append(".");
            }
        }
        return decimalString.toString();
    }

    // Method to display the subnet address ranges
    public static void displaySubnetAddressRanges(String networkAddress, String subnetMask, int numberOfSubnets) {
        // Convert network address and subnet mask to binary format
        String binaryNetworkAddress = decimalToBinary(networkAddress);
        String binarySubnetMask = decimalToBinary(subnetMask);

        // Calculate the size of each subnet
        int subnetSize = (int) Math.pow(2, 32 - binarySubnetMask.replace(".", "").indexOf("0"));

        // Display the subnet address ranges
        for (int i = 0; i < numberOfSubnets; i++) {
            int subnetNumber = i + 1;
            int subnetStart = i * subnetSize;
            int subnetEnd = (i + 1) * subnetSize - 1;

            System.out.println("Subnet " + subnetNumber + " Range: "
                    + binaryToDecimal(addLeadingZeros(Integer.toBinaryString(subnetStart)))
                    + " - "
                    + binaryToDecimal(addLeadingZeros(Integer.toBinaryString(subnetEnd))));
        }
    }

    // Method to convert decimal string to binary format
    public static String decimalToBinary(String decimalString) {
        StringBuilder binaryString = new StringBuilder();
        String[] octets = decimalString.split("\\.");
        for (String octet : octets) {
            String binaryOctet = Integer.toBinaryString(Integer.parseInt(octet));
            binaryString.append(addLeadingZeros(binaryOctet));
        }
        return binaryString.toString();
    }

    // Method to add leading zeros to a binary string
    public static String addLeadingZeros(String binaryString) {
        StringBuilder paddedString = new StringBuilder(binaryString);
        while (paddedString.length() < 8) {
            paddedString.insert(0, "0");
        }
        return paddedString.toString();
    }
}
