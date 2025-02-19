package cloud.autotests.tests.defect;

import cloud.autotests.api.defect.CreateDefectRequestDto;
import cloud.autotests.api.defect.DefectApi;
import cloud.autotests.data.DefectStatus;
import cloud.autotests.helpers.WithLogin;
import cloud.autotests.pages.defect.DefectPage;
import cloud.autotests.pages.defect.DefectsListPage;
import cloud.autotests.tests.BaseTest;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cloud.autotests.data.DefectActionMenuItem.*;

@Story("Defect tests")
public class DefectCrudTests extends BaseTest {

    private final DefectsListPage defectsListPage = new DefectsListPage();
    private final DefectPage defectPage = new DefectPage();
    private final DefectApi defectApi = new DefectApi();

    // Test project [dont-remove-autotests-for-defects]
    private final static int PROJECT_ID = 1157;
    private final CreateDefectRequestDto defect = CreateDefectRequestDto.builder()
            .name(faker.random().hex(15))
            .description(faker.random().hex(20))
            .projectId(PROJECT_ID)
            .build();

    @Test
    @WithLogin
    @DisplayName("Create defect")
    void createDefect() {
        defectsListPage.openPage(PROJECT_ID);

        defectsListPage.createNewDefect(defect.getName(), defect.getDescription());
        int createdDefectId = defectPage.getDefectId();

        defectsListPage.checkThatDefectsListContainsDefect(defect.getName());
        defectPage.checkThatDefectNameIs(defect.getName());
        defectPage.checkThatDefectDescriptionIs(defect.getDescription());
        defectPage.checkThatDefectStatusIs(DefectStatus.OPEN);

        defectApi.deleteDefect(createdDefectId);
    }

    @Test
    @WithLogin
    @DisplayName("Edit defect")
    void editDefect() {
        String newName = faker.random().hex(15);
        String newDescription = faker.random().hex(20);

        int createdDefectId = defectApi.createDefect(defect).getId();
        defectPage.openPage(PROJECT_ID, createdDefectId);

        defectPage.editDefectName(newName);
        defectPage.editDefectDescription(newDescription);
        defectPage.editDefectStatus(CLOSE.getDisplayedName());

        defectPage.checkThatDefectNameIs(newName);
        defectPage.checkThatDefectDescriptionIs(newDescription);
        defectPage.checkThatDefectStatusIs(DefectStatus.CLOSED);

        defectApi.deleteDefect(createdDefectId);
    }

    @Test
    @WithLogin
    @DisplayName("Delete defect")
    void deleteDefect() {
        int createdDefectId = defectApi.createDefect(defect).getId();
        defectPage.openPage(PROJECT_ID, createdDefectId);

        defectPage.deleteDefect();
        defectsListPage.checkThatDefectsListDoNotContainsDefect(defect.getName());
    }

}
