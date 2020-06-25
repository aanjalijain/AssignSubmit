package com.pkg.compare;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*import org.w3c.tools.crypt.Md5;*/

/**
 * Class to do the actual comparison of jar files,
 * populating a list of EntryDetails objects
 */
public abstract class Comparer
{
	private static final int NOT_FOUND = -1;
	/**
	 * Compare the two given files and return the results
	 * @param inFile1 first file
	 * @param inFile2 second file
	 * @param inMd5 true to also check md5 sums
	 * @return results of comparison
	 */
	public static CompareResults compare(File inFile1, File inFile2, boolean inMd5)
	{
		// Make results object and compare file sizes
		CompareResults results = new CompareResults();
		results.setSize(0, inFile1.length());
		results.setSize(1, inFile2.length());
		// Make empty list
		ArrayList<EntryDetails> entryList = new ArrayList<EntryDetails>();
		// load first file, make entrydetails object for each one
		final int numFiles1 = makeEntries(entryList, inFile1,inFile2, 0);
		results.setNumFiles(0, numFiles1);
		// load second file, try to find entrydetails for each file or make new one
		final int numFiles2 = makeEntries(entryList, inFile1,inFile2, 1);
		results.setNumFiles(1, numFiles2);
		results.setEntryList(entryList);

		// Check md5 sums if necessary
		if (inMd5) {
			calculateMd5(results, inFile1, 0);
			calculateMd5(results, inFile2, 1);
		}
		return results;
	}

	// TODO: Maybe we need to add an option to ignore path, just look at filenames?

	/**
	 * Make entrydetails objects for each entry in the given file and put in list
	 * @param inList list of entries so far
	 * @param inFile zip/jar file to search through
	 * @param inIndex 0 for first file, 1 for second
	 * @return number of files found
	 */
	private static int makeEntries(ArrayList<EntryDetails> inList, File inFile,File inFile2, int inIndex)
	{
		System.out.println("inList"+inList);
		System.out.println("inFile"+inFile);
		System.out.println("inIndex"+inIndex);
		boolean checkList = (inList.size() > 0);
		int numFiles = 0;
		JarFile zip=null;
		JarFile zipJar1=null;
		try
		{
			if(inIndex==0){
				zip = new JarFile(inFile);
				
			}else{
				zipJar1 = new JarFile(inFile);
				zip = new JarFile(inFile2);
			}
			Enumeration<?> zipEntries = zip.entries();
			while (zipEntries.hasMoreElements())
			{
				JarEntry ze = (JarEntry) zipEntries.nextElement();
				numFiles++;
			
				/* java.io.InputStream is = zip.getInputStream(ze); // get the input stream
				    java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
				    while (is.available() > 0) {  // write contents of 'is' to 'fos'
				        fos.write(is.read());
				    }*/
				String name = ze.getName();
				FileTime time=ze.getLastModifiedTime();
				EntryDetails details = null;
			
				if (checkList) {
					details = getEntryFromList(inList, name,zip,zipJar1);
					}
				// Construct new details object if necessary
				if (details == null)
				{
					details = new EntryDetails();
					details.setName(name);
					inList.add(details);
				}
				// set size
				details.setSize(inIndex, ze.getSize());
				/*if("CHANGED_SIZE".equals(details.getStatus())){
					
				}
				System.out.println(details.getStatus());*/
			}
		}
		catch (IOException ioe) {
			System.err.println("Ouch: " + ioe.getMessage());
		}
		finally {
			try {zip.close();} catch (Exception e) {}
		}
		return numFiles;
	}

