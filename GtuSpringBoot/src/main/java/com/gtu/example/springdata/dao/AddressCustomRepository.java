package com.gtu.example.springdata.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
//import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Repository;

import com.gtu.example.springdata.entity.Address;

@Profile({ "spring-data" })

@Repository
public class AddressCustomRepository {

    private static final Logger log = LoggerFactory.getLogger(AddressCustomRepository.class);

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    @Qualifier("serversEntityManager")
    private EntityManager em;

    // @Autowired // 兩個datasource不work
    // public AddressCustomRepository(JpaContext context) {
    // this.em = context.getEntityManagerByManagedType(Address.class);
    // }

    public List<Address> findAllWithRownum() {
        long cnt = addressRepository.count();
        log.info("cnt = {}", cnt);

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT @rownum:=@rownum+1 'no', m ");
        sb.append(" FROM Address m, (SELECT @rownum:=0) r WHERE 1=1  ");
        sb.append(" ORDER BY m.city asc ");

        Query query = em.createQuery(sb.toString());
        return query.getResultList();
    }
}
