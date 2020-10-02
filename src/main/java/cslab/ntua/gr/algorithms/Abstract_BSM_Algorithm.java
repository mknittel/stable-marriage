package cslab.ntua.gr.algorithms;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipInputStream;
import java.util.Random;

import cslab.ntua.gr.entities.Student;
import cslab.ntua.gr.entities.School;
import cslab.ntua.gr.entities.Marriage;

public abstract class Abstract_BSM_Algorithm
{
	protected int n, m;
	protected Student[] students;
	protected School[] schools;
    protected long rounds;
    protected double time;

	public Abstract_BSM_Algorithm(int n, int m, Student[] students, School[] schools)
    {
        this.n = n;
		this.m = m;
        this.rounds = 0;
        this.time = 0;
        this.students = students;
		this.schools = schools;
    }

    public Abstract_BSM_Algorithm(int n, int m, String studentFile, String schoolFile, String affiliateFile)
    {
    	this.n = n;
		this.m = m;
        rounds = 0;
        time = 0;

		students = new Student[n*m];
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
            /*for (i = 0; i < n; i++)
            {
                // New student with random threshold
                students[i] = new Student(n, i, 0, 1, r.nextDouble());
            }*/
			;
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
                        students[i] = new Student(n, m, i, 0, sCurrentLine);
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
                        students[i] = new Student(n, m, i, 0, sCurrentLine);
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
            /*for (i = 0; i < n; i++)
            {
                // New school
                schools[i] = new School(n, i, 1, r.nextDouble(), r.nextDouble());
            }*/
			;
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
						ArrayList<String> sCurrentLines2 = new ArrayList<String>();
						for (int j = 0; j < m; j++) {
							sCurrentLines2.add(br2.readLine());
						}
                        schools[i] = new School(n, m, i, 1, sCurrentLine, sCurrentLines2);
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
						ArrayList<String> sCurrentLines2 = new ArrayList<String>();
						for (int j = 0; j < m; j++) {
							sCurrentLines2.add(br2.readLine());
						}
                        schools[i] = new School(n, m, i, 1, sCurrentLine, sCurrentLines2);
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

		int studentIndex = 0;
		int schoolIndex = 0;

		while (studentIndex < n * m) {
			School school = this.schools[schoolIndex];
			Student student = this.students[studentIndex];
			school.addAffiliate(student);
			student.setSchool(school);

			studentIndex++;

			if (studentIndex % m == 0) {
				schoolIndex++;
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

    public Student[] getStudents(){ return students; }
    public School[] getSchools(){ return schools; }
    public int getSize(){ return n; }
    public long getRounds(){ return rounds; }
    public double getTime(){ return time; }
}
