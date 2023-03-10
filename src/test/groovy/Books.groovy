import io.restassured.response.Response
import org.testng.Assert
import org.testng.annotations.Test
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath

import static io.restassured.RestAssured.*

class Books extends Base {
    @Test
    void getBooksListTest() {
        Response response = get("/books")

        ArrayList<String> allBooks = response.path("data.title")
        Assert.assertTrue(allBooks.size() >1, "No books returned.")
    }

    @Test
    void booksSchemaIsValidTest() {
        get("/books")
            .then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath("booksSchema.json"))
    }

    @Test
    void createAndDeleteBookTest() {
        File bookFile = new File(getClass().getResource("/book.json").toURI())
        Response createResponse = given().body(bookFile).when().post("/books")

        String responseID = createResponse.jsonPath().getString("post.book_id")
        Assert.assertEquals(createResponse.getStatusCode(), 201)

        Response deleteResponse = given()
                .body("{\n"+
                        "\t\"book_id\": " + responseID + "\n" +
                      "}").when().delete("/books")

        Assert.assertEquals(deleteResponse.getStatusCode(), 200)
        Assert.assertEquals(deleteResponse.jsonPath().getString("message"), "Book successfully deleted")
    }

    @Test()
    void deleteNonExistentBookTest() {
        String nonExistentID = "9009"
        Response deleteResponse = given()
                .body("{\n"+
                        "\t\"book_id\": " + nonExistentID + "\n" +
                        "}").when().delete("/books")
        Assert.assertEquals(deleteResponse.getStatusCode(), 500)
        Assert.assertEquals(deleteResponse.jsonPath().getString("error"), "Unable to find book id: "+ nonExistentID)


    }
}
