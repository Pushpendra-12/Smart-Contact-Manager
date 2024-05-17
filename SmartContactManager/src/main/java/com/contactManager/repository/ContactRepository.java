package com.contactManager.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contactManager.modals.Contact;
import com.contactManager.modals.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	//pegination..
	
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);
		
	//Search method
	public List<Contact> findByNameContainingAndUser(String name,User user);

}
