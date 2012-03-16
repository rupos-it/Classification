package org.processmining.tests.classification;

import org.junit.Test;
import org.processmining.contexts.cli.CLI;
import org.processmining.contexts.test.PromTest;


public class ClassificationTest  extends PromTest {

	  @Test
	  public void testClassification1() throws Throwable {
	    String args[] = new String[] {"-l"};
	    CLI.main(args);
	  }

	  @Test
	  public void testClassification2() throws Throwable {
	    String testFileRoot = System.getProperty("test.testFileRoot", PromTest.defaultTestDir);
	    String args[] = new String[] {"-f", testFileRoot+"/Classification_Example.txt"};
	    
	    CLI.main(args);
	  }
	  
	  public static void main(String[] args) {
	    junit.textui.TestRunner.run(ClassificationTest.class);
	  }
	  
	}
