package core.utilities;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.lang.model.element.Modifier;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeSpec;

import core.testdata.manager.TestCase;
import core.testdata.manager.TestStep;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import selenium.driver.Driver;

/**
 * @summary This class is responsible for generating test code from jsons
 * @author Surendra.Shekhawat
 */
public class TestGenerator {
	final static Logger logger = Logger.getLogger(TestGenerator.class);
	private Modifier classAccessModifier = Modifier.PUBLIC;
	private Modifier methodAccessModifier = Modifier.PUBLIC;
	private Modifier modifierType = Modifier.STATIC;
	private TestCase testCase;
	private String groupDefination = "groups = { \"%1s\"}";

	/**
	 * @summary Default constructor which accepts test case object which has json populated
	 * @param testCase
	 */
	public TestGenerator(TestCase testCase) {
		this.testCase = testCase;
	}

	/**
	 * @summary Builds @Epic annotation
	 * @param value
	 * @return
	 */
	public AnnotationSpec getEpicAnnotation(String value) {
		return AnnotationSpec.builder(Epic.class).addMember("value", "$S", value).build();
	}

	/**
	 * @summary Builds @Feature annotation
	 * @param value
	 * @return
	 */
	public AnnotationSpec getFeatureAnnotation(String value) {
		return AnnotationSpec.builder(Feature.class).addMember("value", "$S", value).build();
	}

	/**
	 * @summary Builds @Description annotation
	 * @param value
	 * @return
	 */
	public AnnotationSpec getDescriptionAnnotation(String value) {
		return AnnotationSpec.builder(Description.class).addMember("value", "$S", value).build();
	}

	/**
	 * @summary Builds @Link annotation
	 * @param value
	 * @return
	 */
	public AnnotationSpec getLinkAnnotation(String value) {
		return AnnotationSpec.builder(Link.class).addMember("value", "$S", value).build();
	}

	/**
	 * @summary Builds @Test annotation
	 * @param group
	 * @return
	 */
	public AnnotationSpec getTestAnnotation(String group) {
		return AnnotationSpec.builder(Test.class).addMember("value", "$N", String.format(groupDefination, group))
				.build();
	}

	/**
	 * @summary Builds custom annotation
	 * @param methodBuilder
	 * @return
	 */
	public Builder generateAnnotations(Builder methodBuilder) {
		if (this.testCase.getCategory() != null && this.testCase.getCategory() != "") {
			methodBuilder.addAnnotation(getEpicAnnotation(this.testCase.getCategory()));
		}
		if (this.testCase.getBattery() != null && this.testCase.getBattery() != "") {
			methodBuilder.addAnnotation(getFeatureAnnotation(this.testCase.getBattery()));
		}
		if (this.testCase.getDescription() != null && this.testCase.getDescription() != "") {
			methodBuilder.addAnnotation(getDescriptionAnnotation(this.testCase.getDescription()));
		}
		if (this.testCase.getKey() != null && this.testCase.getKey() != "") {
			methodBuilder.addAnnotation(getLinkAnnotation(this.testCase.getKey()));
		}
		methodBuilder.addAnnotation(getTestAnnotation(this.testCase.getGroup()));

		return methodBuilder;
	}

