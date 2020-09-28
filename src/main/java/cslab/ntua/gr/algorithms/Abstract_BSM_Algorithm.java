package cslab.ntua.gr.algorithms;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.zip.ZipInputStream;
import java.util.Random;

import cslab.ntua.gr.entities.BinaryAgent;
import cslab.ntua.gr.entities.School;
import cslab.ntua.gr.entities.Marriage;

public abstract class Abstract_BSM_Algorithm
{
	protected int n;
	protected BinaryAgent[] students;
	protected School[] schools;
    protected long rounds;
    protected double time;

	public Abstract_BSM_Algorithm(int n, BinaryAgent[] students, School[] schools)
    {
        this.n = n;
        this.rounds = 0;
        this.time = 0;
        this.students = students;
		this.schools = schools;
    }

    public Abstract_BSM_Algorithm(int n, String studentFile, String schoolFile, String affiliateFile)
    {
    	this.n = n;
        rounds = 0;
        time = 0;

		students = new BinaryAgent[n];
		schools = new School[n];

		Random r = new Random();

        BufferedReader br, br2;
        ZipInputStream zipStream, zipStream2;
        FileReader fr, fr2;
        FileInputStream fis, fis2;
        BufferedInputStream bis, bis2;
        String sCurrentLine, sCurrentLine2;
        int i;

        
        if (studentFile == null)
        {
            for (i = 0; i < n; i++)
            {
                // New student with random threshold
                students[i] = new BinaryAgent(n, i, 0, r.nextDouble());
            }
        }
        else
        {
            if (studentFile.endsWith(".zip"))
            {
                br = null;
                zipStream = null;
                try 
                {
                    fis = new FileInputStream(studentFile);
                    bis = new BufferedInputStream(fis);
                    zipStream = new ZipInputStream(bis, Charset.forName("UTF-8"));
                    zipStream.getNextEntry();

                    br = new BufferedReader(new InputStreamReader(zipStream));
                    i = 0;
                    while ((sCurrentLine = br.readLine()) != null) 
                    {
                        students[i] = new BinaryAgent(n, i, 0, sCurrentLine);
                        i++;
                    } 
                }
                catch (IOException e) 
                {
                    System.err.println("Caught IOException: " + e.getMessage());
                    System.exit(1);
                } 
                finally 
                {
                    try 
                    {
                        if (br != null) br.close();
                    }
                    catch (IOException ex) 
                    {
                        ex.printStackTrace();
                    }
                } 
            }
            else
            {
                br = null;
                fr = null;
                try 
                {
                    // Read student file
                    fr = new FileReader(studentFile);
                    br = new BufferedReader(fr);

                    i = 0;
                    while ((sCurrentLine = br.readLine()) != null) 
                    {
                        students[i] = new BinaryAgent(n, i, 0, sCurrentLine);
                        i++;
                    }               
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                } 
                finally 
                {
                    try 
                    {
                        if (br != null) br.close();
                        if (fr != null) fr.close();
                    }
                    catch (IOException ex) 
                    {
                        ex.printStackTrace();
                    }
                }                
            }
        }

		// Assume we do random generation if either file is null
        if (schoolFile == null || affiliateFile == null)
        {
            for (i = 0; i < n; i++)
            {
                // New school
                schools[i] = new School(n, i, 1, r.nextDouble(), r.nextDouble());
            }
        }
        else
        {
            if (schoolFile.endsWith(".zip"))
            {
                br = null;
				br2 = null;
                zipStream = null;
				zipStream2 = null;
                try 
                {
                    fis = new FileInputStream(schoolFile);
					fis2 = new FileInputStream(affiliateFile);
                    bis = new BufferedInputStream(fis);
					bis2 = new BufferedInputStream(fis2);
                    zipStream = new ZipInputStream(bis, Charset.forName("UTF-8"));
					zipStream2 = new ZipInputStream(bis2, Charset.forName("UTF-8"));
                    zipStream.getNextEntry();
					zipStream2.getNextEntry();

                    br = new BufferedReader(new InputStreamReader(zipStream));
					br2 = new BufferedReader(new InputStreamReader(zipStream2));
                    i = 0;
                    while ((sCurrentLine = br.readLine()) != null) 
                    {
						sCurrentLine2 = br2.readLine();
                        schools[i] = new School(n, i, 1, sCurrentLine, sCurrentLine2);
                        i++;
                    }  
                }
                catch (IOException e) 
                {
                    System.err.println("Caught IOException: " + e.getMessage());
                    System.exit(1);
                } 
                finally 
                {
                    try 
                    {
                        if (br != null) br.close();
						if (br2 != null) br2.close();
                    }
                    catch (IOException ex) 
                    {
                        ex.printStackTrace();
                    }
                } 
            }
            else
            {
                br = null;
				br2 = null;
                fr = null;
				fr2 = null;
                try 
                {              
                    // Read school file
                    fr = new FileReader(schoolFile);
					fr2 = new FileReader(affiliateFile);
                    br = new BufferedReader(fr);
					br2 = new BufferedReader(fr2);

                    i = 0;
                    while ((sCurrentLine = br.readLine()) != null) 
                    {
						sCurrentLine2 = br2.readLine();
                        schools[i] = new School(n, i, 1, sCurrentLine, sCurrentLine2);
                        i++;
                    }                
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                } 
                finally 
                {
                    try 
                    {
                        if (br != null) br.close();
						if (br2 != null) br2.close();
                        if (fr != null) fr.close();
						if (fr2 != null) fr2.close();
                    }
                    catch (IOException ex) 
                    {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public abstract Marriage match();

    public int flip(int side)
    {
        return side^1;
    }

    public static String getName()
    {
        String className = Thread.currentThread().getStackTrace()[2].getClassName(); 
        return className;
    }

    public BinaryAgent[] getStudents(){ return students; }
    public School[] getSchools(){ return schools; }
    public int getSize(){ return n; }
    public long getRounds(){ return rounds; }
    public double getTime(){ return time; }
}
