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

- In some cases, you may want to retrieve a local filesystem instance, in which case you can use the convenience method, getLocal():
```
public static LocalFileSystem getLocal(Configuration conf) throws IOException
```

- With a FileSystem instance in hand, we invoke an open() method to get the input stream for a file:
```
public FSDataInputStream open(Path f) throws IOException
public abstract FSDataInputStream open(Path f, int bufferSize) throws IOException
```
- The first method uses a default buffer size of 4 K.

**Displaying files from a Hadoop filesystem on standard output by using the FileSystem directly:**
```
public class FileSystemCat
{
	public static void main(String[] args) throws Exception
 	{
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		InputStream in = null;
		try
  		{
			in = fs.open(new Path(uri));
			IOUtils.copyBytes(in, System.out, 4096, false);
		}
  		finally
    		{
			IOUtils.closeStream(in);
		}
	}
}
```

- The program runs as follows:
```
% hadoop FileSystemCat hdfs://localhost/user/tom/quangle.txt
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat.
```

<br>

### FSDataInputStream:
- The open() method on FileSystem actually returns a FSDataInputStream rather than a standard java.io class.
- This class is a specialization of java.io.DataInputStream with support for random access, so you can read from any part of the stream:
```
package org.apache.hadoop.fs;
public class FSDataInputStream extends DataInputStream implements Seekable, PositionedReadable
{
	// implementation elided
}
```

- The Seekable interface permits seeking to a position in the file and a query method for the current offset from the start of the file (getPos()):
```
public interface Seekable
{
	void seek(long pos) throws IOException;
	long getPos() throws IOException;
}
```

- Calling seek() with a position that is greater than the length of the file will result in an IOException.
- Unlike the skip() method of java.io.InputStream that positions the stream at a point later than the current position, seek() can move to an arbitrary, absolute position in the file.

**Displaying files from a Hadoop filesystem on standard output twice, by using seek:**
```
public class FileSystemDoubleCat
{
	public static void main(String[] args) throws Exception
	{
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		FSDataInputStream in = null;
		try
		{
			in = fs.open(new Path(uri));
			IOUtils.copyBytes(in, System.out, 4096, false);
			in.seek(0); // go back to the start of the file
			IOUtils.copyBytes(in, System.out, 4096, false);
		}
		finally
		{
			IOUtils.closeStream(in);
		}
	}
}
```

**Here’s the result of running it on a small file:**
```
% hadoop FileSystemDoubleCat hdfs://localhost/user/tom/quangle.txt
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat.
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat.
```

- FSDataInputStream also implements the PositionedReadable interface for reading parts of a file at a given offset:
```
public interface PositionedReadable
{
	public int read(long position, byte[] buffer, int offset, int length) throws IOException;
	public void readFully(long position, byte[] buffer, int offset, int length) throws IOException;
	public void readFully(long position, byte[] buffer) throws IOException;
}
```