	/**
	 * @sumamry Method generating test steps from a json action
	 * @param methodBuilder
	 * @param actionMap
	 * @param battery
	 * @return
	 */
	public Builder generateStatements(Builder methodBuilder, Map<String, Class<?>> actionMap, String battery) {
		String classInitiateSyntex = null;
		// If battery is API then constructor will proceed without webdriver i.e. browser handling not required 
		if (battery.startsWith("API") || battery.equalsIgnoreCase("API")) {
			classInitiateSyntex = "$T %1s = new $T($S)";
		} else {
			classInitiateSyntex = "$T %1s = new $T($T.getDriver(), $S)";
		}

		// Get all steps from a test case and generate java statement for it
		List<TestStep> testSteps = this.testCase.get_testSteps();
		for (TestStep testStep : testSteps) {
			String jsonString = new Gson().toJson(testStep.getTestParams(), Map.class);
			logger.info(testStep.getName() +", ");
			Class<?> clazz = actionMap.get(testStep.getName());
			if(clazz==null)
			{
				logger.info("Test Step name not found in find action class -" +testStep.getName());
			}
			String objectname = testStep.getName().toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
			objectname = objectname + generateRandomeString(5);

			if (battery.startsWith("API") || battery.equalsIgnoreCase("API")) {
				if (testStep.getReport() != null && testStep.getReport().equals("true")) {
					classInitiateSyntex = "$T %1s = new $T($S, $S, $S)";
					methodBuilder.addStatement(String.format(classInitiateSyntex, objectname), clazz, clazz,
							jsonString, this.testCase.getName(), this.testCase.getDescription());
				} else {
					classInitiateSyntex = "$T %1s = new $T($S)";
					methodBuilder.addStatement(String.format(classInitiateSyntex, objectname), clazz, clazz,
							jsonString);
				}
			} else {
				if (testStep.getReport() != null && testStep.getReport().equals("true")) {
					classInitiateSyntex = "$T %1s = new $T($T.getDriver(), $S, $S, $S)";
					methodBuilder.addStatement(String.format(classInitiateSyntex, objectname), clazz, clazz, Driver.class,
							jsonString, this.testCase.getName(), this.testCase.getDescription());
				} else {
					classInitiateSyntex = "$T %1s = new $T($T.getDriver(), $S)";
					methodBuilder.addStatement(String.format(classInitiateSyntex, objectname), clazz, clazz, Driver.class,
							jsonString);
				}
			}
			
			// Generating all action signatures
			methodBuilder.addStatement("$N.setup()", objectname);
			methodBuilder.addStatement("$N.execute()", objectname);
			methodBuilder.addStatement("$N.validate()", objectname);
			methodBuilder.addStatement("$N.tearDown()", objectname);
		}

		return methodBuilder;
	}

	/**
	 * @summary Generates test method
	 * @return
	 * @throws IOException
	 */
	public Builder generateMethod() throws IOException {
		String testClassName = getClassName().replace("-", "_");
		return MethodSpec.methodBuilder(testClassName.toLowerCase()).addModifiers(Modifier.PUBLIC);
	}

	/**
	 * @summary Generates test class
	 * @param method
	 * @param baseClazz
	 * @param listenerClazz
	 * @return
	 * @throws IOException
	 */
	public String generateTestClass(MethodSpec method, Class baseClazz, Class listenerClazz) throws IOException {
		String testClassName = getClassName().replace("-", "_");
		String className = "tests.ui." + testClassName;
		try {
			File file = new File("src/test/java");
			TypeSpec testClass = TypeSpec.classBuilder(testClassName).superclass(baseClazz)
					.addModifiers(Modifier.PUBLIC).addMethod(method).build();

			JavaFile javaFile = JavaFile.builder("tests.ui", testClass).build();
			javaFile.writeTo(file.getAbsoluteFile());

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(className);
		return className;
	}


	/**
	 * @summary Generates random string
	 * @param length
	 * @return
	 */
	private String generateRandomeString(int length) {
		Random RANDOM = new SecureRandom();
		String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder returnValue = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		return new String(returnValue);
	}

	/**
	 * @summary To check if given string is a class else will return class not found 
	 * @param className
	 * @return
	 */
	private boolean isClass(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * @summary Generates a class name out of test case name
	 * @return
	 */
	private String getClassName() {
		String testCaseName = null;
		String testCaseId = this.testCase.getId();
		String testCaseDescription = this.testCase.getName();
		if (testCaseDescription != null) {
			testCaseName = testCaseDescription.replaceAll("[^a-zA-Z0-9_-]", "_");
		} else {
			testCaseName = testCaseId.replaceAll("[^a-zA-Z0-9_-]", " ");
		}
		return testCaseName.toUpperCase();
	}

}