package com.putchar.jwtgenvalidate.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.putchar.jwtgenvalidate.model.LoginUser;

@Repository
public interface UserRepository  extends CrudRepository<LoginUser, Integer>
{

}
