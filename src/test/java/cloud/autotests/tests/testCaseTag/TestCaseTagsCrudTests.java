package cloud.autotests.tests.testCaseTag;

import cloud.autotests.api.testCaseTag.*;
import cloud.autotests.helpers.WithLogin;
import cloud.autotests.tests.BaseTest;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Story("Test case tags tests")
public class TestCaseTagsCrudTests extends BaseTest {

	private final TestCaseTagApi tagApi = new TestCaseTagApi();

	// Project name [GOST_group_tests]
	private static final int PROJECT_ID = 291;
	private final String randomTagName = faker.random().hex(10);

	@Test
	@WithLogin
	@DisplayName("Create new tag from test case detail")
	void createNewTag() {
		// Test data
		// Test case name [Test case for autotest [Create new tag on test case]]
		int testCaseId = 7962;
		String oldTagName = "OLD TAG NAME";

		// Arrange
		testCasePage.openPage(PROJECT_ID, testCaseId);

		// Act
		testCasePage.addNewTag(randomTagName);

		// Assert
		testCasePage.checkThatTagsSectionContainsTag(oldTagName, randomTagName);

		// Cleaning data
		// Удаляем новый созданный tag
		Integer newTagId = tagApi.getTestCaseTagId(testCaseId, randomTagName);
		tagApi.deleteTestCaseTagId(newTagId);
	}

	@Test
	@Disabled
	@WithLogin
	@DisplayName("Edit test case tag")
	void editTestCaseTag() {}

	@Test
	@WithLogin
	@DisplayName("Remove test case tag via bulk action")
	void removeTagViaBulkAction() {
		// Test data
		// Test case name [Test case for autotest [Remove test case tag via bulk action]]
		int testCaseId = 7980;

		// Arrange
		TestCaseTagDto tag = tagApi.createNewTestCaseTag(randomTagName);
		tagApi.setTestCaseTags(testCaseId, tag);
		testCasePage.openPage(PROJECT_ID, testCaseId);
		testCasePage.checkThatTagsSectionContainsTag(tag.getName());

		// Act
		testCasePage.testCasesTable.selectTestCase(testCaseId);
		testCasePage.testCasesTable.removeTagViaBulkAction(tag.getName());

		// Assert
		testCasePage.checkThatTagsSectionDoNotContainsTag(tag.getName());
	}

}
