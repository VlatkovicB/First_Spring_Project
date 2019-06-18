package com.spring.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Component("users")
public class UsersDao {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private SessionFactory sessionFactory;

	public Session session() {
		return sessionFactory.getCurrentSession();
	}

//	@Transactional
	public void create(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		session().save(user);
		/*
		 * MapSqlParameterSource params = new MapSqlParameterSource();
		 * params.addValue("username", user.getUsername()); params.addValue("password",
		 * passwordEncoder.encode(user.getPassword())); params.addValue("email",
		 * user.getEmail()); params.addValue("name", user.getName());
		 * params.addValue("enabled", user.isEnabled()); params.addValue("authority",
		 * user.getAuthority()); return jdbc.update(
		 * "INSERT INTO users (username, name, password, email, enabled, authority) VALUES (:username, :name, :password, :email, :enabled, :authority)"
		 * , params) == 1;
		 */

	}

	public boolean exists(String username) {

		Criteria crit = session().createCriteria(User.class);
		/*
		 * SInce username is PK, using idEq lowers chance of typos
		 */
//		crit.add(Restrictions.eq("username", username));
		crit.add(Restrictions.idEq(username));
		User user = (User) crit.uniqueResult();

		return user != null;
//		return jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE username=:username",
//				new MapSqlParameterSource("username", username), Integer.class) > 0;
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		return session().createQuery("FROM User").list();
//		return jdbc.query("SELECT * FROM users", BeanPropertyRowMapper.newInstance(User.class));
	}

}
