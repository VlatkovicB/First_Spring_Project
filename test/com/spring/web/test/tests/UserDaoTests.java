package com.spring.web.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.web.dao.User;
import com.spring.web.dao.UsersDao;

@ActiveProfiles("dev")
@ContextConfiguration(locations = { "classpath:com/spring/web/beans/dao-context.xml",
		"classpath:com/spring/web/beans/security-context.xml", "classpath:com/spring/web/test/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTests {

	@Autowired
	private UsersDao usersDao;
	@Autowired
	private DataSource dataSource;
	private User user1 = new User("Bojan", "Bojan Vlatkovic", "hello", "bojanvlatkovic@gmail.com", true, "ROLE_USER");
	private User user2 = new User("Jelena", "Jelena Cerovic", "hello", "jelena@cerovic.com", true, "ROLE_USER");
	private User user3 = new User("Serpa", "Serpa Lonac", "hello", "serpa@gmail.com", true, "ROLE_USER");
	private User user4 = new User("Lonac", "Lonac Serpa", "hello", "lonac@gmail.com", true, "ROLE_USER");

	@Before
	public void init() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);
		jdbc.execute("DELETE FROM offers");
		jdbc.execute("DELETE FROM users");
	}

	@Test
	public void testCreateRetrieve() {
		usersDao.create(user1);

		List<User> users1 = usersDao.getAllUsers();

		assertEquals("One user should have been created and retrieved", 1, users1.size());

		assertEquals("Asserted user should match retrieved", user1, users1.get(0));
		usersDao.create(user2);
		usersDao.create(user3);
		usersDao.create(user4);
		List<User> users2 = usersDao.getAllUsers();

		assertEquals("Should be four retrieved users.", 4, users2.size());

	}

	@Test
	public void testExists() {
		usersDao.create(user1);
		usersDao.create(user2);
		usersDao.create(user3);
		assertTrue("User should exist.", usersDao.exists(user1.getUsername()));
		assertTrue("User should exist.", usersDao.exists(user2.getUsername()));
		assertFalse("User should not exist.", usersDao.exists("nekonebitan"));

	}

}
