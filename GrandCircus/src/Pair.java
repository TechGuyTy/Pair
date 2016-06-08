import java.util.Scanner;

public class Pair {
	public static void main(String[] args){
		System.out.println("Learn your squares and cubes!");
		
		int number = 0;
		int squared = number*number;
		int cubed = number * number * number;
		
		
		System.out.println("Number" + "\t"  +"Squared " + "\t" + "cubed");
		
		do{
			
			Scanner scan = new Scanner (System.in);
			
			System.out.print("Enter an integer: ");
			
			number = scan.nextInt();
			
			System.out.println(number + "\t" + squared + "\t" + cubed  );
			
			
			
			
		}while(true);
	}
}
