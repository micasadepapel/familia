package api.test;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTest 
	{
		Faker faker;
		User userPayload;
		
		@BeforeTest
		public void setupData() 
		{
			faker = new Faker();
			userPayload = new User();
		
			userPayload.setId(faker.idNumber().hashCode());
			userPayload.setUsername(faker.name().username());
			userPayload.setFirstName(faker.name().firstName());
			userPayload.setLastName(faker.name().lastName());
			userPayload.setEmail(faker.internet().safeEmailAddress());
			userPayload.setPassword(faker.internet().password(5,10));
			userPayload.setPhone(faker.phoneNumber().cellPhone());
		}
		@Test(priority=1)
		public void testPostUser() 
		{
			Response res = UserEndPoints.createUser(userPayload);
			res.then().log().all();	
		}
		
		@Test(priority=2)
		public void testGetUser() 
		{
			Response res = UserEndPoints.getUser(this.userPayload.getUsername());
			res.then().log().all();
			//Assert.assertEquals(res.statusCode(), 200);
			//Assert.assertEquals(res.contentType(), "application/json");
			//Assert.assertEquals(res.jsonPath().getInt("id"),userPayload.getId());
		}
		@Test(priority=3)
		public void testUpdateUser() 
		{
			userPayload.setLastName(faker.name().lastName());
			userPayload.setEmail(faker.internet().safeEmailAddress());
			userPayload.setPhone(faker.phoneNumber().cellPhone());
			
			//sending the update request >>
			Response res = UserEndPoints.updateUser(this.userPayload.getUsername(),userPayload);
			res.then().log().body();
			
			//sending get request after updating the data >>
			//Response resNew = UserEndPoints.getUser(this.userPayload.getUsername());
			//resNew.then().log().all();
		}
		@Test(priority=4)
		public void testDeleteUser() 
		{
			Response res = UserEndPoints.deleteUser(this.userPayload.getUsername());
			res.then().log().all();
			
			//checking for the deleted record/user by sending get request
			Response resNew = UserEndPoints.getUser(this.userPayload.getUsername());
			resNew.then().log().all();
		}
	}