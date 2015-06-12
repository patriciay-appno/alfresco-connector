
package org.mule.modules.alfresco.automation.testrunners;

import org.junit.runner.RunWith;
import org.mule.modules.alfresco.automation.SmokeTests;
import org.mule.modules.alfresco.automation.testcases.GetChildAuthoritiesForGroupTestCases;

@RunWith(org.junit.experimental.categories.Categories.class)
@org.junit.experimental.categories.Categories.IncludeCategory(SmokeTests.class)
@org.junit.runners.Suite.SuiteClasses({
    GetChildAuthoritiesForGroupTestCases.class
})
public class SmokeTestSuite {


}
