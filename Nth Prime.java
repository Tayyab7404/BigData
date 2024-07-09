	
class NthPrime
{
    	public static void main(String args[])
	    {
	          int count = 0;
            int N = 2;
            int mod = 0;
            while(true)
            {
                for(int i=2; i<=(int)Math.sqrt(N); i++)
                {
                    if(N % i == 0)
                    {
                        mod = 1;
                        break;
                    }
                }
                
                if(mod == 0)
                {
                    count += 1;
                }
                else
                {
                    mod = 0;
                }
                if(count == input1)
                {
                    return N;
                }
              
                N += 1;
            }
	    }
}
