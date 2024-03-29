package com.mkolongo.product_shop.repositories;

import com.mkolongo.product_shop.domain.entities.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);
}
