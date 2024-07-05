# BigData

## **The Java Interface**

## Reading Data from a Hadoop URL:
- One of the simplest ways to read a file from a Hadoop filesystem is by using a java.net.URL object to open a stream to read the data from.

**Code:**
``` 
InputStream in = null;
try
{
    in = new URL("hdfs://host/path").openStream();
    // process in
}
finally
{
    IOUtils.closeStream(in);
}
```

- We can make Java recognize Hadoop's hdfs URL scheme by calling the setURLStreamHandlerFactory method on URL with an instance of FsUrlStreamHandlerFactory.
- This method can only be called once per JVM, so it is typically executed in a static block. This limitation means that if some other part of your program sets a URLStreamHandlerFactory, you won't be able to use this approach for reading data from Hadoop.

**Displaying files from a Hadoop filesystem on standard output using a URLStreamHandler:**
```
public class URLCat
{
 	static
	{
 	URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
 	}

 	public static void main(String[] args) throws Exception 
	{
 		InputStream in = null;
 		try 
		{
 			in = new URL(args[0]).openStream();
 			IOUtils.copyBytes(in, System.out, 4096, false);
 		}
		finally
		{
 			IOUtils.closeStream(in);
 		}
 	}
}
```

**Here's a sample run:**
```
% hadoop URLCat hdfs://localhost/user/tom/quangle.txt
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat.
```