	/**
	 * Look up the given name in the list
	 * @param inList list of EntryDetails objects
	 * @param inName name to look up
	 * @throws IOException 
	 */
	private static EntryDetails getEntryFromList(ArrayList<EntryDetails> inList, String inName,JarFile zip,JarFile zipJar1) throws IOException
	{
		EntryDetails details = null;
		for (int i=0; i<inList.size(); i++)
		{
			details = inList.get(i);
			if (details.getName() != null && details.getName().equals(inName)) {
				ZipEntry zipEntry = zip.getEntry(inName);
				System.out.println(zipEntry.getName().getClass());
				 if (zipEntry.isDirectory()) {
                   System.out.println(  new File(File.separator + zipEntry.getName())
                                     .mkdirs());
                 } 
                ZipEntry zipEntryJar1 = zipJar1.getEntry(details.getName());
                try{
               	
               	 InputStream fis = zip.getInputStream(zipEntry);

                 InputStreamReader isr = new InputStreamReader(fis,
                            StandardCharsets.UTF_8);
                   
           		 BufferedReader br = new BufferedReader(isr);
                    br.lines().forEach(line -> System.out.println(line));
            	//Print the file for jarpoc1 data
               	 InputStream fiss = zipJar1.getInputStream(zipEntryJar1);
                 InputStreamReader isrr = new InputStreamReader(fiss,StandardCharsets.UTF_8);
           		 BufferedReader  buffr = new BufferedReader(isrr);
               	 buffr.lines().forEach(
               			linee -> {
               				System.out.println(linee);
               				});
             	try{
               	if("class".equals(getExtension(inName))){
               	File f = new File(inName);  
              
               	System.out.println(f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\")+1));
               	String className=f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("\\")+1);
               
                Object classobj = Class.forName(className);
               	Method[] methods = ((Class<? extends String>) classobj).getMethods();
                for (Method method : methods) { 
                	  
                    String MethodName = method.getName(); 
                    System.out.println("Name of the method: "
                                       + MethodName); 
                } 
               		
               	}
               	}catch(Exception ex){
               		System.out.println(ex.getMessage());
               	}
               //	
                //System.out.println(contentEqualsIgnoreEOL(fileReader,fileReader2));
                }catch(Exception e){
               	 
                }
           
               
				return details;
			}
		}
		return null;
	}
	 public static String getExtension(final String filename) {
		         if (filename == null) {
		            return null;
		        }
		        final int index = indexOfExtension(filename);
		        if (index == NOT_FOUND) {
		           return "";
		        } else {
		           return filename.substring(index + 1);
		       }
		    }
	 public static int indexOfExtension(final String filename) {
		        if (filename == null) {
		            return NOT_FOUND;
		        }
		        final int extensionPos = filename.lastIndexOf('.');
		        final int lastSeparator = indexOfLastSeparator(filename);
		        return lastSeparator > extensionPos ? NOT_FOUND : extensionPos;
		    }
	 
	 public static int indexOfLastSeparator(final String filename) {
		       if (filename == null) {
		            return NOT_FOUND;
		       }
		      final int lastUnixPos = filename.lastIndexOf("/");
		      final int lastWindowsPos = filename.lastIndexOf("\\");
		        return Math.max(lastUnixPos, lastWindowsPos);
		    }
	public static boolean contentEqualsIgnoreEOL(final Reader input1, final Reader input2)
	          throws IOException {
	        if (input1 == input2) {
	            return true;
	       }
	       if (input1 == null ^ input2 == null) {
	            return false;
	        }
	       final BufferedReader br1 = toBufferedReader(input1);
	       final BufferedReader br2 = toBufferedReader(input2);

	       String line1 = br1.readLine();
	       String line2 = br2.readLine();
	        while (line1 != null && line1.equals(line2)) {
	            line1 = br1.readLine();
	            line2 = br2.readLine();
	       }
	       return Objects.equals(line1, line2);
	    }
	
	 public static BufferedReader toBufferedReader(final Reader reader) {
		 return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
		 }

	/**
	 * Calculate the md5 sums of all relevant entries
	 * @param inResults results from preliminary check
	 * @param inFile file to read
	 * @param inIndex 0 or 1
	 */
	private static void calculateMd5(CompareResults inResults, File inFile, int inIndex)
	{
		ArrayList<EntryDetails> list = inResults.getEntryList();
		ZipFile zip = null;
		try
		{
			zip = new ZipFile(inFile);
			for (int i=0; i<list.size(); i++)
			{
				EntryDetails entry = list.get(i);
				if (entry.getStatus() == EntryDetails.EntryStatus.SAME_SIZE)
				{
					// Must be present in both archives if size is the same
					ZipEntry zipEntry = zip.getEntry(entry.getName());
					if (zipEntry == null) {
						System.err.println("zipEntry for " + entry.getName() + " shouldn't be null!");
					}
					//Md5 hasher = new Md5(zip.getInputStream(zipEntry));
					/*byte[] digest = hasher.getDigest();
					if (digest != null) {
						String hash = hasher.getStringDigest();
						// System.out.println("Calculated md5 sum for " + entry.getName() + " - '" + hash + "'");
						entry.setMd5Sum(inIndex, hash);
					}*/
				}
			}
		}
		catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		finally {
			try {zip.close();} catch (Exception e) {}
		}
	}
}

