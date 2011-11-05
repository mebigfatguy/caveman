package com.mebigfatguy.cavemantask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;


public class CaveManTask extends Task {

	private static final String[] PRIMITIVES = new String[] { "boolean", "byte", "char", "short", "int", "long", "float", "double" };
	private static final Pattern WHOLE_WORD_CAVEMAN = Pattern.compile("\bCaveMan\b");

	private File srcDir;
	private File dstDir;
	private String dstPackage;
	
	public void setSourceFolder(File src) {
		srcDir = src;
	}
	
	public void setDestinationFolder(File dst) {
		dstDir = dst;
	}
	
	public void setPackage(String pckg) {
		dstPackage = pckg;
	}
	
	public void execute() {
		validateProperties();
		
		dstDir.mkdirs();
		
		File[] cmFiles = srcDir.listFiles();
		for (File cmf : cmFiles) {
			if (cmf.isFile()) {
				for (String primitive : PRIMITIVES) {
					generate(cmf, primitive);
				}
			}
		}
	}
	
	private void generate(File cavemanProtoFile, String primitive) {
		String primitiveLabel = Character.toUpperCase(primitive.charAt(0)) + primitive.substring(1);
		
		String fileName = cavemanProtoFile.getName();
		String className = fileName.substring(0, fileName.length() - ".java".length()).replaceAll("CaveMan", primitiveLabel);
		File f = new File(dstDir, className + ".java");
		
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			br = new BufferedReader(new FileReader(cavemanProtoFile));
			pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
			
			String line = br.readLine();
			while (line != null) {
				if (line.trim().startsWith("package ")) {
					pw.println("package " + dstPackage + ";");
				} else if (!line.trim().startsWith("import")) {
					pw.println(line.replaceAll("\\bCaveMan\\b", primitive).replaceAll("CaveMan", primitiveLabel));
				}
				line = br.readLine();
			}
		} catch (IOException ioe) {
			throw new BuildException("Failed writing to file: " + f, ioe);
		} finally {
			closeQuietly(pw);
			closeQuietly(br);
		}
	}
	
	private void validateProperties() {
		if (srcDir == null) {
			throw new BuildException("'sourceFolder' property not set");
		} else {
			if (!srcDir.isDirectory()) {
				throw new BuildException(srcDir + " for 'sourceFolder' does not exist");
			}
		}
		
		if (dstDir == null) {
			throw new BuildException("'destinationFolder' property not set");
		}
		
		if (dstPackage == null) {
			throw new BuildException("'package' property not set");
		}
	}
	
	private void closeQuietly(Closeable c) {
		try {
			if (c != null)
				c.close();
		} catch (Exception e) {
		}
	}
	/** 
	 * just for testing 
	 */
	public static void main(String[] args) {
		
		Project p = new Project();
		CaveManTask task = new CaveManTask();
		task.setProject(p);
		task.setSourceFolder(new File("/home/dave/dev/caveman/prototype/com/mebigfatguy/caveman/proto/"));
		task.setDestinationFolder(new File("/home/dave/dev/caveman/src/com/mebigfatguy/caveman/"));
		task.setPackage("com.mebigfatguy.caveman");
		
		task.execute();
	}
}
