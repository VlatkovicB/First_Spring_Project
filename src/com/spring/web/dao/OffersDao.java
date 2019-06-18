package com.spring.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Component("offersDao")
public class OffersDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session session() {
		return sessionFactory.getCurrentSession();
	}

	public OffersDao() {
	}

	@SuppressWarnings("unchecked")
	public List<Offer> getOffers() {

		Criteria crit = session().createCriteria(Offer.class);
		crit.createAlias("user", "u").add(Restrictions.eq("u.enabled", true));

		return crit.list();

//		return jdbc.query("SELECT * FROM offers, users WHERE offers.username=users.username and users.enabled = true",
//				new OfferRowMapper());
	}

	@SuppressWarnings("unchecked")
	public List<Offer> getOffers(String username) {

		Criteria crit = session().createCriteria(Offer.class);
		crit.createAlias("user", "u");
		crit.add(Restrictions.eq("u.enabled", true));
		crit.add(Restrictions.eq("u.username", username));

		return crit.list();

//		return jdbc.query(
//				"SELECT * FROM offers, users WHERE offers.username=users.username AND users.enabled = true AND offers.username = :username",
//				new MapSqlParameterSource("username", username), new OfferRowMapper());

	}

	/*
	 * public void update(Offer offer) { // BeanPropertySqlParameterSource params =
	 * new BeanPropertySqlParameterSource(offer); // return
	 * jdbc.update("UPDATE offers SET text = :text WHERE id = :id", params) == 1;
	 * 
	 * session().update(offer); }
	 */

	public void saveOrUpdate(Offer offer) {
//		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(offer);
//		return jdbc.update("INSERT INTO offers (username, text) VALUES (:username, :text)", params) == 1;
		session().saveOrUpdate(offer);
	}

	/*
	 * Google: hibernate batch
	 * 
	 * @Transactional public int[] create(List<Offer> offers) {
	 * 
	 * SqlParameterSource[] params =
	 * SqlParameterSourceUtils.createBatch(offers.toArray());
	 * 
	 * return jdbc.
	 * batchUpdate("INSERT INTO offers (username, text) VALUES (:username, :text)",
	 * params); }
	 */

	public boolean delete(int id) {
//		MapSqlParameterSource params = new MapSqlParameterSource("id", id);
		// jdbc.getJdbcOperations() JdbcTemplate WRAPPER
//		return jdbc.update("DELETE FROM offers WHERE id = :id", params) == 1;
		Query query = session().createQuery("DELETE FROM Offer WHERE id = :id");
		query.setLong("id", id);
		return query.executeUpdate() == 1;
	}

	public Offer getOffer(int id) {
		Criteria crit = session().createCriteria(Offer.class);
		crit.createAlias("user", "u");
		crit.add(Restrictions.eq("u.enabled", true));
		crit.add(Restrictions.idEq(id));

		return (Offer) crit.uniqueResult();

//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("id", id);
//
//		return jdbc.queryForObject(
//				"SELECT * FROM offers, users WHERE offers.username=users.username and users.enabled = true AND id=:id",
//				params, new OfferRowMapper());

	}
}
