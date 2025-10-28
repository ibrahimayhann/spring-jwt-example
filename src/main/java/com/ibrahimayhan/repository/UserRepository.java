package com.ibrahimayhan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ibrahimayhan.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	//@Query(value = "from User where username=:username") böyle yapsakta olurdu ama gerek yok isimlendirme standardına uyarsak ne istediğimizi anlayıp bu query arkaplanda yazıyor zaten
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);
}
