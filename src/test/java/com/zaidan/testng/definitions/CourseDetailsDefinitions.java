package com.zaidan.testng.definitions;

import com.zaidan.testng.actions.HomePageActions; // Asumsi: Anda punya kelas ini
import com.zaidan.testng.actions.CourseDetailsPageActions; // Asumsi: Anda perlu membuat kelas ini
import com.zaidan.testng.dao.CourseDAO;
import com.zaidan.testng.model.CourseDetails;
import com.zaidan.testng.utils.HelperClass;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class CourseDetailsDefinitions {

    // Inisialisasi objek yang diperlukan
    HomePageActions homePageActions = new HomePageActions();
    CourseDetailsPageActions courseDetailsPageActions = new CourseDetailsPageActions();
    CourseDAO courseDetailsDAO = new CourseDAO();

    // Variabel untuk menyimpan data antar langkah
    private String courseName;
    private CourseDetails dbCourseDetails;

    @When("User navigates to the {string} page and selects the course named {string}")
    public void user_navigates_to_page_and_selects_course(String pageName, String courseName) {
        // Menyimpan nama kursus untuk digunakan di langkah selanjutnya
        this.courseName = courseName;

        // TODO: Anda perlu membuat method homePageActions.selectCourseByName(courseName);
        homePageActions.selectCourseByName(courseName);
        System.out.println("User selects course: " + courseName);
    }

    @Then("The course details page should display the correct information for {string}")
    public void the_course_details_page_should_display_correct_information(String expectedCourseName) {
        // Verifikasi cepat bahwa halaman detail kursus yang benar telah terbuka
        String actualCourseTitle = courseDetailsPageActions.getCourseTitle();
        Assert.assertEquals(actualCourseTitle, expectedCourseName, "Judul kursus di halaman detail tidak sesuai.");

        Assert.assertTrue(courseDetailsPageActions.isInstructorVisible(), "Nama instruktur tidak ditampilkan.");
        Assert.assertTrue(courseDetailsPageActions.isProgressBarVisible(), "Progress bar tidak ditampilkan.");
    }

    @Then("The displayed course progress should be greater than 0 and less than 100 percent")
    public void the_displayed_course_progress_should_be_between_0_and_100() {
        // Mengambil nilai progres dari UI
        // TODO: Anda perlu membuat method courseDetailsPageActions.getProgressPercentage();
        double uiProgress = courseDetailsPageActions.getProgressPercentage();

        // Melakukan asersi sesuai logika bisnis
        Assert.assertTrue(uiProgress > 0, "Progres seharusnya lebih besar dari 0.");
        Assert.assertTrue(uiProgress < 100, "Progres seharusnya lebih kecil dari 100.");
        System.out.println("UI progress (" + uiProgress + "%) is valid.");
    }

    @Then("The displayed course information for student {string} should match the database records")
    public void the_displayed_course_information_should_match_database(String studentEmail) {
        // 1. Mengambil data dari Database menggunakan DAO
        dbCourseDetails = courseDetailsDAO.getCourseDetailsForStudent(studentEmail, this.courseName);
        Assert.assertNotNull(dbCourseDetails, "Data untuk kursus '" + this.courseName + "' tidak ditemukan di database untuk mahasiswa " + studentEmail);
//        System.out.println("DB Details: " +
//                "\nJudul Kursus: " + dbCourseDetails.getCourseName() +
//                "\nGambar Kursus: " + dbCourseDetails.getCourseImage() +
//                "\nInstruktur: " + dbCourseDetails.getInstructorName() +
//                "\nProgress: " + dbCourseDetails.getProgressPercentage());

        // 2. Mengambil data dari UI menggunakan Page Actions
//        System.out.println("UI Details: " +
//                "\nJudul Kursus: " + courseDetailsPageActions.getCourseTitle() +
//                "\nGambar Kursus: " + courseDetailsPageActions.getCourseImageFilename() +
//                "\nInstruktur: " + courseDetailsPageActions.getInstructorName() +
//                "\nProgress: " + courseDetailsPageActions.getProgressPercentage());

        // 3. Membandingkan data UI dengan data Database
        Assert.assertEquals(courseDetailsPageActions.getCourseTitle(), dbCourseDetails.getCourseName(), "Nama Kursus tidak sama.");
        Assert.assertEquals(courseDetailsPageActions.getCourseImageFilename(), dbCourseDetails.getCourseImage(), "Nama file gambar tidak sama.");
        Assert.assertEquals(courseDetailsPageActions.getInstructorName(), dbCourseDetails.getInstructorName(), "Nama instruktur antara UI dan DB tidak cocok.");
        Assert.assertEquals(courseDetailsPageActions.getProgressPercentage(), dbCourseDetails.getProgressPercentage(), 0.1, "Persentase progres antara UI dan DB tidak cocok.");
    }

    // INVALID KEY ENROLLMENT
    @When("User navigates to the course list and selects the unenrolled course {string}")
    public void user_selects_unenrolled_course(String courseName) {
        homePageActions.selectCourseByName(courseName);
    }

    // Valid dan Invalid Key
    @And("User enters an enrollment key {string}")
    public void user_enters_an_invalid_enrollment_key(String Key) {
        courseDetailsPageActions.enterEnrollmentKey(Key);
    }

    @And("User clicks the enroll button")
    public void user_clicks_the_enroll_button() {
        courseDetailsPageActions.clickEnrollButton();
    }

    @Then("User should see an enrollment error message {string}")
    public void user_should_see_an_enrollment_error_message(String expectedMessage) {
        String actualMessage = courseDetailsPageActions.getEnrollmentErrorMessage();
        Assert.assertEquals(actualMessage, expectedMessage, "Pesan tidak sesuai ekspektasi");
    }

    @And("User should remain on the course information page")
    public void user_should_remain_on_the_course_information_page() {
        String currentUrl = HelperClass.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/course/"), "User tidak berada di halaman informasi kursus.");
    }

}