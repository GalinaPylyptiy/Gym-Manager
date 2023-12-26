package com.epam.gym.config;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/main/test/resources/features", glue = "com/epam/gym/stepDef")
public class CucumberTest {

}
