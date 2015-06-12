
package org.mule.modules.alfresco.automation.testrunners;

import org.junit.runner.RunWith;
import org.mule.modules.alfresco.automation.RegressionTests;
import org.mule.modules.alfresco.automation.testcases.GetChildAuthoritiesForGroupTestCases;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(RegressionTests.class)
@org.junit.runners.Suite.SuiteClasses({
    GetChildAuthoritiesForGroupTestCases.class
})
public class RegressionTestSuite {


}
