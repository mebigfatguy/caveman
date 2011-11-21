package com.mebigfatguy.cavemantask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

public class CaveManTask extends Task {

	private static final String[] PRIMITIVES = new String[] { "boolean", "byte", "char", "short", "int", "long", "float", "double" };

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

	@Override
	public void execute() {
		validateProperties();

		dstDir.mkdirs();

		File[] cmFiles = srcDir.listFiles();
		for (File cmf : cmFiles) {
			if (cmf.isFile()) {
				if (cmf.getName().contains("CMKeyCMValue")) {
					for (String keyPrimitive : PRIMITIVES) {
						for (String valuePrimitive : PRIMITIVES) {
							generate(cmf, keyPrimitive, valuePrimitive);
						}
					}
				} else {
					for (String primitive : PRIMITIVES) {
						generate(cmf, primitive);
					}
				}
			}
		}
	}

	private void generate(File cavemanProtoFile, String primitive) {
		String primitiveLabel = Character.toUpperCase(primitive.charAt(0)) + primitive.substring(1);

		String fileName = cavemanProtoFile.getName();
		String className = applyCMReplacements(fileName.substring(0, fileName.length() - ".java".length()),
											primitive, primitiveLabel);
		File f = new File(dstDir, className + ".java");

		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			br = new BufferedReader(new FileReader(cavemanProtoFile));
			pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));

			String line = br.readLine();
			while (line != null) {
				if (line.contains("assertEquals") && line.contains("toCaveMan")) {
					if ("float".equals(primitive)) {
						line = line.replaceAll("assertEquals\\(([^,]*),([^;]*)", "assertEquals($1,$2");
						line = line.substring(0, line.length() - 2) + ", 0.0001f);";
					} else if ("double".equals(primitive)) {
						line = line.replaceAll("assertEquals\\(([^,]*),([^;]*)", "assertEquals($1,$2");					
						line = line.substring(0, line.length() - 2) + ", 0.0001f);";
					}
				}
				
				if (line.trim().startsWith("package ")) {
					pw.println("package " + dstPackage + ";");
				} else if (line.contains("toCaveMan")) {
					if (!line.contains("private")) {
						if ("boolean".equals(primitive)) {
							pw.println(applyCMReplacements(
										line.replaceAll("toCaveMan\\(([^\\)]*)\\)", "(($1 == 0) ? false : true)"),
										primitive, primitiveLabel));
							
						} else {
							pw.println(applyCMReplacements(
									line.replaceAll("toCaveMan\\(([^\\)]*)\\)", "(" + primitive + ") $1"),
									primitive, primitiveLabel));
						}
					}
				} else if (line.contains("fromCaveMan")) {
					if (!line.contains("private")) {
						if ("boolean".equals(primitive)) {
							pw.println(applyCMReplacements(
									line.replaceAll("fromCaveMan\\(([^\\)]*)\\)", "(($1) ? 1 : 0)"),
									primitive, primitiveLabel));
							
						} else {
							pw.println(applyCMReplacements(
									line.replaceAll("fromCaveMan\\(([^\\)]*)\\)", "(int) $1"),
									primitive, primitiveLabel));
						}
					}
				} else if (!line.contains(".aux.")) {
					if (line.contains(".proto."))
						pw.println(applyCMReplacements(
								line.replaceAll("\\.proto", ""),
								primitive, primitiveLabel));
					else
						pw.println(applyCMReplacements(line,primitive, primitiveLabel));
				} else if (line.contains(".proto.aux.CMKeySet") || line.contains(".proto.aux.CMValueSet")) {
					pw.println(applyCMReplacements(line.replaceAll("\\.proto\\.aux", ""),
							primitive, primitiveLabel));
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
	
	private String applyCMReplacements(String input, String primitive, String primitiveLabel) {
		return input.replaceAll("\\bCMKey\\b", primitive).replaceAll("CMKey", primitiveLabel)
				.replaceAll("\\bCMValue\\b", primitive).replaceAll("CMValue", primitiveLabel)
				.replaceAll("\\bCM\\b", primitive).replaceAll("CM", primitiveLabel);
	}

	private void generate(File cavemanProtoFile, String keyPrimitive, String valuePrimitive) {
		String keyPrimitiveLabel = Character.toUpperCase(keyPrimitive.charAt(0)) + keyPrimitive.substring(1);
		String valuePrimitiveLabel = Character.toUpperCase(valuePrimitive.charAt(0)) + valuePrimitive.substring(1);

		String fileName = cavemanProtoFile.getName();
		String className = fileName.substring(0, fileName.length() - ".java".length()).replaceAll("CMKey", keyPrimitiveLabel)
				.replaceAll("CMValue", valuePrimitiveLabel);
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
				} else if (line.contains("toCaveMan")) {
					if (!line.contains("private")) {
						if (line.contains("toCaveManKey")) {
							if ("boolean".equals(keyPrimitive)) {
								line = applyCMReplacements(line.replaceAll("toCaveManKey\\(([^\\)]*)\\)", "(($1 == 0) ? false : true)"), 
										keyPrimitive, keyPrimitiveLabel);
								
							} else {
								line = applyCMReplacements(line.replaceAll("toCaveManKey\\(([^\\)]*)\\)", "(" + keyPrimitive + ") $1"), 
										keyPrimitive, keyPrimitiveLabel);
							}
						} 
						
						if (line.contains("toCaveManValue")) {
							if ("boolean".equals(valuePrimitive)) {
								line = applyCMReplacements(line.replaceAll("toCaveManValue\\(([^\\)]*)\\)", "(($1 == 0) ? false : true)"), 
										valuePrimitive, valuePrimitiveLabel);
								
							} else {
								line = applyCMReplacements(line.replaceAll("toCaveManValue\\(([^\\)]*)\\)", "(" + valuePrimitive + ") $1"), 
										valuePrimitive, valuePrimitiveLabel);
							}
						}
						
						pw.println(line);
					}					
				} else if (line.contains("fromCaveMan")) {
					if (!line.contains("private")) {
						if (line.contains("fromCaveManKey")) {
							if ("boolean".equals(keyPrimitive)) {
								line = (applyCMReplacements(
										line.replaceAll("fromCaveManKey\\(([^\\)]*)\\)", "(($1) ? 1 : 0)"),
										keyPrimitive, keyPrimitiveLabel));
								
							} else {
								line = (applyCMReplacements(
										line.replaceAll("fromCaveManKey\\(([^\\)]*)\\)", "(int) $1"),
										keyPrimitive, keyPrimitiveLabel));
							}
						}
						
						if (line.contains("fromCaveManValue")) {
							if ("boolean".equals(valuePrimitive)) {
								line = (applyCMReplacements(
										line.replaceAll("fromCaveManValue\\(([^\\)]*)\\)", "(($1) ? 1 : 0)"),
										valuePrimitive, valuePrimitiveLabel));
								
							} else {
								line = (applyCMReplacements(
										line.replaceAll("fromCaveManValue\\(([^\\)]*)\\)", "(int) $1"),
										valuePrimitive, valuePrimitiveLabel));
							}
						}
						
						pw.println(line);
					}
				} else if (!line.trim().startsWith("import") || line.contains("java.") || line.contains("org.")) {
					pw.println(line.replaceAll("\\bCMKey\\b", keyPrimitive).replaceAll("\\bCMValue\\b", valuePrimitive)
							.replaceAll("CMKey", keyPrimitiveLabel).replaceAll("CMValue", valuePrimitiveLabel));
				} else if (line.trim().startsWith("import") && (line.contains("CMKeySet") || line.contains("CMValueBag"))) {
					pw.println(line.replaceAll("\\.proto\\.aux", "").replaceAll("CMKey", keyPrimitiveLabel).replaceAll("CMValue", valuePrimitiveLabel));
				} else if (line.trim().startsWith("import") && (line.contains("proto.CMKeyCMValue") || line.contains("proto.impl.CaveManCMKeyCMValue"))) {
					pw.println(line.replaceAll("\\.proto", "").replaceAll("CMKey", keyPrimitiveLabel).replaceAll("CMValue", valuePrimitiveLabel));
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
		
		task.setSourceFolder(new File("/home/dave/dev/caveman/prototype/com/mebigfatguy/caveman/proto/impl"));
		task.setDestinationFolder(new File("/home/dave/dev/caveman/src/com/mebigfatguy/caveman/impl"));
		task.setPackage("com.mebigfatguy.caveman.impl");

		task.execute();

		task.setSourceFolder(new File("/home/dave/dev/caveman/prototype/com/mebigfatguy/caveman/proto/test"));
		task.setDestinationFolder(new File("/home/dave/dev/caveman/test/com/mebigfatguy/caveman/test"));
		task.setPackage("com.mebigfatguy.caveman.test");

		task.execute();
	}
}
