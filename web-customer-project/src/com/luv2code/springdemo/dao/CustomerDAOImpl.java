package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

	// need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Customer> getCustomers() {

		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();

		// create a query
		Query<Customer> theQuery = currentSession.createQuery("from Customer order by lastName", Customer.class);

		// execute and get result list
		List<Customer> customers = theQuery.getResultList();

		// return the result
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		// get the current session from session factory
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(theCustomer);
	}

	@Override
	public Customer getCustomer(int theId) {
		Session theSession = sessionFactory.getCurrentSession();
		Customer theCustomer = theSession.get(Customer.class, theId);
		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId) {

		Session theSession = sessionFactory.getCurrentSession();
		Query theQuery = theSession.createQuery("delete from Customer where id=:customerId");
		theQuery.setParameter("customerId", theId);
		theQuery.executeUpdate();

	}

	@Override
	public List<Customer> getCustomer(String theSearchName) {
		Session theSession = sessionFactory.getCurrentSession();
		Query theQuery = null;
		//
		// only search by name if theSearchName is not empty
		//
		if (theSearchName != null && theSearchName.trim().length() > 0) {

			// search for firstName or lastName ... case insensitive
			theQuery = theSession.createQuery(
					"from Customer where lower(firstName) like :theName or lower(lastName) like :theName",
					Customer.class);
			theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");

		} else {
			// theSearchName is empty ... so just get all customers
			theQuery = theSession.createQuery("from Customer", Customer.class);
		}

		List<Customer> theCustomer = theQuery.getResultList();
		return theCustomer;
	}

}
