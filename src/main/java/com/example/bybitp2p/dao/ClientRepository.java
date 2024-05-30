package com.example.bybitp2p.dao;

import com.example.bybitp2p.data.entity.Client;
//import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//@EnableScan
@Repository
public interface ClientRepository extends CrudRepository<Client, String> {
}