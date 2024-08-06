import java.util.Scanner;
import static java.lang.System.out;

class Solution1
{
	public static void main(String arg[])
	{
		int PIN, evenSum=0, oddSum=0, rem;
		
		Scanner scan = new Scanner(System.in);

		out.print("Enter input 1: ");
		int input1 = scan.nextInt();

		out.print("Enter input 2: ");
		int input2 = scan.nextInt();
		
		out.print("Enter input 3: ");
		int input3 = scan.nextInt();
		
		out.print("Enter input 4: ");
		int input4 = scan.nextInt();
		
		while(input1 > 0)
		{
			rem = input1 % 10;
			
			if(rem % 2 == 0) evenSum += rem;
			else oddSum += rem;

			input1 /= 10;
		}

		while(input2 > 0)
		{
			rem = input2 % 10;
			
			if(rem % 2 == 0) evenSum += rem;
			else oddSum += rem;

			input2 /= 10;
		}

		while(input3 > 0)
		{
			rem = input3 % 10;
			
			if(rem % 2 == 0) evenSum += rem;
			else oddSum += rem;

			input3 /= 10;
		}

		if(input4 % 2 == 0) PIN = evenSum - oddSum;
		else PIN = oddSum - evenSum;

		out.println("PIN: " + PIN);
	}
}

class Solution2
{
	public static void main(String args[])
	{
		Scanner scan = new Scanner(System.in);
		
		out.print("Enter size of the array: ");
		int n = scan.nextInt();

		int arr[] = new int[n];
		out.print("Enter elements into the array: ");
		for(int i=0; i<n; i++)
		{
			arr[i] = scan.nextInt();
		}

		int sum = 0;

		for(int i=0; i<n; i++)
			if(isPrime(i)) sum += arr[i];

		out.println("Sum of elements at prime indices: " + sum);
	}

	public static boolean isPrime(int n)
	{
		if(n < 2) return false;

		for(int i=2; i<=n/2; i++)
			if(n % i == 0) return false;

		return true;
	}
}