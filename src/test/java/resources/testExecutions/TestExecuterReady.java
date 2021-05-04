package resources.testExecutions;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.github.mkolisnyk.cucumber.reporting.CucumberResultsOverview;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
@RunWith(Cucumber.class)
@CucumberOptions(
					features = {"."},
					tags={"@CucumberReady"},
					glue="resources.stepDefinitions",
					plugin = {"html:target/cucumber-html-report",
							"json:target/cucumber.json",
							"pretty:target/cucumber-pretty.txt",
							"usage:target/cucumber-usage.json",
							"junit:target/cucumber-results.xml"},
					monochrome = true
				)
public class TestExecuterReady {
}