# Big Data

## **The Java Interface**

### Reading Data from a Hadoop URL:
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

<br>

### Reading Data Using the FileSystem API:
- Sometimes it is impossible to set a URLStreamHandlerFactory for your application so you need to use the FileSystem API to open an input stream for a file.
- A file in a Hadoop filesystem is represented by a Hadoop Path object. You can think of a Path as a Hadoop filesystem URI, such as hdfs://localhost/user/tom/quangle.txt.
- FileSystem is a general filesystem API, so the first step is to retrieve an instance for the filesystem we want to use (HDFS in this case).
- There are several static factory methods for getting a FileSystem instance:
```
public static FileSystem get(Configuration conf) throws IOException
public static FileSystem get(URI uri, Configuration conf) throws IOException
public static FileSystem get(URI uri, Configuration conf, String user) throws IOException
```

- The first method returns the default filesystem.
- The second uses the given URI’s scheme and authority to determine the filesystem to use, falling back to the default filesystem if no scheme is specified in the given URI.
- The third retrieves the filesystem as the given user.

<br>

- In some cases, you may want to retrieve a local filesystem instance, in which case you can use the convenience method, getLocal():
```
public static LocalFileSystem getLocal(Configuration conf) throws IOException
```
