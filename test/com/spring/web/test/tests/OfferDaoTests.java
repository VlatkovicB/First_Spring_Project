package com.spring.web.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import com.spring.web.dao.Offer;
import com.spring.web.dao.OffersDao;
import com.spring.web.dao.User;
import com.spring.web.dao.UsersDao;

@ActiveProfiles("dev")
@ContextConfiguration(locations = { "classpath:com/spring/web/beans/dao-context.xml",
		"classpath:com/spring/web/beans/security-context.xml", "classpath:com/spring/web/test/config/datasource.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class OfferDaoTests {

	@Autowired
	private OffersDao offersDao;
	@Autowired
	private UsersDao usersDao;

	private User user1 = new User("Bojan", "Bojan Vlatkovic", "hello", "bojanvlatkovic@gmail.com", true, "ROLE_USER");
	private User user2 = new User("Jelena", "Jelena Cerovic", "hello", "jelena@cerovic.com", true, "ROLE_USER");
	private User user3 = new User("Serpa", "Serpa Lonac", "hello", "serpa@gmail.com", true, "ROLE_USER");
	private User user4 = new User("Lonac", "Lonac Serpa", "hello", "lonac@gmail.com", false, "ROLE_USER");

	private Offer offer1 = new Offer(user1, "this is a test offer");
	private Offer offer2 = new Offer(user1, "this is a another test offer");
	private Offer offer3 = new Offer(user2, "this is a  yet another test offer");
	private Offer offer4 = new Offer(user3, "this is a  test offer offer test offer");
	private Offer offer5 = new Offer(user3, "this is a an interesting test offer");
	private Offer offer6 = new Offer(user3, "this is a just a test offer");
	private Offer offer7 = new Offer(user4, "this is a test bad english all over place offer");

	@Autowired
	private DataSource dataSource;

	@Before
	public void init() {
		JdbcTemplate jdbc = new JdbcTemplate(dataSource);

		jdbc.execute("delete from offers");
		jdbc.execute("delete from users");
	}

	@Test
	public void testCreateRetrieve() {
		usersDao.create(user1);
		usersDao.create(user2);
		usersDao.create(user3);
		usersDao.create(user4);

		offersDao.saveOrUpdate(offer1);
		List<Offer> offers2 = offersDao.getOffers();
		assertEquals("There should be 1 offer.", 1, offers2.size());

		assertEquals("Retrieved offer shoudl equal expected.", offer1, offers2.get(0));

		offersDao.saveOrUpdate(offer2);
		offersDao.saveOrUpdate(offer3);
		offersDao.saveOrUpdate(offer4);
		offersDao.saveOrUpdate(offer5);
		offersDao.saveOrUpdate(offer6);
		offersDao.saveOrUpdate(offer7);

		List<Offer> offers = offersDao.getOffers();

		assertEquals("There should be 6 offers", 6, offers.size());
	}

	@Test
	public void testGetByUsername() {
		usersDao.create(user1);
		usersDao.create(user2);
		usersDao.create(user3);
		usersDao.create(user4);
		offersDao.saveOrUpdate(offer1);
		offersDao.saveOrUpdate(offer2);
		offersDao.saveOrUpdate(offer3);
		offersDao.saveOrUpdate(offer4);
		offersDao.saveOrUpdate(offer5);
		offersDao.saveOrUpdate(offer6);
		offersDao.saveOrUpdate(offer7);

		List<Offer> offers1 = offersDao.getOffers(user3.getUsername());
		assertEquals("Should be 3 offers for this user", 3, offers1.size());

		List<Offer> offers2 = offersDao.getOffers("nekonebitan");
		assertEquals("Should be 0 offers for this user", 0, offers2.size());

		List<Offer> offers3 = offersDao.getOffers(user2.getUsername());
		assertEquals("Should be 1 offers for this user", 1, offers3.size());
	}

	@Test
	public void testUpdate() {
		usersDao.create(user1);
		usersDao.create(user2);
		usersDao.create(user3);
		usersDao.create(user4);
		offersDao.saveOrUpdate(offer1);
		offersDao.saveOrUpdate(offer2);
		offersDao.saveOrUpdate(offer3);
		offersDao.saveOrUpdate(offer4);
		offersDao.saveOrUpdate(offer5);
		offersDao.saveOrUpdate(offer6);
		offersDao.saveOrUpdate(offer7);

		offer3.setText("This offer has updated text");
		offersDao.saveOrUpdate(offer3);

		Offer retrieved = offersDao.getOffer(offer3.getId());
		assertEquals("Offers should be equal", offer3, retrieved);
		System.out.println(retrieved.getText());
	}

	@Test
	public void testDelete() {
		usersDao.create(user1);
		usersDao.create(user2);
		usersDao.create(user3);
		usersDao.create(user4);
		offersDao.saveOrUpdate(offer1);
		offersDao.saveOrUpdate(offer2);
		offersDao.saveOrUpdate(offer3);
		offersDao.saveOrUpdate(offer4);
		offersDao.saveOrUpdate(offer5);
		offersDao.saveOrUpdate(offer6);
		offersDao.saveOrUpdate(offer7);

		Offer retrieved1 = offersDao.getOffer(offer2.getId());
		assertNotNull("Offer with ID" + retrieved1.getId() + "should be null", retrieved1);

		offersDao.delete(offer2.getId());

		Offer retrieved2 = offersDao.getOffer(offer2.getId());
		assertNull("Offer with ID" + retrieved1.getId() + "should be null", retrieved2);

	}

	@Test
	public void testGetById() {
		usersDao.create(user1);
		usersDao.create(user2);
		usersDao.create(user3);
		usersDao.create(user4);
		offersDao.saveOrUpdate(offer1);
		offersDao.saveOrUpdate(offer2);
		offersDao.saveOrUpdate(offer3);
		offersDao.saveOrUpdate(offer4);
		offersDao.saveOrUpdate(offer5);
		offersDao.saveOrUpdate(offer6);
		offersDao.saveOrUpdate(offer7);

		Offer retrieved1 = offersDao.getOffer(offer1.getId());
		assertEquals("Offers should match", offer1, retrieved1);

		Offer retrieved2 = offersDao.getOffer(offer7.getId());
		assertNull("Should not retrieve offer for disabled user.", retrieved2);
	}
}
