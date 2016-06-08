import java.util.Scanner;

public class Pair {
	public static void main(String[] args) {

		System.out.println("Learn your squares and cubes!");
		Scanner scan = new Scanner(System.in);

		int number;
		int count = 1;

			do{
		System.out.print("Enter an integer: ");
		number = scan.nextInt();
		while (count <= number) {
			int squared = count * count;
			int cubed = (count * count * count);
			System.out.println(count + "\t" + squared + "\t" + cubed);
			count++;
		} count = 1;

		 } while(true);
			
		
	}
//	scan.close();
}