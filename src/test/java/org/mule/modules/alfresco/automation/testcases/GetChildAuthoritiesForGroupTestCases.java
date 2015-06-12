
package org.mule.modules.alfresco.automation.testcases;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.alfresco.automation.AlfrescoTestParent;
import org.mule.modules.alfresco.automation.RegressionTests;
import org.mule.modules.alfresco.automation.SmokeTests;

public class GetChildAuthoritiesForGroupTestCases
    extends AlfrescoTestParent
{


    @Before
    public void setup()
        throws Exception
    {
        //TODO: Add setup required to run test or remove method
        initializeTestRunMessage("getChildAuthoritiesForGroupTestData");
    }

    @After
    public void tearDown()
        throws Exception
    {
        //TODO: Add code to reset sandbox state to the one before the test was run or remove
    }

    @Category({
        RegressionTests.class,
        SmokeTests.class
    })
    @Test
    public void testGetChildAuthoritiesForGroup()
        throws Exception
    {
        Object result = runFlowAndGetPayload("get-child-authorities-for-group");
        throw new RuntimeException("NOT IMPLEMENTED METHOD");
    }

}
